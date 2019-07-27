package com.fjhcit.retire.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fjhcit.constant.ParentIdContant;
import com.fjhcit.entity.BaseDepartmentDO;
import com.fjhcit.entity.RetirePartyDO;
import com.fjhcit.retire.dao.RetirePartyDAO;
import com.fjhcit.retire.service.RetirePartyService;

@Service
public class RetirePartyServiceImpl implements RetirePartyService {

	@Autowired
	private RetirePartyDAO retirePartyDAO;

	@Override
	public List<RetirePartyDO> listRetireParty(Map<String,String> map){
		return this.retirePartyDAO.listRetireParty(map);
	}

	@Override
	public RetirePartyDO getRetirePartyDOById(String id){
		return this.retirePartyDAO.getRetirePartyDOById(id);
	}

	@Override
	public void insertRetireParty(RetirePartyDO retirePartyDO){
		this.retirePartyDAO.insertRetireParty(retirePartyDO);
	}

	@Override
	public void updateRetireParty(RetirePartyDO retirePartyDO){
		this.retirePartyDAO.updateRetireParty(retirePartyDO);
	}

	@Override
	public void removeRetirePartyByIdsArr(Map<String,Object> map){
		this.retirePartyDAO.removeRetirePartyByIdsArr(map);
	}

	@Override
	public List<BaseDepartmentDO> listBaseDepartment(Map<String, String> map) {
		return this.retirePartyDAO.listBaseDepartment(map);
	}

	@Override
	public List<Map<String, String>> listRetirePartyOrganization(Map<String, String> map) {
    	String unit_id = (String) map.get("unit_id");
    	if(StringUtils.isEmpty(unit_id)) {
    		map.put("parent_id", ParentIdContant.BASE_DEPARTMENT);
    	}else {
    		map.put("id", unit_id);
    	}
		return this.retirePartyDAO.listRetirePartyOrganization(map);
	}
}