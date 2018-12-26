package server;

import Codec.RequestBody;
import Codec.ResponseBody;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author larry miao
 * @date 2018-11-11
 */
public class MRpcHandler extends SimpleChannelInboundHandler<RequestBody>
{
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private Map<String, Object> serviceImplMap;

    MRpcHandler(Map<String, Object> serviceImplMap)
    {
        this.serviceImplMap=serviceImplMap;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx , Throwable cause)
    {
        cause.printStackTrace();
        log.error(cause.getMessage());
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
    protected void messageReceived(ChannelHandlerContext ctx , RequestBody msg)
    {
        log.info("netty server channel receive msg");

        Object serviceImpl=serviceImplMap.get(msg.getClassName());
        Class<?> service = serviceImpl.getClass();
        Object resultValue = null;
        String errorMessage = null;
        try
        {
            Method method = service.getMethod(msg.getMethodName() , msg.getParameterTypes());
            resultValue = method.invoke(serviceImpl , msg.getArgs());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e)
        {
            errorMessage = e.getMessage();
        }

        // 将返回值封装成响应结果
        ResponseBody response = new ResponseBody();
        response.setError(errorMessage);
        response.setBody(resultValue);

        // 模拟服务响应时延2000 ms
        try
        {
            Thread.sleep(2000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        ctx.writeAndFlush(response);
    }
}
