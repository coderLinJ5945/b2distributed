package com.lin.common;

public enum ResponseCodeEnum {
    /**
     * 1、定义几种返回枚举类型
     * 2、使用final 定义 code和desc描述
     * 3、构造器
     * 4、code和desc 用public开放出去
     */
    //定义枚举的时候是用逗号分隔，不是用分号！！！
    SUCCESS(0 ,"SUCCESS","成功"),//成功
    ERROR(1,"ERROR","失败"),//失败
    NEED_LOGIN(10,"NEED_LOGIN","未登录"),//未登录
    ILLEGAL_ARGUMENT(20,"ILLEGAL_ARGUMENT","错误参数"),//错误参数
    LOGICAL_EXCEPTION(30,"LOGICAL_EXCEPTION","后台逻辑异常");//用于try catch 测试

    private final int code;

    private final  String desc;//描述

    private  final  String productDesc;//中文描述

      ResponseCodeEnum(int code,String desc, String productDesc){
        this.code = code;
        this.desc = desc;
        this.productDesc = productDesc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
    public String getProductDesc() {
        return productDesc;
    }
}
