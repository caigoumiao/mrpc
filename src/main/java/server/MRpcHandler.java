package server;

import Codec.RequestBody;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Logger;

public class MRpcHandler extends SimpleChannelInboundHandler<RequestBody>
{
    private Logger log = Logger.getLogger(this.getClass().getName());

    private Map<String, Object> serviceImplMap;

    MRpcHandler(Map<String, Object> serviceImplMap)
    {
        this.serviceImplMap=serviceImplMap;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx , Throwable cause)
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
    protected void messageReceived(ChannelHandlerContext ctx , RequestBody msg) throws Exception
    {
        log.info("netty server channel receive msg");

        Object serviceImpl=serviceImplMap.get(msg.getClassName());
        Class<?> service = serviceImpl.getClass();
        Method method = service.getMethod(msg.getMethodName(), msg.getParameterTypes());
        Object result = method.invoke(service.newInstance(), msg.getArgs());
        ctx.writeAndFlush(result);
    }
}
