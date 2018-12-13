package server.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import server.MRpcServer;
import server.ServiceRegister;
import util.Contants;

/**
 * @author larry miao
 * @date 2018-12-13
 */
@Configuration
@ComponentScan("service")
@PropertySource("server.properties")
public class ServerConfig
{
    @Value("${server.url}")
    private String serverUrl;

    @Bean
    public ServiceRegister serviceRegister(){
        return new ServiceRegister(Contants.ZK_URL);
    }

    @Bean
    public MRpcServer mRpcServer(ServiceRegister serviceRegister){
        return new MRpcServer(serverUrl, serviceRegister);
    }
}
