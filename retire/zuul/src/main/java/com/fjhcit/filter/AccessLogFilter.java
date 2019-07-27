package com.fjhcit.filter;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.fjhcit.common.kit.CommonUtils;
import com.fjhcit.model.ResultVO;
import com.fjhcit.service.FeignBaseServer;
import com.fjhcit.utils.RedisStringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description：网关拦截器（是否登录、菜单权限）
 * @author：陈 麟
 * @date：2019年5月31日 上午10:33:59
 */
@Component
public class AccessLogFilter extends ZuulFilter {
	@Autowired
	private RedisStringUtils redisString;   	//Redis_工具类
	@Autowired
	private FeignBaseServer feignBaseServer;	//网关公共服务_控制器

	private static final Logger logger = LoggerFactory.getLogger(AccessLogFilter.class);

	/**
	 * to classify a filter by type. Standard types in Zuul are "pre" for pre-routing filtering,
	 * "route" for routing to an origin, "post" for post-routing filters, "error" for error handling.
	 * We also support a "static" type for static responses see  StaticResponseFilter.
	 * Any filterType made be created or added and run by calling FilterProcessor.runFilters(type)
	 *
	 * @return A String representing that type
	 */
	@Override
	public String filterType() {
		return FilterConstants.POST_TYPE;
	}

	/**
	 * filterOrder() must also be defined for a filter. Filters may have the same  filterOrder if precedence is not
	 * important for a filter. filterOrders do not need to be sequential.
	 *
	 * @return the int order of a filter
	 */
	@Override
	public int filterOrder() {
		return 0;
	}

	/**
	 * 拦截器开关
	 * a "true" return from this method means that the run() method should be invoked
	 *
	 * @return true if the run() method should be invoked. false will not invoke the run() method
	 */
	@Override
	public boolean shouldFilter() {
		return false;
	}

	/**
	 * 登录拦截和权限验证
	 * if shouldFilter() is true, this method will be invoked. this method is the core method of a ZuulFilter
	 *
	 * @return Some arbitrary artifact may be returned. Current implementation ignores it.
	 */
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		String uri = ctx.getRequest().getRequestURI();
		Map<String, String> map = new LinkedHashMap<>();
		HttpServletRequest request = ctx.getRequest();
		Enumeration<String> enums = request.getHeaderNames();
		while (enums.hasMoreElements()) {
			String key = enums.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		try {
			//请求参数
			String queryParam = JSON.toJSONString(ctx.getRequest().getParameterMap());
			//logger.info("[zuul request info] url: " + uri + "; param: " + queryParam);
			String tokenid=map.get("token"); 
			Claims claims =null;
			//不是登录页，需要验证登录信息
			if(!uri.contains("login")) {
				if(tokenid!=null) {
					tokenid=tokenid.substring(1, tokenid.length()-1);
					byte[] secretKey = Base64.getEncoder().encode(CommonUtils.SECURITY_KEY.getBytes());
					claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(tokenid).getBody();
				}
			}
			/**
            if(true) {
				InputStream is = ctx.getResponseDataStream();
				String response2 = IOUtils.toString(is, Charset.defaultCharset());
				logger.info("[zuul request info] url: " + uri + "; param: " + queryParam + "; [zuul response info]: " + response2);
				insertAuditTemp(userId+"",userName,menu,uri,queryParam,redisString,feignBaseServer,response);
				ctx.setResponseDataStream(IOUtils.toInputStream(response2, Charset.defaultCharset()));
				return null;
			}
			*/
			if(claims == null && !(uri.contains("login"))) {
				ctx.setSendZuulResponse(false);
				ctx.setResponseStatusCode(401);
				ctx.setResponseBody("{\"code\":403,\"msg\":\"用户未登录！\"}");
				ctx.getResponse().setContentType("text/html;charset=UTF-8");
				insertAudit("","","用户未登录！","2","403","1",redisString,feignBaseServer);
				return null;
			}else {
				logger.info("根据"+uri+"判断不同的业务+这个是查询的参数"+queryParam);
				if(claims!=null) {
					String userName = (String) claims.get(CommonUtils.USER_CODE);
					Integer userId = (Integer) claims.get(CommonUtils.USER_ID);
					//获取redis里面数据的存活时间
					String userNameToken = userName + "token";
					String userNamemenu = userName + "menu";
					Long expirationDate = this.redisString.getExpire(userNameToken,TimeUnit.SECONDS);
					String menu= this.redisString.getValue(userNamemenu);
					if(menu!=null) {
						if(!menu.contains(getLast(uri))) {
							ctx.setSendZuulResponse(false);
							ctx.setResponseStatusCode(401);
							ctx.setResponseBody("{\"code\":402,\"msg\":\"没有操作权限！\"}");
							ctx.getResponse().setContentType("text/html;charset=UTF-8");
							this.insertAudit(userId+"",userName,"没有操作权限！","2","402","1",redisString,feignBaseServer);
							return null;
						}
					}
					if(expirationDate > 300){
						this.redisString.expire(userNameToken,1800,TimeUnit.SECONDS);
					}else {
						ctx.setSendZuulResponse(false);
						ctx.setResponseStatusCode(401);
						ctx.setResponseBody("{\"code\":401,\"msg\":\"超时！\"}");
						ctx.getResponse().setContentType("text/html;charset=UTF-8");
						this.insertAudit(userId+"",userName,"超时！","2","401","1",redisString,feignBaseServer);
						return null;
					}
					String response2 ="";
					ResultVO c=new ResultVO();
					c.setSuccess(false);
					InputStream is = ctx.getResponseDataStream();
					if(is!=null) {
						response2 = IOUtils.toString(is, Charset.defaultCharset());
						Gson a=new Gson();
						c=a.fromJson(response2, ResultVO.class);
					}
					logger.info("[zuul request info] url: " + uri + "; param: " + queryParam + "; [zuul response info]: " + response2);
					insertAuditTemp(userId+"",userName,menu,uri,queryParam,redisString,feignBaseServer,c.isSuccess());
					ctx.setResponseDataStream(IOUtils.toInputStream(response2, Charset.defaultCharset()));
				}else{
					InputStream is = ctx.getResponseDataStream();
					String response2 = IOUtils.toString(is, Charset.defaultCharset());
					logger.info("[zuul request info] url: " + uri + "; param: " + queryParam + "; [zuul response info]: " + response2);
					//insertAuditTemp(userId+"",userName,menu,uri,queryParam,redisString,feignBaseServer,response);
					ctx.setResponseDataStream(IOUtils.toInputStream(response2, Charset.defaultCharset()));
				}
			}
		} catch (IOException e) {
			logger.error("记录访问日志异常：", e);
            insertAudit("","","未知异常！","2","401","1",redisString,feignBaseServer);
		}
		return null;
	}
	
