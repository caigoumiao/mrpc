package client;

import Codec.RequestBody;
import Codec.ResponseBody;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * @author larry miao
 * @date 2018-11-11
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<Object>
{
    private Channel channel;
    private ResponseBody result;
    private Logger log = LoggerFactory.getLogger(this.getClass());
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
    protected void messageReceived(ChannelHandlerContext ctx , Object msg)
    {
        log.info("netty client channel receive msg");
        this.result = (ResponseBody) msg;
        latch.countDown();
    }

    public ResponseBody sendMsg(RequestBody req)
    {

        log.info("netty client handler send msg");

        channel.writeAndFlush(req).addListener(new ChannelFutureListener()
        {
            @Override
            public void operationComplete(ChannelFuture future)
            {
                log.info("netty client finish send msg");
            }
        });
        try
        {
            // todo 设置请求超时时间，防止一直阻塞
            // todo 使用Future 改写
            // todo 判断返回结果是超时 or 成功， 若超时客户端需抛出异常
            latch.await();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
