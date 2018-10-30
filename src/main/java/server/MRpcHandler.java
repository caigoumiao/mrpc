package server;

import Codec.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.logging.Logger;

public class MRpcHandler extends SimpleChannelInboundHandler<RpcRequest>
{
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx , Throwable cause) throws Exception
    {
        cause.printStackTrace();
        log.severe(cause.getMessage());
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception
    {
        log.info("netty server channel registered");

        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        log.info("netty server channel active");

        super.channelActive(ctx);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx , RpcRequest msg) throws Exception
    {
        log.info("netty server channel receive msg");

        Class<?> service = Class.forName(msg.getClassName());
        Method method = service.getMethod(msg.getMethodName(), msg.getParameterTypes());
        Object result = method.invoke(service.newInstance(), msg.getArgs());
        ctx.writeAndFlush(result);
    }
}
