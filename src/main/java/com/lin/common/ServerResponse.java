package com.lin.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 *  统一的Response返回的泛型类(泛型的好处，返回时可以指定也可以不指定里面返回的数据)
 * @param <T>
 */
@JsonSerialize(include =  JsonSerialize.Inclusion.NON_NULL)//SpringMVC注解，如果返回的字段值为null，则不返回key
public class ServerResponse<T> implements Serializable {
    /**
     * 1、response返回前端所需的3种数据
     * 2、多种构造器（）new初始化对象
     */

    private int status;//状态码 0：成功  其他：失败
    private String msg;//返回的状态码对应的信息
    private T data;//返回的数据

    //私有构造方法，用于不同参数的初始化, String和T组合没有多大意义，所以省略
    private ServerResponse(int status){
        this.status = status;
    }
    private ServerResponse(int status,String msg){
        this.status = status;
        this.msg = msg;
    }
    private ServerResponse(int status,T data){
        this.status = status;
        this.data = data;
    }
    private ServerResponse(int status,String msg,T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    //这些数据的传递方向都是从后台到前端，所以不需要set方法
    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    @JsonIgnore
    //返回的json对象中，忽略isSuccess字段
    public boolean isSuccess(){
        return this.status == ResponseCodeEnum.SUCCESS.getCode();
    }

    //多种构造器，用于生成对应的返回对象 ，泛型的相关知识点？
    public static <T>  ServerResponse<T> createBySuccess(){
        return  new ServerResponse<T>(ResponseCodeEnum.SUCCESS.getCode());
    }

    public static <T>  ServerResponse<T> createBySuccess(String msg){
        return  new ServerResponse<T>(ResponseCodeEnum.SUCCESS.getCode(),msg);
    }

    public static <T>  ServerResponse<T> createBySuccess(T data){
        return  new ServerResponse<T>(ResponseCodeEnum.SUCCESS.getCode(),data);
    }

    public static <T>  ServerResponse<T> createBySuccess(String msg,T data){
        return  new ServerResponse<T>(ResponseCodeEnum.SUCCESS.getCode(),msg,data);
    }
    public static <T>  ServerResponse<T> createBySuccess(int code,String msg,T data){
        return  new ServerResponse<T>(code,msg,data);
    }

    public static <T> ServerResponse<T> createByError(){
        return  new ServerResponse<T>(ResponseCodeEnum.ERROR.getCode(),ResponseCodeEnum.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByError(String  errorMsg){
        return  new ServerResponse<T>(ResponseCodeEnum.ERROR.getCode(),errorMsg);
    }
    public  static <T> ServerResponse<T> createByError(int code, String  errorMsg){//自定义error
        return  new ServerResponse<T>(code,errorMsg);
    }


}
