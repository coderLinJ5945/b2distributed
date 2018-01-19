package com.lin.service;

import com.lin.common.ServerResponse;
import com.lin.pojo.Category;

import java.util.List;

/**
 * 商品分类接口
 */
public interface ICategoryService {

    //新增商品分类
    ServerResponse<String> addCategory(Category category);

    //更新商品分类
    ServerResponse<String> updateCategoryById(Category category);

    //树展开事件，根据当前节点id 获取当前节点下的（一级）子节点信息
    ServerResponse<List<Category>>getOneLevelChildCategory(Integer categoryId);

    //传入商品分类id 执行递归操作，返回当前商品分类的所有树信息
    ServerResponse<List<Category>> getCategoryAndDeepChildrenCategory(Integer gategoryId);
}
