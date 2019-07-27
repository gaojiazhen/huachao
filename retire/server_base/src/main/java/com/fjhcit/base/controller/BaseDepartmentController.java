package com.fjhcit.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.fjhcit.constant.ParentIdContant;
import com.fjhcit.model.CacheModel;
import com.fjhcit.model.ResultVO;
import com.fjhcit.utils.RedisStringUtils;
import com.fjhcit.entity.BaseDepartmentDO;
import com.fjhcit.base.service.BaseDepartmentService;
import com.fjhcit.base.service.DepartmentService;
import com.fjhcit.common.kit.StringUtils;

/**
 * @description 基础管理_组织机构_控制器
 * @author 陈麟
 * @date 2019年06月04日 下午19:49:25
 */
@RestController
@RequestMapping("/baseDepartment")
public class BaseDepartmentController{
	@Autowired
	private BaseDepartmentService	baseDepartmentService;  		//基础管理_组织机构_业务接口
    @Autowired
	private RedisStringUtils	redisString;		//redis工具类

	/**
	 * @description 查基础管理_组织机构列表数据
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listBaseDepartment", method = RequestMethod.POST)
	public ResultVO listBaseDepartment(@RequestParam Map<String, String> param){
		String department_id = param.get("department_id");
		List<BaseDepartmentDO> baseDepartmentList = new ArrayList<BaseDepartmentDO>();
		//省公司本部_可查看所有单位数据（当查部门列表时department_id必须传值等于ParentIdContant.BASE_DEPARTMENT_PROVINCIA）
		if(ParentIdContant.BASE_DEPARTMENT_PROVINCIAL.contentEquals(department_id)) {
	    	String parent_id = param.get("parent_id");
	    	String departmentJson = this.redisString.getValue("BaseDepartment_" + parent_id);
	    	Gson gson =new Gson();
	    	if(StringUtils.isNotEmpty(departmentJson) && !"[]".contentEquals(departmentJson)) {
	    		baseDepartmentList.addAll(gson.fromJson(departmentJson,baseDepartmentList.getClass()));
	    	}else {
	    		baseDepartmentList = this.baseDepartmentService.listBaseDepartment(param);
	    		this.redisString.setKey("BaseDepartment_" + parent_id, gson.toJson(baseDepartmentList));
	    	}
		}else {
			//地市级单位只能选择自己单位
			BaseDepartmentDO departmentDO = this.baseDepartmentService.getBaseDepartmentDOById(department_id);
			baseDepartmentList.add(departmentDO);
		}
        return new ResultVO(baseDepartmentList,true,"查询成功");
	}

	/**
	 * @description 查基础管理_组织机构分页列表数据
	 */
    @RequestMapping(value = "/listBaseDepartmentByPaging", method = RequestMethod.POST)
	public ResultVO listBaseDepartmentByPaging(@RequestParam Map<String, String> param){
		ResultVO result=new ResultVO();
    	if(StringUtils.isEmpty(param.get("pageNum"))){
    		param.put("pageNum", "1");
    	}
    	if(StringUtils.isEmpty(param.get("pageSize"))){
    		param.put("pageSize", "10");
    	}
    	int page=Integer.parseInt(param.get("pageNum"));		//当前页
    	int pageSize=Integer.parseInt(param.get("pageSize"));//每页条数
    	PageHelper.startPage(page , pageSize);
		List<BaseDepartmentDO> personList = this.baseDepartmentService.listBaseDepartment(param);
        PageInfo<BaseDepartmentDO> personPageInfo = new PageInfo<BaseDepartmentDO>(personList);
    	result.setData(personPageInfo);
    	result.setMessage("成功");
    	result.setSuccess(true);
        return result;
	}

	/**
	 * @description 保存基础管理_组织机构数据（新增、修改）
	 */
    @RequestMapping(value = "/saveBaseDepartment", method = RequestMethod.POST)
	public ResultVO saveBaseDepartment(@RequestBody BaseDepartmentDO baseDepartmentDO) {
    	ResultVO result = new ResultVO();
    	baseDepartmentDO.setModified_user_id("-1");
    	if(StringUtils.isEmpty(baseDepartmentDO.getId())) {
    		this.baseDepartmentService.insertBaseDepartment(baseDepartmentDO);
    	}else {
    		this.baseDepartmentService.updateBaseDepartment(baseDepartmentDO);
    	}
		//更新缓存
    	Gson gson =new Gson();
    	Map<String,String> dparam = new HashMap<String, String>();
    	dparam.put("parent_id",baseDepartmentDO.getParent_id());
    	List<BaseDepartmentDO> departmentlist = this.baseDepartmentService.listBaseDepartment(dparam);
		if(StringUtils.isNotEmpty(departmentlist)) {
			this.redisString.setKey("BaseDepartment_" + baseDepartmentDO.getParent_id(), gson.toJson(departmentlist));
		}
		//返回值
    	result.setData(null);
    	result.setMessage("成功");
    	result.setSuccess(true);
        return result;
	}

	/**
	 * @description 删除基础管理_组织机构数据
	 */
    @RequestMapping(value = "/removeBaseDepartment", method = RequestMethod.POST)
	public ResultVO removeBaseDepartment(@RequestParam Map<String, Object> param){
    	ResultVO result = new ResultVO();
    	String ids = (String)param.get("ids");
    	String[] idsArr = ids.split(",");
    	param.put("idsArr",idsArr);
		this.baseDepartmentService.removeBaseDepartmentByIdsArr(param);
		//更新缓存
    	Gson gson =new Gson();
    	List<BaseDepartmentDO> departmentlist = this.baseDepartmentService.listBaseDepartmentDOByIdsArr(param);
		if(StringUtils.isNotEmpty(departmentlist)) {
			for(BaseDepartmentDO d : departmentlist) {
				Map<String,String> dparam = new HashMap<String, String>();
				dparam.put("parent_id", d.getParent_id());
				this.redisString.setKey("BaseDepartment_" + d.getParent_id(), gson.toJson(this.baseDepartmentService.listBaseDepartment(dparam)));
			}
		}
		//返回值
    	result.setData(null);
    	result.setMessage("成功");
    	result.setSuccess(true);
        return result;
	}
    

    /**
     * 
     * 通用控制器
     * 
     */
    
    @Autowired
	private DepartmentService department;
	
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
			if(StringUtils.isEmpty(paramMap.get("updateFlag"))) {
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