package com.fjhcit.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @Description：web请求拦截器
 * @author：陈 麟
 * @date：2019年5月31日 下午3:02:30
 */
@Configuration // 加上该注解 ，则不需要FeignClient里面加属性configuration
public class FeignHeadersInterceptor implements RequestInterceptor {
	
	/**
	 *  把请求过来的header请求头 原样设置到Feign RequestTemplate请求头中
	 */
	@Override 
	public void apply(RequestTemplate template) {
	    HttpServletRequest request = getHttpServletRequest();
	    String token=(String) request.getSession().getAttribute("token");
	    if (Objects.isNull(request)) {
	    	return;
		}
	    Map<String, String> headers = getHeaders(request);
	    if (headers.size() > 0) {
	    	Iterator<Entry<String, String>> iterator = headers.entrySet().iterator();
	    	while (iterator.hasNext()) {
	    		Entry<String, String> entry = iterator.next();
	    		// 把请求过来的header请求头 原样设置到Feign RequestTemplate请求头中
	    		// 包括token
	    		template.header(entry.getKey(), entry.getValue());
	    	}
	    	template.header("token", token);
		}
	}

	private HttpServletRequest getHttpServletRequest() {
		try {
			//这种方式获取的HttpServletRequest是线程安全的
			return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 获取请求头信息
	 * @param request
	 * @return
	 */
	private Map<String, String> getHeaders(HttpServletRequest request) {
		Map<String, String> map = new LinkedHashMap<>();
		Enumeration<String> enums = request.getHeaderNames();
		while (enums.hasMoreElements()) {
			String key = enums.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		return map;
	}
}