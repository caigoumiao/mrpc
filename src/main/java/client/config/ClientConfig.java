package client.config;

import client.ServiceDiscovery;
import client.ServiceImporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

/**
 * @author larry miao
 * @date 2018-12-14
 */
@Configuration
@ComponentScan("client")
@PropertySource("client.properties")
public class ClientConfig
{
    @Bean
    public ServiceDiscovery serviceDiscovery(@Value("${zookeeper.url}") String zkUrl)
    {
        return new ServiceDiscovery(zkUrl);
    }

    @Bean
    public ServiceImporter serviceImporter(ServiceDiscovery serviceDiscovery)
    {
        return new ServiceImporter(serviceDiscovery);
    }

    @Bean
    public ClientPostProcessor clientPostProcessor(ServiceImporter serviceImporter)
    {
        return new ClientPostProcessor(serviceImporter);
    }
}
