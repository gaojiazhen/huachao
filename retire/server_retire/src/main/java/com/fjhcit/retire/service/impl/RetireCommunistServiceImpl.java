package com.fjhcit.retire.service.impl;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fjhcit.common.kit.CommonUtils;
import com.fjhcit.constant.ParentIdContant;
import com.fjhcit.entity.RetireCommunistDO;
import com.fjhcit.model.ResultVO;
import com.fjhcit.model.RetireCommunistExcelVO;
import com.fjhcit.retire.dao.RetireCommunistDAO;
import com.fjhcit.retire.dao.RetireUserDAO;
import com.fjhcit.retire.service.FeignZuulServer;
import com.fjhcit.retire.service.RetireCommunistService;

@Service
public class RetireCommunistServiceImpl implements RetireCommunistService {

	@Autowired
	private RetireCommunistDAO 	retireCommunistDAO;
	@Autowired
	private RetireUserDAO 		retireUserDAO;
	@Autowired
	private FeignZuulServer		feignZuulServer;			//远程调用其他组件接口

	@Override
	public List<RetireCommunistDO> listRetireCommunist(Map<String,String> map){
		return this.retireCommunistDAO.listRetireCommunist(map);
	}

	@Override
	public RetireCommunistDO getRetireCommunistDOById(String id){
		return this.retireCommunistDAO.getRetireCommunistDOById(id);
	}
	
	@Override
	public RetireCommunistDO getRetireCommunistDOByUserid(String userid) {
		return this.retireCommunistDAO.getRetireCommunistDOByUserid(userid);
	}

	@Override
	public void insertRetireCommunist(RetireCommunistDO retireCommunistDO){
		this.retireCommunistDAO.insertRetireCommunist(retireCommunistDO);
	}

	@Override
	public void updateRetireCommunist(RetireCommunistDO retireCommunistDO){
		this.retireCommunistDAO.updateRetireCommunist(retireCommunistDO);
	}

	@Override
	public void removeRetireCommunistByIdsArr(Map<String,Object> map){
		this.retireCommunistDAO.removeRetireCommunistByIdsArr(map);
	}

