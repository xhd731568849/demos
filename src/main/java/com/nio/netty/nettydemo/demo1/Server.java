package com.nio.netty.nettydemo.demo1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by xuhandong on 17-9-27.
 */
public class Server {
    public static final int port = 8999;
    public void start() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();// 引导辅助程序
        EventLoopGroup group = new NioEventLoopGroup();// 通过nio方式来接收连接和处理连接
        try {
            serverBootstrap.group(group);
            serverBootstrap.channel(NioServerSocketChannel.class);// 设置nio类型的channel
            serverBootstrap.localAddress(new InetSocketAddress(port));// 设置监听端口
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {//有连接到达时会创建一个channel
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // pipeline管理channel中的Handler，在channel队列中添加一个handler来处理业务
                    ch.pipeline().addLast("my--Handler",new EchoServerHandler());
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind().sync();// 配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功
            System.out.println(EchoServerHandler.class.getName()+"started and listen on "+channelFuture.channel().localAddress());
            channelFuture.channel().closeFuture().sync();// 应用程序会一直等待，直到channel关闭


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) {
        try{
            new Server().start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
