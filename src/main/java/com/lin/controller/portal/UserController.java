package com.lin.controller.portal;


import com.lin.common.Constant;
import com.lin.common.ServerResponse;
import com.lin.pojo.User;
import com.lin.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 思路：
     * 1、 Controller ---》 service ---》 serviceImpl ---》dao ---》 xml entity
     * 2、封装一个高复用的统一返回对象类ServerResponse
     * 3、对应的复用ServerResponse返回对象相关的ResponseCode枚举类，用于统一化处理返回状态码
     *
     */
    /**
     *  用户登录
     * @param username
     * @param password
     * @param session(登录传入session？)
     * @return
     */
    @RequestMapping(value = "login.do",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody//将响应对象自动序列化成json
    public Object login(String username, String password, HttpSession session){
        ServerResponse<User> response =  iUserService.login(username,password);
        if(response.isSuccess()){
            //只用将用户信息存入session
            session.setAttribute(Constant.CURRENT_USER,response.getData());
            //session.setAttribute("user",response.getData());//这里可以使用常量来作为key使用
        }
        return response;
    }

    /**
     * 登出
     * @param session
     * @return
     */
    @RequestMapping(value="logout.do", method = {RequestMethod.GET})
    @ResponseBody
    public ServerResponse<String> logOut(HttpSession session){
        session.removeAttribute(Constant.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * 注册
     * @param user
     * @return
     */
    public ServerResponse<String> register(User user){
        //iUserService.register(user);
        return null;
    }



}
