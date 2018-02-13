package com.lin.service;

import com.lin.common.ServerResponse;
import com.lin.pojo.Shipping;

public interface IShippingAddressService {

    ServerResponse list(Integer userId,Integer pageNum,Integer pageSize);

    ServerResponse add(Integer userId,Shipping shipping);

    ServerResponse del(Integer userId, Integer[] shippingAddressIds);

    ServerResponse update(Integer userId, Shipping shipping);

    ServerResponse searchDetail(Integer userId, Integer shippingId);
}
