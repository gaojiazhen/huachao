package com.fjhcit.retire.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.fjhcit.common.kit.StringUtils;
import com.fjhcit.constant.ParentIdContant;
import com.fjhcit.model.ExcelVO;
import com.fjhcit.model.ResultVO;
import com.fjhcit.entity.BaseDepartmentDO;
import com.fjhcit.entity.RetirePartyDO;
import com.fjhcit.retire.service.RetirePartyService;
import com.fjhcit.utils.POIExcelUtils;

/**
 * @description 离（退）休人员_党组织设置情况库_控制器
 * @author 陈麟
 * @date 2019年06月17日 上午08:48:46
 */
@RestController
@RequestMapping("/retireParty")
public class RetirePartyController{
	@Autowired
	private RetirePartyService		retirePartyService;  		//离（退）休人员_党组织设置情况库_业务接口

	/**
	 * @description 查离（退）休人员_党组织设置情况库列表数据
	 */
    @RequestMapping(value = "/listRetireParty", method = RequestMethod.POST)
	public ResultVO listRetireParty(@RequestParam Map<String, String> param){
    	String gmt_statistics = param.get("gmt_statistics");
    	if(StringUtils.isNotEmpty(gmt_statistics)) {
    		gmt_statistics = gmt_statistics.replaceAll("年", "-").replaceAll("月", "");
    	}
    	param.put("gmt_statistics","'"+gmt_statistics+"'");
    	String modified_user_id = param.get("modified_user_id");
    	//1、查党组织设置情况库列表
    	param.put("BASE_CODE_POLITICS_STATUS_COMMUNIST", ParentIdContant.BASE_CODE_POLITICS_STATUS_COMMUNIST);
    	param.put("BASE_CODE_PARTY_LOCATED_COMPANY", ParentIdContant.BASE_CODE_PARTY_LOCATED_COMPANY);
    	param.put("BASE_CODE_PARTY_LOCATED_PLACE", ParentIdContant.BASE_CODE_PARTY_LOCATED_PLACE);
    	param.put("modified_user_id", "");

    	List<RetirePartyDO> partyList = this.retirePartyService.listRetireParty(param);
    	//2、如果没有数据直接新增
    	String unit_id = param.get("unit_id");
    	if(StringUtils.isEmpty(unit_id)) {
    		Map<String,String> unitParam =  new HashMap<String, String>();
    		unitParam.put("parent_id", ParentIdContant.BASE_DEPARTMENT);
    		unitParam.put("gmt_statistics", gmt_statistics);
    		List<BaseDepartmentDO> unitList = this.retirePartyService.listBaseDepartment(unitParam);
			for(int i=0;i<unitList.size();i++) {
    			BaseDepartmentDO d = unitList.get(i);
    			if(StringUtils.isEmpty(d.getParty_id())) {
    				RetirePartyDO retirePartyDO = new RetirePartyDO();
    	    		retirePartyDO.setUnit_id(d.getId());
    	    		retirePartyDO.setGmt_statistics(gmt_statistics);
    	    		retirePartyDO.setCreate_user_id(modified_user_id);
    	    		retirePartyDO.setModified_user_id(modified_user_id);
    	    		this.retirePartyService.insertRetireParty(retirePartyDO);
    			}
    		}
    	} else if (StringUtils.isEmpty(partyList)){
    		RetirePartyDO retirePartyDO = new RetirePartyDO();
    		retirePartyDO.setUnit_id(unit_id);
    		retirePartyDO.setGmt_statistics(gmt_statistics);
    		retirePartyDO.setCreate_user_id(modified_user_id);
    		retirePartyDO.setModified_user_id(modified_user_id);
    		this.retirePartyService.insertRetireParty(retirePartyDO);
    	}
    	partyList = this.retirePartyService.listRetireParty(param);
    	return new ResultVO(partyList,true,"查询成功");
	}

	/**
	 * @description 查离（退）休人员_党组织设置情况库分页列表数据
	 */
    @RequestMapping(value = "/listRetirePartyByPaging", method = RequestMethod.POST)
	public ResultVO listRetirePartyByPaging(@RequestParam Map<String, String> param){
    	if(StringUtils.isEmpty(param.get("pageNum"))){
    		param.put("pageNum", "1");
    	}
    	if(StringUtils.isEmpty(param.get("pageSize"))){
    		param.put("pageSize", "10");
    	}
    	int pageNum = Integer.parseInt(param.get("pageNum"));		//当前页
    	int pageSize = Integer.parseInt(param.get("pageSize"));//每页条数
    	Page<Object> p = PageHelper.startPage(pageNum, pageSize);
		List<RetirePartyDO> personList = this.retirePartyService.listRetireParty(param);
    	return new ResultVO(personList, true, "查询成功", p.getTotal(), pageNum, pageSize);
	}

