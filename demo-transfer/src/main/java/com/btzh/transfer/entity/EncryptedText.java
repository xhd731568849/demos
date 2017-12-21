package com.btzh.transfer.entity;

import com.btzh.Main;
import com.btzh.consts.BusinessType;
import com.btzh.consts.Protocol;
import com.btzh.transfer.util.gm.GmUtil;
import com.btzh.util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * 加密Text
 * @author wanglidong
 * @date 2017/9/25
 */
public class EncryptedText extends Text {

    public static final int LEN_KEY_LENGTH = 1;

    @Override
    public int getHeaderLength() {
        return super.getHeaderLength() + LEN_KEY_LENGTH + this.keyLength;
    }

    @Override
    public void encode(ByteBuf out) {
        out.writeByte(this.keyLength);
        out.writeBytes(Unpooled.copiedBuffer(this.key, CharsetUtil.UTF_8));

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
            ByteBuf keyByteBuf = in.readBytes(keyLength);
            String cipherKey = keyByteBuf.toString(CharsetUtil.UTF_8);
            keyByteBuf.release();

            this.key = GmUtil.decryptByPrivatekey(Main.getLocalPrivateKey(), cipherKey);
        }
        if (super.decode(in)) {
            this.text = GmUtil.sm4DecryptString(this.text, this.key);
            return true;
        }

        return false;
    }

    protected String key;
    protected Integer keyLength;

    public EncryptedText() {
        this.protocol = Protocol.EncryptedText;
    }

    public EncryptedText(BusinessType businessType, String text) {
        super(businessType, text);
        this.protocol = Protocol.EncryptedText;
        String remotePublicKey = Main.getRemotePublicKey();
        if (null == remotePublicKey) {
            throw new RuntimeException("remote public key missing!");
        }

        String randomSm4Key = GmUtil.getRandomSm4Key();
        this.text = GmUtil.sm4EncryptString(this.text, randomSm4Key);
        this.textLength = StringUtil.getByteLength(this.text);
        this.key = GmUtil.encryptByPublickey(remotePublicKey, randomSm4Key);
        this.keyLength = StringUtil.getByteLength(this.key);
    }
}