package com.fjhcit.retire.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fjhcit.common.kit.CommonUtils;
import com.fjhcit.entity.RetireQuitDO;
import com.fjhcit.model.RetireQuitExcelVO;
import com.fjhcit.retire.dao.RetireQuitDAO;
import com.fjhcit.retire.dao.RetireUserDAO;
import com.fjhcit.retire.service.RetireQuitService;

@Service
public class RetireQuitServiceImpl implements RetireQuitService {

	@Autowired
	private RetireQuitDAO 		retireQuitDAO;
	@Autowired
	private RetireUserDAO 		retireUserDAO;
	
	@Override
	public List<RetireQuitDO> listRetireQuitUser(Map<String, String> map) {
		return this.retireQuitDAO.listRetireQuitUser(map);
	}

	@Override
	public List<RetireQuitDO> listRetireQuit(Map<String,String> map){
		return this.retireQuitDAO.listRetireQuit(map);
	}

	@Override
	public RetireQuitDO getRetireQuitDOById(String id){
		return this.retireQuitDAO.getRetireQuitDOById(id);
	}

	@Override
	public void insertRetireQuit(RetireQuitDO retireQuitDO){
		this.retireQuitDAO.insertRetireQuit(retireQuitDO);
	}

	@Override
	public void updateRetireQuit(RetireQuitDO retireQuitDO){
		this.retireQuitDAO.updateRetireQuit(retireQuitDO);
	}

	@Override
	public void removeRetireQuitByIdsArr(Map<String,Object> map){
		this.retireQuitDAO.removeRetireQuitByIdsArr(map);
	}

	@Override
	public RetireQuitDO getRetireQuitDOByUserid(String userid) {
		return this.retireQuitDAO.getRetireQuitDOByUserid(userid);
	}

