package com.lin.util;


import java.math.BigDecimal;

/**
 *  用于商业计算工具类
 */
public class BigDecimalUtil {
    /**
     * BigDecimal 计算经度丢失问题及解决方案：
     *      BigDecimal 商业计算中一定要使用String构造
     */
    public static BigDecimal add (double a , double b){
        BigDecimal ab = new BigDecimal(Double.toString(a));
        BigDecimal bb = new BigDecimal(Double.toString(b));
        return ab.add(bb);
    }

    public static BigDecimal subtract (double a , double b){
        BigDecimal ab = new BigDecimal(Double.toString(a));
        BigDecimal bb = new BigDecimal(Double.toString(b));
        return ab.subtract(bb);
    }

    public static BigDecimal multiply (double a , double b){
        BigDecimal ab = new BigDecimal(Double.toString(a));
        BigDecimal bb = new BigDecimal(Double.toString(b));
        return ab.multiply(bb);
    }

    public static BigDecimal divide (double a , double b){
        BigDecimal ab = new BigDecimal(Double.toString(a));
        BigDecimal bb = new BigDecimal(Double.toString(b));
        return ab.divide(bb,2, BigDecimal.ROUND_HALF_UP);
    }
}
