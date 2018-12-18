package client.config;

import client.ServiceDiscovery;
import client.ServiceImporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author larry miao
 * @date 2018-12-14
 */
@Configuration
@ComponentScan("client")
public class ClientConfig
{
    @Bean
    public ServiceDiscovery serviceDiscovery()
    {
        return new ServiceDiscovery("");
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
