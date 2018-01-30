package com.lin.controller.backstage;

import com.lin.common.Constant;
import com.lin.common.ServerResponse;
import com.lin.pojo.User;
import com.lin.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 后台管理员用户相关接口
 */
@Controller
@RequestMapping(value = "/manage/user")
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    //登录
    @RequestMapping(value = "login.do", method = {RequestMethod.POST})
    @ResponseBody
    public ServerResponse<User> login(HttpSession session, String username, String password){
        ServerResponse<User> response =  iUserService.login(username,password);
        if(response.isSuccess()){
            User user = response.getData();
            if(user.getRole() == Constant.Role.ROLE_ADMIN){
                session.setAttribute(Constant.CURRENT_USER, user);
                //这里不需要返回了
            }else{
                return ServerResponse.createByError("非管理员用户，无法登录后台管理系统");
            }
        }
        return response;
    }

}
