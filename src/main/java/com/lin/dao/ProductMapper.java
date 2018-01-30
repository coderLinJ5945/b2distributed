package com.lin.dao;

import com.lin.pojo.Product;
import com.lin.vo.ProductViewObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    int updateStatusByPrimaryKey(@Param("id") Integer id, @Param("status")Integer status);

    List<Product> selectByNameAndProductId(@Param("productName")String productName, @Param("productId") Integer productId);

    //List<ProductViewObject> selectAllByNameAndProductId(@Param("productName")String productName, @Param("productId") Integer productId);
}