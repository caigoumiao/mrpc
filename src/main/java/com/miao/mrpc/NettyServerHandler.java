package com.miao.mrpc;

import com.miao.mrpc.util.ProtoStuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Method;

public class NettyServerHandler extends ChannelHandlerAdapter
{
    @Override
    public void channelRead(ChannelHandlerContext ctx , Object msg) throws Exception
    {
        ByteBuf buf = (ByteBuf) msg;
        String className = readString(buf);
        String methodName = readString(buf);
        int parameterTypeSize = buf.readInt();
        Class<?>[] parameterTypes = new Class<?>[parameterTypeSize];
        Object[] arguments = new Object[parameterTypeSize];
        for (int i = 0; i < parameterTypeSize; i++)
        {
            int lengthOfType = buf.readInt();
            byte[] tmp = new byte[lengthOfType];
            buf.readBytes(tmp);
            Class<?> clazz = ProtoStuffUtil.deserializer(tmp, Class.class);
            parameterTypes[i] = clazz;

            int lengthOfValue = buf.readInt();
            tmp = new byte[lengthOfValue];
            buf.readBytes(tmp);
            Object obj = ProtoStuffUtil.deserializer(tmp, Object.class);
            arguments[i] = obj;
        }
        Class<?> service = Class.forName(className);
        Method method = service.getMethod(methodName, parameterTypes);
        Object result = method.invoke(service.newInstance(), arguments);
        ctx.writeAndFlush(result);
    }

    private String readString(ByteBuf buf) throws Exception{
        int length = buf.readInt();
        byte[] tmp = new byte[length];
        buf.readBytes(tmp);
        return new String(tmp, "UTF-8");
    }
}
