package com.miao.mrpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

public class ServiceImporter<S>
{
    public S importer(Class<?> serviceClass, InetSocketAddress addr){
        return (S) Proxy.newProxyInstance(
                serviceClass.getClassLoader() ,
                new Class<?>[] {serviceClass.getInterfaces()[0]} ,
                new InvocationHandler()
                {
                    @Override
                    public Object invoke(Object proxy , Method method , Object[] args) throws Throwable
                    {
                        NettyClient nettyClient = new NettyClient();
                        nettyClient.connect(addr.getHostName(), addr.getPort());
                        RpcRequest req = new RpcRequest();
                        req.setClassName(serviceClass.getName());
                        req.setMethodName(method.getName());
                        req.setParameterTypes(method.getParameterTypes());
                        req.setArgs(args);
                        return nettyClient.sendMsg(req);
                    }
                }
        );
    }
}
