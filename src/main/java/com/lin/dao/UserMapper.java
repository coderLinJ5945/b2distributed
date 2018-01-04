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
    //检测email
    int checkEmail(String username);

    //验证用户名密码
    User loginCheckedPwd(@Param("username") String username ,@Param("password") String password);

    //获取用户验证的问题
    String getQuestionByUserName(String username);

    /*
        校验用户的问题答案
        因为参数都是String，所以特意加@Param标签区分
     */
    int checkForgetAnswer(@Param("username") String username,@Param("question") String question,@Param("answer") String answer);
}