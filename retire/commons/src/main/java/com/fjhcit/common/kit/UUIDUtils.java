package com.fjhcit.common.kit;

import java.util.UUID;

/**
 * @author sunguoliang
 * @create 2018-09-25 上午11:36
 */
public class UUIDUtils {

    /**
    * 获取UUID
    * @Author sunguoliang
    * @Date 2018/9/25 上午11:37
    */
    public static String getUUID(){
        return UUID.randomUUID().toString();
    }

}