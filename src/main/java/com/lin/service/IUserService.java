package com.lin.service;

import com.lin.common.ServerResponse;
import com.lin.pojo.User;

public interface IUserService {
    //登录
    ServerResponse<User> login(String username, String password);

    //注册
    ServerResponse<String> register(User user);

    //校验用户名是否存在
    ServerResponse<String> checkValid(String str,String type);

    //获取验证问题
    ServerResponse<String>  getQuestion(String username);

    //校验用户的问题答案
    ServerResponse<String> checkForgetAnswer(String username,String question,String answer);
}
