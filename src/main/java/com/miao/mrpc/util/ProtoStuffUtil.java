package com.miao.mrpc.util;


import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.miao.mrpc.RpcRequest;

public class ProtoStuffUtil
{
    public static <T> byte[] serializer(T o) {
        Schema schema = RuntimeSchema.getSchema(o.getClass());
        return ProtobufIOUtil.toByteArray(o, schema, LinkedBuffer.allocate(256));
    }

    public static <T> T deserializer(byte[] bytes, Class<T> clazz) {
        T obj = null;
        try
        {
            obj = clazz.newInstance();
            Schema schema  =RuntimeSchema.getSchema(obj.getClass());
            ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return obj;
    }

    public static void main(String[] args)
    {
        RpcRequest rpcRequest=new RpcRequest();
        rpcRequest.setClassName("NettyClient");
        rpcRequest.setMethodName("connect");
        rpcRequest.setParameterTypes(null);
        rpcRequest.setArgs(new Object[]{1,"2"});
        byte[] tmp = SerializationUtil.serialize(rpcRequest);
        RpcRequest r2 = SerializationUtil.deserialize(tmp, RpcRequest.class);
        System.out.println(r2);
    }
}
