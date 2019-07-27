package com.fjhcit.base.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.fjhcit.model.CacheModel;

public interface DepartmentService {
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