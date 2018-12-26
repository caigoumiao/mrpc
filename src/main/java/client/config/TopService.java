package client.config;

import api.TestService;
import client.MRpcInjection;
import org.springframework.stereotype.Component;
import util.User;

/**
 * @author larry miao
 * @date 2018-12-18
 */
@Component
public class TopService
{
    @MRpcInjection(timeOut = 1000)
    private TestService testService;

    public void callTestService()
    {
        User u = testService.getUser(21);
        System.out.println(u);

//        String s = testService.hello("miao");
//        System.out.println(s);
    }
}
