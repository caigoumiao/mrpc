package server;

import util.Contants;

import javax.activation.MailcapCommandMap;

public class TestServer
{
    public static void main(String[] args)
    {
        ServiceRegister serviceRegister=new ServiceRegister(Contants.ZK_URL);
        MRpcServer server=new MRpcServer("192.168.2.124:9999", serviceRegister);
        try
        {
            server.start();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
