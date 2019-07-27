package com.fjhcit.retire.service.impl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fjhcit.common.kit.StringUtils;
import com.fjhcit.constant.ParentIdContant;
import com.fjhcit.entity.RetireCostDO;
import com.fjhcit.retire.dao.RetireCostDAO;
import com.fjhcit.retire.service.RetireCostService;

@Service
public class RetireCostServiceImpl implements RetireCostService {

	@Autowired
	private RetireCostDAO retireCostDAO;

	@Override
	public List<RetireCostDO> listRetireCost(Map<String,String> map){
		return this.retireCostDAO.listRetireCost(map);
	}

	@Override
	public RetireCostDO getRetireCostDOById(String id){
		return this.retireCostDAO.getRetireCostDOById(id);
	}

	@Override
	public void insertRetireCost(RetireCostDO retireCostDO){
		this.retireCostDAO.insertRetireCost(retireCostDO);
	}

	@Override
	public void updateRetireCost(RetireCostDO retireCostDO){
		this.retireCostDAO.updateRetireCost(retireCostDO);
	}

	@Override
	public void removeRetireCostByIdsArr(Map<String,Object> map){
		this.retireCostDAO.removeRetireCostByIdsArr(map);
	}

	@Override
	public List<Map<String, String>> listRetireEmployeeCost(Map<String, Object> param) {
    	String unit_id = (String) param.get("unit_id");
    	if(StringUtils.isEmpty(unit_id)) {
    		param.put("parent_id", ParentIdContant.BASE_DEPARTMENT);
    	}else {
    		param.put("id", unit_id);
    	}
    	String dynamicFields = "";
    	@SuppressWarnings("unchecked")
		List<LinkedHashMap<String,String>> natureList = (List<LinkedHashMap<String,String>>) param.get("natureList");
    	@SuppressWarnings("unchecked")
		List<LinkedHashMap<String,String>> rankList = (List<LinkedHashMap<String,String>>) param.get("rankList");
    	for(int i=0;i<natureList.size();i++) {
    		LinkedHashMap<String,String> codeDO = natureList.get(i);
    		dynamicFields += ",to_char((select count(1) from retire_contact where unit_id = d.id and USER_NATURE_ID = '" + codeDO.get("value")+
    				"')) NATURE_" + codeDO.get("value").toUpperCase();
    		//专职需要额外统计职级人数
    		if("专职".equals(codeDO.get("name"))) {
    			for(int j=0;j<rankList.size();j++) {
    				LinkedHashMap<String,String> rankDO = rankList.get(j);
    				dynamicFields += ",to_char((select count(1) from retire_contact where unit_id = d.id and USER_NATURE_ID = '" + codeDO.get("value")+
    	    				"' and USER_RANK_ID = '" + rankDO.get("value") + "')) RANK_" + codeDO.get("value").toUpperCase() + "_" + rankDO.get("value").toUpperCase();
    			}
    		}
    	}
    	param.put("dynamicFields", dynamicFields);
    	List<Map<String, String>> dataList = this.retireCostDAO.listRetireEmployeeCost(param);
    	for(int i=0;i<dataList.size();i++) {
    		Map<String, String> costMap = dataList.get(i);
    		int total = 0;
    		if(StringUtils.isNotEmpty(costMap.get("UTILITIES"))) {
    			total += Double.valueOf(costMap.get("UTILITIES"));
    		}
    		if(StringUtils.isNotEmpty(costMap.get("EMOLUMENT"))) {
    			total += Double.valueOf(costMap.get("EMOLUMENT"));
    		}
    		if(StringUtils.isNotEmpty(costMap.get("DEPRECIATION"))) {
    			total += Double.valueOf(costMap.get("DEPRECIATION"));
    		}
    		if(StringUtils.isNotEmpty(costMap.get("CHUMMAGE"))) {
    			total += Double.valueOf(costMap.get("CHUMMAGE"));
    		}
    		if(StringUtils.isNotEmpty(costMap.get("OFFICE_ALLOWANCE"))) {
    			total += Double.valueOf(costMap.get("OFFICE_ALLOWANCE"));
    		}
    		if(StringUtils.isNotEmpty(costMap.get("TRAVEL_EXPENSE"))) {
    			total += Double.valueOf(costMap.get("TRAVEL_EXPENSE"));
    		}
    		if(StringUtils.isNotEmpty(costMap.get("CONVENTION_EXPENSE"))) {
    			total += Double.valueOf(costMap.get("CONVENTION_EXPENSE"));
    		}
    		if(StringUtils.isNotEmpty(costMap.get("PUBLICITY_EXPENSE"))) {
    			total += Double.valueOf(costMap.get("PUBLICITY_EXPENSE"));
    		}
    		if(StringUtils.isNotEmpty(costMap.get("OTHER"))) {
    			total += Double.valueOf(costMap.get("OTHER"));
    		}
    		costMap.put("TOTAL", total + "");
    	}
		return dataList;
	}

