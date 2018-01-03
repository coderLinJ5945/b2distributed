package com.lin.service.impl;

import com.lin.common.Constant;
import com.lin.common.ServerResponse;
import com.lin.dao.UserMapper;
import com.lin.pojo.User;
import com.lin.service.IUserService;
import com.lin.util.MD5Util;
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
        //比对密码，需要用md5加密之后的进行比对
        String MD5password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.loginCheckedPwd(username,MD5password);
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
        ServerResponse<String> valid = this.checkValid(user.getUsername(),Constant.USER_NAME);
        if(!valid.isSuccess()){//验证失败 返回验证信息
            return valid;
        }
        valid = this.checkValid(user.getUsername(),Constant.EMAIL);
        if(!valid.isSuccess()){//验证失败 返回验证信息
            return valid;
        }
        /*//验证用户名
        int count = userMapper.checkUsername(user.getUsername());
        if(count > 0){
            return ServerResponse.createByError("该用户已存在");
        }
        //验证email
        count = userMapper.checkEmail(user.getUsername());
        if(count >0){
            return ServerResponse.createByError("该邮箱已被注册");
        }*/
        //给用户添加默认角色
        user.setRole(Constant.Role.ROLE_CUSTOMER);
        //md5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int  count =  userMapper.insert(user);
        if(count == 0){
            return ServerResponse.createByError("注册失败");
        }
        return ServerResponse.createBySuccess("注册成功");

    }

    /**
     *校验
     * @param str 校验内容
     * @param type 校验类型
     * @return
     */
    public ServerResponse<String> checkValid(String str, String type) {
       /*
             StringUtils.isNotEmpty(" ")       = true
              StringUtils.isNotBlank(" ")       = false
        */
        if(StringUtils.isNotBlank(type)){  //后续可以添加多种其他类型进行验证
            switch(type){
                case Constant.USER_NAME:
                    System.out.println("验证类型："+Constant.USER_NAME);
                    int count = userMapper.checkUsername(str);
                    if(count > 0){
                        return ServerResponse.createByError("该用户已存在");
                    }
                    break;
                case Constant.EMAIL:
                    System.out.println("验证类型："+Constant.EMAIL);
                    int countEmail = userMapper.checkEmail(str);
                    if(countEmail >0){
                        return ServerResponse.createByError("该邮箱已被注册");
                    }
                    break;
                default:
                    System.out.println("default");
                    return ServerResponse.createByError("参数错误");
            }
        }else{
            return ServerResponse.createByError("参数错误");
        }
        return  ServerResponse.createBySuccess("验证通过");
    }
}
