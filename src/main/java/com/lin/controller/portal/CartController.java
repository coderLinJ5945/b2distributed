package com.lin.controller.portal;

import com.lin.common.Constant;
import com.lin.common.ResponseCodeEnum;
import com.lin.common.ServerResponse;
import com.lin.pojo.User;
import com.lin.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 *  购物车接口
 *  @author  linj
 */
@Controller
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    private ICartService iCartService;

    /**
     * 获取用户购物车商品信息
     * @param session
     * @return
     */
    @RequestMapping(value = "list.do" , method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ServerResponse list(HttpSession session){

        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return iCartService.list(user.getId());

    }

    /**
     * 购物车中添加或修改商品
     * @param session
     * @param productId
     * @param count
     * @return
     */
    @RequestMapping(value = "saveOrUpdate.do",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ServerResponse saveOrUpdate(HttpSession session , Integer productId , Integer count){
        User user =  (User)session.getAttribute(Constant.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(),ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return iCartService.add(user.getId(),productId,count);
    }

    /**
     * 删除购物车商品
     * @param session
     * @param productIds
     * @return
     */
    @RequestMapping(value = "del.do",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ServerResponse del(HttpSession session , @RequestParam(value ="productIds[]" ) Integer [] productIds ){
        User user =  (User)session.getAttribute(Constant.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(),ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return iCartService.delCartProducts(user.getId(),productIds);
    }

    /**
     * 商品全选 勾选和反选 同一个接口
     * @param session
     * @param checked  0：反选；  1 ：勾选
     * @return
     */
    @RequestMapping(value = "selectAllOrUnSelectAll.do",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ServerResponse selectAllOrUnSelectAll(HttpSession session ,
                                           @RequestParam(value="checked" ,defaultValue = "0") Integer checked){
        User user =  (User)session.getAttribute(Constant.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(),ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),null,checked);
    }

    /**
     * 商品单选 勾选和反选 同一个接口
     * @param session
     * @param productId 商品id
     * @param checked  0：反选；  1 ：勾选
     * @return
     */
    @RequestMapping(value = "selectOrUnSelect.do",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ServerResponse selectOrUnSelect(HttpSession session ,
                                           @RequestParam(value="productId") Integer productId,
                                           @RequestParam(value="checked" ,defaultValue = "0") Integer checked){
        User user =  (User)session.getAttribute(Constant.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(),ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),productId,checked);
    }

}
