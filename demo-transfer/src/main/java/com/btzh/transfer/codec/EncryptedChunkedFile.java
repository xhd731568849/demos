package com.btzh.transfer.codec;

import com.btzh.config.Config;
import com.btzh.transfer.util.gm.GmUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.handler.stream.ChunkedFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 分块加密文件
 * @author wanglidong
 * @date 2017/9/25
 */
public class EncryptedChunkedFile extends ChunkedFile {
	
	private String key;

	public EncryptedChunkedFile(String key, File file) throws IOException {
		this(key, file, Config.getConfig().getFileChunkSize());
	}

	public EncryptedChunkedFile(String key, File file, int chunkSize) throws IOException {
		this(key, new RandomAccessFile(file, "r"), chunkSize);
	}

	public EncryptedChunkedFile(String key, RandomAccessFile file) throws IOException {
		this(key, file, Config.getConfig().getFileChunkSize());
	}

	public EncryptedChunkedFile(String key, RandomAccessFile file, int chunkSize) throws IOException {
		this(key, file, 0, file.length(), chunkSize);
	}

	public EncryptedChunkedFile(String key, RandomAccessFile file, long offset, long length, int chunkSize) throws IOException {
		super(file, offset, length, chunkSize);
		this.key = key;
	}

	@Override
	public ByteBuf readChunk(ByteBufAllocator allocator) throws Exception {
		ByteBuf plainBuf = super.readChunk(allocator);
		byte[] plainBytes = new byte[plainBuf.readableBytes()];
		plainBuf.readBytes(plainBytes);
		plainBuf.release();

		return Unpooled.wrappedBuffer(GmUtil.sm4EncryptData(plainBytes, key, this.isEndOfInput()));
	}
}