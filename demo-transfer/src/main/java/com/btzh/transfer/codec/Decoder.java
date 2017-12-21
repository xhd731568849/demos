package com.btzh.transfer.codec;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.btzh.Main;
import com.btzh.config.Config;
import com.btzh.consts.BusinessType;
import com.btzh.consts.Protocol;
import com.btzh.service.MessageProcessor;
import com.btzh.transfer.entity.AbstractMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 解码器
 * @author wanglidong
 * @date 2017/9/25
 */
public class Decoder extends ByteToMessageDecoder {

    private Long time;
    private Protocol protocol;
    private BusinessType businessType;
    private AbstractMessage abstractMessage;

    private boolean checked = false;

    private static volatile ExecutorService businessThreadPool = null;

    public Decoder() {
        if (null == businessThreadPool) {
	    		synchronized (Decoder.class) {
				if (null == businessThreadPool) {
					int businessThreadPoolSize = Config.getConfig().getBusinessThreadPoolSize();
		            ThreadFactory namedThreadFactory = (Runnable r) -> new Thread(r, "business_thread_pool_" + r.hashCode());
		            businessThreadPool = new ThreadPoolExecutor(businessThreadPoolSize, businessThreadPoolSize, 3, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), namedThreadFactory);
				}	
			}
	    }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Config config = Config.getConfig();
        String messageSendSpeed = config.getMessageSendSpeed();
        if (null == time) {
            int len = AbstractMessage.SEND_SPEED_MILLSECOND.equals(messageSendSpeed) ? 8 : 4;
            if (in.readableBytes() < len) {
                return;
            }
            time = (len == 8 ? in.readLong() : in.readInt() * 1000L);
        }
        if (!StringUtils.isEmpty(messageSendSpeed)) {
            //重放攻击保护
            if (!checked && (time + config.getMessageExpireTime() < System.currentTimeMillis())) {
                throw new RuntimeException("abstractMessage expire");
            }
            checked = true;
        }
        if (null == protocol) {
            if (in.readableBytes() < 1) {
                return;
            }
            protocol = Protocol.values()[in.readByte() & 0xFF];
        }
        if (null == businessType) {
            if (in.readableBytes() < 1) {
                return;
            }
            businessType = BusinessType.values()[in.readByte() & 0xFF];
        }
        if (null == abstractMessage) {
            String beanId = protocol.getProtocol();
            Class<?> messageClass = Class.forName(beanId);
            if (!AbstractMessage.class.isAssignableFrom(messageClass)) {
                throw new RuntimeException("not a abstractMessage: " + beanId);
            }
            abstractMessage = (AbstractMessage) messageClass.newInstance();
            abstractMessage.setTime(time);
            abstractMessage.setProtocol(protocol);
            abstractMessage.setBusinessType(businessType);
        }

        if (abstractMessage.decode(in)) {
            abstractMessage.close();

            businessThreadPool.execute(new Runnable() {
                @SuppressWarnings("unchecked")
                @Override
                public void run() {
                    MessageProcessor.class.cast(Main.getApplicationContext().getBean(abstractMessage.getBusinessType().getProcessor())).process(abstractMessage);
                }
            });
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
        LoggerFactory.getLogger(Decoder.class).error(e.getMessage(), e);
        try {
            abstractMessage.close();
        } catch (Exception e1) {
            LoggerFactory.getLogger(Decoder.class).error(e1.getMessage(), e1);
        }
        ctx.close();
    }
}