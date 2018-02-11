package com.lin.common;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

public class Constant {
    public static final String CURRENT_USER = "currentUser";

    public static final String USER_NAME = "userName";

    public static final String EMAIL = "email";

    //通过接口做一个角色分类
    public interface Role{
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1;//管理员
    }
    //商品排序字段
    public interface ProductListOrderBy{
        Set<String> PRODUCT_ASC_DESC = Sets.newHashSet("price_desc","price_asc","name_desc","name_asc");
    }

}
