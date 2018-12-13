package server;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import server.Config.ServerConfig;

public class TestServiceFound
{
    public static void main(String[] args)
    {
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(ServerConfig.class);
        context.getBeansWithAnnotation(MRpcService.class).entrySet().forEach(e -> {
            System.out.println("bean name:"+e.getKey());
            System.out.println("bean class name:"+e.getValue().getClass().getInterfaces()[0].getName());
        });
    }
}
