package com.lin.controller.backstage;

import com.lin.common.Constant;
import com.lin.common.ResponseCodeEnum;
import com.lin.common.ServerResponse;
import com.lin.pojo.Category;
import com.lin.pojo.User;
import com.lin.service.ICategoryService;
import com.lin.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 商品分类管理
 */
@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    //添加商品分类
    //思路：
    //考虑了一下：添加商品分类这里不需要返回当前分类数据，因为后续会有一个获取所有分类的接口来回填商品分类树
    @RequestMapping(value = "addCategory.do", method = {RequestMethod.POST})
    @ResponseBody
    public ServerResponse<String> addCategory (HttpSession session , Category category){
        //1、判断是否登录
        User user =  (User)session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(),ResponseCodeEnum.NEED_LOGIN.getProductDesc());
        }
        // 2、判断是否是管理员
        ServerResponse roleResponse =  iUserService.checkAdminRole(user);
        if(roleResponse.isSuccess()){
            //3、执行添加操作
            return iCategoryService.addCategory(category);
        }
        return ServerResponse.createByError("无权限操作，需要管理员权限");
    }

    //修改商品分类(这里就不专门的做修改名称了，有可能修改状态、排序等等)
    @RequestMapping(value = "updateCategory.do", method = {RequestMethod.POST})
    @ResponseBody
    public ServerResponse<String> updateCategory(HttpSession session, Category category){
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(),ResponseCodeEnum.NEED_LOGIN.getProductDesc());
        }
        // 2、判断是否是管理员
        ServerResponse roleResponse =  iUserService.checkAdminRole(user);
        if(roleResponse.isSuccess()){
            if(category != null ){
                //执行更新操作
                iCategoryService.updateCategoryById(category);
            }
        }else{
            return ServerResponse.createByError("无权限操作，需要管理员权限");
        }
        return ServerResponse.createByError("参数异常");
    }

    /**
     * 树展开事件，根据当前节点id 获取当前节点下的（一级）子节点信息
     * @param session
     * @param gategoryId 商品分类id
     * @return
     */
    @RequestMapping(value = "getOneLevelChildCategory.do",method = {RequestMethod.POST})
    @ResponseBody
    public ServerResponse getOneLevelChildCategory(HttpSession session,@RequestParam(value = "gategoryId",required = true) Integer gategoryId){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(),ResponseCodeEnum.NEED_LOGIN.getProductDesc());
        }
        ServerResponse roleResponse =  iUserService.checkAdminRole(user);
        if(roleResponse.isSuccess()){
            return iCategoryService.getOneLevelChildCategory(gategoryId);
        }else{
            return ServerResponse.createByError("无权限操作，需要管理员权限");
        }
    }

    /**
     * 递归获取当前商品分类的所有子商品信息
     * @param session
     * @param gategoryId
     * @return
     */
    @RequestMapping(value = "getCategoryAndDeepChildrenCategory.do",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory (HttpSession session, @RequestParam(value = "gategoryId",required = true ) Integer gategoryId){
        /**思路：1.判断用户是否登录 2.判断用户是否有权限 3、传入商品分类id 执行递归操作，返回当前商品分类的所有树信息*/

        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(),ResponseCodeEnum.NEED_LOGIN.getProductDesc());
        }
        ServerResponse roleResponse =  iUserService.checkAdminRole(user);
        if(roleResponse.isSuccess()){
            // 3、传入商品分类id 执行递归操作，返回当前商品分类的所有树信息
            return iCategoryService.getCategoryAndDeepChildrenCategory(gategoryId);
        }else{
            return ServerResponse.createByError("无权限操作，需要管理员权限");
        }
    }

    /*
        todo
        不明白为什么少一个删除接口，数据库只设计了一个status字段，
        如果用status字段进行逻辑删除，则执行更新操作修改status就行
     */




}
