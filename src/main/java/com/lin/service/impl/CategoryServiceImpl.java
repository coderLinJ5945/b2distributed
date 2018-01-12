package com.lin.service.impl;

import com.lin.common.ServerResponse;
import com.lin.dao.CategoryMapper;
import com.lin.pojo.Category;
import com.lin.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

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

    //树展开事件，根据当前节点id 获取当前节点下的（一级）子节点信息
    public ServerResponse<List<Category>>getOneLevelChildCategory(Integer categoryId){
        List<Category> list = categoryMapper.getOneLevelChildCategory(categoryId);
        if(CollectionUtils.isEmpty(list)){//未找到的时候不算报错，只需要日志记录返回前台的data为空就行
            logger.info("商品分类： 未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(list);
    }

}
