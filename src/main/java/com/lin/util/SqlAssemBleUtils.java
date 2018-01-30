package com.lin.util;

import org.apache.commons.lang3.StringUtils;

public class SqlAssemBleUtils {

    //like语句全匹配拼接
    public static String assembleSqlLike(String sqlstr) {
        if (StringUtils.isNotBlank(sqlstr)) {
            sqlstr = new StringBuffer().append("%").append(sqlstr).append("%").toString();
        }
        return sqlstr;
    }
}
