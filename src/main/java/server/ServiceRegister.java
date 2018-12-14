package server;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Contants;

import java.util.*;

/**
 * @author larry miao
 * @date 2018-12-10
 */
public class ServiceRegister implements ApplicationContextAware
{
    private Logger log= LoggerFactory.getLogger(this.getClass());

    private ZkClient zkClient;
    // map serviceApi to serviceImpl
    private Map<String, Object> serviceImplMap = new HashMap<>();

    private ApplicationContext applicationContext;

    public Map<String, Object> getServiceImplMap()
    {
        return serviceImplMap;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext=applicationContext;
    }

    public ServiceRegister(String zkUrl){
        log.info("init zookeeper client");
        zkClient=new ZkClient(zkUrl);
    }

    private void addRootNode(){
        if(!zkClient.exists(Contants.ZK_ROOT)){
            zkClient.createPersistent(Contants.ZK_ROOT);
            log.info("addRootNode: "+Contants.ZK_ROOT);
        }
    }

    private String addServiceNode(String service){
        String serviceNode=Contants.ZK_ROOT+"/"+service;
        if(!zkClient.exists(serviceNode)){
            zkClient.createPersistent(serviceNode);
            zkClient.createPersistent(serviceNode+ Contants.ZK_PROVIDER);
            zkClient.createPersistent(serviceNode+ Contants.ZK_CONSUMER);
        }
        log.info("Add service node: "+serviceNode+Contants.ZK_PROVIDER);
        return serviceNode+Contants.ZK_PROVIDER;
    }

    /**
     * 基于注解 @MRpcService 查找需要暴露的服务
     * @return
     */
    private Set<String> findServicesWithAnnotation()
    {
        applicationContext.getBeansWithAnnotation(MRpcService.class)
                .forEach((key , bean) ->
                {
                    Class<?>[] interfaces = bean.getClass().getInterfaces();
                    if (interfaces.length > 0)
                    {
                        serviceImplMap.put(interfaces[0].getName() , bean);
                        log.info("Found service[" + interfaces[0].getName() + "],Impl is [" + bean.getClass().getName() + "]");
                    }
                });
        return serviceImplMap.keySet();
    }

    public void register(String serverUrl){
        addRootNode();
        Set<String> services=findServicesWithAnnotation();
        services.forEach(s -> {
            String providersNode = addServiceNode(s);
            zkClient.createEphemeral(providersNode+"/"+serverUrl);
        });
        log.info("Register server["+serverUrl+"] to services["+String.join(",", services)+"]");
    }
}
