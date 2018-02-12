package com.lin.service;

import com.lin.common.ServerResponse;
import com.lin.vo.CartViewObject;

public interface ICartService {
    //购物车list
    ServerResponse<CartViewObject> list(Integer userId);

    //商品添加到购物车
    ServerResponse<CartViewObject> add(Integer userId,Integer productId,Integer count);

    //商品移除购物车
    ServerResponse delCartProducts(Integer userId,Integer[] productIds);

    //单选选中或取消
    ServerResponse selectOrUnSelect(Integer userId,Integer productId,Integer checked);
}
