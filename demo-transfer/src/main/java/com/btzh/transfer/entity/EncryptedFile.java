package com.btzh.transfer.entity;

import com.btzh.Main;
import com.btzh.consts.BusinessType;
import com.btzh.consts.Protocol;
import com.btzh.transfer.codec.EncryptedChunkedFile;
import com.btzh.transfer.util.gm.GmUtil;
import com.btzh.util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.IOException;

/**
 * 加密文件
 * @author wanglidong
 * @date 2017/9/25
 */
public class EncryptedFile extends BaseFile {

    public static final int LEN_KEY_LENGTH = 1;

    @Override
    public int getHeaderLength() {
        return super.getHeaderLength() + LEN_KEY_LENGTH + this.keyLength;
    }

    @Override
    public Object getBody() {
        try {
            return new EncryptedChunkedFile(this.key, this.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void encode(ByteBuf out) {
        String remotePublicKey = Main.getRemotePublicKey();
        if (null == remotePublicKey) {
            throw new RuntimeException("remote public key missing!");
        }

        String cipherKey = GmUtil.encryptByPublickey(remotePublicKey, key);
        out.writeByte(StringUtil.getByteLength(cipherKey, CharsetUtil.UTF_8));
        out.writeBytes(Unpooled.copiedBuffer(cipherKey, CharsetUtil.UTF_8));

        super.encode(out);
    }

    @Override
    public boolean decode(ByteBuf in) {
        if (null == this.keyLength) {
            if (in.readableBytes() < LEN_KEY_LENGTH) {
                return false;
            }
            this.keyLength = in.readByte() & 0xFF;
        }
        if (null == this.key) {
            if (in.readableBytes() < this.keyLength) {
                return false;
            }
            ByteBuf keyByteBuf = in.readBytes(this.keyLength);
            String cipherKey = keyByteBuf.toString(CharsetUtil.UTF_8);
            keyByteBuf.release();

            this.key = GmUtil.decryptByPrivatekey(Main.getLocalPrivateKey(), cipherKey);
        }

        return super.decode(in);
    }

    @Override
    public boolean readToFile(ByteBuf in) {
        int cipherBlockSize = 1024;
        long encryptedFileLength = calculateEncryptedFileLength();

        while (true) {
            if (!in.isReadable()) {
                break;
            }

            int readable = in.readableBytes();
            if (readable < cipherBlockSize && (readable + this.readed < encryptedFileLength)) {
                return false;
            }

            byte[] cipherBytes;
            if (readable + this.readed < encryptedFileLength) {
                cipherBytes = new byte[cipherBlockSize];
                in.readBytes(cipherBytes, 0, cipherBlockSize);
                this.readed += cipherBlockSize;
            } else if (readable + this.readed == encryptedFileLength) {
                // 已经是最后一组数据了
                cipherBytes = new byte[readable];
                in.readBytes(cipherBytes, 0, readable);
                this.readed += readable;
            } else {
                throw new TooLongFrameException("Frame too big!");
            }

            try {
                this.outputStream.write(decrypt(cipherBytes));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return this.readed == encryptedFileLength;
    }

    /**
     * 临时变量
     */
    private Long encryptedFileLength;

    private long calculateEncryptedFileLength() {
        if (null == encryptedFileLength) {
            this.encryptedFileLength = GmUtil.getSm4CipherTextLength(this.fileLength);
        }

        return this.encryptedFileLength;
    }

    private byte[] decrypt(byte[] cipher) {
        return GmUtil.sm4DecryptData(cipher, key, encryptedFileLength == this.readed);
    }

    protected String key;
    protected Integer keyLength;

    public EncryptedFile() {
        this.protocol = Protocol.EncryptedFile;
    }

    public EncryptedFile(String filePath, File file) {
        this(BusinessType.FILE, filePath, file);
    }

    public EncryptedFile(BusinessType businessType, String filePath, File file) {
        super(businessType, filePath, file);
        this.protocol = Protocol.EncryptedFile;
        this.key = GmUtil.getRandomSm4Key();
        this.keyLength = StringUtil.getByteLength(this.key);
    }
}