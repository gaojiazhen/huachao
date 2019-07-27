package com.fjhcit.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * @Description：Redis工具类
 * @author：陈 麟
 * @date：2019年06月03日 上午11:17:35
 */
@Component
public class RedisStringUtils {
	@Autowired
    private  StringRedisTemplate template;
 
    public  void setKey(String key,String value){
        ValueOperations<String, String> ops = template.opsForValue();

        ops.set(key,value);
    }
 
    public  String getValue(String key){
        ValueOperations<String, String> ops = template.opsForValue();
        return ops.get(key);
    }

    public  Boolean expire(String key,int i,TimeUnit TimeUnit){
    	return	template.expire(key,i,TimeUnit);
    }
    
    public  void delKey(String key){
        template.delete(key);
    }
    /**
     * 获取过期时间
     * @param key
     * @param TimeUnit
     * @return
     */
    public  Long getExpire(String key,TimeUnit TimeUnit){
    	return	template.getExpire(key,TimeUnit);
    }
}