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
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.fjhcit.common.kit.StringUtils;
import com.fjhcit.constant.ParentIdContant;
import com.fjhcit.model.ExcelVO;
import com.fjhcit.model.ResultVO;
import com.fjhcit.model.RetireArenaExcelVO;
import com.fjhcit.entity.RetireArenaDO;
import com.fjhcit.retire.service.FeignZuulServer;
import com.fjhcit.retire.service.RetireArenaService;
import com.fjhcit.utils.POIExcelUtils;

/**
 * @description 离（退）休人员_活动场所及老年大学建设情况_控制器
 * @author 陈麟
 * @date 2019年06月25日 下午21:46:27
 */
@RestController
@RequestMapping("/retireArena")
public class RetireArenaController{
	@Autowired
	private RetireArenaService		retireArenaService;  		//离（退）休人员_活动场所及老年大学建设情况统计表_业务接口
	@Autowired
	private FeignZuulServer  		feignZuulServer;			//远程调用其他组件接口

	/**
	 * @description 查离（退）休人员_活动场所及老年大学建设情况统计表列表数据
	 */
    @RequestMapping(value = "/listRetireArena", method = RequestMethod.POST)
	public ResultVO listRetireArena(@RequestParam Map<String, String> param){
    	return new ResultVO(this.retireArenaService.listRetireArena(param),true,"查询成功");
	}

	/**
	 * @description 查离（退）休人员_活动场所及老年大学建设情况统计表分页列表数据
	 */
    @RequestMapping(value = "/listRetireArenaByPaging", method = RequestMethod.POST)
	public ResultVO listRetireArenaByPaging(@RequestParam Map<String, String> param){
    	if(StringUtils.isEmpty(param.get("pageNum"))){
    		param.put("pageNum", "1");
    	}
    	if(StringUtils.isEmpty(param.get("pageSize"))){
    		param.put("pageSize", "10");
    	}
    	int pageNum = Integer.parseInt(param.get("pageNum"));		//当前页
    	int pageSize = Integer.parseInt(param.get("pageSize"));//每页条数
    	Page<Object> p = PageHelper.startPage(pageNum, pageSize);
		List<RetireArenaDO> personList = this.retireArenaService.listRetireArena(param);
    	return new ResultVO(personList, true, "查询成功", p.getTotal(), pageNum, pageSize);
	}

	/**
	 * @description 保存国网统计报表_活动场所及老年大学录入（新增、修改）
	 */
    @RequestMapping(value = "/saveRetireArena", method = RequestMethod.POST)
	public ResultVO saveRetireArena(@RequestBody RetireArenaDO retireArenaDO) {
    	ResultVO result;
    	try {
    		Map<String,String> arenaParam = new HashMap<String,String>();
    		arenaParam.put("unit_id", retireArenaDO.getUnit_id());
    		arenaParam.put("year", retireArenaDO.getYear());
    		List<RetireArenaDO> arenaList = this.retireArenaService.listRetireArena(arenaParam);
    		if(StringUtils.isEmpty(arenaList)) {
    			retireArenaDO.setCreate_user_id(retireArenaDO.getModified_user_id());
    			this.retireArenaService.insertRetireArena(retireArenaDO);
    		}else {
    			retireArenaDO.setId(arenaList.get(0).getId());
    			this.retireArenaService.updateRetireArena(retireArenaDO);
    		}
    		result = new ResultVO(true,true,"保存成功");
		} catch (Exception e) {
    		result = new ResultVO(false,false,"保存失败");
        	e.printStackTrace();
		}
        return result;
	}

	/**
	 * @description 删除离（退）休人员_活动场所及老年大学建设情况统计表数据
	 */
    @RequestMapping(value = "/removeRetireArena", method = RequestMethod.POST)
	public ResultVO removeRetireArena(@RequestParam Map<String, String> param){
    	ResultVO result;
    	try {
    		String ids = (String)param.get("ids");
    		String[] idsArr = ids.split(",");
    		//删除
    		Map<String,Object> delParam = new HashMap<String, Object>();
    		delParam.put("idsArr",idsArr);
			this.retireArenaService.removeRetireArenaByIdsArr(delParam);
    		result = new ResultVO(true,true,"删除成功");
    	} catch (Exception e) {
    		result = new ResultVO(false,false,"删除失败");
        	e.printStackTrace();
        }
        return result;
	}
    
    /**
	 * @description 查询国网统计报表_活动场所及老年大学录入
	 */
    @RequestMapping(value = "/listRetireArenaAndUniversity", method = RequestMethod.POST)
	public ResultVO listRetireArenaAndUniversity(@RequestParam Map<String, String> param){
        return new ResultVO(this.retireArenaService.listRetireArenaAndUniversity(param), true, "查询成功");
	}
    
