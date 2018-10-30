package examples;

import util.User;

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
