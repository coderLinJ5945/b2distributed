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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 *  商品相关接口（后台）
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    /**
     * 商品保存方法（新增、修改）ok
     * @param session
     * @param product
     * @return
     */
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

    /**
     * 更新商品状态（逻辑删除）ok
     * @param session
     * @param product
     * @return
     */
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

    /**
     * 产品 SearchList ,不传分页参数，默认是查询所有
     * @param session
     * @param productName
     * @param productId
     * @param pageNumber  用int的原因，设置了默认值，所以不可能为null，如果不设置默认值建议用Integer
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "searchList.do", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ServerResponse searchList(HttpSession session, String productName, Integer productId ,@RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getProductDesc());
        }
        ServerResponse response = iUserService.checkAdminRole(user);
        if (response.isSuccess()) {
            return iProductService.searchList(productName,productId,pageNumber,pageSize);
        }
        return ServerResponse.createByError("无权限操作");
    }

    /**
     * 产品详情  ok
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "detail.do", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ServerResponse detail(HttpSession session , Integer productId){
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getProductDesc());
        }
        ServerResponse response = iUserService.checkAdminRole(user);
        if (response.isSuccess()) {
            return iProductService.getProductDetail(productId);
        }
        return ServerResponse.createByError("无权限操作");
    }


    //3.富文本上传图片
    //4.图片上传


}
