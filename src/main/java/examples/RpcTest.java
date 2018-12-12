package examples;

import client.ServiceImporter;
import server.MRpcServer;
import server.ServiceRegister;

import java.net.InetSocketAddress;

public class RpcTest
{
    public static void main(String[] args) throws InterruptedException
    {
        new Thread(() -> {
            try
            {
                ServiceRegister serviceRegister=new ServiceRegister("123.56.24.247:2182");
                new MRpcServer("", serviceRegister).start();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(2000);
        ServiceImporter<TestService> importer = new ServiceImporter<>();
        TestService testService = importer.importer(TestServiceImpl.class, new InetSocketAddress("localhost", 9098));
//        System.out.println(testService.hello("miaoqiao"));
        System.out.println(testService.getUser(22));
    }
}
