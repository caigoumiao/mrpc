package service;

import api.TestService;
import org.springframework.stereotype.Component;
import server.MRpcService;
import util.User;

@Component
@MRpcService
public class TestServiceImpl implements TestService
{
    @Override
    public String hello(String name)
    {
        return "hello, " + name;
    }

    @Override
    public User getUser(int a)
    {
        User u = new User();
        u.setName("miao");
        u.setAge(a);
        return u;
    }
}
