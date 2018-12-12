package server;

import org.I0Itec.zkclient.ZkClient;
import util.Contants;

public class TestServerRegister
{
    public static void main(String[] args)
    {
        ServiceRegister serviceRegister=new ServiceRegister("123.56.24.247:2182");
        MRpcServer server=new MRpcServer("192.168.10.188:9999", serviceRegister);
        try
        {
            server.start();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        ZkClient zkClient=new ZkClient("123.56.24.247:2182");
        zkClient.getChildren(Contants.ZK_ROOT).forEach(System.out::println);
        zkClient.getChildren(Contants.ZK_ROOT+"/examples.TestService/"+Contants.ZK_PROVIDER).forEach(System.out::println);
    }
}
