package client;

import examples.TestService;
import util.Contants;

public class TestClient
{
    public static void main(String[] args)
    {
        ServiceDiscovery serviceDiscovery=new ServiceDiscovery(Contants.ZK_URL);
        ServiceImporter serviceImporter=new ServiceImporter(serviceDiscovery);
        TestService testService= (TestService) serviceImporter.importService(TestService.class);
        System.out.println(testService.getUser(22));
    }
}
