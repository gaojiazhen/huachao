package com.fjhcit.base.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 权限管理持久层
 * @author bule
 *
 */
public interface BaseRoleDAO {
	/**
	 * 角色数据数量
	 * @param paramMap
	 * @return
	 */
	public String roleCount(Map<String, String> paramMap);
	/**
	 * 角色数据
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> roleInfo(Map<String, String> paramMap);
	/**
	 * 删除角色数据
	 * @param paramMap
	 */
	public void deleteRole(Map<String, String> paramMap);
	/**
	 * 验证角色
	 * @param paramMap
	 * @return
	 */
	public String getRoleUserCount(@Param("roleId") String roleId);

	/**
	 * 插入角色数据
	 * @param paramMap
	 */
	public void insertRole(Map<String, String> paramMap);
	/**
	 * 编码验证
	 * @param paramMap
	 */
	public String checkRoleCount(Map<String, String> paramMap);
	/**
	 * 角色菜单配置信息
	 * @param paramMap
	 */
	public List<Map<String, String>> selectRoleMenu(Map<String, String> paramMap);
	/**
	 * 角色菜单配置信息
	 * @param paramMap
	 */
	public List<Map<String, String>> selectRoleMenuRela(Map<String, String> paramMap);
	/**
	 * 删除角色菜单配置信息
	 * @param paramMap
	 */
	public void deleteRoleMenuRela(@Param("roleId") String roleId);
	/**
	 * 插入角色菜单配置信息
	 * @param paramMap
	 */
	public void addRoleMenuRela(@Param("roleId") String roleId,@Param("roleMenuId") String roleMenu);
}