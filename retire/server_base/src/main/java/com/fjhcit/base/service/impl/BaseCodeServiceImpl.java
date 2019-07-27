package com.fjhcit.base.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fjhcit.entity.BaseCodeDO;
import com.fjhcit.base.dao.BaseCodeDAO;
import com.fjhcit.base.service.BaseCodeService;

@Service
public class BaseCodeServiceImpl implements BaseCodeService {

	@Autowired
	private BaseCodeDAO baseCodeDAO;

	@Override
	public List<BaseCodeDO> listBaseCode(Map<String,String> map){
		return this.baseCodeDAO.listBaseCode(map);
	}

	@Override
	public BaseCodeDO getBaseCodeDOById(String id){
		return this.baseCodeDAO.getBaseCodeDOById(id);
	}

	@Override
	public String insertBaseCode(BaseCodeDO baseCodeDO){
		return this.baseCodeDAO.insertBaseCode(baseCodeDO);
	}

	@Override
	public void updateBaseCode(BaseCodeDO baseCodeDO){
		this.baseCodeDAO.updateBaseCode(baseCodeDO);
	}

	@Override
	public void removeBaseCodeByIdsArr(Map<String,Object> map){
		this.baseCodeDAO.removeBaseCodeByIdsArr(map);
	}

	@Override
	public String[] getBaseCodeIdArray(Map<String, String> map) {
		return this.baseCodeDAO.getBaseCodeIdArray(map);
	}
}