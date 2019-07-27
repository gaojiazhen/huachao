package com.fjhcit.base.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fjhcit.base.dao.BaseRoleDAO;
import com.fjhcit.base.service.BaseRoleService;

/**
 * 权限管理实现层
 * @author bule
 *
 */
@Service
public class BaseRoleServiceImpl implements BaseRoleService {
	
	@Autowired
	private BaseRoleDAO baseRoleDAO;

	@Override
	public String getRoleCount(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return baseRoleDAO.roleCount(paramMap);
	}

	@Override
	public List<Map<String, Object>> getRoleInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return baseRoleDAO.roleInfo(paramMap);
	}

	@Override
	public void deleteRole(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		baseRoleDAO.deleteRole(paramMap);
	}

	@Override
	public void addRole1(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		baseRoleDAO.insertRole(paramMap);
	}

	@Override
	public String checkRoleCount(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return baseRoleDAO.checkRoleCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectRoleMenu(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return baseRoleDAO.selectRoleMenu(paramMap);
	}

	@Override
	public List<Map<String, String>> selectRoleMenuRela(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return baseRoleDAO.selectRoleMenuRela(paramMap);
	}

	@Override
	public void deleteRoleMenuRela(String roleId) {
		// TODO Auto-generated method stub
		baseRoleDAO.deleteRoleMenuRela(roleId);
	}

	@Override
	public void addRoleMenuRela(String roleId,String roleMenu) {
		// TODO Auto-generated method stub
		baseRoleDAO.addRoleMenuRela(roleId,roleMenu);
	}

	@Override
	public String getRoleUserCount(String roleId) {
		// TODO Auto-generated method stub
		return baseRoleDAO.getRoleUserCount(roleId);
	}

}