	@Override
	public Map<String, String> saveRetireCommunistByExcel(List<Object> dataList) {
		Map<String,String> retMap = new HashMap<>();
		//1、查组织关系所在单位
		Map<String,String> codeParam = new HashMap<String, String>();
		codeParam.put("code", ParentIdContant.BASE_CODE_PARTY_LOCATED);
		ResultVO resultVO = this.feignZuulServer.getCode(codeParam);
		@SuppressWarnings("unchecked")
		Map<String,List<LinkedHashMap<String, String>>> dataMap = (Map<String, List<LinkedHashMap<String, String>>>) resultVO.getData();
		List<LinkedHashMap<String, String>> locatedList = dataMap.get(ParentIdContant.BASE_CODE_PARTY_LOCATED);
        System.out.println(Arrays.asList(locatedList));
		//开始处理数据
		String errorMessageHtml = "";
		int addNum = 0;
		int updateNum = 0;
		int errorNum = 0;
		for(int i=0;i<dataList.size();i++) {
			RetireCommunistExcelVO excelVO = (RetireCommunistExcelVO)dataList.get(i);
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
			String category_personnel = excelVO.getCategory_personnel();
			if(StringUtils.isNotEmpty(category_personnel)) {
				if("正式党员".equals(category_personnel)) {
					excelVO.setCategory_personnel("1");
				}else {
					errorMessage += "人员类别错误；";
				}
			}
			String gmt_party = excelVO.getGmt_party();
			if(StringUtils.isNotEmpty(gmt_party)) {
				if(!com.fjhcit.common.kit.StringUtils.isDate(gmt_party, "yyyy-MM-dd")) {
					errorMessage += "加入党组织日期错误；";
				}
			}
			String gmt_become = excelVO.getGmt_become();
			if(StringUtils.isNotEmpty(gmt_become)) {
				if(!com.fjhcit.common.kit.StringUtils.isDate(gmt_become, "yyyy-MM-dd")) {
					errorMessage += "转为正式党员日期错误；";
				}
			}
			String operating_post = excelVO.getOperating_post();
			if(StringUtils.isNotEmpty(operating_post)) {
				if(!"退休人员".equals(operating_post)) {
					errorMessage += "工作岗位错误；";
				}
			}
			String membership_credentials_id = excelVO.getMembership_credentials_id();
			if(StringUtils.isNotEmpty(membership_credentials_id)) {
				String codeid = isDictionaryByCodename(membership_credentials_id, locatedList);
				excelVO.setMembership_credentials_id(codeid);
				if(StringUtils.isEmpty(codeid)) {
					errorMessage += "组织关系所在单位错误；";
				}
			}
			String party_membership = excelVO.getParty_membership();
			if(StringUtils.isNotEmpty(party_membership)) {
				if("正常".equals(party_membership)) {
					excelVO.setParty_membership("1");
				}else if("不正常".equals(party_membership)) {
					excelVO.setParty_membership("0");
				}else {
					errorMessage += "党籍状态错误；";
				}
			}
			String party_membership_dues = excelVO.getParty_membership_dues();
			if(StringUtils.isNotEmpty(party_membership_dues)) {
				if(!StringUtils.isNumeric(party_membership_dues) && !com.fjhcit.common.kit.StringUtils.isNumberOrDecimal(party_membership_dues)) {
					errorMessage += "月应缴纳党费错误；";
				}
			}
			String gmt_paid_until = excelVO.getGmt_paid_until();
			if(StringUtils.isNotEmpty(gmt_paid_until)) {
				if(!com.fjhcit.common.kit.StringUtils.isDate(gmt_paid_until, "yyyy-MM-dd")) {
					errorMessage += "党费缴至年月错误；";
				}
			}
			String is_missing = excelVO.getIs_missing();
			if(StringUtils.isNotEmpty(is_missing)) {
				if("是".equals(is_missing)) {
					excelVO.setIs_missing("1");
				}else if("否".equals(is_missing)) {
					excelVO.setIs_missing("0");
				}else {
					errorMessage += "是否为失联党员错误；";
				}
			}
			String gmt_missing = excelVO.getGmt_missing();
			if(StringUtils.isNotEmpty(gmt_missing)) {
				if(!com.fjhcit.common.kit.StringUtils.isDate(gmt_missing, "yyyy-MM-dd")) {
					errorMessage += "失去联系日期错误；";
				}
			}
			String is_flow_communist = excelVO.getIs_flow_communist();
			if(StringUtils.isNotEmpty(is_flow_communist)) {
				if("是".equals(is_flow_communist)) {
					excelVO.setIs_flow_communist("1");
				}else if("否".equals(is_flow_communist)) {
					excelVO.setIs_flow_communist("0");
				}else {
					errorMessage += "是否为失联党员错误；";
				}
			}
			//拼装最终输出的错误提示消息
			if(StringUtils.isNotEmpty(errorMessage)) {
				errorNum++;
				errorMessageHtml += userinfo + errorMessage + "<br>";
			}else {   //人员数据没有格式问题
				//根据单位ID + 身份证查人员基本信息是否存在、离休人员信息是否存在
				String loginUserid = CommonUtils.getCurrentUserId();
				RetireCommunistDO communistDO = this.retireCommunistDAO.getRetireCommunistDOByUserid(userList.get(0) + "");
				excelVO.setUser_id(userList.get(0) + "");
				excelVO.setModified_user_id(loginUserid);
				if(null==communistDO) {
					excelVO.setCreate_user_id(loginUserid);
					this.retireCommunistDAO.insertRetireCommunist(excelVO);
					addNum++;
				}else {
					excelVO.setId(communistDO.getId());
					this.retireCommunistDAO.updateRetireCommunist(excelVO);
					updateNum++;
				}
			}
		}
		retMap.put("errorMessageHtml", "<b>成功新增" + addNum + "条数据，成功更新" + updateNum + "条数据，以下" + errorNum + 
				"条错误数据未入库：</b><br>" + errorMessageHtml);
		return retMap;
	}
	
	/**
	 * 判断Excel数据是否在数据字典中存在（存在有效，不存在无效）
	 * @param excelVal
	 * @param codeList
	 * @return
	 */
	private String isDictionaryByCodename(String excelVal,List<LinkedHashMap<String,String>> codeList) {
		String codeid = "";
		if(StringUtils.isNotEmpty(excelVal)) {
			for(int j=0;j<codeList.size();j++) {
				if(excelVal.equals(codeList.get(j).get("code_name"))) {
					codeid = codeList.get(j).get("id");
				}
			}
		}
		return codeid;
	}
}