package com.fjhcit.retire.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.fjhcit.common.kit.StringUtils;
import com.fjhcit.constant.ParentIdContant;
import com.fjhcit.model.ExcelVO;
import com.fjhcit.model.ResultVO;
import com.fjhcit.model.RetireContactExcelVO;
import com.fjhcit.entity.RetireContactDO;
import com.fjhcit.retire.service.FeignZuulServer;
import com.fjhcit.retire.service.RetireContactService;
import com.fjhcit.utils.POIExcelUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description 离（退）休人员_在职人员通讯录库_控制器
 * @author 陈麟
 * @date 2019年06月14日 上午08:17:13
 */
@RestController
@RequestMapping("/retireContact")
public class RetireContactController{
	@Autowired
	private RetireContactService	retireContactService;  		//离（退）休人员_在职人员通讯录库_业务接口
	@Autowired
	private FeignZuulServer 		feignZuulServer;			//远程调用其他组件接口

	/**
	 * @description 查离（退）休人员_在职人员通讯录库列表数据
	 */
    @RequestMapping(value = "/listRetireContact", method = RequestMethod.POST)
	public ResultVO listRetireContact(@RequestParam Map<String, String> param){
    	return new ResultVO(this.retireContactService.listRetireContact(param),true,"查询成功");
	}

	/**
	 * @description 查离（退）休人员_在职人员通讯录库分页列表数据
	 */
    @RequestMapping(value = "/listRetireContactByPaging", method = RequestMethod.POST)
	public ResultVO listRetireContactByPaging(@RequestParam Map<String, String> param){
    	if(StringUtils.isEmpty(param.get("pageNum"))){
    		param.put("pageNum", "1");
    	}
    	if(StringUtils.isEmpty(param.get("pageSize"))){
    		param.put("pageSize", "10");
    	}
    	int pageNum = Integer.parseInt(param.get("pageNum"));		//当前页
    	int pageSize = Integer.parseInt(param.get("pageSize"));//每页条数
    	Page<Object> p = PageHelper.startPage(pageNum, pageSize);
		List<RetireContactDO> personList = this.retireContactService.listRetireContact(param);
    	return new ResultVO(personList, true, "查询成功", p.getTotal(), pageNum, pageSize);
	}

	/**
	 * @description 保存离（退）休人员_在职人员通讯录库数据（新增、修改）
	 */
    @RequestMapping(value = "/saveRetireContact", method = RequestMethod.POST)
	public ResultVO saveRetireContact(@RequestBody RetireContactDO retireContactDO) {
    	ResultVO result;
    	try {
    		String idcard = retireContactDO.getIdcard();
    		if(StringUtils.isNotEmpty(idcard)) {
    			//1、查身份证是否重复
    			Map<String,String> cparam = new HashMap<String, String>();
    			cparam.put("idcard", idcard);
    			cparam.put("id", retireContactDO.getId());
    			List<Integer> idList = this.retireContactService.listRetireContactIdByIdcard(cparam);
    			if(StringUtils.isNotEmpty(idList)) {
    				return new ResultVO(-5,false,"身份证不能重复");
    			}
    		}
    		if(StringUtils.isEmpty(retireContactDO.getId())) {
    			this.retireContactService.insertRetireContact(retireContactDO);
    		}else {
    			this.retireContactService.updateRetireContact(retireContactDO);
    		}
    		result = new ResultVO(retireContactDO.getId(),true,"保存成功");
		} catch (Exception e) {
    		result = new ResultVO(-1,false,"保存失败");
        	e.printStackTrace();
		}
        return result;
	}

	/**
	 * @description 删除离（退）休人员_在职人员通讯录库数据
	 */
    @RequestMapping(value = "/removeRetireContact", method = RequestMethod.POST)
	public ResultVO removeRetireContact(@RequestParam Map<String, String> param){
    	ResultVO result;
    	try {
    		String ids = (String)param.get("ids");
    		String[] idsArr = ids.split(",");
    		//删除
    		Map<String,Object> delParam = new HashMap<String, Object>();
    		delParam.put("idsArr",idsArr);
			this.retireContactService.removeRetireContactByIdsArr(delParam);
    		result = new ResultVO(true,true,"删除成功");
    	} catch (Exception e) {
    		result = new ResultVO(false,false,"删除失败");
        	e.printStackTrace();
        }
        return result;
	}

