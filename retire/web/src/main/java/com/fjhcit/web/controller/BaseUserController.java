package com.fjhcit.web.controller;

import com.fjhcit.model.ResultVO;
import com.fjhcit.web.service.FeignZuulServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/web/baseUser")
@CrossOrigin
public class BaseUserController {

	@Autowired
	private FeignZuulServer feignZuulServer;
 
	/**
	 * 通过网管进行登陆的 http://localhost:9001/web/baseUser/zuul/login
	 */
	@RequestMapping("/zuul/login")
	public ResultVO login(@RequestParam Map<String, String> params) {
		String userName = params.get("userName");
		String pwd = params.get("pwd");
		ResultVO result = this.feignZuulServer.saveLogin(userName, pwd);
		@SuppressWarnings("rawtypes")
		Map users = (Map) result.getData();
		if (users != null) {
			HttpServletRequest request = getHttpServletRequest();
			request.getSession().setAttribute("token", users.get("token"));
			request.getSession().setAttribute("users", users);
		}
		return result;
	}

	@RequestMapping("/zuul/signOut")
	public ResultVO signOut(@RequestParam Map<String, String> params) {
		HttpServletRequest request = getHttpServletRequest();
		request.getSession().removeAttribute("token");
		request.getSession().removeAttribute("users");
		return new ResultVO();
	}

	private HttpServletRequest getHttpServletRequest() {
		try {
			// 这种方式获取的HttpServletRequest是线程安全的
			return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
		} catch (Exception e) {
			return null;
		}
	}
}