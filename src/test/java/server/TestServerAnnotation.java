package server;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import server.Config.ServerConfig;

public class TestServerAnnotation
{
    public static void main(String[] args)
    {
        AnnotationConfigApplicationContext configApplicationContext=new AnnotationConfigApplicationContext(ServerConfig.class);
        MRpcServer server=configApplicationContext.getBean(MRpcServer.class);
        try
        {
            server.start();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
