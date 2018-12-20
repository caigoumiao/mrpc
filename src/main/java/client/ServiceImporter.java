package client;

import Codec.RequestBody;
import Codec.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;


/**
 * @author larry miao
 * @date 2018-12-12
 */
public class ServiceImporter
{
    private Logger log= LoggerFactory.getLogger(this.getClass());
    private ServiceDiscovery serviceDiscovery;
    private Map<String, Object> serviceProxyMap=new HashMap<>();
    private Map<String, NettyClient> nettyClientMap=new HashMap<>();
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
    // todo netty client 什么时候停止，清理掉？
    // todo proxy 缓存是否有必要？不能缓存
    /**
     * create proxy class for specified service
     * @param clazz service class
     * @return proxy of service
     */
    public Object importService(Class<?> clazz){
        log.info("import service for "+clazz.getName());
//        if(serviceProxyMap.containsKey(clazz.getName()))
//            return serviceProxyMap.get(clazz.getName());
        return  Proxy.newProxyInstance(
                clazz.getClassLoader() ,
                new Class<?>[] {clazz} ,
                (proxy , method , args) ->
                {
                    if(!nettyClientMap.containsKey(clazz.getName())){
                        //get server url of service provider
                        String serverUrl=serviceDiscovery.getServerUrlWithBalancing(clazz.getName());
                        String[] url=serverUrl.split(":");
                        String host=url[0];
                        int port = Integer.parseInt(url[1]);
                        log.info("get server url:"+serverUrl);

                        // todo netty client 阻塞
                        NettyClient tmp = new NettyClient();
                        tmp.connect(host, port);
                        nettyClientMap.put(clazz.getName(), tmp);
                    }
                    NettyClient nettyClient=nettyClientMap.get(clazz.getName());
                    RequestBody req = new RequestBody();
                    req.setClassName(clazz.getName());
                    req.setMethodName(method.getName());
                    req.setParameterTypes(method.getParameterTypes());
                    req.setArgs(args);
                    log.info("nettyClient begin to sendMsg");

                    // get result from ResponseBody
                    ResponseBody response = (ResponseBody) nettyClient.sendMsg(req);
                    return response.getBody();
                }
        );
    }
}
