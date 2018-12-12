package client;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Contants;

import java.util.List;

/**
 * @author larry miao
 * @date 2018-12-12
 */
public class ServiceDiscovery
{
    private Logger log= LoggerFactory.getLogger(this.getClass());
    private ZkClient zkClient;

    public ServiceDiscovery(String zkUrl){
        log.info("init zookeeper client");
        zkClient=new ZkClient(zkUrl);
    }

    // todo 关于负载均衡策略的定义
    // todo server url 缓存一下 太慢了
    /**
     * return serverUrl of service provider with a Load Balancing Strategy
     * @param serviceName service name
     * @return server url
     */
    public String getServerUrlWithBalancing(String serviceName){
        log.info("");
        String providersNode= Contants.ZK_ROOT+"/"+serviceName+Contants.ZK_PROVIDER;
        List<String> servers=zkClient.getChildren(providersNode);
        // 默认负载均衡策略：随机
        int chosenServerId= (int) (System.currentTimeMillis()%servers.size());
        return servers.get(chosenServerId);
    }
}
