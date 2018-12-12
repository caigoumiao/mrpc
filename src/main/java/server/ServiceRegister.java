package server;

import org.I0Itec.zkclient.ZkClient;
import util.Contants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ServiceRegister
{
    private ZkClient zkClient;

    public ServiceRegister(String zkUrl){
        zkClient=new ZkClient(zkUrl);
    }

    private void addRootNode(){
        if(!zkClient.exists(Contants.ZK_ROOT)){
            zkClient.createPersistent(Contants.ZK_ROOT);
        }
    }

    private String addServiceNode(String service){
        String serviceNode=Contants.ZK_ROOT+"/"+service;
        if(!zkClient.exists(serviceNode)){
            zkClient.createPersistent(serviceNode);
            zkClient.createPersistent(serviceNode+ Contants.ZK_PROVIDER);
            zkClient.createPersistent(serviceNode+ Contants.ZK_CONSUMER);
        }
        return serviceNode+Contants.ZK_PROVIDER;
    }

    // todo 基于注解@MRpcService 查找要注册的服务
    private List<String> serviceDiscovery(){
        return Collections.singletonList("examples.TestService");
    }

    public void register(String serverUrl){
        addRootNode();
        List<String> services=serviceDiscovery();
        services.forEach(s -> {
            String providersNode = addServiceNode(s);
            zkClient.createEphemeral(providersNode+serverUrl);
        });
    }
}
