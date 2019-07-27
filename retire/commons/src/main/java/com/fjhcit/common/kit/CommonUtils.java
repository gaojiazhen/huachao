package com.fjhcit.common.kit;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * @Description：对象转换工具类
 * @author：陈 麟
 * @date：2019年06月04日 下午21:35:43
 */
@Component
public class CommonUtils {
	public static final String SECURITY_KEY = "test";
	public static final String USER_CODE = "userName";
	public static final String USER_ID = "id";

	/**
	 * Object对象转换map
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> objectToMap(Object obj) throws Exception {
		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			map.put(field.getName(), field.get(obj) + "");
		}
		return map;
	}

	/**
	 * 对象list转换maplist
	 */
	public static List<Map<String, Object>> objectListToMapList(List<?> list) throws Exception {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (Object object : list) {
			resultList.add(objectToMap(object));
		}
		return resultList;
	}

	/**
	 * 获取当前用户用户名
	 */
	public static String getCurrentUserName() {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();

		HttpServletRequest request = servletRequestAttributes.getRequest();
		Enumeration<String> enums = request.getHeaderNames();
		Map<String, String> map = new LinkedHashMap<>();
		while (enums.hasMoreElements()) {
			String key = enums.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		String token2 = map.get("token");
		if (token2 == null) {
			return null;
		}
		Claims claims = null;
		token2 = token2.substring(1, token2.length() - 1);
		byte[] secretKey = Base64.getEncoder().encode(SECURITY_KEY.getBytes());
		claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token2).getBody();
		if (claims != null) {
			String userId = claims.get(USER_CODE) + "";
			return userId;
		}
		return null;
	}

	public static Map<String,Object> copyMap(Map<?,?> paramMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Iterator<?> it = paramMap.entrySet().iterator();
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			if (key != null && paramMap.get(key) != null) {
				resultMap.put(key.toString(), paramMap.get(key));
			}
		}
		// 执行结束
		return resultMap;
	}

	/**
	  *  获取当前用户id
	 * @return
	 */
	public static String getCurrentUserId() {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = servletRequestAttributes.getRequest();
		Enumeration<String> enums = request.getHeaderNames();
		Map<String, String> map = new LinkedHashMap<>();
		while (enums.hasMoreElements()) {
			String key = enums.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		String token2 = map.get("token");
		if (token2 == null) {
			return null;
		}
		Claims claims = null;
		token2 = token2.substring(1, token2.length() - 1);
		byte[] secretKey = Base64.getEncoder().encode(SECURITY_KEY.getBytes());
		claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token2).getBody();
		if (claims != null) {
			String userId = claims.get(USER_ID) + "";
			return userId;
		}
		return null;
	}
}