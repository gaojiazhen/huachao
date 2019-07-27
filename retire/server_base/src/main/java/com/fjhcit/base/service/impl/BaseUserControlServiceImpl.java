package com.fjhcit.base.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fjhcit.base.dao.BaseUserControlDAO;
import com.fjhcit.base.service.BaseUserControlService;

/**
 * 权限管理实现层
 */
@Service
public class BaseUserControlServiceImpl implements BaseUserControlService {
	
	@Autowired
	private BaseUserControlDAO baseUserControlDAO;

	@Override
	public String getUserControlCount(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return baseUserControlDAO.userControlCount(paramMap);
	}

	@Override
	public List<Map<String, Object>> getUserControlInfo(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return baseUserControlDAO.userControlInfo(paramMap);
	}

	@Override
	public List<Map<String, Object>> selectUserRole() {
		// TODO Auto-generated method stub
		return baseUserControlDAO.selectUserRole();
	}

	@Override
	public List<Map<String, String>> selectUserRoleRela(String userId) {
		// TODO Auto-generated method stub
		return baseUserControlDAO.selectUserRoleRela(userId);
	}

	@Override
	public void deleteUserRoleRela(String userId) {
		// TODO Auto-generated method stub
		baseUserControlDAO.removeUserRoleRela(userId);
	}

	@Override
	public void addUserRoleRela(String userId, String roleId) {
		// TODO Auto-generated method stub
		baseUserControlDAO.addUserRoleRela(userId, roleId);
	}

	@Override
	public List<Map<String, String>> selectUserDepRela(String userId) {
		// TODO Auto-generated method stub
		return baseUserControlDAO.selectUserDepRela(userId);
	}

	@Override
	public void deleteUserDepRela(String userId) {
		// TODO Auto-generated method stub
		baseUserControlDAO.removeUserDepRela(userId);
	}

	@Override
	public void addUserDepRela(String userId, String depId) {
		// TODO Auto-generated method stub
		baseUserControlDAO.addUserDepRela(userId, depId);
	}

	@Override
	public void updateUserPwd(String pwd, String userId) {
		// TODO Auto-generated method stub
		baseUserControlDAO.updateUserPwd(pwd, userId);
	}

	@Override
	public String verifyUserAdmin(String userId) {
		// TODO Auto-generated method stub
		return baseUserControlDAO.verifyUserAdmin(userId);
	}

	@Override
	public List<String> verifyUserRole(List<String> roleId) {
		// TODO Auto-generated method stub
		return baseUserControlDAO.verifyUserRole(roleId);
	}

	@Override
	public void updateStateByUserId(String userId,String updateState) {
		// TODO Auto-generated method stub
		baseUserControlDAO.updateStateByUserId(userId,updateState);
	}

	@Override
	public String checkPwd(String userId) {
		// TODO Auto-generated method stub
		return baseUserControlDAO.checkPwd(userId);
	}


}