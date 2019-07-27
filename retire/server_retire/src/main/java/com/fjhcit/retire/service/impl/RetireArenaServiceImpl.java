package com.fjhcit.retire.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fjhcit.common.kit.CommonUtils;
import com.fjhcit.constant.ParentIdContant;
import com.fjhcit.entity.RetireArenaDO;
import com.fjhcit.model.ResultVO;
import com.fjhcit.model.RetireArenaExcelVO;
import com.fjhcit.retire.dao.RetireArenaDAO;
import com.fjhcit.retire.service.FeignZuulServer;
import com.fjhcit.retire.service.RetireArenaService;

@Service
public class RetireArenaServiceImpl implements RetireArenaService {

	@Autowired
	private RetireArenaDAO 		retireArenaDAO;
	@Autowired
	private FeignZuulServer		feignZuulServer;			//远程调用其他组件接口

	@Override
	public List<RetireArenaDO> listRetireArena(Map<String,String> map){
		return this.retireArenaDAO.listRetireArena(map);
	}

	@Override
	public RetireArenaDO getRetireArenaDOById(String id){
		return this.retireArenaDAO.getRetireArenaDOById(id);
	}

	@Override
	public void insertRetireArena(RetireArenaDO retireArenaDO){
		this.retireArenaDAO.insertRetireArena(retireArenaDO);
	}

	@Override
	public void updateRetireArena(RetireArenaDO retireArenaDO){
		this.retireArenaDAO.updateRetireArena(retireArenaDO);
	}

	@Override
	public void removeRetireArenaByIdsArr(Map<String,Object> map){
		this.retireArenaDAO.removeRetireArenaByIdsArr(map);
	}

	@Override
	public List<Map<String,String>> listRetireArenaAndUniversity(Map<String, String> map) {
		String unit_id = map.get("unit_id");
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
		return this.retireArenaDAO.listRetireArenaAndUniversity(map);
	}

	@Override
	public List<Map<String, String>> listRetireArenaServiceCondition(Map<String, String> map) {
		String unit_id = map.get("unit_id");
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
		return this.retireArenaDAO.listRetireArenaServiceCondition(map);
	}