	/**
	 * @description 保存离（退）休人员_党组织设置情况库数据（新增、修改）
	 */
    @RequestMapping(value = "/saveRetireParty", method = RequestMethod.POST)
	public ResultVO saveRetireParty(@RequestBody RetirePartyDO retirePartyDO) {
    	ResultVO result;
    	try {
    		if(StringUtils.isEmpty(retirePartyDO.getId())) {
    			this.retirePartyService.insertRetireParty(retirePartyDO);
    		}else {
    			this.retirePartyService.updateRetireParty(retirePartyDO);
    		}
    		result = new ResultVO(true,true,"保存成功");
		} catch (Exception e) {
    		result = new ResultVO(false,false,"保存失败");
        	e.printStackTrace();
		}
        return result;
	}

	/**
	 * @description 删除离（退）休人员_党组织设置情况库数据
	 */
    @RequestMapping(value = "/removeRetireParty", method = RequestMethod.POST)
	public ResultVO removeRetireParty(@RequestParam Map<String, String> param){
    	ResultVO result;
    	try {
    		String ids = (String)param.get("ids");
    		String[] idsArr = ids.split(",");
    		//删除
    		Map<String,Object> delParam = new HashMap<String, Object>();
    		delParam.put("idsArr",idsArr);
			this.retirePartyService.removeRetirePartyByIdsArr(delParam);
    		result = new ResultVO(true,true,"删除成功");
    	} catch (Exception e) {
    		result = new ResultVO(false,false,"删除失败");
        	e.printStackTrace();
        }
        return result;
	}
    
    /**
	 * @description 查询国网统计报表_离退休党组织设置统计
	 */
    @RequestMapping(value = "/listRetirePartyOrganization", method = RequestMethod.POST)
	public ResultVO listRetirePartyOrganization(@RequestParam Map<String, String> param){
    	List<Map<String,String>> partyList = this.retirePartyService.listRetirePartyOrganization(param);
    	return new ResultVO(partyList,true,"查询成功");
    }
    
    /**
	 * @description 导出国网统计报表_离退休党组织设置统计_Excel
	 */
    @RequestMapping(value = "/excelRetirePartyOrganization", method = RequestMethod.GET)
	public void excelRetirePartyOrganization(HttpServletResponse response, @RequestParam Map<String, String> param){
    	ExcelVO excelVO = new ExcelVO();
    	excelVO.setSheetName("离退休党组织设置统计");
    	int[] columnWidthArr = {30, 8, 10, 12, 16, 16, 18, 18, 18, 10};
    	String[] mergedRegionArr = {"0,0,0," + (columnWidthArr.length-1),
    			"1,2,0,0","1,2,1,1","1,2,2,2","1,2,3,3","1,1,4,8","1,2,9,9"};
    	excelVO.setMergedRegionArr(mergedRegionArr);
    	excelVO.setTitleName("离退休党组织设置情况统计表");
    	float[] rowHeightArr = {40f,15f,15f};
    	excelVO.setRowHeight(rowHeightArr);
    	String[] tableHeadArr1 = {"项目","编号","党总支数","党支部总数","其中","","","","","党小组数"};   
    	String[] tableHeadArr2 = {"","","","","离休干部党支部","离/退休合编数","退休人员党支部数","离休与在职合编数","退休与在职合编数",""};
    	List<String[]> tableHeadList = new ArrayList<String[]>();
    	tableHeadList.add(tableHeadArr1);
    	tableHeadList.add(tableHeadArr2);
    	excelVO.setTableHeadList(tableHeadList);
    	excelVO.setColumnWidthArr(columnWidthArr);
    	String[] dataFieldArr = {"DEPARTMENT_NAME","SEQUENCE","PARTY_GENERAL_BRANCH","PARTY_BRANCH_NUMBER","PARTY_GROUP_NUMBER",
    			"QUIT_CADRE_PARTY_BRANCH","RETIRE_COMBINE_NUMBER","RETIRE_PARTY_BRANCH","QUIT_INSERVICE_COMBINE","RETIRE_INSERVICE_COMBINE"};
    	excelVO.setDataFieldArr(dataFieldArr);
    	excelVO.setFileName("离退休党组织设置情况统计表");
    	String gmt_statistics = param.get("gmt_statistics");
    	if(StringUtils.isNotEmpty(gmt_statistics)) {
    		gmt_statistics = gmt_statistics.replace("年", "-").replace("月", "");
    		param.put("gmt_statistics", gmt_statistics);
    	}
    	List<Map<String,String>> partyList = this.retirePartyService.listRetirePartyOrganization(param);
    	POIExcelUtils.exportExcel(response, excelVO, partyList);
    }
}