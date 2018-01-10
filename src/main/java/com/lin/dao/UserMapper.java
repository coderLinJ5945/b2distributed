package com.lin.dao;

import com.lin.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    //只更新传入的部分不为null的字段
    int updateByPrimaryKeySelective(User record);

    //根据用户名修改密码
    int updatePwdByUsername(@Param("username") String username, @Param("newPassword")String newPassword);

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

    // 根据用户名和passwordOld 匹配用户和当前密码
    int selectCountByUsernamePwd(@Param("username") String username,@Param("password") String password);

    //根据主键验证email是否已存在与其他用户
    int checkEmailById(@Param("email")String email, @Param("id")Integer id);
}