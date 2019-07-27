package com.fjhcit.common.kit;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description：时间格式工具类
 * @author：陈 麟
 * @date：2019年06月04日 下午21:35:58
 */
public class DateUtils {

    public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";

    public static Date getDate(){
        return new Date();
    }

    public static String getDateStr(String pattern){
        if(StringUtils.isBlank(pattern)){
            pattern = DATETIME;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(getDate());
    }
}