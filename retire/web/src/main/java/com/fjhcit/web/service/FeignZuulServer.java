package com.fjhcit.web.service;

import com.fjhcit.model.ResultVO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description：通过网管请求的地址
 * @author：陈 麟
 * @date：2019年06月03日 下午15:14:51
 */
@FeignClient(name = "zuul")
public interface FeignZuulServer {
	
	/**
	 * 登录验证
	 * @return
	 */
	@RequestMapping(value = "/base/baseUser/login",method = RequestMethod.POST)
	ResultVO saveLogin(@RequestParam("userName") String userName, @RequestParam("pwd") String pwd);
}
