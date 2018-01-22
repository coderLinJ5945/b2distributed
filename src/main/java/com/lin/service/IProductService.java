package com.lin.service;

import com.lin.common.ServerResponse;
import com.lin.pojo.Product;

public interface IProductService {
    //新增和修改方法
    ServerResponse poductSaveOrUpdate(Product product);
    //更新商品状态
    ServerResponse setProductStatus(Product product);
}
