package com.miao.mrpc;

import com.miao.mrpc.util.User;

public interface TestService
{
    String hello(String name);

    User getUser(int a);
}
