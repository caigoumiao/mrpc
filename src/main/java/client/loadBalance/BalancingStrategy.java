package client.loadBalance;

import java.util.List;

/**
 * @author larry miao
 * @date 2018-12-12
 */
public enum BalancingStrategy
{
    /**
     * 随机负载均衡策略
     */
    RANDOM{
        @Override
        public int chosenItem(List<String> servers)
        {
            return (int) (System.currentTimeMillis()%servers.size());
        }
    },
    /**
     * 权重负载均衡策略
     */
    WEIGHT
            {
                @Override
                public int chosenItem(List<String> servers)
                {
                    return 0;
                }
            },
    /**
     * 最少连接数
     */
    LEASTCONN
            {
                @Override
                public int chosenItem(List<String> servers)
                {
                    return 0;
                }
            },
    /**
     * ip hash
     */
    IPHASH
            {
                @Override
                public int chosenItem(List<String> servers)
                {
                    return 0;
                }
            };

    public abstract int chosenItem(List<String> servers);
}
