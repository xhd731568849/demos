package com.btzh.transfer.entity;

import com.btzh.config.Config;
import com.btzh.consts.BusinessType;
import com.btzh.consts.Protocol;
import com.btzh.util.RepositoryUtil;
import com.btzh.util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 文件
 * @author wanglidong
 * @date 17-10-22 下午2:57
 */
public class BaseFile extends AbstractMessage {

    public static final int LEN_FILE_PATH_LENGTH = 1;
    public static final int LEN_FILE_LENGTH = 8;

    @Override
    public int getHeaderLength() {
        return super.getHeaderLength() + LEN_FILE_PATH_LENGTH + this.filePathLength + LEN_FILE_LENGTH;
    }

    @Override
    public void encode(ByteBuf out) {
        out.writeByte(this.filePathLength);
        if (this.filePathLength > 0) {
            if (!this.filePath.startsWith(File.separator)) {
                //确保文件路径头有文件分隔符，接收文件时转换路径用
                throw new RuntimeException("file path header separator missing!");
            }
            out.writeBytes(Unpooled.copiedBuffer(this.filePath, CharsetUtil.UTF_8));
        }
        out.writeLong(this.fileLength);
    }

    @Override
    public Object getBody() {
        try {
            return new ChunkedFile(this.file, Config.getConfig().getFileChunkSize());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean decode(ByteBuf in) {
        if (null == this.filePathLength) {
            if (in.readableBytes() < LEN_FILE_PATH_LENGTH) {
                return false;
            }
            this.filePathLength = in.readByte() & 0xFF;
        }
        if (null == this.filePath) {
            if (0 == this.filePathLength) {
                this.filePath = StringUtil.getUUID();
            } else {
                if (in.readableBytes() < this.filePathLength) {
                    return false;
                }
                ByteBuf filePathBuf = in.readBytes(this.filePathLength);
                this.filePath = StringUtil.getRealFilePath(filePathBuf.toString(CharsetUtil.UTF_8));
                filePathBuf.release();
            }
        }
        if (null == this.fileLength) {
            if (in.readableBytes() < LEN_FILE_LENGTH) {
                return false;
            }
            this.fileLength = in.readLong();
        }

        if (null == this.file) {
            try {
                Path path = RepositoryUtil.getRemoteFilePath().resolve(this.filePath.substring(1));
                try {
                    Path parentPath = path.getParent();
                    if (Files.exists(parentPath)) {
                        Files.deleteIfExists(path);
                    } else {
                        Files.createDirectories(parentPath);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                this.file = Files.createFile(path).toFile();
                this.outputStream = new FileOutputStream(this.file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return readToFile(in);
    }

    public boolean readToFile(ByteBuf in) {
        try {
            int readable = in.readableBytes();
            if (this.readed + readable > this.fileLength) {
                in.readBytes(this.outputStream, (int) (this.fileLength - this.readed));

                throw new TooLongFrameException("Frame too big!");
            } else {
                in.readBytes(this.outputStream, readable);
                this.readed += readable;

                return this.readed == this.fileLength;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        if (null != this.outputStream) {
            this.outputStream.close();
            this.outputStream = null;
        }
    }

    protected Integer filePathLength;
    protected String filePath;
    protected Long fileLength;

    protected transient File file;
    protected transient OutputStream outputStream;
    protected transient long readed;

    public BaseFile() {
        this.protocol = Protocol.BaseFile;
        this.businessType = BusinessType.FILE;
    }

    public BaseFile(String filePath, File file) {
        this(BusinessType.FILE, filePath, file);
    }

    public BaseFile(BusinessType businessType, String filePath, File file) {
        this.protocol = Protocol.BaseFile;
        this.businessType = businessType;
        this.filePathLength = StringUtil.getByteLength(filePath);
        this.filePath = filePath;
        this.file = file;

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            this.fileLength = randomAccessFile.length();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getFilePathLength() {
        return filePathLength;
    }

    public void setFilePathLength(Integer filePathLength) {
        this.filePathLength = filePathLength;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}