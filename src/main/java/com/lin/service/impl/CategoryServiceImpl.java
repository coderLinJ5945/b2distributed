package com.lin.service.impl;

import com.lin.common.ServerResponse;
import com.lin.dao.CategoryMapper;
import com.lin.pojo.Category;
import com.lin.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{

    @Autowired
    private CategoryMapper categoryMapper;

    //新增商品分类
    public ServerResponse<String> addCategory(Category category){
        if(category != null){
           int count =  categoryMapper.insertSelective(category);
           if(count > 0){
                return ServerResponse.createBySuccess("商品分类添加成功");
           }
        }

        return ServerResponse.createByError("商品分类新增失败");
    }

    //更新商品分类
    public ServerResponse<String> updateCategoryById(Category category){
        if(category.getId() != null){
            //按道理应该是去数据库根据id 查询一次，看看有没有该条数据
            int count = categoryMapper.updateByPrimaryKeySelective(category);
            if(count > 0 ){
                return ServerResponse.createBySuccess("更新商品分类成功");
            }
        }else{
            return ServerResponse.createByError("更新商品分类失败");
        }
        return ServerResponse.createByError("商品分类参数异常");
    }

}