	@Override
	public Map<String, String> saveRetireQuitByExcel(Map<String, Object> map) {
		Map<String,String> retMap = new HashMap<>();
		@SuppressWarnings("unchecked")
		List<Object> dataList = (List<Object>)map.get("dataList");
		//开始处理数据
		String errorMessageHtml = "";
		int addNum = 0;
		int updateNum = 0;
		int errorNum = 0;
		for(int i=0;i<dataList.size();i++) {
			RetireQuitExcelVO excelVO = (RetireQuitExcelVO)dataList.get(i);
			String userinfo = "第" + (i+1) + "条记录：";
			String errorMessage = "";
			List<Integer> userList = null;
			if(StringUtils.isEmpty(excelVO.getIdcard())) {
				errorMessage += "身份号码必填；";
			}else if(excelVO.getIdcard().length()!=18) {
				errorMessage += "身份号码必需18位；";
			}else {
				//根据身份证判断人员是否存在
				Map<String,String> cparam = new HashMap<String, String>();
				cparam.put("idcard", excelVO.getIdcard());
				userList = this.retireUserDAO.listRetireUserIdByIdcard(cparam);
				if(com.fjhcit.common.kit.StringUtils.isEmpty(userList)) {
					errorMessage += "身份证号码查询不到人员基本信息；";
				}
			}
			String monthly_income = excelVO.getMonthly_income();
			if(StringUtils.isNotEmpty(monthly_income)) {
				if(!StringUtils.isNumeric(monthly_income) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(monthly_income)) {
					errorMessage += "月收入总额错误；";
				}
			}
			String basic_expenses = excelVO.getBasic_expenses();
			if(StringUtils.isNotEmpty(basic_expenses)) {
				if(!StringUtils.isNumeric(basic_expenses) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(basic_expenses)) {
					errorMessage += "基本离休费错误；";
				}
			}
			String province_subsidy = excelVO.getProvince_subsidy();
			if(StringUtils.isNotEmpty(province_subsidy)) {
				if(!StringUtils.isNumeric(province_subsidy) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(province_subsidy)) {
					errorMessage += "省（部）规定补贴错误；";
				}
			}
			String other_subsidy = excelVO.getOther_subsidy();
			if(StringUtils.isNotEmpty(other_subsidy)) {
				if(!StringUtils.isNumeric(other_subsidy) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(other_subsidy)) {
					errorMessage += "其它各项补贴错误；";
				}
			}
			String self_employed_fee = excelVO.getSelf_employed_fee();
			if(StringUtils.isNotEmpty(self_employed_fee)) {
				if(!StringUtils.isNumeric(self_employed_fee) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(self_employed_fee)) {
					errorMessage += "自雇费错误；";
				}
			}
			String nursing_fee = excelVO.getNursing_fee();
			if(StringUtils.isNotEmpty(nursing_fee)) {
				if(!StringUtils.isNumeric(nursing_fee) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(nursing_fee)) {
					errorMessage += "护理费错误；";
				}
			}
			String car_fare = excelVO.getCar_fare();
			if(StringUtils.isNotEmpty(car_fare)) {
				if(!StringUtils.isNumeric(car_fare) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(car_fare)) {
					errorMessage += "交通费错误；";
				}
			}
			String children_number = excelVO.getChildren_number();
			if(StringUtils.isNotEmpty(children_number)) {
				if(!StringUtils.isNumeric(children_number)) {
					errorMessage += "子女总人数错误；";
				}
			}
			String raising_a_child_number = excelVO.getRaising_a_child_number();
			if(StringUtils.isNotEmpty(raising_a_child_number)) {
				if(!StringUtils.isNumeric(raising_a_child_number)) {
					errorMessage += "无劳动能力需抚养子女人数错误；";
				}
			}
			String spouse_situation = excelVO.getSpouse_situation();
			if(StringUtils.isNotEmpty(spouse_situation)) {
				if("健在".equals(spouse_situation.trim())){
					excelVO.setSpouse_situation("1");
				}else if("死亡".equals(spouse_situation.trim())) {
					excelVO.setSpouse_situation("9");
				}else {
					errorMessage += "配偶状况错误；";
				}
			}
			String spouse_birth_date = excelVO.getSpouse_birth_date();
			if(StringUtils.isNotEmpty(spouse_birth_date)) {
				if(!com.fjhcit.common.kit.StringUtils.isDate(spouse_birth_date, "yyyy-MM-dd")) {
					errorMessage += "配偶出生年月错误；";
				}
			}
			String spouse_is_work = excelVO.getSpouse_is_work();
			if(StringUtils.isNotEmpty(spouse_is_work)) {
				if("有工作".equals(spouse_is_work.trim())){
					excelVO.setSpouse_is_work("1");
				}else if("无工作".equals(spouse_is_work.trim())) {
					excelVO.setSpouse_is_work("0");
				}else {
					errorMessage += "配偶有无工作情况错误；";
				}
			}
			String regular_subsidy = excelVO.getRegular_subsidy();
			if(StringUtils.isNotEmpty(regular_subsidy)) {
				if(!StringUtils.isNumeric(regular_subsidy) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(regular_subsidy)) {
					errorMessage += "定期补助/月（元）错误；";
				}
			}
			//拼装最终输出的错误提示消息
			if(StringUtils.isNotEmpty(errorMessage)) {
				errorNum++;
				errorMessageHtml += userinfo + errorMessage + "<br>";
			}else {   //人员数据没有格式问题
				//根据单位ID + 身份证查人员基本信息是否存在、离休人员信息是否存在
				String loginUserid = CommonUtils.getCurrentUserId();
				RetireQuitDO quitDO = this.retireQuitDAO.getRetireQuitDOByUserid(userList.get(0) + "");
				excelVO.setUser_id(userList.get(0) + "");
				excelVO.setModified_user_id(loginUserid);
				if(null==quitDO) {
					excelVO.setCreate_user_id(loginUserid);
					this.retireQuitDAO.insertRetireQuit(excelVO);
					addNum++;
				}else {
					excelVO.setId(quitDO.getId());
					this.retireQuitDAO.updateRetireQuit(excelVO);
					updateNum++;
				}
			}
		}
		retMap.put("errorMessageHtml", "<b>成功新增" + addNum + "条数据，成功更新" + updateNum + "条数据，以下" + errorNum + 
				"条错误数据未入库：</b><br>" + errorMessageHtml);
		return retMap;
	}
}