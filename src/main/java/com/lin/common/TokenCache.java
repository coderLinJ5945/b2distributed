package com.lin.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * token 缓存类
 */
public class TokenCache {
    //日志对象
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    //token前缀的常量标记
    public static final String TOKEN_PREFIX = "token_";

    /*
     * 1、LRU算法扩大内存
     * 2、CacheBuilder ： guava的缓存对象
     * 3、initialCapacity ：初始化大小
     * 4、maximumSize ： 最大扩容
     * 5、expireAfterAccess ： 存储的时间
     */
    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //默认的数据加载实现,当调用get取值的时候,如果key没有对应的值,就调用这个方法进行加载.
                @Override
                public String load(String s) throws Exception {
                    //防止null用equal比较报nullpoint异常，转成null字符串
                    return "null";
                }
            });
    //缓存对象get和set方法，方便后续操作
    public static void setKey(String key,String value){
        localCache.put(key,value);
    }

    public static String getKey(String key){
        String value = null;
        try {
            value = localCache.get(key);
            if("null".equals(value)){
                return null;
            }
            return value;
        }catch (Exception e){
            logger.error("localCache get error",e);
        }
        return null;
    }
}
