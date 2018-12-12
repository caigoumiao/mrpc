package server;

import Codec.MRpcDecoder;
import Codec.MRpcEncoder;
import Codec.RequestBody;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;
import server.MRpcHandler;

import java.util.Scanner;

public class MRpcServer
{
    private String serverUrl;
    private ServiceRegister serviceRegister;

    public MRpcServer(String serverUrl, ServiceRegister serviceRegister){
        this.serverUrl=serverUrl;
        this.serviceRegister=serviceRegister;
    }

    public void start() throws InterruptedException
    {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    protected void initChannel(SocketChannel ch)
                    {
//                            ch.pipeline().addLast(new ObjectEncoder());
                        ch.pipeline().addLast(new MRpcEncoder());
//                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(169373290, 0 ,4, 0 ,4));
                        ch.pipeline().addLast(new MRpcDecoder(RequestBody.class));
                        ch.pipeline().addLast(new MRpcHandler(serviceRegister.getServiceImplMap()));
                    }
                });

        String[] url=serverUrl.split(":");
        String host=url[0];
        int port=Integer.parseInt(url[1]);
        ChannelFuture future = serverBootstrap.bind(port).sync();

        if(serviceRegister!=null){
            serviceRegister.register(serverUrl);
        }

        future.channel().closeFuture().sync();
    }
}
