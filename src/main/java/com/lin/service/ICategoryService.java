package com.lin.service;

import com.lin.common.ServerResponse;
import com.lin.pojo.Category;

/**
 * 商品分类接口
 */
public interface ICategoryService {

    //新增商品分类
    ServerResponse<String> addCategory(Category category);

    //更新商品分类
    ServerResponse<String> updateCategoryById(Category category);
}
