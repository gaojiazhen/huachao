package com.fjhcit.base.service;

import java.util.List;
import java.util.Map;

/**
 * 用户管理服务层
 */
public interface BaseUserControlService {
	/**
	 * 用户数据数量
	 * @param paramMap
	 * @return
	 */
	public String getUserControlCount(Map<String, Object> paramMap);
	/**
	 * 用户数据
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getUserControlInfo(Map<String, Object> paramMap);
	/**
	 * 用户角色配置表
	 * @return
	 */
	public List<Map<String, Object>> selectUserRole();
	/**
	 * 用户角色配置信息
	 * @param userId
	 * @return
	 */
	public List<Map<String, String>> selectUserRoleRela(String userId);
	/**
	 * 删除用户角色配置信息
	 * @param userId
	 */
	public void deleteUserRoleRela(String userId);
	/**
	 * 插入用户角色配置信息
	 * @param userId
	 * @param roleId
	 */
	public void addUserRoleRela(String userId,String roleId);
	/**
	 * 用户部门配置信息
	 * @param userId
	 * @return
	 */
	public List<Map<String, String>> selectUserDepRela(String userId);
	/**
	 * 删除用户部门配置信息
	 * @param userId
	 */
	public void deleteUserDepRela(String userId);
	/**
	 * 插入用户部门配置信息
	 * @param userId
	 * @param depId
	 */
	public void addUserDepRela(String userId,String depId);
	/**
	 * 修改用户密码
	 * @param pwd
	 * @param userId
	 */
	public void updateUserPwd(String pwd,String userId);
	/**
	 * 验证用户管理员
	 * @param userId
	 * @return
	 */
	public String verifyUserAdmin(String userId);
	/**
	 * 验证用户角色
	 * @param roleId
	 * @return
	 */
	public List<String> verifyUserRole(List<String> roleId);
	/**
	 * 根据用户ID改变用户状态
	 * @param userId
	 * @param updateState
	 */
	public void updateStateByUserId(String userId,String updateState);
	/**
	 * 通过用户id获取密码
	 * @param userId
	 * @return
	 */
	public String checkPwd(String userId);
}