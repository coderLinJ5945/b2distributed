package com.lin.service.impl;

import com.lin.common.ServerResponse;
import com.lin.dao.UserMapper;
import com.lin.pojo.User;
import com.lin.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iUserService")
public class UserServiceImpl  implements IUserService{
    //这里是通过扫描包的方式进行进行注入的，这里报找不到路径位置，可以配置idea来取消这个地方的报错
    @Autowired
    private UserMapper userMapper;

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    public ServerResponse<User> login(String username, String password){
        /*
            1、检测用户是否存在
            2、检测密码是否正确
            3、密码正确，清空数据对象
            4、返回登录成功的信息，包含登录成功的信息
         */
        int count = userMapper.checkUsername(username);
        if(count == 0){
            return ServerResponse.createByError("该用户不存在");
        }
        //todo 预留md5登录验证
        User user = userMapper.loginCheckedPwd(username,password);
        if(user == null){
            return ServerResponse.createByError("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);//置空密码

        return ServerResponse.createBySuccess("登录成功",user);
    }

    /**
     * 注册
     * @param user
     * @return
     */
    public ServerResponse<String> register(User user){
        //验证用户名
        return null;
    }
}
