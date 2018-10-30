package client;

import Codec.RequestBody;
import io.netty.channel.*;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

public class NettyClientHandler extends SimpleChannelInboundHandler<Object>
{
    private Channel channel;
    private Object result;
    private Logger log = Logger.getLogger(this.getClass().getName());
    private CountDownLatch latch = new CountDownLatch(1);


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception
    {
        log.info("netty client channel registered");
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        log.info("netty client channel active");
        super.channelActive(ctx);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx , Object msg) throws Exception
    {
        log.info("netty client channel receive msg");
        this.result = msg;
        latch.countDown();
    }

    //  todo 请求消息封装成Request对象
    // todo protostuff序列化编解码器
    public Object sendMsg(RequestBody req){

        log.info("netty client handler send msg");

        // todo 请求消息封装成Bytebuf

        channel.writeAndFlush(req).addListener(new ChannelFutureListener()
        {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception
            {
                log.info("netty client receive msg from server");
//                latch.countDown();
            }
        });
        try
        {
            latch.await();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
