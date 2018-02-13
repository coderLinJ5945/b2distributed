package com.lin.dao;

import com.lin.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int updateShippingAddressStatus(@Param("userId")Integer userId, @Param("shippingAddressIds")Integer[] shippingAddressIds);

    int updateByUserIdAndShipping(@Param("userId")Integer userId, @Param("shipping")Shipping shipping);

    Shipping selectByUserIdAndShippingId(@Param("userId")Integer userId, @Param("shippingId")Integer shippingId);

    List<Shipping> selectByUserId(Integer userId);

}