    /**
	 * @description 查找离（退）休人员_在职人员通讯录库_最大排序号
	 */
    @RequestMapping(value = "/getRetireContactNextSortnum", method = RequestMethod.POST)
	public ResultVO getRetireContactNextSortnum(@RequestParam Map<String, String> param){
    	String unit_id = param.get("unit_id");
    	String sortnum = this.retireContactService.getRetireContactNextSortnum(unit_id);
    	return new ResultVO(sortnum,true,"查询成功");
    }

    /**
	 * @description 查询国网统计报表_离退休工作人员统计
	 */
    @RequestMapping(value = "/listRetireContactStaff", method = RequestMethod.POST)
	public ResultVO listRetireContactStaff(@RequestParam Map<String, Object> param){
		//1、查学历分类
    	Map<String,String> codeParam = new HashMap<String, String>();
    	codeParam.put("code", ParentIdContant.BASE_CODE_EDUCATION);
    	ResultVO educationVO = this.feignZuulServer.getCode(codeParam);
    	@SuppressWarnings("unchecked")
		Map<String,List<LinkedHashMap<String,String>>> educations = (Map<String, List<LinkedHashMap<String, String>>>) educationVO.getData();
		List<LinkedHashMap<String, String>> educationList = educations.get(ParentIdContant.BASE_CODE_EDUCATION);
		//2、查统计列表
    	param.put("educationList", educationList);
    	param.put("parent_id", ParentIdContant.BASE_CODE_USER_NATURE);
    	return new ResultVO(this.retireContactService.listRetireContactStaff(param),true,"查询成功");
    }

