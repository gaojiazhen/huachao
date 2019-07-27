package com.fjhcit.base.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.fjhcit.entity.BaseDepartmentDO;
import com.fjhcit.model.CacheModel;

/**
 * @description 基础管理_组织机构_业务逻辑接口
 * @author 陈麟
 * @date 2019年06月04日 下午19:49:25
 */
public interface BaseDepartmentService {

	/**
	 * @description 查找基础管理_组织机构列表信息
	 * @param map
	 * @return
	 */
	List<BaseDepartmentDO> listBaseDepartment(Map<String,String> map);

	/**
	 * @description 查找基础管理_组织机构单条信息
	 * @param id
	 * @return
	 */
	BaseDepartmentDO getBaseDepartmentDOById(String id);

	/**
	 * @description 添加基础管理_组织机构信息
	 * @param baseDepartmentDO
	 * @return
	 */
	String insertBaseDepartment(BaseDepartmentDO baseDepartmentDO);

	/**
	 * @description 更新基础管理_组织机构信息
	 * @param baseDepartmentDO
	 * @return
	 */
	void updateBaseDepartment(BaseDepartmentDO baseDepartmentDO);

	/**
	 * @description 删除基础管理_组织机构信息
	 * @param map
	 * @return
	 */
	void removeBaseDepartmentByIdsArr(Map<String,Object> map);
	
	/**
	 * 根据主键ID串数组_查找基础管理_组织机构列表信息
	 * @param map
	 * @return
	 */
	List<BaseDepartmentDO> listBaseDepartmentDOByIdsArr(Map<String,Object> map);
	
	/**
	 * 
	 * 通用接口
	 * 
	 */
	
	/**
	 * 组织机构信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getDepartmentInfo(Map<String, String> paramMap);
	/**
	 * 组织机构信息数量
	 * @param paramMap
	 * @return
	 */
	public String getdepartmentCount(Map<String, String> paramMap);
	/**
	 * 删除组织机构
	 * @param paramMap
	 */
	public void deleteDepartment(Map<String, String> paramMap);
	/**
	 * 新增组织机构
	 * @param paramMap
	 */
	public void addDepartment(Map<String, String> paramMap);
	/**
	 * 修改组织机构信息
	 * @param paramMap
	 */
	public void eaditDepartment(Map<String, String> paramMap);
	/**
	 * 验证编码是否重复
	 * @param depCode
	 * @return
	 */
	public String checkDepCode(@Param("depCode") String depCode);
	/**
	 * 缓存
	 * @param code
	 * @return
	 */
	public ArrayList<CacheModel> getCode();
	/**
	 * 插入新增数据字典
	 * @param paramMap
	 */
	public void insertSysCode(Map<String, String> paramMap);
	/**
	 * 插入新增数据字典
	 * @param paramMap
	 */
	public void updateSysCode(Map<String, String> paramMap);
	/**
	 * 删除数据字典及对应编码明细
	 * @param paramMap
	 */
	public void removeSysCode(Map<String, String> paramMap);
}