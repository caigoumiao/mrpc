package client;

import Codec.RequestBody;
import Codec.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;


/**
 * @author larry miao
 * @date 2018-12-12
 */
public class ServiceImporter
{
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private ServiceDiscovery serviceDiscovery;
    private Map<String, NettyClient> nettyClientMap = new HashMap<>();

    private Map<String, Map<String, Object>> serviceInjectPropMap = new HashMap<>();

    public ServiceImporter(ServiceDiscovery serviceDiscovery)
    {
        this.serviceDiscovery = serviceDiscovery;
    }

    /**
     * 实现服务的每一个注入都可以灵活的设置属性
     *
     * @param key   由beanName+"-"+service 类名组合而成
     * @param attrs Map<String, Object> 属性列表（属性名-> String，属性值-> Object）
     */
    public void addServiceInjectProp(String key , Map<String, Object> attrs)
    {
        if (serviceInjectPropMap.containsKey(key))
            log.warn("Duplicate key[" + key + "] occurred !");
        serviceInjectPropMap.put(key , attrs);
        log.info("add injectProp. key=" + key + ", attrs=" + attrs);
    }

    // todo netty client 什么时候停止，清理掉？设置一个超时清理？(清理连接，并不是清理nettyClient)
    // todo InvocationHandler 可以缓存一下
    /**
     * create proxy class for specified service
     *
     * @param clazz service class
     * @return proxy of service
     */
    public Object importService(Class<?> clazz , String beanName)
    {
        log.info("import service for " + clazz.getName());

        return Proxy.newProxyInstance(
                clazz.getClassLoader() ,
                new Class<?>[] {clazz} ,
                (proxy , method , args) ->
                {
                    if (!nettyClientMap.containsKey(clazz.getName()))
                    {
                        //get server url of service provider
                        String serverUrl = serviceDiscovery.getServerUrlWithBalancing(clazz.getName());
                        String[] url = serverUrl.split(":");
                        String host = url[0];
                        int port = Integer.parseInt(url[1]);
                        log.info("get server url:" + serverUrl);

                        // todo netty client 阻塞
                        NettyClient tmp = new NettyClient();
                        tmp.connect(host , port);
                        nettyClientMap.put(clazz.getName() , tmp);
                    }
                    NettyClient nettyClient = nettyClientMap.get(clazz.getName());
                    RequestBody req = new RequestBody();
                    req.setClassName(clazz.getName());
                    req.setMethodName(method.getName());
                    req.setParameterTypes(method.getParameterTypes());
                    req.setArgs(args);
                    log.info("nettyClient begin to sendMsg");

                    // get result from ResponseBody
                    String keyName = beanName + "-" + clazz.getName();
                    Map<String, Object> attrs = serviceInjectPropMap.get(keyName);
                    ResponseBody response = nettyClient.sendMsg(req , attrs);
                    return response.getBody();
                }
        );
    }
}