    /**
	 * @description 导出国网统计报表_离退休工作人员统计_Excel文件
	 */
    @RequestMapping(value = "/excelRetireContactStaff", method = RequestMethod.GET)
	public void excelRetireContactStaff(HttpServletResponse response, @RequestParam Map<String, Object> param){
		//1、查学历分类
    	Map<String,String> codeParam = new HashMap<String, String>();
    	codeParam.put("parent_id", ParentIdContant.BASE_CODE_EDUCATION);
    	codeParam.put("is_available", "1");
    	ResultVO  educationVO = this.feignZuulServer.getCode(codeParam);
    	@SuppressWarnings("unchecked")
		List<LinkedHashMap<String,String>> educationList = (List<LinkedHashMap<String,String>>)educationVO.getData();
    	//2、导出Excel
    	ExcelVO excelVO = new ExcelVO();
    	excelVO.setSheetName("离退休工作人员统计");
    	int[] columnWidthArr = new int[educationList.size() + 9];
    	columnWidthArr[0] = 20;
    	columnWidthArr[1] = 8;
    	columnWidthArr[2] = 8;
    	String[] tableHeadArr1 = new String[educationList.size() + 9];
    	tableHeadArr1[0] = "项目";
    	tableHeadArr1[1] = "编号";
    	tableHeadArr1[2] = "总数";
    	tableHeadArr1[3] = "学历";
    	String[] tableHeadArr2 = new String[educationList.size() + 9];
    	tableHeadArr2[0] = "";
    	tableHeadArr2[1] = "";
    	tableHeadArr2[2] = "";
    	String[] dataFieldArr = new String[educationList.size() + 9];
    	dataFieldArr[0] = "CODE_NAME";
    	dataFieldArr[1] = "SEQUENCE";
    	dataFieldArr[2] = "TOTAL";
    	for(int i=0;i<educationList.size();i++) {
    		LinkedHashMap<String,String> code = educationList.get(i);
    		columnWidthArr[i+3] = 12;
    		if(i!=0) {
    			tableHeadArr1[i+3] = "";
    		}
    		tableHeadArr2[i+3] = code.get("code_name");
    		dataFieldArr[i+3] = "EDUCATION_" + code.get("id");
    	}
    	columnWidthArr[educationList.size()+3] = 11;
    	columnWidthArr[educationList.size()+4] = 11;
    	columnWidthArr[educationList.size()+5] = 11;
    	columnWidthArr[educationList.size()+6] = 11;
    	columnWidthArr[educationList.size()+7] = 11;
    	columnWidthArr[educationList.size()+8] = 11;
    	tableHeadArr1[educationList.size()+3] = "年龄";
    	tableHeadArr1[educationList.size()+4] = "";
    	tableHeadArr1[educationList.size()+5] = "";
    	tableHeadArr1[educationList.size()+6] = "从事离退休工作年限";
    	tableHeadArr1[educationList.size()+7] = "";
    	tableHeadArr1[educationList.size()+8] = "";
    	tableHeadArr2[educationList.size()+3] = "40岁及以下";
    	tableHeadArr2[educationList.size()+4] = "41岁至50岁";
    	tableHeadArr2[educationList.size()+5] = "51岁至60岁";
    	tableHeadArr2[educationList.size()+6] = "5年\012及以下";
    	tableHeadArr2[educationList.size()+7] = "6年至\012不满10年";
    	tableHeadArr2[educationList.size()+8] = "10年及以上";
    	dataFieldArr[educationList.size()+3] = "AGE_40";
    	dataFieldArr[educationList.size()+4] = "AGE_41_50";
    	dataFieldArr[educationList.size()+5] = "AGE_51_60";
    	dataFieldArr[educationList.size()+6] = "WORK_SENIORITY_5";
    	dataFieldArr[educationList.size()+7] = "WORK_SENIORITY_6_10";
    	dataFieldArr[educationList.size()+8] = "WORK_SENIORITY_10";
    	String[] mergedRegionArr = {"0,0,0," + (columnWidthArr.length-1),
    			"1,2,0,0","1,2,1,1","1,2,2,2","1,1,3,"+(educationList.size()+2),"1,1,"+(educationList.size()+3)+"," + (educationList.size()+5),
    			"1,1," + (educationList.size()+6) + "," + (columnWidthArr.length-1)};
    	excelVO.setMergedRegionArr(mergedRegionArr);
    	excelVO.setTitleName("离退休工作人员情况统计表");
    	float[] rowHeightArr = {40f,26f,15f};
    	excelVO.setRowHeight(rowHeightArr);
    	List<String[]> tableHeadList = new ArrayList<String[]>();
    	tableHeadList.add(tableHeadArr1);
    	tableHeadList.add(tableHeadArr2);
    	excelVO.setTableHeadList(tableHeadList);
    	excelVO.setColumnWidthArr(columnWidthArr);
    	excelVO.setDataFieldArr(dataFieldArr);
    	excelVO.setFileName("离退休工作人员情况统计表");
    	//3、查列表数据
    	param.put("educationList", educationList);
    	param.put("parent_id", ParentIdContant.BASE_CODE_USER_NATURE);
    	List<Map<String,String>> staffList = this.retireContactService.listRetireContactStaff(param);
    	POIExcelUtils.exportExcel(response, excelVO, staffList);
	}

	@RequestMapping(value = "/saveRetireContactByExcel", method = {RequestMethod.POST})
	@ResponseBody
	public Map<String, String> saveRetireContactByExcel(@RequestParam("file") MultipartFile file,@RequestParam Map<String, Object> param) {
		Map<String,String> retMap = new HashMap<>();
		try {
			//上传附件到Minio服务器
//			String fileName = file.getOriginalFilename();
//			String contentType = file.getContentType();
//			InputStream inputStram = file.getInputStream();
//			return MinioUtil.uploadfile(inputStram, fileName, contentType);
			POIExcelUtils poi = new POIExcelUtils();
			List<Object> dataList = poi.importExcel(file,new RetireContactExcelVO(),2,1);
			param.put("dataList", dataList);
			retMap = this.retireContactService.saveRetireContactByExcel(param);
		} catch (Exception e) {
			if("当前Excel表和模板不匹配！".equals(e.getMessage())) {
				retMap.put("errorMessageHtml", e.getMessage());
			}else {
				retMap.put("errorMessageHtml", "Excel解析错误！");
			}
			e.printStackTrace();
		}
		return retMap;
	}
}