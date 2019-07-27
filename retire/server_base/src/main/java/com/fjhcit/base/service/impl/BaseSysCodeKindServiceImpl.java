package com.fjhcit.base.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fjhcit.base.dao.BaseSysCodeKindDAO;
import com.fjhcit.base.service.BaseSysCodeKindService;
import com.fjhcit.model.CacheModel;

/**
 * 数据字典实现层
 * @author bule
 *
 */
@Service
public class BaseSysCodeKindServiceImpl implements BaseSysCodeKindService {
	
	@Autowired
	private BaseSysCodeKindDAO baseSysCodeKindDAO;
	@Override
	public String getSysCodeKindCount(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return baseSysCodeKindDAO.getSysCodeKindCount(paramMap);
	}

	@Override
	public List<Map<String, Object>> getSysCodeKindInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return baseSysCodeKindDAO.getSysCodeKindInfo(paramMap);
	}

	@Override
	public void deleteSysCodeKind(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		baseSysCodeKindDAO.deleteSysCodeKind(paramMap);
	}

	@Override
	public void addSysCodeKind(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		baseSysCodeKindDAO.addSysCodeKind(paramMap);
	}

	@Override
	public void updateSysCodeKind(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		baseSysCodeKindDAO.updateSysCodeKind(paramMap);
	}

	@Override
	public void deleteSysCode(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		baseSysCodeKindDAO.deleteSysCode(paramMap);
	}

	@Override
	public String getSysCodeCount(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return baseSysCodeKindDAO.getSysCodeCount(paramMap);
	}

	/**
	 * 数据字典数据与拓展数据
	 */
	@Override
	public Map<String, List<Map>> getSysCodeInfo(Map<String, String> paramMap) {
		List<Map> templist=new ArrayList<Map>();
		List<Map> templist2=new ArrayList<Map>();
		List<Map<String, Object>> colList= baseSysCodeKindDAO.getSysCodeInfo(paramMap);
		List<Map<String, String>> codeAddiList= baseSysCodeKindDAO.selectKdCodeAddIByKdcode(paramMap);
		for (Map<String, String> map : codeAddiList) {
			Map<String,String> temp1 = new HashMap<String, String>();
			temp1.put("field", map.get("KD_CODE_ADDI"));
			temp1.put("title", map.get("NA_CODE_ADDI"));
			temp1.put("edit", "text");
			temp1.put("align", "center");
			templist.add(temp1);
		}
		List<Map<String, String>> codeAddiValList= baseSysCodeKindDAO.selectKdCodeAddIValByKdcode(paramMap);
		for (Map<String, Object> map : colList) {
			for (Map<String, String> map2 : codeAddiValList) {
				if(map.get("CODE").equals(map2.get("CODE"))) {
					map.put(map2.get("KD_CODE_ADDI"), map2.get("CODE_ADDI_VAL"));
				}
			}
			templist2.add(map);
		}
		Map<String, List<Map>> resultList=new HashMap<String, List<Map>> ();
		resultList.put("data", templist2);
		resultList.put("cols", templist);
		return resultList;
	}

	@Override
	public void addSysCode(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		baseSysCodeKindDAO.addSysCode(paramMap);
	}

	@Override
	public String checkSysCodeKindCount(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return baseSysCodeKindDAO.checkSysCodeKindCount(paramMap);
	}

	@Override
	public ArrayList<CacheModel> getCode(String code) {
		// TODO Auto-generated method stub
		return baseSysCodeKindDAO.getCode(code);
	}

	@Override
	public void deleteCodeAddiVal(String kdCode) {
		// TODO Auto-generated method stub
		baseSysCodeKindDAO.deleteCodeAddiVal(kdCode);
	}

	@Override
	public void addCodeAddiVal(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		baseSysCodeKindDAO.addCodeAddiVal(paramMap);
	}

	@Override
	public void addSysCodeAddI(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		baseSysCodeKindDAO.addSysCodeAddI(paramMap);
	}

	@Override
	public void deleteKdCodeAddIByKdcode(String kdCode) {
		// TODO Auto-generated method stub
		baseSysCodeKindDAO.deleteKdCodeAddIByKdcode(kdCode);
	}

}