    /**
     * @description 导出国网统计报表_活动场所及老年大学录入_Excel文件
     * @param response response对象
     */
    @RequestMapping(value = "/excelRetireArenaAndUniversity", method = RequestMethod.GET)
    public void excelRetireArenaAndUniversity(HttpServletResponse response, @RequestParam Map<String, String> param){
    	ExcelVO excelVO = new ExcelVO();
    	excelVO.setSheetName("活动场所及老年大学录入");
    	int[] columnWidthArr = new int[30];
    	columnWidthArr[0] = 30;
    	columnWidthArr[1] = 10;
    	for(int i=0;i<28;i++) {
    		columnWidthArr[i+2] = 15;
    	}
    	excelVO.setColumnWidthArr(columnWidthArr);
    	String[] mergedRegionArr = {"0,0,0," + (columnWidthArr.length-1),"1,2,0,0","1,2,1,1",
    			"1,1,2,4","1,1,5,6","1,1,7,10","1,1,11,14","1,1,15,18","1,1,19,20","1,1,21,25","1,1,26,27","1,1,28,29"};
    	excelVO.setMergedRegionArr(mergedRegionArr);
    	String[] tableHeadArr1 = {"项目","编号","现有活动场所","","","在建活动场所","","退休人员独立使用","","","","与在职人员共同使用","","","",
    			"具备移交条件的活动场所","","","","年末固定资产情况（万元）","","现有老年大学","","","","","在建老年大学","","就读社会老年大学",""};
    	String[] tableHeadArr2 = {"","","总数\012（个）","总建筑面积\012（平方米）","日均活动\012人数","总数\012（个）","总建筑面积\012（平方米）",
    			"总数\012（个）","总建筑面积\012（平方米）","年末固定资产\012原值（万元）","年末固定资产\012净值（万元）",
    			"总数\012（个）","总建筑面积\012（平方米）","年末固定资产\012原值（万元）","年末固定资产\012净值（万元）",
    			"总数\012（个）","总建筑面积\012（平方米）","年末固定资产\012原值（万元）","年末固定资产\012净值（万元）",
    			"原值","净值","总数\012（个）","总建筑面积\012（平方米）","正式\012工作人员","在读学员\012人数","累计结业\012人数",
    			"总数\012（个）","总建筑面积\012（平方米）","在读学员\012人数","累计结业\012人数"};
    	List<String[]> tableHeadList = new ArrayList<String[]>();
    	tableHeadList.add(tableHeadArr1);
    	tableHeadList.add(tableHeadArr2);
    	excelVO.setTableHeadList(tableHeadList);
    	String[] dataFieldArr = {"DEPARTMENT_NAME","SEQUENCE","EXISTING_SUM","EXISTING_ACREAGE","EXISTING_EVERYDAY","ABUILDING_SUM","ABUILDING_ACREAGE",
    			"INDEPENDENT_SUM","INDEPENDENT_ACREAGE","INDEPENDENT_ORIGINAL_VALUE","INDEPENDENT_NET_VALUE",
    			"COMMON_SUM","COMMON_ACREAGE","COMMON_ORIGINAL_VALUE","COMMON_NET_VALUE",
    			"ELIGIBLE_SUM","ELIGIBLE_ACREAGE","ELIGIBLE_ORIGINAL_VALUE","ELIGIBLE_NET_VALUE",
    			"FIXED_ASSETS_ORIGINAL_VALUE","FIXED_ASSETS_NET_VALUE","UNIVERSITY_SUM","UNIVERSITY_ACREAGE","UNIVERSITY_REGULAR_STAFF",
    			"UNIVERSITY_STUDENTS","UNIVERSITY_GRADUATE","ABUILDING_UNIVERSITY_SUM","ABUILDING_UNIVERSITY_ACREAGE","ATTEND_STUDENTS",
    			"ATTEND_GRADUATE"};
    	excelVO.setDataFieldArr(dataFieldArr);
    	excelVO.setTitleName("离退休人员活动场所及老年大学建设情况统计表");
    	float[] rowHeightArr = {40f,25f,15f};
    	excelVO.setRowHeight(rowHeightArr);
    	excelVO.setFileName("离退休人员活动场所及老年大学建设情况统计表");
		//列表数据
    	List<Map<String,String>> dataList = this.retireArenaService.listRetireArenaAndUniversity(param);
    	POIExcelUtils.exportExcel(response, excelVO, dataList);
    }
    
    /**
	 * @description 查询国网统计报表_退休人员活动场所统计
	 */
    @RequestMapping(value = "/listRetireArenaServiceCondition", method = RequestMethod.POST)
	public ResultVO listRetireArenaServiceCondition(@RequestParam Map<String, String> param){
        return new ResultVO(this.retireArenaService.listRetireArenaServiceCondition(param), true, "查询成功");
	}
    
