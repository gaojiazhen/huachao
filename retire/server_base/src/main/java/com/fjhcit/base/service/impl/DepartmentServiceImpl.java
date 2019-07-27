package com.fjhcit.base.service.impl;

import com.fjhcit.base.dao.DepartmentMapper;
import com.fjhcit.base.service.DepartmentService;
import com.fjhcit.model.CacheModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 组织机构实现层
 * @author bule
 *
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentMapper departmentMapper;
	
	@Override
	public List<Map<String, Object>> getDepartmentInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return departmentMapper.getDepartmentInfo(paramMap);
	}

	@Override
	public String getdepartmentCount(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return departmentMapper.getdepartmentCount(paramMap);
	}

	@Override
	public void deleteDepartment(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		departmentMapper.deleteDepartment(paramMap);
	}

	@Override
	public void addDepartment(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		departmentMapper.insertDepartment(paramMap);
	}

	@Override
	public void eaditDepartment(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		departmentMapper.updateDepartment(paramMap);
	}

	@Override
	public String checkDepCode(String depCode) {
		// TODO Auto-generated method stub
		return departmentMapper.checkDepCode(depCode);
	}

	@Override
	public ArrayList<CacheModel> getCode() {
		// TODO Auto-generated method stub
		return departmentMapper.getCode();
	}

	@Override
	public void insertSysCode(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		departmentMapper.insertSysCode(paramMap);
	}

	@Override
	public void updateSysCode(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		departmentMapper.updateSysCode(paramMap);
	}

	@Override
	public void removeSysCode(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		departmentMapper.removeSysCode(paramMap);
	}

}