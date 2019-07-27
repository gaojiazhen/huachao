package com.fjhcit.base.service;

import java.util.List;
import java.util.Map;

/**
 * 权限管理服务层
 * @author bule
 *
 */
public interface BaseRoleService {
	/**
	 * 角色数据数量
	 * @param paramMap
	 * @return
	 */
	public String getRoleCount(Map<String, String> paramMap);
	/**
	 * 验证角色
	 * @param paramMap
	 * @return
	 */
	public String getRoleUserCount(String roleId);
	/**
	 * 角色详细
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getRoleInfo(Map<String, String> paramMap);
	/**
	 * 删除角色数据
	 * @param paramMap
	 */
	public void deleteRole(Map<String, String> paramMap);
	/**
	 * 新增角色数据
	 * @param paramMap
	 */
	public void addRole1(Map<String, String> paramMap);
	/**
	 * 角色名验证
	 * @param paramMap
	 * @return
	 */
	public String checkRoleCount(Map<String, String> paramMap);
	/**
	 * 角色菜单配置表
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, String>> selectRoleMenu(Map<String, String> paramMap);
	/**
	 * 角色菜单配置信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, String>> selectRoleMenuRela(Map<String, String> paramMap);
	/**
	 * 删除角色菜单配置信息
	 * @param paramMap
	 * @return
	 */
	public void deleteRoleMenuRela(String roleId);
	/**
	 * 插入角色菜单配置信息
	 * @param paramMap
	 * @return
	 */
	public void addRoleMenuRela(String roleId,String roleMenu);
}