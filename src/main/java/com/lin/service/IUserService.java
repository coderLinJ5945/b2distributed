package com.lin.service;

import com.lin.common.ServerResponse;
import com.lin.pojo.User;

public interface IUserService {
    //登录
    ServerResponse<User> login(String username, String password);

    //注册
    ServerResponse<String> register(User user);

    //校验
    ServerResponse<String> checkValid(String str,String type);
}
