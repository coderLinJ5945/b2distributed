package com.lin.dao;

import com.lin.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    //检测用户名
    int checkUsername(String username);
    //验证用户名密码
    User loginCheckedPwd(@Param("username") String username ,@Param("password") String password);
}