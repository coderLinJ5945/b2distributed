package com.lin.common;

public class Constant {
    public static final String CURRENT_USER = "currentUser";

    public static final String USER_NAME = "userName";

    public static final String EMAIL = "email";

    //通过接口做一个角色分类
    public interface Role{
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1;//管理员
    }
}
