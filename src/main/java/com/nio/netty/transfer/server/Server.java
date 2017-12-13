package com.nio.netty.transfer.server;

import com.nio.netty.transfer.config.Config;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.ServerSocket;

/**
 * Created by xuhandong on 17-9-26.
 */
public class Server {
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public void start(Config config){
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.option(ChannelOption.SO_BACKLOG,2048);
            serverBootstrap.childHandler(new ChannelInitializer<ServerSocket>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    channel.pipeline().addLast(new EncryptedFileDecoder(config.getRepository())).addLast(new BusinessProcessor());
                }
            });


        }catch (Exception e){

        }

    }
}
