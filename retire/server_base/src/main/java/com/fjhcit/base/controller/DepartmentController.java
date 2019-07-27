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

import com.fjhcit.base.service.DepartmentService;
import com.fjhcit.model.CacheModel;
import com.fjhcit.utils.RedisStringUtils;
import com.google.gson.Gson;

@RestController
@RequestMapping("/department")
public class DepartmentController {
	@Autowired
	private DepartmentService department;
	@Autowired
	private RedisStringUtils redisString;
	
	/**
	 *组织机构页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/departmentInfo", method = RequestMethod.POST)
	public Object findMenu(@RequestParam Map<String, String> paramMap){
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = department.getDepartmentInfo(paramMap);
		result.put("data", resultList);
		result.put("count", resultList.size());
		result.put("code", 0);//返回码
		return result;
	}
	
	/**
	 * 添加和编辑组织机构
	 * @param paramMap
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@RequestMapping(value = "/editDepartment", method = RequestMethod.POST)
	public String addMenu(@RequestParam Map<String, String> paramMap) {
		//1成功2异常3数据重复
		try {
			if(StringUtils.isBlank(paramMap.get("updateFlag"))) {
				department.addDepartment(paramMap);
				department.insertSysCode(paramMap);
			}else {
				department.eaditDepartment(paramMap);
				department.updateSysCode(paramMap);
			}
			//更新缓存
			ArrayList<CacheModel> list = new ArrayList<CacheModel>();
			CacheModel all=new CacheModel();
	    	all.setKd_code("DEPARTMENT");
	    	all.setName("全部");
	    	all.setValue("");
	    	list.add(all);
	    	Gson gson =new Gson();
	    	list.addAll(department.getCode());
    		if(list!=null&&list.size()>0) {
    			redisString.setKey("DEPARTMENT", gson.toJson(list));
    		}
    		return "1";
		}catch(org.springframework.dao.DuplicateKeyException e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
			return "3";
		}catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
			return "2";
		} 
	}
	
	/**
	 * 删除组织机构
	 * @param request
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@RequestMapping(value = "/deleteDepartment", method = RequestMethod.POST)
	public String deleteMenu(@RequestParam Map<String, String> paramMap) {
		//1成功0失败2异常
		try {
			String i = department.getdepartmentCount(paramMap);
			if("0".equals(i)) {
				department.deleteDepartment(paramMap);
				department.removeSysCode(paramMap);
				return "1";
			}
			//更新缓存
			ArrayList<CacheModel> list = new ArrayList<CacheModel>();
			CacheModel all=new CacheModel();
	    	all.setKd_code("DEPARTMENT");
	    	all.setName("全部");
	    	all.setValue("");
	    	list.add(all);
	    	Gson gson =new Gson();
	    	list.addAll(department.getCode());
    		if(list!=null&&list.size()>0) {
    			redisString.setKey("DEPARTMENT", gson.toJson(list));
    		}
			return "0";
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
			return "2";
		}
	}

}
























