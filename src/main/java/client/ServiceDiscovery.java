package client;

import client.loadBalance.BalancingStrategy;
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
    private BalancingStrategy strategy;

    public ServiceDiscovery(String zkUrl){
        this(zkUrl, BalancingStrategy.RANDOM);
    }

    public ServiceDiscovery(String zkUrl, BalancingStrategy _strategy){
        log.info("init zookeeper client");
        this.strategy=_strategy;
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
        if(servers.size()==0) throw new IllegalStateException("There is no service provider for "+serviceName);

        // 根据配置的策略选择server
        int chosenServerId= strategy.chosenItem(servers);
        return servers.get(chosenServerId);
    }
}
