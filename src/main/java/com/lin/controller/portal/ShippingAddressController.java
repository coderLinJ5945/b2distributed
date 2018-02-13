package com.lin.controller.portal;

import com.lin.common.Constant;
import com.lin.common.ResponseCodeEnum;
import com.lin.common.ServerResponse;
import com.lin.pojo.Shipping;
import com.lin.pojo.User;
import com.lin.service.IShippingAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author linj
 * 收货地址相关接口
 */
@Controller
@RequestMapping("/shippingAddress/")
public class ShippingAddressController {

    @Autowired
    private IShippingAddressService iShippingAddressService;



    @RequestMapping(value = "list.do", method={RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ServerResponse list(HttpSession session,
                               @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(),ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return iShippingAddressService.list(user.getId(),pageNum,pageSize);
    }

    //todo 注意横向越权问题
    /**
     *
     * @param session
     * @param shipping 收货地址对象
     * @return
     */
    @RequestMapping(value = "add.do", method={RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ServerResponse add(HttpSession session, Shipping shipping){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(),ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return iShippingAddressService.add(user.getId(),shipping);
    }

    //执行更新状态操作就行

    /**
     *
     * @param session
     * @param shippingAddressIds 地址主键数组
     * @return
     */
    @RequestMapping(value = "del.do", method={RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ServerResponse del(HttpSession session, @RequestParam(value = "shippingAddressIds[]") Integer [] shippingAddressIds){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(),ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return iShippingAddressService.del(user.getId(),shippingAddressIds);
    }

    @RequestMapping(value = "update.do", method={RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ServerResponse update(HttpSession session, Shipping shipping){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(),ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return iShippingAddressService.update(user.getId(),shipping);
    }

    @RequestMapping(value = "searchDetail.do", method={RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ServerResponse searchDetail(HttpSession session, Integer shippingId){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(),ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return iShippingAddressService.searchDetail(user.getId(),shippingId);
    }

}
