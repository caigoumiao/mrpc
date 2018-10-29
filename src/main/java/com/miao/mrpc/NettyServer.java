package com.miao.mrpc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class NettyServer
{
    public void bind(int port) throws InterruptedException
    {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try
        {
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception
                        {
                            ch.pipeline().addLast(new ObjectEncoder());
//                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(169373290, 0 ,4, 0 ,4));
                            ch.pipeline().addLast(new MRpcDecoder());
                            ch.pipeline().addLast(new MRpcHandler());
                        }
                    });

            ChannelFuture future = serverBootstrap.bind(port).sync();
        } finally
        {

        }
    }
}
