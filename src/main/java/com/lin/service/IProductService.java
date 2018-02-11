package com.lin.service;

import com.lin.common.ServerResponse;
import com.lin.pojo.Product;

import java.util.List;

public interface IProductService {
    //新增和修改方法
    ServerResponse poductSaveOrUpdate(Product product);

    //更新商品状态
    ServerResponse setProductStatus(Product product);

    //根据商品id获取商品详细信息
    ServerResponse getProductDetail(Integer productId);

    //后台分页查询商品
    ServerResponse searchList(String productName,Integer productId,Integer pageNumber,Integer pageSize);

    //前台用户搜索商品功能
    ServerResponse searchProduct(String searchContent, Integer[] categoryIds, Integer pageNum, Integer pageSize, String[] orderBys);

}
