package com.fjhcit.retire.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fjhcit.entity.RetireFamilyDO;
import com.fjhcit.retire.dao.RetireFamilyDAO;
import com.fjhcit.retire.service.RetireFamilyService;

@Service
public class RetireFamilyServiceImpl implements RetireFamilyService {

	@Autowired
	private RetireFamilyDAO retireFamilyDAO;

	@Override
	public List<RetireFamilyDO> listRetireFamily(Map<String,String> map){
		return this.retireFamilyDAO.listRetireFamily(map);
	}

	@Override
	public RetireFamilyDO getRetireFamilyDOById(String id){
		return this.retireFamilyDAO.getRetireFamilyDOById(id);
	}

	@Override
	public void insertRetireFamily(RetireFamilyDO retireFamilyDO){
		this.retireFamilyDAO.insertRetireFamily(retireFamilyDO);
	}

	@Override
	public void updateRetireFamily(RetireFamilyDO retireFamilyDO){
		this.retireFamilyDAO.updateRetireFamily(retireFamilyDO);
	}

	@Override
	public void removeRetireFamilyByIdsArr(Map<String,Object> map){
		this.retireFamilyDAO.removeRetireFamilyByIdsArr(map);
	}

	@Override
	public void removeRetireFamilyByUserid(Map<String, String> map) {
		this.retireFamilyDAO.removeRetireFamilyByUserid(map);
	}
}