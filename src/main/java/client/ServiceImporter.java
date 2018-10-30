package client;

import Codec.RpcRequest;
import client.NettyClient;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

public class ServiceImporter<S>
{
    public S importer(Class<?> serviceClass, InetSocketAddress addr){
        return (S) Proxy.newProxyInstance(
                serviceClass.getClassLoader() ,
                new Class<?>[] {serviceClass.getInterfaces()[0]} ,
                (proxy , method , args) ->
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
        );
    }
}
