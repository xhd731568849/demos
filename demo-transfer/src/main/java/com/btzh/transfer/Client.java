package com.btzh.transfer;

import com.btzh.config.Config;
import com.btzh.transfer.codec.Encoder;
import com.btzh.transfer.entity.AbstractMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.concurrent.atomic.AtomicLong;

/**
 * netty客户端
 * @author wanglidong
 * @date 2017/10/22 15:03.
 */
public class Client {

    private static AtomicLong LAST_SEND_TIME = new AtomicLong();

    public void send(AbstractMessage abstractMessage) {
        Config config = Config.getConfig();
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ChunkedWriteHandler()).addLast(new Encoder());
                }
            });

            Long duration = null;
            String messageSendSpeed = config.getMessageSendSpeed();
            if (AbstractMessage.SEND_SPEED_SECOND.equals(messageSendSpeed)) {
                duration = 1000L;
            } else if (AbstractMessage.SEND_SPEED_MILLSECOND.equals(messageSendSpeed)) {
                duration = 1L;
            }

            if (null == duration) {
                abstractMessage.setTime(System.currentTimeMillis());
            } else {
                for (; ; ) {
                    long now = System.currentTimeMillis();
                    long lastSendTime = LAST_SEND_TIME.get();
                    if (lastSendTime > now) {
                        now = System.currentTimeMillis();
                    }

                    if (lastSendTime + duration > now) {
                        try {
                            Thread.sleep(now - lastSendTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (LAST_SEND_TIME.compareAndSet(lastSendTime, System.currentTimeMillis())) {
                        break;
                    }
                }
                abstractMessage.setTime(LAST_SEND_TIME.get());
            }

            Channel channel = bootstrap.connect(config.getRemoteHost(), config.getRemotePort()).sync().channel();
            channel.writeAndFlush(abstractMessage).addListener(ChannelFutureListener.CLOSE);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}