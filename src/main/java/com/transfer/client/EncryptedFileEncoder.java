package com.transfer.client;

import com.transfer.entity.EncryptedFile;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by xuhandong on 17-9-25.
 *
 *
 * MessageToMessageEncoder
 * ChannelHandlerContext
 * ByteBuf
 * Unpooled
 * DefaultFileRegion
 */
public class EncryptedFileEncoder extends MessageToMessageEncoder<EncryptedFile> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, EncryptedFile encryptedFile, List<Object> list) throws Exception {
        Logger logger = LoggerFactory.getLogger(EncryptedFileEncoder.class);
        logger.info("["+encryptedFile.getKey()+"] - Send "+encryptedFile.getType().name()+":"+encryptedFile.getFile().getAbsolutePath());
        ByteBuf header = channelHandlerContext.alloc().buffer(EncryptedFile.LEN_TYPE+EncryptedFile.LEN_KEY+EncryptedFile.LEN_LENGTH);
        header.writeInt(encryptedFile.getType().ordinal());
        header.writeBytes(Unpooled.copiedBuffer(encryptedFile.getKey(), CharsetUtil.UTF_8));
        header.writeLong(encryptedFile.getLength());
        list.add(header);
        list.add(new DefaultFileRegion(encryptedFile.getFile(),0,encryptedFile.getLength()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LoggerFactory.getLogger(EncryptedFileEncoder.class).error(cause.getMessage(),cause);
    }
}
