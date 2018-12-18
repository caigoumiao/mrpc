package client;

import api.TestService;
import client.config.ClientConfig;
import client.config.TopService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import util.User;

public class TestClientAutowired
{
    public static void main(String[] args)
    {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ClientConfig.class);
        context.getBean(TopService.class).callTestService();
    }
}
