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

    //重置填写新密码1
    ServerResponse<String> resetPassword1(String username, String newPassword, String forgetToken);

    //重置填写新密码2
    ServerResponse<String> resetPassword2(String username, String newPassword, String forgetToken);

    //登录成功后修改密码
    ServerResponse<String> updatePassword(User user, String passwordOld, String passwordNew);

    //修改用户信息
    ServerResponse<User> updateUser(User user);

}