	/**
	 *  记录日志（临时方法）
	 * @param loginId
	 * @param userName
	 * @param menu
	 * @param uri
	 * @param queryParam
	 * @param redisString2
	 * @param feignBaseServer2
	 * @param b
	 */
	private void insertAuditTemp(String loginId, String userName, String menu, String uri, String queryParam,
			RedisStringUtils redisString2, FeignBaseServer feignBaseServer2, boolean b) {
		Map<String,String> map=new HashMap<String,String>();
		Gson gs = new Gson();
		@SuppressWarnings("unchecked")
		List<Map<String,String>> muneList  = gs.fromJson(menu,  ArrayList.class);
		map.put("OPER_ITEM", userName);
		map.put("OPER_ID", loginId);
		map.put("IP", "127.0.0.1");
		map.put("OPER_RESULT",b?"200":"500");
		for (Map<String, String> map2 : muneList) {
			if(map2.get("MENUURL").contains(getLast(uri))) {				
				map.put("OPER_TYPE", map2.get("MENUTYPE"));
				map.put("OPER_EVENT_TYPE", map2.get("MENUEVENTTYPE"));
				map.put("OPER_RECODE",  map2.get("MENUNAME")+queryParam);
                    break;
			}	
		}
		feignBaseServer2.insertAudit(map); 
	}

	/**
	 * 通过Feign插入审计数据
	 * @param loginId
	 * @param loginName
	 * @param mes
	 * @param oper_type
	 * @param code
	 * @param isSucc
	 * @param redisString2
	 * @param feignBaseServer2
	 */
	private void insertAudit(String loginId,String loginName ,String mes, String oper_type, String code,String isSucc,
			RedisStringUtils redisString2, FeignBaseServer feignBaseServer2) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("OPER_ITEM", loginName);
		map.put("OPER_TYPE", oper_type);
		map.put("OPER_EVENT_TYPE", code);
		map.put("OPER_ID", loginId);
		map.put("IP", "127.0.0.1");
		map.put("OPER_RECODE", mes);
		map.put("OPER_RESULT",isSucc);
		feignBaseServer2.insertAudit(map);
	}

	/**
	 * 获取请求路径
	 * @param uri
	 * @return
	 */
	private  CharSequence getLast(String uri) {
		String[] temp = uri.split("/");
		return temp[temp.length-1];
	}	
}