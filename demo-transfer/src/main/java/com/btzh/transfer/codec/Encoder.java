package com.btzh.transfer.codec;

import com.btzh.config.Config;
import com.btzh.transfer.entity.AbstractMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 编码器
 * @author wanglidong
 * @date 2017/9/25
 */
public class Encoder extends MessageToMessageEncoder<AbstractMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractMessage abstractMessage, List<Object> out) throws Exception {
        ByteBuf header = ctx.alloc().heapBuffer(abstractMessage.getHeaderLength());
        String messageSendSpeed = Config.getConfig().getMessageSendSpeed();
        if (AbstractMessage.SEND_SPEED_MILLSECOND.equals(messageSendSpeed)) {
            header.writeLong(abstractMessage.getTime());
        } else {
            header.writeInt((int) (abstractMessage.getTime() / 1000L));
        }
        header.writeByte(abstractMessage.getProtocol().ordinal());
        header.writeByte(abstractMessage.getBusinessType().ordinal());
        abstractMessage.encode(header);

        out.add(header);

        Object body = abstractMessage.getBody();
        if (null != body) {
            out.add(body);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
        LoggerFactory.getLogger(Encoder.class).error(e.getMessage(), e);
    }
}