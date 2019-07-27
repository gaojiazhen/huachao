package com.fjhcit.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.fjhcit.base.service.BaseRoleService;

/**
 * 角色权限管理控制器
 * @author bule
 *
 */
@RestController
@RequestMapping("/role")
public class BaseRoleController {
	@Autowired
	BaseRoleService baseRoleService;

	/**
	 * 角色信息
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/roleInfo", method = RequestMethod.POST)
	public Object roleInfo(@RequestParam Map<String, String> paramMap) {
		System.out.println(paramMap.toString());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", baseRoleService.getRoleInfo(paramMap));
		result.put("count",baseRoleService.getRoleCount(paramMap));
		result.put("code", "0");
		return result;
	}
	/**
	 * 新增角色数据
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/addRole", method = RequestMethod.POST)
	public String addRole(@RequestParam Map<String, String> paramMap) {
		System.out.println(paramMap.toString());
		//1成功2异常3重复
		try {
			String str = baseRoleService.checkRoleCount(paramMap);
			if("0".equals(str)){
				baseRoleService.addRole1(paramMap);
				return "1";
			}else {
				return "3";
			}
		}catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
	}
	/**
	 * 删除角色数据
	 * @param paramMap
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@RequestMapping(value = "/deleteRole", method = RequestMethod.POST)
	public String deleteRole(@RequestParam Map<String, String> paramMap) {
		System.out.println(paramMap.toString());
		try {
			String index = baseRoleService.getRoleUserCount(paramMap.get("ID"));
			//1成功2异常0失败
			if("0".equals(index)) {
					baseRoleService.deleteRole(paramMap);
					baseRoleService.deleteRoleMenuRela(paramMap.get("ID"));
					return "1";
				
			}else {
				return "0";
			}
		}catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
			return "2";
		}
	}
	/**
	 * 角色菜单配置信息
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/selectRoleMenu", method = RequestMethod.POST)
	public Object selectRoleMenu(@RequestParam Map<String, String> paramMap) {
		System.out.println(paramMap.toString());
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> result2 = new HashMap<String, Object>();
		List<Map<String, String>> result3=baseRoleService.selectRoleMenu(paramMap);
		result2.put("tree", result3);
		result.put("data", result2);
		//角色已选菜单配置表
		List<Map<String, String>> m = baseRoleService.selectRoleMenuRela(paramMap);
		List<String> list = new ArrayList<String>();
		for (Map<String, String> map : m) {
			list.add(map.get("MENU_ID"));
		}
		result2.put("checkedId", list);
		result.put("msg","获取成功");
		result.put("code", "0");
		return result;
	}
	/**
	 * 修改色菜单配置信息
	 * @param paramMap
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@RequestMapping(value = "/updateRoleMenu", method = RequestMethod.POST)
	@SuppressWarnings("unchecked")
	public String updateRoleMenu(@RequestParam Map<String, String> paramMap) {
		System.out.println(paramMap.toString());
		//1成功2异常
		try {
			String roleId =paramMap.get("ROLE_ID");
			String str =paramMap.get("ROLE_TYPE");
			baseRoleService.deleteRoleMenuRela(roleId);
			Gson g = new Gson();
			List<String> m = g.fromJson(str, List.class);
			for (String string : m) {
				baseRoleService.addRoleMenuRela(roleId, string);
			}
			return "1";
		}catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
			return "2";
		}
	}
}
