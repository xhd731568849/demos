package com.nio.netty.transfer.client;

import com.nio.netty.transfer.config.Config;
import com.nio.netty.transfer.entity.EncryptedFile;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by xuhandong on 17-9-25.
 */
public class Client {
    private Config config;
    public Client(Config config){
        this.config = config;
    }
    public void send(EncryptedFile encryptedFile){
        File file = encryptedFile.getFile();
        if(!file.exists()){
            return ;
        }
        String key = encryptedFile.getKey();
        Logger logger = LoggerFactory.getLogger(Client.class);
        logger.info("["+key+"] - Send "+encryptedFile.getType().name()+":"+encryptedFile.getFile().getAbsolutePath()+" to "+config.getRemoteHost()+":"+config.getRemotePort());

        long fileLength = 0;
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(file,"r")){
            fileLength = randomAccessFile.length();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(fileLength == 0){
            logger.error("["+key+"] - encrypted file's length is zero ");
            return ;
        }
        encryptedFile.setLength(fileLength);

        EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new EncryptedFileEncoder());
                }
            });

            Channel channel = bootstrap.connect(config.getRemoteHost(),config.getRemotePort()).sync().channel();
            channel.writeAndFlush(encryptedFile).addListener(ChannelFutureListener.CLOSE);
            channel.closeFuture().sync();

            logger.info("["+key+"] - send success");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
        }
    }
}
