package client;

import Codec.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

/**
 * @author larry miao
 * @date 2018-12-12
 */
public class ServiceImporter
{
    private Logger log= LoggerFactory.getLogger(this.getClass());
    private ServiceDiscovery serviceDiscovery;
    public ServiceImporter(ServiceDiscovery serviceDiscovery){
        this.serviceDiscovery=serviceDiscovery;
    }

//    public S importer(Class<?> serviceClass, InetSocketAddress addr){
//        return (S) Proxy.newProxyInstance(
//                serviceClass.getClassLoader() ,
//                new Class<?>[] {serviceClass.getInterfaces()[0]} ,
//                (proxy , method , args) ->
//                {
//                    NettyClient nettyClient = new NettyClient();
//                    nettyClient.connect(addr.getHostName(), addr.getPort());
//                    RequestBody req = new RequestBody();
//                    req.setClassName(serviceClass.getName());
//                    req.setMethodName(method.getName());
//                    req.setParameterTypes(method.getParameterTypes());
//                    req.setArgs(args);
//                    return nettyClient.sendMsg(req);
//                }
//        );
//    }

    // todo netty client 需要缓存一下
    /**
     * create proxy class for specified service
     * @param clazz service class
     * @return proxy of service
     */
    public Object importService(Class<?> clazz){
        log.info("import service for "+clazz.getName());
        //get server url of service provider
        String serverUrl=serviceDiscovery.getServerUrlWithBalancing(clazz.getName());
        String[] url=serverUrl.split(":");
        String host=url[0];
        int port = Integer.parseInt(url[1]);
        log.info("get server url:"+serverUrl);
        return  Proxy.newProxyInstance(
                clazz.getClassLoader() ,
                new Class<?>[] {clazz} ,
                (proxy , method , args) ->
                {
                    NettyClient nettyClient = new NettyClient();
                    nettyClient.connect(host, port);
                    RequestBody req = new RequestBody();
                    req.setClassName(clazz.getName());
                    req.setMethodName(method.getName());
                    req.setParameterTypes(method.getParameterTypes());
                    req.setArgs(args);
                    return nettyClient.sendMsg(req);
                }
        );
    }
}
