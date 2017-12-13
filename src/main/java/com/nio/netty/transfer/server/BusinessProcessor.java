package com.nio.netty.transfer.server;

import com.nio.netty.transfer.entity.EncryptedFile;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xuhandong on 17-9-26.
 */
public class BusinessProcessor extends SimpleChannelInboundHandler<EncryptedFile> {

    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(20);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, EncryptedFile msg) throws Exception {
        THREAD_POOL.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(msg);
            }
        });
    }
}
