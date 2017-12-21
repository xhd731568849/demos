package com.btzh.transfer;

import com.btzh.Main;
import com.btzh.config.Config;
import com.btzh.transfer.codec.Decoder;
import com.btzh.util.RepositoryUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务端
 * @author wanglidong
 * @date 2017/9/25
 */
public class Server {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public void start() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 2048);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new Decoder());
                }
            });

            Config config = Config.getConfig();
            ChannelFuture channelFuture = serverBootstrap.bind(config.getLocalHost(), config.getLocalPort()).sync();
            Logger logger = LoggerFactory.getLogger(Main.class);
            RepositoryUtil.getLocalFilePath();
            logger.info("Server start at " + config.getLocalHost() + ":" + config.getLocalPort());
            logger.info("File repository : " + config.getRepository());
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        try {
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        LoggerFactory.getLogger(Server.class).info("Server shutdown");
    }
}