package com.lin.dao;

import com.lin.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;


public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByProductIdsAndUserId(@Param("productIds") Set<Integer> productIds,@Param("userId") Integer userId);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    Cart selectByUserIdAndProductId(@Param("userId")Integer userId, @Param("productId")Integer productId);

    List<Cart> selectByUserId(Integer userId);

    int selectCartProductAllCheckedByUserId(Integer userId);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    int selectOrUnSelect(@Param("userId")Integer userId,@Param("productId")Integer productId,@Param("checked")Integer checked);
}