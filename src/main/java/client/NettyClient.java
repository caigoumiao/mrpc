package client;

import Codec.MRpcDecoder;
import Codec.MRpcEncoder;
import Codec.RequestBody;
import Codec.ResponseBody;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeoutException;

/**
 * @author larry miao
 * @date 2018-11-11
 */
public class NettyClient
{
    private Logger log= LoggerFactory.getLogger(this.getClass());

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
//                        ch.pipeline().addLast(new ObjectDecoder(1024*1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                        ch.pipeline().addLast(new MRpcDecoder());
                        ch.pipeline().addLast("clientHandler", new NettyClientHandler());
                    }
                });
        future = bootstrap.connect(host, port).sync();
        log.info("netty client has connected.....");
        // todo netty client 什么时候close?
//        future.channel().closeFuture().sync();
//        log.info("netty client closed......");
    }

    // todo 改为异步回调形式
    public ResponseBody sendMsg(RequestBody req) throws TimeoutException
    {
        log.info("netty client send msg");

        NettyClientHandler handler = future.channel().pipeline().get(NettyClientHandler.class);

        // todo 向服务端发送请求前织入一些配置，例如配置
        return handler.sendMsg(req);
    }
}