	@Override
	public RetireCostDO getRetireCostDOByUnitidAndYear(Map<String, String> map) {
		return this.retireCostDAO.getRetireCostDOByUnitidAndYear(map);
	}

	@Override
	public List<Map<String, String>> listRetireBasicEndowmentInsurance(Map<String, Object> map) {
    	String unit_id = (String) map.get("unit_id");
    	if(StringUtils.isEmpty(unit_id)) {
    		map.put("parent_id",ParentIdContant.BASE_DEPARTMENT);
    	}else {
    		map.put("id",unit_id);
    	}
		@SuppressWarnings("unchecked")
		List<LinkedHashMap<String,String>> archivesAreaList = (List<LinkedHashMap<String, String>>) map.get("archivesAreaList");
		String dynamicFields = "";
		for(int i=0;i<archivesAreaList.size();i++) {
			LinkedHashMap<String,String> codeDO = archivesAreaList.get(i);
			dynamicFields += ",to_char((select count(1) from retire_user u where unit_id = d.id and (select nvl(special_mark,0) from base_code where code = u.HEALTH_STATUS) != 9 and ARCHIVES_AREA_ID = '" +
					codeDO.get("value").toUpperCase()+"')) ARCHIVES_AREA_"+codeDO.get("value").toUpperCase();
		}
		map.put("dynamicFields", dynamicFields);
		List<Map<String, String>> dataList = this.retireCostDAO.listRetireBasicEndowmentInsurance(map);
		for(int i=0;i<dataList.size();i++) {
			Map<String, String> archivesMap = dataList.get(i);
			int ARCHIVES_AREA_TOTAL = 0;
			for(int j=0;j<archivesAreaList.size();j++) {
				LinkedHashMap<String,String> codeDO = archivesAreaList.get(j);
				ARCHIVES_AREA_TOTAL += Integer.valueOf(archivesMap.get("ARCHIVES_AREA_"+codeDO.get("value").toUpperCase()));
			}
			archivesMap.put("ARCHIVES_AREA_TOTAL", ARCHIVES_AREA_TOTAL + "");
		}
		return dataList;
	}

