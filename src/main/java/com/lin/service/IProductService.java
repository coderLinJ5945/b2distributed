package com.lin.service;

import com.lin.common.ServerResponse;
import com.lin.pojo.Product;

public interface IProductService {
    //新增和修改方法
    ServerResponse poductSaveOrUpdate(Product product);

    //更新商品状态
    ServerResponse setProductStatus(Product product);

    //根据商品id获取商品详细信息
    ServerResponse getProductDetail(Integer productId);

    //分页查询商品
    ServerResponse searchList(String productName,Integer productId,Integer pageNumber,Integer pageSize);
}
