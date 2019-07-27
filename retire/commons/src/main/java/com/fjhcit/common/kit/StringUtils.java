package com.fjhcit.common.kit;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * @Description：字符串处理工具类
 * @author：陈 麟
 * @date：2019年06月04日 下午21:44:01
 */
public class StringUtils {

	public static boolean notNullAndEmpty(String str) {
		if (str == null && "".equals(str)) {
			return false;
		}
		return true;
	}

	public static String listToString(List<?> list, String separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append("'").append(list.get(i)).append("'").append(separator);
		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}

	@SuppressWarnings("rawtypes")
	public static Map transferToLowerCase(Map orgMap) {
		Map<String, String> resultMap = new HashMap<>();
		if (orgMap == null || orgMap.isEmpty()) {
			return resultMap;
		}
		@SuppressWarnings("unchecked")
		Set<Entry> entrySet = orgMap.entrySet();
		for (Entry entry : entrySet) {
			String key = entry.getKey() + "";
			String value = entry.getValue() + "";
			resultMap.put(key.toLowerCase(), value);
		}
		return resultMap;
	}

	public static String getInType(List<String> dataRangelist) {
		if (dataRangelist != null) {
			String temp = "";
			for (String dataRange : dataRangelist) {
				temp = temp + "'" + dataRange + "',";   
			}
			if(temp!="") {
				temp = temp.substring(0, temp.length() - 1); 
			}
			System.out.println(temp);
			return temp;
		}
		return "";
	}

	public static boolean isEmpty(String encryptStr) {
		if(encryptStr!=null && !("".equals(encryptStr))) {
			return false;
		}
		return true;
	}
	public static boolean isNotEmpty(String encryptStr) {
		if(encryptStr!=null && !("".equals(encryptStr))) {
			return true;
		}
		return false;
	}
	/**
	 * 判断List是否为空
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(List<?> list){
		if(null == list || list.size()==0){
			return true;
		}
		return false;
	}
	public static boolean isNotEmpty(List<?> list){
		if(null == list || list.size()==0){
			return false;
		}
		return true;
	}
	//获取当前时间的年度
	public static int getYear() {
		Calendar calendar = null;
		calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(1);
	}
	//获取当前时间的月份
	public static int getMonth() {
		Calendar calendar = null;
		calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return 1 + calendar.get(2);
	}
	//获取当前时间的day
	public static int getDay() {
		Calendar calendar = null;
		calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(5);
	}
	/**
	 * 判断是否为null 或空
	 * @return 若s为空则返回s1，否则返回s
	 */
	public String ChkNull(String s, String s1) {
		if (s == null || s.trim().length() == 0) {
			return s1;
		}else {
			return s;
		}
	}
	
	/**
	 * 字符数据转换成日期数据 格式参见 java.text.SimpleDateFormat 
	 * @param strData 待转换的字符串数据 
	 * 		  strDateFormat 格式化样式
	 * @return 返回转换后的日期数据（根据日期格式）,如传入参数为空则返回null
	 */
	public static Date toDate(String strData, String strDateFormat) {
		try {
			if (strData == null) {
				return null;
			}
			SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
			Date dReturn = sdf.parse(strData);
			return dReturn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//判断s是否是数字 [不包括小数]
	public static boolean isNumeric(String s) {
		int i = s.length();
		for (int j = 0; j < i; j++)
			if (!Character.isDigit(s.charAt(j)))
				return false;
		return true;
	}
		
	//判断s是否是数字 [包括小数]
	public static boolean isNumberOrDecimal(String number){  
		int index = number.indexOf(".");  
		if(index<0){  
			return StringUtils.isNumeric(number);  
		}else{  
			String num1 = number.substring(0,index);  
			String num2 = number.substring(index+1);     
			return StringUtils.isNumeric(num1) && StringUtils.isNumeric(num2);  
		}  
	}
	//判断是否是时间格式   isDate("21011-02-18","yyyy-MM-dd")
	public static boolean isDate(String value,String format){  
        SimpleDateFormat sdf = null;  
        ParsePosition pos = new ParsePosition(0);//指定从所传字符串的首位开始解析  
        if(value == null || isEmpty(format)){  
            return false;  
        }  
        try {  
            sdf = new SimpleDateFormat(format);  
            sdf.setLenient(false);  
            Date date = sdf.parse(value,pos);  
            if(date == null){  
                return false;  
            }else{
                //更为严谨的日期,如2011-03-024认为是不合法的  
                if(pos.getIndex() > sdf.format(date).length()){  
                    return false;  
                }  
                return true;  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }
    }
}