	@Override
	public Map<String, String> saveRetireArenaByExcel(Map<String, Object> map) {
		Map<String,String> retMap = new HashMap<>();
		String year = (String)map.get("year");
    	if(StringUtils.isNotEmpty(year)) {
    		year = year.replace("年", "");
    		map.put("year", year);
    	}else {
    		retMap.put("errorMessageHtml", "统计年度选择不能为空");
    		return retMap;
    	}
		@SuppressWarnings("unchecked")
		List<Object> dataList = (List<Object>)map.get("dataList");
		//1、查单位名称
    	Map<String,String> unitParam = new HashMap<String,String>();
    	unitParam.put("department_id", ParentIdContant.BASE_DEPARTMENT_PROVINCIAL);
    	unitParam.put("parent_id", ParentIdContant.BASE_DEPARTMENT);
    	unitParam.put("is_available", "1");
    	ResultVO unitVO = this.feignZuulServer.listBaseDepartment(unitParam);
    	@SuppressWarnings("unchecked")
    	List<LinkedHashMap<String,String>> unitList = (List<LinkedHashMap<String,String>>)unitVO.getData();
		//开始处理数据
		String errorMessageHtml = "";
		int addNum = 0;
		int updateNum = 0;
		int errorNum = 0;
		for(int i=0;i<dataList.size();i++) {
			RetireArenaExcelVO excelVO = (RetireArenaExcelVO)dataList.get(i);
			String userinfo = "第" + (i+1) + "条记录：" + excelVO.getUnit_id() + "的";
			String errorMessage = "";
			String unitname = excelVO.getUnit_id();
			if(StringUtils.isNotEmpty(unitname)) {
				String codeid = isDictionaryByDepartmentName(unitname, unitList);
				excelVO.setUnit_id(codeid);
				if(StringUtils.isEmpty(codeid)) {
					errorMessage += "单位名称错误；";
				}
			}else {
				errorMessage += "单位名称必填；";
			}
			String existing_sum = excelVO.getExisting_sum();
			if(StringUtils.isNotEmpty(existing_sum)) {
				if(!StringUtils.isNumeric(existing_sum)) {
					errorMessage += "现有活动场所_总数（个）错误；";
				}
			}
			String existing_acreage = excelVO.getExisting_acreage();
			if(StringUtils.isNotEmpty(existing_acreage)) {
				if(!StringUtils.isNumeric(existing_acreage) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(existing_acreage)) {
					errorMessage += "现有活动场所_总建筑面积（平方米）错误；";
				}
			}
			String existing_everyday = excelVO.getExisting_everyday();
			if(StringUtils.isNotEmpty(existing_everyday)) {
				if(!StringUtils.isNumeric(existing_everyday)) {
					errorMessage += "现有活动场所_日均活动人数错误；";
				}
			}
			String abuilding_sum = excelVO.getAbuilding_sum();
			if(StringUtils.isNotEmpty(abuilding_sum)) {
				if(!StringUtils.isNumeric(abuilding_sum)) {
					errorMessage += "在建活动场所_总数（个）错误；";
				}
			}
			String abuilding_acreage = excelVO.getAbuilding_acreage();
			if(StringUtils.isNotEmpty(abuilding_acreage)) {
				if(!StringUtils.isNumeric(abuilding_acreage) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(abuilding_acreage)) {
					errorMessage += "在建活动场所_总建筑面积（平方米）错误；";
				}
			}
			String independent_sum = excelVO.getIndependent_sum();
			if(StringUtils.isNotEmpty(independent_sum)) {
				if(!StringUtils.isNumeric(independent_sum)) {
					errorMessage += "退休人员独立使用_总数（个）错误；";
				}
			}
			String independent_acreage = excelVO.getIndependent_acreage();
			if(StringUtils.isNotEmpty(independent_acreage)) {
				if(!StringUtils.isNumeric(independent_acreage) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(independent_acreage)) {
					errorMessage += "退休人员独立使用_总建筑面积（平方米）错误；";
				}
			}
			String independent_original_value = excelVO.getIndependent_original_value();
			if(StringUtils.isNotEmpty(independent_original_value)) {
				if(!StringUtils.isNumeric(independent_original_value) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(independent_original_value)) {
					errorMessage += "退休人员独立使用_年末固定资产原值（万元）错误；";
				}
			}
			String independent_net_value = excelVO.getIndependent_net_value();
			if(StringUtils.isNotEmpty(independent_net_value)) {
				if(!StringUtils.isNumeric(independent_net_value) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(independent_net_value)) {
					errorMessage += "退休人员独立使用_年末固定资产净值（万元）错误；";
				}
			}
			String common_sum = excelVO.getCommon_sum();
			if(StringUtils.isNotEmpty(common_sum)) {
				if(!StringUtils.isNumeric(common_sum)) {
					errorMessage += "与在职人员共同使用_总数（个）错误；";
				}
			}
			String common_acreage = excelVO.getCommon_acreage();
			if(StringUtils.isNotEmpty(common_acreage)) {
				if(!StringUtils.isNumeric(common_acreage) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(common_acreage)) {
					errorMessage += "与在职人员共同使用_总建筑面积（平方米）错误；";
				}
			}
			String common_original_value = excelVO.getCommon_original_value();
			if(StringUtils.isNotEmpty(common_original_value)) {
				if(!StringUtils.isNumeric(common_original_value) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(common_original_value)) {
					errorMessage += "与在职人员共同使用_年末固定资产原值（万元）错误；";
				}
			}
			String common_net_value = excelVO.getCommon_net_value();
			if(StringUtils.isNotEmpty(common_net_value)) {
				if(!StringUtils.isNumeric(common_net_value) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(common_net_value)) {
					errorMessage += "与在职人员共同使用_年末固定资产净值（万元）错误；";
				}
			}
			String eligible_sum = excelVO.getEligible_sum();
			if(StringUtils.isNotEmpty(eligible_sum)) {
				if(!StringUtils.isNumeric(eligible_sum)) {
					errorMessage += "具备移交条件的活动场所_总数（个）错误；";
				}
			}
			String eligible_acreage = excelVO.getEligible_acreage();
			if(StringUtils.isNotEmpty(eligible_acreage)) {
				if(!StringUtils.isNumeric(eligible_acreage) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(eligible_acreage)) {
					errorMessage += "具备移交条件的活动场所_总建筑面积（平方米）错误；";
				}
			}
			String eligible_original_value = excelVO.getEligible_original_value();
			if(StringUtils.isNotEmpty(eligible_original_value)) {
				if(!StringUtils.isNumeric(eligible_original_value) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(eligible_original_value)) {
					errorMessage += "具备移交条件的活动场所_年末固定资产原值（万元）错误；";
				}
			}
			String eligible_net_value = excelVO.getEligible_net_value();
			if(StringUtils.isNotEmpty(eligible_net_value)) {
				if(!StringUtils.isNumeric(eligible_net_value) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(eligible_net_value)) {
					errorMessage += "具备移交条件的活动场所_年末固定资产净值（万元）错误；";
				}
			}
			String fixed_assets_original_value = excelVO.getFixed_assets_original_value();
			if(StringUtils.isNotEmpty(fixed_assets_original_value)) {
				if(!StringUtils.isNumeric(fixed_assets_original_value) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(fixed_assets_original_value)) {
					errorMessage += "年末固定资产原值错误；";
				}
			}
			String fixed_assets_net_value = excelVO.getFixed_assets_net_value();
			if(StringUtils.isNotEmpty(fixed_assets_net_value)) {
				if(!StringUtils.isNumeric(fixed_assets_net_value) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(fixed_assets_net_value)) {
					errorMessage += "年末固定资产净值错误；";
				}
			}
			String university_sum = excelVO.getUniversity_sum();
			if(StringUtils.isNotEmpty(university_sum)) {
				if(!StringUtils.isNumeric(university_sum)) {
					errorMessage += "现有老年大学_总数（个）错误；";
				}
			}
			String university_acreage = excelVO.getUniversity_acreage();
			if(StringUtils.isNotEmpty(university_acreage)) {
				if(!StringUtils.isNumeric(university_acreage) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(university_acreage)) {
					errorMessage += "现有老年大学_总建筑面积（平方米）错误；";
				}
			}
			String university_regular_staff = excelVO.getUniversity_regular_staff();
			if(StringUtils.isNotEmpty(university_regular_staff)) {
				if(!StringUtils.isNumeric(university_regular_staff)) {
					errorMessage += "现有老年大学_正式工作人员错误；";
				}
			}
			String university_students = excelVO.getUniversity_students();
			if(StringUtils.isNotEmpty(university_students)) {
				if(!StringUtils.isNumeric(university_students)) {
					errorMessage += "现有老年大学_在读学员人数错误；";
				}
			}
			String university_graduate = excelVO.getUniversity_graduate();
			if(StringUtils.isNotEmpty(university_graduate)) {
				if(!StringUtils.isNumeric(university_graduate)) {
					errorMessage += "现有老年大学_累计结业人数错误；";
				}
			}
			String abuilding_university_sum = excelVO.getAbuilding_university_sum();
			if(StringUtils.isNotEmpty(abuilding_university_sum)) {
				if(!StringUtils.isNumeric(abuilding_university_sum)) {
					errorMessage += "在建老年大学_总数（个）错误；";
				}
			}
			String abuilding_university_acreage = excelVO.getAbuilding_university_acreage();
			if(StringUtils.isNotEmpty(abuilding_university_acreage)) {
				if(!StringUtils.isNumeric(abuilding_university_acreage) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(abuilding_university_acreage)) {
					errorMessage += "在建老年大学_总建筑面积（平方米）错误；";
				}
			}
			String attend_students = excelVO.getAttend_students();
			if(StringUtils.isNotEmpty(attend_students)) {
				if(!StringUtils.isNumeric(attend_students)) {
					errorMessage += "就读社会老年大学_在读学员人数错误；";
				}
			}
			String attend_graduate = excelVO.getAttend_graduate();
			if(StringUtils.isNotEmpty(attend_graduate)) {
				if(!StringUtils.isNumeric(attend_graduate)) {
					errorMessage += "就读社会老年大学_累计结业人数错误；";
				}
			}
			//拼装最终输出的错误提示消息
			if(StringUtils.isNotEmpty(errorMessage)) {
				errorNum++;
				errorMessageHtml += userinfo + errorMessage + "<br>";
			}else {   //人员数据没有格式问题
				String userId = CommonUtils.getCurrentUserId();
				excelVO.setModified_user_id(userId);
				excelVO.setYear(year);
				//根据单位+年度判断数据是否存在
				Map<String,String> cparam = new HashMap<String, String>();
				cparam.put("unit_id", excelVO.getUnit_id());
				cparam.put("year", year);
				List<String> userList = this.retireArenaDAO.listRetireArenaIdByUnitidAndYear(cparam);
				if(com.fjhcit.common.kit.StringUtils.isNotEmpty(userList)) {	//修改
					for(int j=0;j<userList.size();j++) {
						excelVO.setId(userList.get(j) + "");
						this.retireArenaDAO.updateRetireArena(excelVO);
					}
					updateNum++;
				}else {		//新增
					excelVO.setCreate_user_id(userId);
					this.retireArenaDAO.insertRetireArena(excelVO);
					addNum++;
				}
			}
		}
		retMap.put("errorMessageHtml", "<b>成功新增" + addNum + "条数据，成功更新" + updateNum + "条数据，以下" + errorNum + 
				"条错误数据未入库：</b><br>" + errorMessageHtml);
		return retMap;
	}
	
	/**
	 * 判断Excel数据是否在组织机构中存在（存在有效，不存在无效）
	 * @param excelVal
	 * @param codeList
	 * @return
	 */
	private String isDictionaryByDepartmentName(String excelVal,List<LinkedHashMap<String,String>> codeList) {
		String codeid = "";
		if(StringUtils.isNotEmpty(excelVal)) {
			for(int j=0;j<codeList.size();j++) {
				if(excelVal.equals(codeList.get(j).get("department_name"))) {
					codeid = codeList.get(j).get("id");
				}
			}
		}
		return codeid;
	}

	@Override
	public List<String> listRetireArenaIdByUnitidAndYear(Map<String, String> map) {
		return this.retireArenaDAO.listRetireArenaIdByUnitidAndYear(map);
	}
}