package client;

import Codec.MRpcEncoder;
import Codec.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;

import java.util.logging.Logger;

public class NettyClient
{
    private Logger log = Logger.getLogger(this.getClass().getName());

    private ChannelFuture future;
    public void connect(String host, int port) throws InterruptedException
    {
        log.info("netty client channel connect");

        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1280)
                .handler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception
                    {
                        ch.pipeline().addLast(new MRpcEncoder());
//                        ch.pipeline().addLast(new LengthFieldPrepender(4));
                        ch.pipeline().addLast(new ObjectDecoder(1024*1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                        ch.pipeline().addLast("clientHandler", new NettyClientHandler());
                    }
                });
        future = bootstrap.connect(host, port).sync();
//        future.channel().closeFuture().sync();
    }

    public Object sendMsg(RpcRequest req){
        log.info("netty client channel send msg");

        NettyClientHandler handler = future.channel().pipeline().get(NettyClientHandler.class);
        return handler.sendMsg(req);
    }
}
