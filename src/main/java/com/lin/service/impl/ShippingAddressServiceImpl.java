package com.lin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lin.common.ResponseCodeEnum;
import com.lin.common.ServerResponse;
import com.lin.dao.ShippingMapper;
import com.lin.pojo.Shipping;
import com.lin.service.IShippingAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("iShippingAddressService")
@Transactional
public class ShippingAddressServiceImpl implements IShippingAddressService{

    @Autowired
    private ShippingMapper shippingMapper;

    public ServerResponse list(Integer userId, Integer pageNum, Integer pageSize) {
        try {
            if (userId != null) {
                PageHelper.startPage(pageNum, pageSize);
                List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
                PageInfo info = new PageInfo(shippingList);
                return ServerResponse.createBySuccess(info);
            }
            return ServerResponse.createByError(ResponseCodeEnum.LOGICAL_EXCEPTION.getCode(), ResponseCodeEnum.LOGICAL_EXCEPTION.getProductDesc());
        } catch (Exception e) {
            return ServerResponse.createByError(ResponseCodeEnum.LOGICAL_EXCEPTION.getCode(), ResponseCodeEnum.LOGICAL_EXCEPTION.getProductDesc());
        }
    }

    public ServerResponse add(Integer userId, Shipping shipping) {
        try {
            if (userId != null) {
                shipping.setUserId(userId);//userId从session中获取更靠谱
                int count = shippingMapper.insert(shipping);//这里新增成功之后利用 mybatis generateor 将数据库生成的主键返回回填到java对象中
                if (count > 0) {
                    return ServerResponse.createBySuccess(shipping);
                }
            }
            return ServerResponse.createByError(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(), ResponseCodeEnum.ILLEGAL_ARGUMENT.getProductDesc());

        }catch (Exception e){
            return ServerResponse.createByError(ResponseCodeEnum.LOGICAL_EXCEPTION.getCode(),ResponseCodeEnum.LOGICAL_EXCEPTION.getProductDesc());
        }
    }

    public ServerResponse del(Integer userId, Integer[] shippingAddressIds){
        try {
            if (userId != null) {
                int count = shippingMapper.updateShippingAddressStatus(userId,shippingAddressIds);
                if (count > 0) {
                    return ServerResponse.createBySuccess("收货地址删除成功");
                }
            }
            return ServerResponse.createByError(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(), ResponseCodeEnum.ILLEGAL_ARGUMENT.getProductDesc());

        }catch (Exception e){
            return ServerResponse.createByError(ResponseCodeEnum.LOGICAL_EXCEPTION.getCode(),ResponseCodeEnum.LOGICAL_EXCEPTION.getProductDesc());
        }
    }

    public ServerResponse update(Integer userId, Shipping shipping){
        try {
            if (userId != null && shipping != null && shipping.getId() != null) {
                //更新操作，不能修改地址的userId
                int count = shippingMapper.updateByUserIdAndShipping(userId, shipping);
                if (count > 0) {
                    return ServerResponse.createBySuccess(shipping);
                }
            }
            return ServerResponse.createByError(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(), ResponseCodeEnum.ILLEGAL_ARGUMENT.getProductDesc());

        }catch (Exception e){
            return ServerResponse.createByError(ResponseCodeEnum.LOGICAL_EXCEPTION.getCode(),ResponseCodeEnum.LOGICAL_EXCEPTION.getProductDesc());
        }
    }

    public ServerResponse searchDetail(Integer userId, Integer shippingId){
        try {
            if (userId != null && shippingId != null) {
                Shipping shipping = shippingMapper.selectByUserIdAndShippingId(userId, shippingId);
                if (shipping != null) {
                    return ServerResponse.createBySuccess(shipping);
                }
            }
            return ServerResponse.createByError(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(), ResponseCodeEnum.ILLEGAL_ARGUMENT.getProductDesc());
        }catch (Exception e){
            return ServerResponse.createByError(ResponseCodeEnum.LOGICAL_EXCEPTION.getCode(),ResponseCodeEnum.LOGICAL_EXCEPTION.getProductDesc());
        }
    }
}
