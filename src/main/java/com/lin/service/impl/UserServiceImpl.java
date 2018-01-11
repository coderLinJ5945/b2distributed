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
        //int  count =  userMapper.insert(user);
        int  count =  userMapper.insertSelective(user);
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

    //重置填写新密码
    public ServerResponse<String> resetPassword1(String username, String newPassword, String forgetToken){
        // 1、先验证username
        ServerResponse<String> checkNameResponse =  this.checkValid(username,Constant.USER_NAME);
        if(checkNameResponse.isSuccess()){
            //判断用户，如果不存在，就返回该用户不存在，用户不存在就是用username和email验证都返回的是true
            return ServerResponse.createByError("该用户不存在");
        }
        //2、用户存在之后验证 token，如果 token验证成功，则执行更新该用户密码的操作
        //如果用户存在，传入的username就不可能为空，所以不需要做非空判断
        if(!StringUtils.isNotBlank(forgetToken)){
            return ServerResponse.createByError("参数错误： token不能为空");
        }
        String key =  TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);

        if(StringUtils.equals(forgetToken, key)){
            //验证成功执行更新密码操作
            //注意：这里密码是需要加密后的密码存入数据库中的
            String MD5NewPassword = MD5Util.MD5EncodeUtf8(newPassword);
            int count = userMapper.updatePwdByUsername(username,MD5NewPassword);
            if(count >0){
                return ServerResponse.createBySuccess("修改密码成功");
            }
        }else{
            return ServerResponse.createByError("token验证失败");
        }
        return ServerResponse.createByError("修改密码失败");

        /*if(!forgetToken.equals(key)){  //可以用StringUtils里的方法验证，就不会出现nullpointE异常
            return ServerResponse.createByError("token验证错误");
        }*/

    }

    //重置填写新密码2 ,这个相对的1来说多判断了一个密码不为空
    public ServerResponse<String> resetPassword2(String username, String newPassword, String forgetToken){
        //1、先验证所有参数是否为空
        if(StringUtils.isNotBlank(username)&&StringUtils.isNotBlank(newPassword)&&StringUtils.isNotBlank(forgetToken)){
            //2、判断该用户是否存在
            ServerResponse<String> checkNameResponse =  this.checkValid(username,Constant.USER_NAME);
            if(checkNameResponse.isSuccess()){
                //判断用户，如果不存在，就返回该用户不存在，用户不存在就是用username和email验证都返回的是true
                return ServerResponse.createByError("该用户不存在");
            }
            //3、验证token
            String key =  TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
            if(forgetToken.equals(key)){
                //4、执行更新操作
                //注意：这里密码是需要加密后的密码存入数据库中的
                String MD5NewPassword = MD5Util.MD5EncodeUtf8(newPassword);
                int count = userMapper.updatePwdByUsername(username,MD5NewPassword);
                if(count >0){
                    return ServerResponse.createBySuccess("修改密码成功");
                }
            }else{
                return ServerResponse.createByError("token验证失败");
            }

        }else{
            return ServerResponse.createByError("参数异常：用户名、密码或者token为空");
        }
        return ServerResponse.createBySuccess("修改密码成功");
    }


    //登录成功后修改密码
    public ServerResponse<String> updatePassword(User user, String passwordOld, String passwordNew){
        //1、判断参数不能为空
        if(StringUtils.isNotBlank(passwordOld)&&StringUtils.isNotBlank(passwordNew)){
            //2、根据用户名和passwordOld 匹配用户和当前密码
            //注意的是这里面需要md5加密
            String MD5passwordOld = MD5Util.MD5EncodeUtf8(passwordOld);
            int count = userMapper.selectCountByUsernamePwd(user.getUsername(),MD5passwordOld);
            if(count > 0){//验证该用户成功，执行修改密码操作
                String MD5passwordNew = MD5Util.MD5EncodeUtf8(passwordNew);
                int countNew = userMapper.updatePwdByUsername(user.getUsername(),MD5passwordNew);
                if(countNew > 0){
                    return ServerResponse.createBySuccess("修改密码成功");
                }
            }else{
                return ServerResponse.createByError("用户验证失败：该用户和密码不比配");
            }
        }else{
            return ServerResponse.createByError("参数异常：密码不能为空");
        }
        return ServerResponse.createByError("修改密码错误");

    }

    //修改用户信息
    public ServerResponse<User> updateUser(User user){
        //1、username不能被更改
        /*
            2、email更改的时候需要做一个验证，如果email是已存在其他用户的email，则更新失败
            登录可以使用email，所以email必须要和id 一对一绑定
         */

        if(StringUtils.isNotBlank(user.getEmail())){
            int count = userMapper.checkEmailById(user.getEmail(), user.getId());
            if(count > 0){//email已经被其他用户使用
                return ServerResponse.createByError("email已被使用，请更换绑定的email");
            }
        }
        user.setUsername(null);//用户名不允许修改，将用户名置空
        int count = userMapper.updateByPrimaryKeySelective(user);
        if(count > 0){
            User newUser = userMapper.selectByPrimaryKey(user.getId());
            newUser.setPassword(StringUtils.EMPTY);//置空密码
            return ServerResponse.createBySuccess("修改信息成功",newUser);
        }
        //这样写是不是太麻烦了？这里确实有点费解，当执行更新操作成功之后，返回的user对象是应该返回该对象的所有数据进行回填么？
        /*updateUser.setId(user.getId());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());*/
        return ServerResponse.createByError("修改信息失败");
    }


    //检查是否是管理员用户,不需要查询数据库
    public ServerResponse checkAdminRole(User user){
        Integer role = user.getRole();
        if(role == Constant.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

}