	@Override
	public List<Map<String, String>> listRetireUserCorrelativeCharges(Map<String, String> map) {
    	String unit_id = (String) map.get("unit_id");
    	if(StringUtils.isEmpty(unit_id)) {
    		map.put("parent_id", ParentIdContant.BASE_DEPARTMENT);
    	}else {
    		map.put("id", unit_id);
    	}
    	String year = map.get("year");
    	if(StringUtils.isNotEmpty(year)) {
    		year = year.replace("年", "");
    		map.put("year", year);
    	}
    	List<Map<String, String>> dataList = this.retireCostDAO.listRetireUserCorrelativeCharges(map);
    	for(int i=0;i<dataList.size();i++) {
    		Map<String, String> chargesMap = dataList.get(i);
    		double SUBTOTAL = 0;
    		if(StringUtils.isNotEmpty(chargesMap.get("MONTHLY_LIVING_ALLOWANCE"))) {
    			SUBTOTAL += Double.valueOf(chargesMap.get("MONTHLY_LIVING_ALLOWANCE"));
    		}
    		if(StringUtils.isNotEmpty(chargesMap.get("ONE_TIME_LIVING_ALLOWANCE"))) {
    			SUBTOTAL += Double.valueOf(chargesMap.get("ONE_TIME_LIVING_ALLOWANCE"));
    		}
    		if(StringUtils.isNotEmpty(chargesMap.get("MEDICAL_FEE"))) {
    			SUBTOTAL += Double.valueOf(chargesMap.get("MEDICAL_FEE"));
    		}
    		if(StringUtils.isNotEmpty(chargesMap.get("ACTIVITY_FUNDS"))) {
    			SUBTOTAL += Double.valueOf(chargesMap.get("ACTIVITY_FUNDS"));
    		}
    		if(StringUtils.isNotEmpty(chargesMap.get("SUBSIDIES_FOR_HEATING"))) {
    			SUBTOTAL += Double.valueOf(chargesMap.get("SUBSIDIES_FOR_HEATING"));
    		}
    		if(StringUtils.isNotEmpty(chargesMap.get("OTHER_EXPENSES"))) {
    			SUBTOTAL += Double.valueOf(chargesMap.get("OTHER_EXPENSES"));
    		}
    		double EXPENSE_TOTAL = SUBTOTAL;
    		if(StringUtils.isNotEmpty(chargesMap.get("SUPPLEMENTARY_MEDICAL"))) {
    			EXPENSE_TOTAL += Double.valueOf(chargesMap.get("SUPPLEMENTARY_MEDICAL"));
    		}
    		chargesMap.put("SUBTOTAL", String.format("%.2f", SUBTOTAL));
    		chargesMap.put("EXPENSE_TOTAL", String.format("%.2f", EXPENSE_TOTAL));
    	}
		return dataList;
	}

	@Override
	public List<Map<String, String>> listRetireDepartmentAndCost(Map<String, Object> map) {
		String unit_id = (String)map.get("unit_id");
    	if(StringUtils.isEmpty(unit_id)) {
    		map.put("parent_id", ParentIdContant.BASE_DEPARTMENT);
    	}else {
    		map.put("id", unit_id);
    	}
    	String year = (String)map.get("year");
    	if(StringUtils.isNotEmpty(year)) {
    		year = year.replace("年", "");
    		map.put("year", year);
    	}
    	@SuppressWarnings("unchecked")
		List<LinkedHashMap<String,String>> divisionList = (List<LinkedHashMap<String,String>>)map.get("divisionList");
    	@SuppressWarnings("unchecked")
		List<LinkedHashMap<String,String>> typeList = (List<LinkedHashMap<String,String>>)map.get("typeList");
    	//拼装动态SQL语句
    	String dynamicFields = "";
		for(int i=0;i<divisionList.size();i++) {
			LinkedHashMap<String,String> divisionCodeDO = divisionList.get(i);
			if("1".equals(divisionCodeDO.get("special_mark"))) {
				dynamicFields += ",to_char((select count(1) from base_department where PARENT_ID = d.id and IS_AVAILABLE = 1 " +
						" and ADMINISTRATIVE_DIVISION_CODE = '" + divisionCodeDO.get("value") + "')) as DEP_TYPE_" + divisionCodeDO.get("value").toUpperCase();
			}else {
				for(int j=0;j<typeList.size();j++) {
					LinkedHashMap<String,String> typeCodeDO = typeList.get(j);
					dynamicFields += ",to_char((select count(1) from base_department where PARENT_ID = d.id and IS_AVAILABLE = 1 " +
							" and ADMINISTRATIVE_DIVISION_CODE = '" + divisionCodeDO.get("value") + "' and DEPARTMENT_TYPE_ID = '" +
							typeCodeDO.get("value") +"')) as DEP_TYPE_" + divisionCodeDO.get("value").toUpperCase() + "_" + typeCodeDO.get("value").toUpperCase();
				}
			}
		}
		map.put("dynamicFields", dynamicFields);
		return this.retireCostDAO.listRetireDepartmentAndCost(map);
	}
}