package com.lin.controller.backstage;

import com.lin.common.Constant;
import com.lin.common.ResponseCodeEnum;
import com.lin.common.ServerResponse;
import com.lin.pojo.Product;
import com.lin.pojo.User;
import com.lin.service.IProductService;
import com.lin.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 *  商品相关接口（后台）
 */
@Controller("/manage/product/")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;
    //商品保存方法（新增、修改）
    @RequestMapping(value = "productSave.do", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product){
        System.out.println();
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(),ResponseCodeEnum.NEED_LOGIN.getProductDesc());
        }
        ServerResponse response = iUserService.checkAdminRole(user);
        if(response.isSuccess()){
            //执行更新操作
            return iProductService.poductSaveOrUpdate(product);
        }
        return ServerResponse.createByError("无权限操作");
    }

    //更新商品状态（逻辑删除）
    @RequestMapping(value = "setProductStatus.do", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ServerResponse setProductStatus(HttpSession session, Product product){
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(),ResponseCodeEnum.NEED_LOGIN.getProductDesc());
        }
        ServerResponse response = iUserService.checkAdminRole(user);
        if(response.isSuccess()){
            //更新商品状态
            return iProductService.setProductStatus(product);
        }
        return ServerResponse.createByError("无权限操作");
    }


}
