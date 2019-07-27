package com.fjhcit.filter;

import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.fjhcit.common.kit.CommonUtils;
import com.fjhcit.common.kit.SymmetricEncoder;
import com.fjhcit.service.FeignBaseServer;
import com.fjhcit.utils.RedisStringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * @Description：网关拦截器（超时、重放、窜改、登录、权限）
 * @author：陈 麟
 * @date：2019年5月31日 上午10:35:05
 */
@Component
public class PowerFilter extends ZuulFilter{
	@Autowired
	private RedisStringUtils redisString;     //Redis_工具类
	@Autowired
	private FeignBaseServer feignBaseServer;  //网关公共服务_控制器

	@Value("${timestampdiff}")
	private String timestampdiff;
	@Value("${skey}")
	private String skey;

	private static final Logger logger = LoggerFactory.getLogger(AccessLogFilter.class);
	  
	/**
	 *  拦截器开关
	 */
	@Override
	public boolean shouldFilter() {
		return false;
	}

	@Override
	public Object run() throws ZuulException {
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
			String queryParam = JSON.toJSONString(ctx.getRequest().getParameterMap());
			//logger.info("[zuul request info] url: " + uri + "; param: " + queryParam);
			Map<String, String[]> map2 = ctx.getRequest().getParameterMap();
			queryParam=getString(map2);
			String token2=map.get("token"); 
			String nonce=map.get("nonce"); 
			String sign=map.get("sign"); 
			String timestamp=map.get("timestamp");
			if(check(timestamp)) {
				ctx.setSendZuulResponse(false);
				ctx.setResponseStatusCode(403);
				ctx.setResponseBody("{\"code\":403,\"msg\":\"命令超时！\"}");
				ctx.getResponse().setContentType("text/html;charset=UTF-8");
				//insertAudit("","","命令超时！","2","403","1",redisString,feignBaseServer);
				return null;
			}else if(isExit(nonce)) {
				ctx.setSendZuulResponse(false);
				ctx.setResponseStatusCode(403);
				ctx.setResponseBody("{\"code\":403,\"msg\":\"命令重放！\"}");
				ctx.getResponse().setContentType("text/html;charset=UTF-8");
				//insertAudit("","","命令重放！","2","403","1",redisString,feignBaseServer);
				return null;
			}else if(checkSign(sign,queryParam,nonce,timestamp)) {
				ctx.setSendZuulResponse(false);
				ctx.setResponseStatusCode(403);
				ctx.setResponseBody("{\"code\":403,\"msg\":\"参数修改！\"}");
				ctx.getResponse().setContentType("text/html;charset=UTF-8");
				//insertAudit("","","命令重放！","2","403","1",redisString,feignBaseServer);
				return null;
			}
			/*
			 * if(true){ InputStream is = ctx.getResponseDataStream(); String response2 =
			 * IOUtils.toString(is, Charset.defaultCharset());
			 * logger.info("[zuul request info] url: " + uri + "; param: " + queryParam +
			 * "; [zuul response info]: " + response2);
			 * //insertAuditTemp(userId+"",userName,menu,uri,queryParam,redisString,
			 * feignBaseServer,response);
			 * ctx.setResponseDataStream(IOUtils.toInputStream(response2,
			 * Charset.defaultCharset())); return null; }
			 */
			Claims claims =null;
			if(!uri.contains("login")) {
				if(token2!=null) {
					token2=token2.substring(1, token2.length()-1);
					byte[] secretKey = Base64.getEncoder().encode(CommonUtils.SECURITY_KEY.getBytes());
					claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token2).getBody();
				}
			}
			if(claims == null&&!(uri.contains("login"))) {
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
					Long expirationDate = redisString.getExpire(userNameToken,TimeUnit.SECONDS);
					String menu= redisString.getValue(userNamemenu);
					if(menu!=null) {
						if(!menu.contains(getLast(uri))) {
			        	    ctx.setSendZuulResponse(false);
				            ctx.setResponseStatusCode(401);
				            ctx.setResponseBody("{\"code\":402,\"msg\":\"没有操作权限！\"}");
				            ctx.getResponse().setContentType("text/html;charset=UTF-8");
				            insertAudit(userId+"",userName,"没有操作权限！","2","402","1",redisString,feignBaseServer);
				            return null;
						}
					}
					if(expirationDate > 300){
						redisString.expire(userNameToken,1800,TimeUnit.SECONDS);
					}else {
						ctx.setSendZuulResponse(false);
						ctx.setResponseStatusCode(401);
						ctx.setResponseBody("{\"code\":401,\"msg\":\"超时！\"}");
						ctx.getResponse().setContentType("text/html;charset=UTF-8");
						insertAudit(userId+"",userName,"超时！","2","401","1",redisString,feignBaseServer);
						return null;
					}
				} 
			}
		} catch (Exception e) {
			logger.error("记录访问日志异常：", e);
            insertAudit("","","未知异常！","2","401","1",redisString,feignBaseServer);
		}
		return null;
	}
	
	/**
	 * map数据格式转换成url参数拼接
	 * @param map
	 * @return
	 */
	private String getString(Map<String, String[]> map) {
		String a="";
		for (String key : map.keySet()) {
			a=a+key+"="+ map.get(key)[0]+"&";
		}
		return a;
	}

	/**
	 *  确认参数防篡改（前后端参数排序问题暂时未实现）
	 */
	private boolean checkSign(String sign, String queryParam, String nonce, String timestamp) {
		try {
			if(sign==null&&"".equals(sign)) {
				return false;
			}
			String key=queryParam+nonce+timestamp;
			key=SymmetricEncoder.encodeByMD5(key).toLowerCase();
			key=SymmetricEncoder.AESEncode(key,skey,skey);
			if(key.equals(sign)) {
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		//return true;
		return false;
	}
	
	/**
	 * 确认重放
	 */
	private boolean isExit(String nonce) {
		try {
			if(nonce==null&&"".equals(nonce)) {
				return true;
			}
			String temp = redisString.getValue(nonce);
			if(temp==null||"".equals(temp)) {
				redisString.setKey(nonce, nonce);
				int c=Integer.parseInt(timestampdiff);
				redisString.expire(nonce, c, TimeUnit.SECONDS);
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		return true;
	}
	/**
	 * 确认时间戳
	 */
	private boolean check(String timestamp) {
		try {
			if(timestamp!=null&&!"".equals(timestamp)) {
				long a = System.currentTimeMillis();
				long b = Long.parseLong(timestamp);
				long c = Long.parseLong(timestampdiff);
				if(a-b>c*10000) {					
					return true;
				}else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		return true;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 1;
	}
	
	/**
	 *  记录操作日志（临时方法）
	 * @param loginId
	 * @param userName
	 * @param menu
	 * @param uri
	 * @param queryParam
	 * @param redisString2
	 * @param feignBaseServer2
	 * @param b
	 
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
    */
	/**
	  * 通过Feign插入审计数据
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
	 *   请求路径截取
	 * @param uri
	 * @return
	 */
	private  CharSequence getLast(String uri) {
		String[] temp = uri.split("/");
		return temp[temp.length-1];
	}
}