    /**
     * @description 导出国网统计报表_退休人员活动场所统计_Excel文件
     * @param response response对象
     */
    @RequestMapping(value = "/excelRetireArenaServiceCondition", method = RequestMethod.GET)
    public void excelRetireArenaServiceCondition(HttpServletResponse response, @RequestParam Map<String, String> param){
    	ExcelVO excelVO = new ExcelVO();
    	excelVO.setSheetName("退休人员活动场所统计");
    	int[] columnWidthArr = {30,30,20,20,20,20};
    	//查单位条数，计算表格内容跨行
    	Map<String,String> departmentParam = new HashMap<String,String>();
    	departmentParam.put("department_id", ParentIdContant.BASE_DEPARTMENT_PROVINCIAL);
    	departmentParam.put("parent_id", ParentIdContant.BASE_DEPARTMENT);
    	departmentParam.put("is_available", "1");
    	ResultVO departmentVO = this.feignZuulServer.listBaseDepartment(departmentParam);
    	@SuppressWarnings("unchecked")
    	List<LinkedHashMap<String,String>> departmentList = (List<LinkedHashMap<String,String>>)departmentVO.getData();
    	String[] mergedRegionArr = new String[(departmentList.size()*3)+6];
    	mergedRegionArr[0] = "0,0,0," + (columnWidthArr.length-1);
    	mergedRegionArr[1] = "1,2,0,0";
    	mergedRegionArr[2] = "1,2,1,1";
    	mergedRegionArr[3] = "1,2,2,2";
    	mergedRegionArr[4] = "1,2,3,3";
    	mergedRegionArr[5] = "1,1,4,5";
    	for(int i=0;i<departmentList.size();i++) {
    		mergedRegionArr[6+i] = (3+(i*3))+","+(5+(i*3))+",0,0";
    		//mergedRegionArr[6+departmentList.size()+i] = (3+(i*3))+","+(5+(i*3))+",4,4";
    		//mergedRegionArr[6+(departmentList.size()*2)+i] = (3+(i*3))+","+(5+(i*3))+",5,5";
    	}
    	String year = param.get("year");
    	int last_year = StringUtils.getYear() - 1;
    	if(StringUtils.isNotEmpty(year)) {
    		last_year = Integer.valueOf(year.replace("年", "")) - 1;
    	}
    	String[] tableHeadArr1 = {"企业名称","活动场所使用情况","个数（个）","面积（m²）", last_year + "年末固定资产情况（万元）",""};
    	String[] tableHeadArr2 = {"","","","","原值","净值"};
    	String[] dataFieldArr = {"DEPARTMENT_NAME","SERVICE_CONDITION","INDEPENDENT_SUM","INDEPENDENT_ACREAGE",
    			"INDEPENDENT_ORIGINAL_VALUE","INDEPENDENT_NET_VALUE"};
    	excelVO.setMergedRegionArr(mergedRegionArr);
    	excelVO.setTitleName("退休人员活动场所使用情况表");
    	float[] rowHeightArr = {40f,20f,15f};
    	excelVO.setRowHeight(rowHeightArr);
    	List<String[]> tableHeadList = new ArrayList<String[]>();
    	tableHeadList.add(tableHeadArr1);
    	tableHeadList.add(tableHeadArr2);
    	excelVO.setTableHeadList(tableHeadList);
    	excelVO.setColumnWidthArr(columnWidthArr);
    	excelVO.setDataFieldArr(dataFieldArr);
    	excelVO.setFileName("退休人员活动场所使用情况表");
		//列表数据
    	List<Map<String,String>> dataList = this.retireArenaService.listRetireArenaServiceCondition(param);
    	POIExcelUtils.exportExcel(response, excelVO, dataList);
    }
    
    /**
	 * @description 活动场所及老年大学录入_导入Excel
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/saveRetireArenaByExcel", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> saveRetireArenaByExcel(@RequestParam("file") MultipartFile file,@RequestParam Map<String, Object> param) {
		Map<String,String> retMap = new HashMap<>();
		try {
			//上传附件到Minio服务器
//			String fileName = file.getOriginalFilename();
//			String contentType = file.getContentType();
//			InputStream inputStram = file.getInputStream();
//			return MinioUtil.uploadfile(inputStram, fileName, contentType);
			POIExcelUtils poi = new POIExcelUtils();
			List<Object> dataList = poi.importExcel(file,new RetireArenaExcelVO(),3,1);
			param.put("dataList", dataList);
			retMap = this.retireArenaService.saveRetireArenaByExcel(param);
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