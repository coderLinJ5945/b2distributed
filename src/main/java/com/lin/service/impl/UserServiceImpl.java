package com.lin.service.impl;

import com.lin.common.Constant;
import com.lin.common.ServerResponse;
import com.lin.common.TokenCache;
import com.lin.dao.UserMapper;
import com.lin.pojo.User;
import com.lin.service.IUserService;
import com.lin.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
     *校验用户名是否存在
     * @param str 校验内容
     * @param type 校验类型
     * @return 用户不存在的时候返回 isSuccess() : true
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


    //获取验证问题
    public ServerResponse<String> getQuestion(String username){
        //1、判断用户是否存在；
        ServerResponse<String> checkUserNameResponse = this.checkValid(username,Constant.USER_NAME);
        ServerResponse<String> checkUserEmailResponse = this.checkValid(username,Constant.EMAIL);
        //逻辑取反，checkValid 是判断用户是否存在
        if(checkUserNameResponse.isSuccess()&&checkUserEmailResponse.isSuccess()){
            return ServerResponse.createByError("用户不存在");
        }
        //2、获取用户的问题
        String question = userMapper.getQuestionByUserName(username);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByError("该用户没有设置找回密码的问题");
    }


    //校验用户的问题答案
    public ServerResponse<String> checkForgetAnswer(String username,String question,String answer){
        int count = userMapper.checkForgetAnswer(username,question,answer);
        if(count > 0){ //问题验证成功
            //生成一个token，将token放到缓存中，用于后续重置密码的key
            String forgetToken = UUID.randomUUID().toString();
            //将生成的token放入到guava缓存对象中
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByError("问题回答错误");
    }
}
