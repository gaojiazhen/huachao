package com.fjhcit.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fjhcit.entity.BaseDepartmentDO;
import com.fjhcit.model.CacheModel;
import com.fjhcit.base.dao.BaseDepartmentDAO;
import com.fjhcit.base.service.BaseDepartmentService;

@Service
public class BaseDepartmentServiceImpl implements BaseDepartmentService {

	@Autowired
	private BaseDepartmentDAO baseDepartmentDAO;

	@Override
	public List<BaseDepartmentDO> listBaseDepartment(Map<String,String> map){
		return this.baseDepartmentDAO.listBaseDepartment(map);
	}

	@Override
	public BaseDepartmentDO getBaseDepartmentDOById(String id){
		return this.baseDepartmentDAO.getBaseDepartmentDOById(id);
	}

	@Override
	public String insertBaseDepartment(BaseDepartmentDO baseDepartmentDO){
		return this.baseDepartmentDAO.insertBaseDepartment(baseDepartmentDO);
	}

	@Override
	public void updateBaseDepartment(BaseDepartmentDO baseDepartmentDO){
		this.baseDepartmentDAO.updateBaseDepartment(baseDepartmentDO);
	}

	@Override
	public void removeBaseDepartmentByIdsArr(Map<String,Object> map){
		this.baseDepartmentDAO.removeBaseDepartmentByIdsArr(map);
	}

	@Override
	public List<BaseDepartmentDO> listBaseDepartmentDOByIdsArr(Map<String, Object> map) {
		return this.baseDepartmentDAO.listBaseDepartmentDOByIdsArr(map);
	}
	
	/**
	 * 通用实现类
	 * 
	 */
	@Override
	public List<Map<String, Object>> getDepartmentInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return baseDepartmentDAO.getDepartmentInfo(paramMap);
	}

	@Override
	public String getdepartmentCount(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return baseDepartmentDAO.getdepartmentCount(paramMap);
	}

	@Override
	public void deleteDepartment(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		baseDepartmentDAO.deleteDepartment(paramMap);
	}

	@Override
	public void addDepartment(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		baseDepartmentDAO.insertDepartment(paramMap);
	}

	@Override
	public void eaditDepartment(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		baseDepartmentDAO.updateDepartment(paramMap);
	}

	@Override
	public String checkDepCode(String depCode) {
		// TODO Auto-generated method stub
		return baseDepartmentDAO.checkDepCode(depCode);
	}

	@Override
	public ArrayList<CacheModel> getCode() {
		// TODO Auto-generated method stub
		return baseDepartmentDAO.getCode();
	}

	@Override
	public void insertSysCode(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		baseDepartmentDAO.insertSysCode(paramMap);
	}

	@Override
	public void updateSysCode(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		baseDepartmentDAO.updateSysCode(paramMap);
	}

	@Override
	public void removeSysCode(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		baseDepartmentDAO.removeSysCode(paramMap);
	}
}