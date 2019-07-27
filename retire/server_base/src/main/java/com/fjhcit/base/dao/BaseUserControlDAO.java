package com.fjhcit.base.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 用户管理持久层
 */
public interface BaseUserControlDAO {
	/**
	 * 用户数据数量
	 * @param paramMap
	 * @return
	 */
	public String userControlCount(Map<String, Object> paramMap);
	/**
	 * 用户数据
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> userControlInfo(Map<String, Object> paramMap);
	/**
	 * 用户角色配置表
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectUserRole();
	/**
	 * 用户角色配置信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, String>> selectUserRoleRela(@Param("userId") String userId);
	/**
	 * 删除用户角色配置信息
	 * @param userId
	 */
	public void removeUserRoleRela(@Param("userId") String userId);
	/**
	 * 插入用户角色配置信息
	 * @param userId
	 * @param roleId
	 */
	public void addUserRoleRela(@Param("userId") String userId,@Param("roleId") String roleId);
	/**
	 * 用户部门配置信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, String>> selectUserDepRela(@Param("userId") String userId);
	/**
	 * 删除用户部门配置信息
	 * @param userId
	 */
	public void removeUserDepRela(@Param("userId") String userId);
	/**
	 * 插入用户部门配置信息
	 * @param userId
	 * @param depId
	 */
	public void addUserDepRela(@Param("userId") String userId,@Param("depId") String depId);
	/**
	 * 修改用户密码
	 * @param pwd
	 * @param userId
	 */
	public void updateUserPwd(@Param("pwd") String pwd,@Param("userId") String userId);
	/**
	 * 验证用户管理员
	 * @param userId
	 * @return
	 */
	public String verifyUserAdmin(@Param("userId") String userId);
	/**
	 * 验证用户角色
	 * @param roleId
	 * @return
	 */
	public List<String> verifyUserRole(@Param("roleId")List<String>  roleId);
	/**
	 * 根据用户ID改变用户状态
	 * @param userId
	 * @param updateState
	 */
	public void updateStateByUserId(@Param("userId") String userId,@Param("updateState") String updateState);
	/**
	 * 通过用户id获取密码
	 * @param userId
	 * @return
	 */
	public String checkPwd(@Param("id") String userId);
}