package com.fjhcit.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.fjhcit.base.dao.BaseSysCodeKindDAO;
import com.fjhcit.base.service.BaseUserControlService;
import com.fjhcit.common.kit.Aes;
import com.fjhcit.common.kit.CommonUtils;
import com.fjhcit.common.kit.SymmetricEncoder;

/**
 * 权限管理控制器
 * @author bule
 *
 */
@RestController
@RequestMapping("/userControl")
public class BaseUserControlController {
	
	@Autowired
	BaseUserControlService baseUserControlService;
	@Autowired
	CommonUtils commonUtils;
	@Autowired
	private BaseSysCodeKindDAO baseSysCodeKindService;
	
	/**
	 * 角色信息
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/userControlInfo", method = RequestMethod.POST)
	public Object UserControlInfo(@RequestParam Map<String, Object> paramMap) {
		System.out.println(paramMap.toString());
		String str = (String) paramMap.get("DEP_ID");
//		String str = "1,2,3,4,5,6";
		List<String> list = new ArrayList<String>();
		if(!StringUtils.isBlank(str)) {
			String[] arr = str.split(",");
			
			for (String string : arr) {
				list.add(string);
			}
			System.out.println(list.toString());
			paramMap.put("DEPT_ID", list);
			System.out.println(paramMap.get("DEPT_ID").toString());
			
		}
		paramMap.put("DEPT_ID", list);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", baseUserControlService.getUserControlInfo(paramMap));
		result.put("count",baseUserControlService.getUserControlCount(paramMap));
		result.put("code", "0");
		return result;
	}
	/**
	 * 用户角色配置信息
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/selectUserRole", method = RequestMethod.POST)
	public Object selectRoleMenu(@RequestParam Map<String, String> paramMap) {
		System.out.println(paramMap.toString());
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> result2 = new HashMap<String, Object>();
		result2.put("tree", baseUserControlService.selectUserRole());
		result.put("data", result2);
		//用户角色配置表
		List<Map<String, String>> m = baseUserControlService.selectUserRoleRela(paramMap.get("USER_ID"));
		List<String> list = new ArrayList<String>();
		for (Map<String, String> map : m) {
			list.add(map.get("ROLE_ID"));
		}
		result2.put("checkedId", list);
		result.put("msg","获取成功");
		result.put("code", "0");
		return result;
	}
	/**
	 * 修改用户角色配置信息
	 * @param paramMap
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@RequestMapping(value = "/updateUserRole", method = RequestMethod.POST)
	@SuppressWarnings("unchecked")
	public String updateRoleMenu(@RequestParam Map<String, String> paramMap) {
		System.out.println(paramMap.toString());
		//1成功2异常3角色类型冲突
		try {
			String str =paramMap.get("ROLE_ID");
			Gson g = new Gson();
			List<String> m = g.fromJson(str, List.class);
			//判断受否角色大于1个
			if(m.size()>1) {
				List<String> list = baseUserControlService.verifyUserRole(m);
				if(list.size()>1) {
					for (int i = 0; i < list.size(); i++) {
						if(!list.get(0).equals(list.get(i))){
							return "3";
						}
					}
				}
			}
			String userId =paramMap.get("USER_ID");
			baseUserControlService.deleteUserRoleRela(userId);
			for (String roleId : m) {
				baseUserControlService.addUserRoleRela(userId, roleId);
			}
			return "1";
		}catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
			return "2";
		}
	}
	/**
	 * 用户部门配置信息
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/selectUserDep", method = RequestMethod.POST)
	public Object selectDepMenu(@RequestParam Map<String, String> paramMap) {
		System.out.println(paramMap.toString());
		Map<String, Object> result = new HashMap<String, Object>();
		//用户角色配置表
		List<Map<String, String>> m = baseUserControlService.selectUserDepRela(paramMap.get("USER_ID"));
		List<String> list = new ArrayList<String>();
		for (Map<String, String> map : m) {
			list.add(map.get("DEP_ID"));
		}
		result.put("checkedId", list);
		result.put("msg","获取成功");
		result.put("code", "0");
		return result;
	}
	/**
	 * 修改用户部门配置信息
	 * @param paramMap
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@RequestMapping(value = "/updateUserDep", method = RequestMethod.POST)
	@SuppressWarnings("unchecked")
	public String updateDepMenu(@RequestParam Map<String, String> paramMap) {
		System.out.println(paramMap.toString());
		//1成功2异常
		try {
			String userId =paramMap.get("USER_ID");
			String str =paramMap.get("DEP_ID");
			baseUserControlService.deleteUserDepRela(userId);
			Gson g = new Gson();
			List<String> m = g.fromJson(str, List.class);
			for (String depId : m) {
				baseUserControlService.addUserDepRela(userId, depId);
			}
			return "1";
		}catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
			return "2";
		}
	}
	/**
	 * 重置密码
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
	public String resetPwd(@RequestParam Map<String, String> paramMap) {
		System.out.println(paramMap.toString());
		//1成功2异常3为非管理员
		try {
			//获取用户的ID
			@SuppressWarnings("static-access")
			String index = baseUserControlService.verifyUserAdmin(commonUtils.getCurrentUserId());
			if("0".equals(index)){
				return "3";
			}
			//从数据库数据字典取
			Map<String,String> map = new HashMap<String, String>();
			map.put("kdCode", "USER_RESET_PASSWORD");
	    	List<Map<String, Object>> list = baseSysCodeKindService.getSysCodeInfo(map);
	    	String USER_RESET_PASSWORD = (String) list.get(0).get("CODE");
	    	System.out.println(USER_RESET_PASSWORD);
			String md5pwd = SymmetricEncoder.encodeByMD5(USER_RESET_PASSWORD);
//	    	String md5pwd = Aes.aesDecrypt(USER_RESET_PASSWORD);
			baseUserControlService.updateUserPwd(md5pwd, paramMap.get("USER_ID"));
			return "1";
		}catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
	}
	/**
	 * 修改密码
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
	public String updatePwd(@RequestParam Map<String, String> paramMap) {
		System.out.println(paramMap.toString());
		//1成功2异常3密码不可与原密码相同
		try {
			String pwd = Aes.aesDecrypt(paramMap.get("pwd"));
			String pwd2 = Aes.aesDecrypt(paramMap.get("pwd2"));
			@SuppressWarnings("static-access")
			String userId = commonUtils.getCurrentUserId();
			String checkPwd = baseUserControlService.checkPwd(userId);
			if(pwd.equals(pwd2)&&!pwd.equals(checkPwd)) {
				baseUserControlService.updateUserPwd(pwd, userId);
				return "1";
			}
			return "3";
		}catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
	}
	/**
	 * 根据用户ID改变用户状态
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/updateUsetState", method = RequestMethod.POST)
	public String userEnable(@RequestParam Map<String, String> paramMap) {
		System.out.println(paramMap.toString());
		//1成功2异常
		try {
			baseUserControlService.updateStateByUserId(paramMap.get("USER_ID"),paramMap.get("updateState"));
			return "1";
		}catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
	}

}
