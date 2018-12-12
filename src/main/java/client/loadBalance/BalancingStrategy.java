package client.loadBalance;

/**
 * @author larry miao
 * @date 2018-12-12
 */
public enum BalancingStrategy
{
    RANDOM,
    WEIGHT,
    LEASTCONN,
    IPHASH;
}
