package com.fjhcit.retire.service;

import com.fjhcit.model.ResultVO;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description：远程调用其他组件的请求地址
 * @author：陈 麟
 * @date：2019年07月08日 上午10:14:51
 */
@FeignClient(name = "zuul")
public interface FeignZuulServer {
	
	/**
	 * @Description：远程调用查询数据字典类型
	 * @return
	 */
	@RequestMapping(value = "/base/cache/getCode",method = RequestMethod.POST)
	ResultVO getCode(@RequestParam Map<String, String> param);
	
	/**
	 * @Description：远程调用查询数据字典类型（旧表方式，将被弃用）
	 * @return
	 */
//	@RequestMapping(value = "/base/baseCode/listBaseCode",method = RequestMethod.POST)
//	ResultVO listBaseCode(@RequestParam Map<String, String> param);

	/**
	 * @Description：远程调用查询组织机构
	 * @return
	 */
	@RequestMapping(value = "/base/baseDepartment/listBaseDepartment",method = RequestMethod.POST)
	ResultVO listBaseDepartment(@RequestParam Map<String, String> param);
	
	/**
	 * @Description：远程调用查询数据字典类型ID（旧表方式，将被弃用）
	 * @return
	 */
//	@RequestMapping(value = "/base/baseCode/getBaseCodeIdArray",method = RequestMethod.POST)
//	ResultVO getBaseCodeIdArray(@RequestParam Map<String, String> param);
}
