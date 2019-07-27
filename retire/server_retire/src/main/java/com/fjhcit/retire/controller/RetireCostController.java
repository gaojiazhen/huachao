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
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.fjhcit.common.kit.StringUtils;
import com.fjhcit.constant.ParentIdContant;
import com.fjhcit.model.ExcelVO;
import com.fjhcit.model.ResultVO;
import com.fjhcit.entity.RetireCostDO;
import com.fjhcit.retire.service.FeignZuulServer;
import com.fjhcit.retire.service.RetireCostService;
import com.fjhcit.utils.POIExcelUtils;

/**
 * @author 陈麟
 * @description 离（退）休人员_单位运行成本费用_控制器
 * @date 2019年06月24日 上午11:57:51
 */
@RestController
@RequestMapping("/retireCost")
public class RetireCostController {
    @Autowired
    private RetireCostService retireCostService;        //离（退）休人员_单位运行成本费用_业务接口
    //	@Autowired
//	private BaseCodeService			baseCodeService;			//基础管理_代码设置_业务接口
    @Autowired
    private FeignZuulServer feignZuulServer;            //远程调用其他组件接口

    /**
     * @description 查离（退）休人员_单位运行成本费用列表数据
     */
    @RequestMapping(value = "/listRetireCost", method = RequestMethod.POST)
    public ResultVO listRetireCost(@RequestParam Map<String, String> param) {
        return new ResultVO(this.retireCostService.listRetireCost(param), true, "查询成功");
    }

    /**
     * @description 查离（退）休人员_单位运行成本费用分页列表数据
     */
    @RequestMapping(value = "/listRetireCostByPaging", method = RequestMethod.POST)
    public ResultVO listRetireCostByPaging(@RequestParam Map<String, String> param) {
        if (StringUtils.isEmpty(param.get("pageNum"))) {
            param.put("pageNum", "1");
        }
        if (StringUtils.isEmpty(param.get("pageSize"))) {
            param.put("pageSize", "10");
        }
        int pageNum = Integer.parseInt(param.get("pageNum"));        //当前页
        int pageSize = Integer.parseInt(param.get("pageSize"));//每页条数
        Page<Object> p = PageHelper.startPage(pageNum, pageSize);
        List<RetireCostDO> personList = this.retireCostService.listRetireCost(param);
        return new ResultVO(personList, true, "查询成功", p.getTotal(), pageNum, pageSize);
    }

    /**
     * @description 保存离（退）休人员_单位运行成本费用数据（新增、修改）
     */
    @RequestMapping(value = "/saveRetireCost", method = RequestMethod.POST)
    public ResultVO saveRetireCost(@RequestBody RetireCostDO retireCostDO) {
        ResultVO result;
        try {
            if (StringUtils.isEmpty(retireCostDO.getId())) {
                this.retireCostService.insertRetireCost(retireCostDO);
            } else {
                this.retireCostService.updateRetireCost(retireCostDO);
            }
            result = new ResultVO(true, true, "保存成功");
        } catch (Exception e) {
            result = new ResultVO(false, false, "保存失败");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @description 删除离（退）休人员_单位运行成本费用数据
     */
    @RequestMapping(value = "/removeRetireCost", method = RequestMethod.POST)
    public ResultVO removeRetireCost(@RequestParam Map<String, String> param) {
        ResultVO result;
        try {
            String ids = (String) param.get("ids");
            String[] idsArr = ids.split(",");
            //删除
            Map<String, Object> delParam = new HashMap<String, Object>();
            delParam.put("idsArr", idsArr);
            this.retireCostService.removeRetireCostByIdsArr(delParam);
            result = new ResultVO(true, true, "删除成功");
        } catch (Exception e) {
            result = new ResultVO(false, false, "删除失败");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @description 查找国网统计报表_退休人员管理费用统计
     */
    @RequestMapping(value = "/listRetireEmployeeCost", method = RequestMethod.POST)
    public ResultVO listRetireEmployeeCost(@RequestParam Map<String, Object> param) {
        Map codeParam = new HashMap();
        codeParam.put("code", ParentIdContant.BASE_CODE_USER_NATURE + "," + ParentIdContant.BASE_CODE_USER_RANK);
        ResultVO resultVO = this.feignZuulServer.getCode(codeParam);
        Map<String, List<LinkedHashMap<String, String>>> mapData = (Map<String, List<LinkedHashMap<String, String>>>) resultVO.getData();
        //1、查人员性质
        List<LinkedHashMap<String, String>> natureList = mapData.get(ParentIdContant.BASE_CODE_USER_NATURE);
        //2、查人员职级
        List<LinkedHashMap<String, String>> rankList = mapData.get(ParentIdContant.BASE_CODE_USER_RANK);
        param.put("natureList", natureList);
        param.put("rankList", rankList);
        return new ResultVO(this.retireCostService.listRetireEmployeeCost(param), true, "查询成功");
    }

    /**
     * @description 保存国网统计报表_退休人员管理费用统计
     * 保存国网统计报表_退休人员相关费用统计
     */
    @RequestMapping(value = "/saveRetireEmployeeCost", method = RequestMethod.POST)
    public ResultVO saveRetireEmployeeCost(@RequestBody RetireCostDO retireCostDO) {
        ResultVO result;
        try {
            Map<String, String> costParam = new HashMap<String, String>();
            costParam.put("unit_id", retireCostDO.getUnit_id());
            costParam.put("year", retireCostDO.getYear());
            RetireCostDO costDO = this.retireCostService.getRetireCostDOByUnitidAndYear(costParam);
            if (costDO == null && StringUtils.isEmpty(retireCostDO.getId())) {
                retireCostDO.setCreate_user_id(retireCostDO.getModified_user_id());
                this.retireCostService.insertRetireCost(retireCostDO);
            } else {
                if (StringUtils.isEmpty(retireCostDO.getId())) {
                    retireCostDO.setId(costDO.getId());
                }
                this.retireCostService.updateRetireCost(retireCostDO);
            }
            result = new ResultVO(true, true, "保存成功");
        } catch (Exception e) {
            result = new ResultVO(false, false, "保存失败");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param response response对象
     * @description 导出国网统计报表_退休人员管理费用统计_Excel文件
     */
    @RequestMapping(value = "/excelRetireEmployeeCost", method = RequestMethod.GET)
    public void excelRetireEmployeeCost(HttpServletResponse response, @RequestParam Map<String, Object> param) {
        ExcelVO excelVO = new ExcelVO();
        excelVO.setSheetName("退休人员管理费用统计");
        //1、查人员性质
        Map<String, String> natureParam = new HashMap<String, String>();
        natureParam.put("parent_id", ParentIdContant.BASE_CODE_USER_NATURE);
        natureParam.put("is_available", "1");
        ResultVO natureVO = this.feignZuulServer.getCode(natureParam);
        @SuppressWarnings("unchecked")
        List<LinkedHashMap<String, String>> natureList = (List<LinkedHashMap<String, String>>) natureVO.getData();
        //2、查人员职级
        Map<String, String> rankParam = new HashMap<String, String>();
        rankParam.put("parent_id", ParentIdContant.BASE_CODE_USER_RANK);
        rankParam.put("is_available", "1");
        ResultVO rankVO = this.feignZuulServer.getCode(rankParam);
        @SuppressWarnings("unchecked")
        List<LinkedHashMap<String, String>> rankList = (List<LinkedHashMap<String, String>>) rankVO.getData();
        //3、POI导出Excel参数信息
        int[] columnWidthArr = new int[natureList.size() + rankList.size() + 16];
        columnWidthArr[0] = 30;
        String[] mergedRegionArr = new String[16 + natureList.size()];
        mergedRegionArr[0] = "0,0,0," + (columnWidthArr.length - 1);
        mergedRegionArr[1] = "1,3,0,0";
        mergedRegionArr[2] = "1,1,1," + (natureList.size() + rankList.size());
        mergedRegionArr[3] = "1,1," + (natureList.size() + rankList.size() + 1) + "," + (columnWidthArr.length - 1);
        String[] tableHeadArr1 = new String[16 + natureList.size() + rankList.size()];
        tableHeadArr1[0] = "企业名称";
        tableHeadArr1[1] = "企业退休人员管理服务从业人员";
        String[] tableHeadArr2 = new String[16 + natureList.size() + rankList.size()];
        tableHeadArr2[0] = "";
        String[] tableHeadArr3 = new String[16 + natureList.size() + rankList.size()];
        tableHeadArr3[0] = "";
        String[] dataFieldArr = new String[16 + natureList.size() + rankList.size()];
        dataFieldArr[0] = "DEPARTMENT_NAME";
        for (int i = 0; i < natureList.size(); i++) {
            LinkedHashMap<String, String> natureDO = natureList.get(i);
            tableHeadArr1[i + 2] = "";
            if (i == 0 || i == 1) {
                columnWidthArr[i + 1] = 15;
                dataFieldArr[i + 1] = "NATURE_" + natureDO.get("id");
                tableHeadArr2[i + 1] = natureDO.get("code_name");
                tableHeadArr3[i + 1] = "";
            } else {
                columnWidthArr[i + rankList.size() + 1] = 15;
                dataFieldArr[i + rankList.size() + 1] = "NATURE_" + natureDO.get("id");
                tableHeadArr2[i + rankList.size() + 1] = natureDO.get("code_name");
                tableHeadArr3[i + rankList.size() + 1] = "";
            }
            if ("专职".equals(natureDO.get("code_name"))) {
                mergedRegionArr[i + 4] = "2,2," + (i + 1) + "," + (rankList.size() + i + 1);
                tableHeadArr3[i + 1] = "小计";
                for (int j = 0; j < rankList.size(); j++) {
                    LinkedHashMap<String, String> rankDO = rankList.get(j);
                    columnWidthArr[i + 1 + j + 1] = 15;
                    tableHeadArr1[i + 2 + j + 1] = "";
                    tableHeadArr2[i + 2 + j + 1] = "";
                    tableHeadArr3[i + 1 + j + 1] = rankDO.get("code_name");
                    dataFieldArr[i + 1 + j + 1] = "RANK_" + natureDO.get("id") + "_" + rankDO.get("id");
                }
            } else {
                if (i != 0) {
                    mergedRegionArr[i + 4] = "2,3," + (i + rankList.size() + 1) + "," + (i + rankList.size() + 1);
                } else {
                    mergedRegionArr[i + 4] = "2,3," + (i + 1) + "," + (i + 1);
                }
            }
        }
        columnWidthArr[natureList.size() + rankList.size() + 1] = 15;
        columnWidthArr[natureList.size() + rankList.size() + 2] = 15;
        columnWidthArr[natureList.size() + rankList.size() + 3] = 15;
        columnWidthArr[natureList.size() + rankList.size() + 4] = 15;
        columnWidthArr[natureList.size() + rankList.size() + 5] = 15;
        columnWidthArr[natureList.size() + rankList.size() + 6] = 15;
        columnWidthArr[natureList.size() + rankList.size() + 7] = 15;
        columnWidthArr[natureList.size() + rankList.size() + 8] = 15;
        columnWidthArr[natureList.size() + rankList.size() + 9] = 15;
        columnWidthArr[natureList.size() + rankList.size() + 10] = 15;
        columnWidthArr[natureList.size() + rankList.size() + 11] = 15;
        columnWidthArr[natureList.size() + rankList.size() + 12] = 15;
        columnWidthArr[natureList.size() + rankList.size() + 13] = 15;
        columnWidthArr[natureList.size() + rankList.size() + 14] = 15;
        columnWidthArr[natureList.size() + rankList.size() + 15] = 15;
        mergedRegionArr[natureList.size() + rankList.size() + 1] = "2,3," + (natureList.size() + rankList.size() + 1) + "," + ((natureList.size() + rankList.size() + 1));
        mergedRegionArr[natureList.size() + rankList.size() + 2] = "2,3," + (natureList.size() + rankList.size() + 2) + "," + ((natureList.size() + rankList.size() + 2));
        mergedRegionArr[natureList.size() + rankList.size() + 3] = "2,3," + (natureList.size() + rankList.size() + 3) + "," + ((natureList.size() + rankList.size() + 3));
        mergedRegionArr[natureList.size() + rankList.size() + 4] = "2,3," + (natureList.size() + rankList.size() + 4) + "," + ((natureList.size() + rankList.size() + 4));
        mergedRegionArr[natureList.size() + rankList.size() + 5] = "2,3," + (natureList.size() + rankList.size() + 5) + "," + ((natureList.size() + rankList.size() + 5));
        mergedRegionArr[natureList.size() + rankList.size() + 6] = "2,3," + (natureList.size() + rankList.size() + 6) + "," + ((natureList.size() + rankList.size() + 6));
        mergedRegionArr[natureList.size() + rankList.size() + 7] = "2,3," + (natureList.size() + rankList.size() + 7) + "," + ((natureList.size() + rankList.size() + 7));
        mergedRegionArr[natureList.size() + rankList.size() + 8] = "2,3," + (natureList.size() + rankList.size() + 8) + "," + ((natureList.size() + rankList.size() + 8));
        mergedRegionArr[natureList.size() + rankList.size() + 9] = "2,3," + (natureList.size() + rankList.size() + 9) + "," + ((natureList.size() + rankList.size() + 9));

        mergedRegionArr[natureList.size() + rankList.size() + 10] = "2,2," + (natureList.size() + rankList.size() + 10) + "," + ((natureList.size() + rankList.size() + 13));

        mergedRegionArr[natureList.size() + rankList.size() + 11] = "2,3," + (natureList.size() + rankList.size() + 14) + "," + ((natureList.size() + rankList.size() + 14));
        mergedRegionArr[natureList.size() + rankList.size() + 12] = "2,3," + (natureList.size() + rankList.size() + 15) + "," + ((natureList.size() + rankList.size() + 15));
        excelVO.setMergedRegionArr(mergedRegionArr);
        excelVO.setTitleName("退休人员管理机构和运行成本费用表");
        float[] rowHeightArr = {40f, 30f, 15f};
        excelVO.setRowHeight(rowHeightArr);
        tableHeadArr1[natureList.size() + rankList.size() + 1] = "运行成本费用（不含给退休人员个人的各种货币和实物类支出）";
        for (int i = 0; i < 14; i++) {
            tableHeadArr1[natureList.size() + rankList.size() + 2 + i] = "";
            if (i < 9) {
                tableHeadArr3[natureList.size() + rankList.size() + 2 + i] = "";
            }
        }
        tableHeadArr3[natureList.size() + rankList.size() + 2 + 8] = "培训费";
        tableHeadArr3[natureList.size() + rankList.size() + 2 + 9] = "次数";
        tableHeadArr3[natureList.size() + rankList.size() + 2 + 10] = "人次";
        tableHeadArr3[natureList.size() + rankList.size() + 2 + 11] = "参加其他\012培训人次";
        tableHeadArr3[natureList.size() + rankList.size() + 2 + 12] = "";
        tableHeadArr3[natureList.size() + rankList.size() + 2 + 13] = "";

        tableHeadArr2[natureList.size() + rankList.size() + 1] = "合计";
        tableHeadArr2[natureList.size() + rankList.size() + 2] = "水电气暖费用";
        tableHeadArr2[natureList.size() + rankList.size() + 3] = "薪酬及附加";
        tableHeadArr2[natureList.size() + rankList.size() + 4] = "折旧及摊销";
        tableHeadArr2[natureList.size() + rankList.size() + 5] = "房租";
        tableHeadArr2[natureList.size() + rankList.size() + 6] = "办公费";
        tableHeadArr2[natureList.size() + rankList.size() + 7] = "交通费（差旅费）";
        tableHeadArr2[natureList.size() + rankList.size() + 8] = "会议费";
        tableHeadArr2[natureList.size() + rankList.size() + 9] = "宣传费";
        tableHeadArr2[natureList.size() + rankList.size() + 10] = "培训情况";
        tableHeadArr2[natureList.size() + rankList.size() + 11] = "";
        tableHeadArr2[natureList.size() + rankList.size() + 12] = "";
        tableHeadArr2[natureList.size() + rankList.size() + 13] = "";
        tableHeadArr2[natureList.size() + rankList.size() + 14] = "其他";
        tableHeadArr2[natureList.size() + rankList.size() + 15] = "备注";
        List<String[]> tableHeadList = new ArrayList<String[]>();
        tableHeadList.add(tableHeadArr1);
        tableHeadList.add(tableHeadArr2);
        tableHeadList.add(tableHeadArr3);
        excelVO.setTableHeadList(tableHeadList);
        excelVO.setColumnWidthArr(columnWidthArr);
        dataFieldArr[natureList.size() + rankList.size() + 1] = "TOTAL";
        dataFieldArr[natureList.size() + rankList.size() + 2] = "UTILITIES";
        dataFieldArr[natureList.size() + rankList.size() + 3] = "EMOLUMENT";
        dataFieldArr[natureList.size() + rankList.size() + 4] = "DEPRECIATION";
        dataFieldArr[natureList.size() + rankList.size() + 5] = "CHUMMAGE";
        dataFieldArr[natureList.size() + rankList.size() + 6] = "OFFICE_ALLOWANCE";
        dataFieldArr[natureList.size() + rankList.size() + 7] = "TRAVEL_EXPENSE";
        dataFieldArr[natureList.size() + rankList.size() + 8] = "CONVENTION_EXPENSE";
        dataFieldArr[natureList.size() + rankList.size() + 9] = "PUBLICITY_EXPENSE";

        dataFieldArr[natureList.size() + rankList.size() + 10] = "TRAIN_EXPENSE";
        dataFieldArr[natureList.size() + rankList.size() + 11] = "TRAIN_COUNT";
        dataFieldArr[natureList.size() + rankList.size() + 12] = "TRAIN_MAN_TIME";
        dataFieldArr[natureList.size() + rankList.size() + 13] = "TRAIN_OTHER";

        dataFieldArr[natureList.size() + rankList.size() + 14] = "OTHER";
        dataFieldArr[natureList.size() + rankList.size() + 15] = "REMARK";
        excelVO.setDataFieldArr(dataFieldArr);
        excelVO.setFileName("退休人员管理机构和运行成本费用表");
        //列表数据
        param.put("year", ((String) param.get("year")).replace("年", ""));
        param.put("natureList", natureList);
        param.put("rankList", rankList);
        List<Map<String, String>> dataList = this.retireCostService.listRetireEmployeeCost(param);
        POIExcelUtils.exportExcel(response, excelVO, dataList);
    }

    /**
     * @description 查找国网统计报表_基本养老保险及档案统计
     */
    @RequestMapping(value = "/listRetireBasicEndowmentInsurance", method = RequestMethod.POST)
    public ResultVO listRetireBasicEndowmentInsurance(@RequestParam Map<String, Object> param) {

        //1、查人事档案存放地
        Map<String, String> natureParam = new HashMap<String, String>();
        natureParam.put("code", ParentIdContant.BASE_CODE_ARCHIVES_AREA);
        ResultVO archivesAreaVO = this.feignZuulServer.getCode(natureParam);
        @SuppressWarnings("unchecked")
        Map<String, List<LinkedHashMap<String,String>>> mapData = (Map<String, List<LinkedHashMap<String, String>>>) archivesAreaVO.getData();
        List<LinkedHashMap<String, String>> archivesAreaList = mapData.get(ParentIdContant.BASE_CODE_ARCHIVES_AREA);

        param.put("archivesAreaList", archivesAreaList);
        return new ResultVO(this.retireCostService.listRetireBasicEndowmentInsurance(param), true, "查询成功");
    }

    /**
     * @param response response对象
     * @description 导出国网统计报表_基本养老保险及档案统计_Excel文件
     */
    @RequestMapping(value = "/excelRetireBasicEndowmentInsurance", method = RequestMethod.GET)
    public void excelRetireBasicEndowmentInsurance(HttpServletResponse response, @RequestParam Map<String, Object> param) {
        ExcelVO excelVO = new ExcelVO();
        excelVO.setSheetName("基本养老保险及档案统计");
        //1、查人事档案存放地
        Map<String, String> natureParam = new HashMap<String, String>();
        natureParam.put("parent_id", ParentIdContant.BASE_CODE_ARCHIVES_AREA);
        natureParam.put("is_available", "1");
        ResultVO archivesAreaVO = this.feignZuulServer.getCode(natureParam);
        @SuppressWarnings("unchecked")
        List<LinkedHashMap<String, String>> archivesAreaList = (List<LinkedHashMap<String, String>>) archivesAreaVO.getData();
        //2、POI导出Excel参数信息
        int[] columnWidthArr = new int[archivesAreaList.size() + 6];
        columnWidthArr[0] = 30;
        columnWidthArr[1] = 10;
        columnWidthArr[2] = 20;
        columnWidthArr[3] = 20;
        columnWidthArr[4] = 20;
        columnWidthArr[5] = 15;
        String[] mergedRegionArr = {"0,0,0," + (columnWidthArr.length - 1), "1,2,0,0", "1,1,1,2", "1,2,3,3", "1,2,4,4", "1,1,5," + (columnWidthArr.length - 1)};
        String[] tableHeadArr1 = new String[archivesAreaList.size() + 6];
        tableHeadArr1[0] = "企业名称";
        tableHeadArr1[1] = "退休人员";
        tableHeadArr1[2] = "";
        tableHeadArr1[3] = "退休人员基本养老金（元/月）";
        tableHeadArr1[4] = "地方基本养老金水平（元/月）";
        tableHeadArr1[5] = "人事档案管理";
        String[] tableHeadArr2 = new String[archivesAreaList.size() + 6];
        tableHeadArr2[0] = "";
        tableHeadArr2[1] = "总数";
        tableHeadArr2[2] = "其中：1997年12月31日以前退休人数";
        tableHeadArr2[3] = "";
        tableHeadArr2[4] = "";
        tableHeadArr2[5] = "合计";
        String[] dataFieldArr = new String[archivesAreaList.size() + 6];
        dataFieldArr[0] = "DEPARTMENT_NAME";
        dataFieldArr[1] = "RETIRE_TOTAL";
        dataFieldArr[2] = "RETIRE_1998";
        dataFieldArr[3] = "RETIRE_BASIC_PENSION";
        dataFieldArr[4] = "BASIC_LOCAL_PENSION";
        dataFieldArr[5] = "ARCHIVES_AREA_TOTAL";
        for (int i = 0; i < archivesAreaList.size(); i++) {
            LinkedHashMap<String, String> natureDO = archivesAreaList.get(i);
            columnWidthArr[i + 6] = 15;
            tableHeadArr1[i + 6] = "";
            tableHeadArr2[i + 6] = natureDO.get("code_name");
            dataFieldArr[i + 6] = "ARCHIVES_AREA_" + natureDO.get("id");
        }
        excelVO.setMergedRegionArr(mergedRegionArr);
        excelVO.setTitleName("退休人员基本养老保险、人事档案情况表");
        float[] rowHeightArr = {40f, 25f, 15f};
        excelVO.setRowHeight(rowHeightArr);
        List<String[]> tableHeadList = new ArrayList<String[]>();
        tableHeadList.add(tableHeadArr1);
        tableHeadList.add(tableHeadArr2);
        excelVO.setTableHeadList(tableHeadList);
        excelVO.setColumnWidthArr(columnWidthArr);
        excelVO.setDataFieldArr(dataFieldArr);
        excelVO.setFileName("退休人员基本养老保险、人事档案情况表");
        //列表数据
        param.put("archivesAreaList", archivesAreaList);
        List<Map<String, String>> dataList = this.retireCostService.listRetireBasicEndowmentInsurance(param);
        POIExcelUtils.exportExcel(response, excelVO, dataList);
    }

    /**
     * @description 查找国网统计报表_退休人员相关费用情况表
     */
    @RequestMapping(value = "/listRetireUserCorrelativeCharges", method = RequestMethod.POST)
    public ResultVO listRetireUserCorrelativeCharges(@RequestParam Map<String, String> param) {
        return new ResultVO(this.retireCostService.listRetireUserCorrelativeCharges(param), true, "查询成功");
    }

    /**
     * @param response response对象
     * @description 导出国网统计报表_退休人员相关费用情况表_Excel文件
     */
    @RequestMapping(value = "/excelRetireUserCorrelativeCharges", method = RequestMethod.GET)
    public void excelRetireUserCorrelativeCharges(HttpServletResponse response, @RequestParam Map<String, String> param) {
        ExcelVO excelVO = new ExcelVO();
        excelVO.setSheetName("退休人员相关费用统计");
        int[] columnWidthArr = {30, 15, 20, 15, 15, 15, 15, 15, 15, 15};
        String[] mergedRegionArr = {"0,0,0," + (columnWidthArr.length - 1), "1,2,0,0", "1,2,1,1", "1,2,2,2", "1,1,3," + (columnWidthArr.length - 1)};
        String[] tableHeadArr1 = new String[columnWidthArr.length];
        tableHeadArr1[0] = "企业名称";
        tableHeadArr1[1] = "合计\012（万元）";
        tableHeadArr1[2] = "在成本中列支的\012补充医疗保险费\012（万元）";
        tableHeadArr1[3] = "退休人员统筹外费用（万元）";
        tableHeadArr1[4] = "";
        tableHeadArr1[5] = "";
        tableHeadArr1[6] = "";
        tableHeadArr1[7] = "";
        tableHeadArr1[8] = "";
        tableHeadArr1[9] = "";
        String[] tableHeadArr2 = new String[columnWidthArr.length];
        tableHeadArr2[0] = "";
        tableHeadArr2[1] = "";
        tableHeadArr2[2] = "";
        tableHeadArr2[3] = "小计";
        tableHeadArr2[4] = "按月生活\012补贴\012（万元）";
        tableHeadArr2[5] = "一次性生活\012补贴\012（万元）";
        tableHeadArr2[6] = "医疗费\012（体检费）\012（万元）";
        tableHeadArr2[7] = "活动经费\012（万元）";
        tableHeadArr2[8] = "供暖费补贴\012（万元）";
        tableHeadArr2[9] = "其他费用\012（万元）";
        String[] dataFieldArr = new String[columnWidthArr.length];
        dataFieldArr[0] = "DEPARTMENT_NAME";
        dataFieldArr[1] = "EXPENSE_TOTAL";
        dataFieldArr[2] = "SUPPLEMENTARY_MEDICAL";
        dataFieldArr[3] = "SUBTOTAL";
        dataFieldArr[4] = "MONTHLY_LIVING_ALLOWANCE";
        dataFieldArr[5] = "ONE_TIME_LIVING_ALLOWANCE";
        dataFieldArr[6] = "MEDICAL_FEE";
        dataFieldArr[7] = "ACTIVITY_FUNDS";
        dataFieldArr[8] = "SUBSIDIES_FOR_HEATING";
        dataFieldArr[9] = "OTHER_EXPENSES";
        excelVO.setMergedRegionArr(mergedRegionArr);
        excelVO.setTitleName("退休人员相关费用情况表");
        float[] rowHeightArr = {40f, 45f, 15f};
        excelVO.setRowHeight(rowHeightArr);
        List<String[]> tableHeadList = new ArrayList<String[]>();
        tableHeadList.add(tableHeadArr1);
        tableHeadList.add(tableHeadArr2);
        excelVO.setTableHeadList(tableHeadList);
        excelVO.setColumnWidthArr(columnWidthArr);
        excelVO.setDataFieldArr(dataFieldArr);
        excelVO.setFileName("退休人员相关费用情况表");
        List<Map<String, String>> dataList = this.retireCostService.listRetireUserCorrelativeCharges(param);
        POIExcelUtils.exportExcel(response, excelVO, dataList);
    }

    /**
     * @description 国网统计报表_离退休工作机构情况
     */
    @RequestMapping(value = "/listRetireDepartmentAndCost", method = RequestMethod.POST)
    public ResultVO listRetireDepartmentAndCost(@RequestParam Map<String, Object> param) {
        Map<String, String> codeParam = new HashMap<String, String>();
        codeParam.put("code", ParentIdContant.BASE_CODE_ADMINISTRATIVE_DIVISION + "," + ParentIdContant.BASE_CODE_ORGANIZATION_TYPE);
        //1、查询行政区划类型
        ResultVO divisionVO = this.feignZuulServer.getCode(codeParam);
        @SuppressWarnings("unchecked")
        Map<String, List<LinkedHashMap<String, String>>> dataMap = (Map<String, List<LinkedHashMap<String, String>>>) divisionVO.getData();
        List<LinkedHashMap<String, String>> divisionList = dataMap.get(ParentIdContant.BASE_CODE_ADMINISTRATIVE_DIVISION);
        //2、查询机构类型
        List<LinkedHashMap<String, String>> typeList = dataMap.get(ParentIdContant.BASE_CODE_ORGANIZATION_TYPE);
        param.put("divisionList", divisionList);
        param.put("typeList", typeList);
        return new ResultVO(this.retireCostService.listRetireDepartmentAndCost(param), true, "查询成功");

    }

    /**
     * @param response response对象
     * @description 导出国网统计报表_离退休工作机构情况_Excel文件
     */
    @RequestMapping(value = "/excelRetireDepartmentAndCost", method = RequestMethod.GET)
    public void excelRetireDepartmentAndCost(HttpServletResponse response, @RequestParam Map<String, Object> param) {
        //1、查询行政区划类型
        Map<String, String> codeParam = new HashMap<String, String>();
        codeParam.put("parent_id", ParentIdContant.BASE_CODE_ADMINISTRATIVE_DIVISION);
        codeParam.put("is_available", "1");
        ResultVO divisionVO = this.feignZuulServer.getCode(codeParam);
        @SuppressWarnings("unchecked")
        List<LinkedHashMap<String, String>> divisionList = (List<LinkedHashMap<String, String>>) divisionVO.getData();
        //2、查询机构类型
        codeParam.put("parent_id", ParentIdContant.BASE_CODE_ORGANIZATION_TYPE);
        ResultVO typeVO = this.feignZuulServer.getCode(codeParam);
        @SuppressWarnings("unchecked")
        List<LinkedHashMap<String, String>> typeList = (List<LinkedHashMap<String, String>>) typeVO.getData();
        param.put("divisionList", divisionList);
        param.put("typeList", typeList);
        ExcelVO excelVO = new ExcelVO();
        excelVO.setSheetName("离退休工作机构情况");
        int[] columnWidthArr = new int[15];
        columnWidthArr[0] = 30;
        columnWidthArr[1] = 8;
        String[] mergedRegionArr = new String[12];
        mergedRegionArr[0] = "0,0,0," + (columnWidthArr.length - 1);
        mergedRegionArr[1] = "1,3,0,0";
        mergedRegionArr[2] = "1,3,1,1";
        String[] tableHeadArr1 = new String[columnWidthArr.length];
        tableHeadArr1[0] = "项目";
        tableHeadArr1[1] = "编号";
        tableHeadArr1[2] = "离退休工作机构";
        String[] tableHeadArr2 = new String[columnWidthArr.length];
        tableHeadArr2[0] = "";
        tableHeadArr2[1] = "";
        String[] tableHeadArr3 = new String[columnWidthArr.length];
        tableHeadArr3[0] = "";
        tableHeadArr3[1] = "";
        tableHeadArr3[2] = "";
        String[] dataFieldArr = new String[columnWidthArr.length];
        dataFieldArr[0] = "DEPARTMENT_NAME";
        dataFieldArr[1] = "SEQUENCE";
        int divisionCol = 0;
        int independentCol = 0;
        for (int i = 0; i < divisionList.size(); i++) {
            LinkedHashMap<String, String> divisionCodeDO = divisionList.get(i);
            if ("1".equals(divisionCodeDO.get("special_mark"))) {
                independentCol += 1;
                columnWidthArr[i + 2] = 8;
                mergedRegionArr[i + 3] = "2,3," + (i * divisionCol + 2) + "," + (i * divisionCol + 2);
                dataFieldArr[i + 2] = "DEPARTMENT_TYPE_" + divisionCodeDO.get("id");
                tableHeadArr2[i + 2] = divisionCodeDO.get("code_name");
            } else {
                tableHeadArr2[(i - independentCol) * typeList.size() + 3] = divisionCodeDO.get("code_name");
                mergedRegionArr[i + 3] = "2,2," + ((i - independentCol) * typeList.size() + 3) + "," + ((i - independentCol) * typeList.size() + 5);
                for (int j = 0; j < typeList.size(); j++) {
                    LinkedHashMap<String, String> typeCodeDO = typeList.get(j);
                    columnWidthArr[i * typeList.size() + j] = 10;
                    tableHeadArr1[(i - 1) * typeList.size() + j + 3] = "";
                    if (i != 0) {
                        tableHeadArr2[i * typeList.size() + j + 2] = "";
                    }
                    tableHeadArr3[divisionCol * typeList.size() + j + 3] = typeCodeDO.get("code_name");
                    dataFieldArr[divisionCol * typeList.size() + j + 3] = "DEPARTMENT_TYPE_" + divisionCodeDO.get("id") + "_" + typeCodeDO.get("id");
                }
                divisionCol += 1;
            }
        }
        columnWidthArr[12] = 8;
        columnWidthArr[13] = 8;
        columnWidthArr[14] = 10;
        excelVO.setColumnWidthArr(columnWidthArr);
        mergedRegionArr[divisionList.size() + 3] = "1,1,2," + (typeList.size() * divisionCol + independentCol + 1);
        mergedRegionArr[divisionList.size() + 4] = "1,1," + (typeList.size() * divisionCol + independentCol + 2) + "," + (columnWidthArr.length - 1);
        mergedRegionArr[divisionList.size() + 5] = "2,3," + (typeList.size() * divisionCol + independentCol + 2) + "," + (typeList.size() * divisionCol + independentCol + 2);
        mergedRegionArr[divisionList.size() + 6] = "2,3," + (typeList.size() * divisionCol + independentCol + 3) + "," + (typeList.size() * divisionCol + independentCol + 3);
        mergedRegionArr[divisionList.size() + 7] = "2,3," + (typeList.size() * divisionCol + independentCol + 4) + "," + (typeList.size() * divisionCol + independentCol + 4);
        excelVO.setMergedRegionArr(mergedRegionArr);
        List<String[]> tableHeadList = new ArrayList<String[]>();
        tableHeadArr1[divisionCol * typeList.size() + 3] = "培训情况";
        tableHeadArr1[divisionCol * typeList.size() + 4] = "";
        tableHeadArr1[divisionCol * typeList.size() + 5] = "";
        tableHeadList.add(tableHeadArr1);
        tableHeadArr2[divisionCol * typeList.size() + 3] = "次数";
        tableHeadArr2[divisionCol * typeList.size() + 4] = "人次";
        tableHeadArr2[divisionCol * typeList.size() + 5] = "参加其他\012培训人次";
        tableHeadList.add(tableHeadArr2);
        tableHeadArr3[divisionCol * typeList.size() + 3] = "";
        tableHeadArr3[divisionCol * typeList.size() + 4] = "";
        tableHeadArr3[divisionCol * typeList.size() + 5] = "";
        tableHeadList.add(tableHeadArr3);
        dataFieldArr[divisionCol * typeList.size() + 3] = "TRAIN_COUNT";
        dataFieldArr[divisionCol * typeList.size() + 4] = "TRAIN_MAN_TIME";
        dataFieldArr[divisionCol * typeList.size() + 5] = "TRAIN_OTHER";
        excelVO.setTitleName("离退休工作机构情况统计表");
        float[] rowHeightArr = {40f, 20f, 15f};
        excelVO.setRowHeight(rowHeightArr);
        excelVO.setTableHeadList(tableHeadList);
        excelVO.setDataFieldArr(dataFieldArr);
        excelVO.setFileName("离退休工作机构情况统计表");
        //列表数据
        List<Map<String, String>> dataList = this.retireCostService.listRetireDepartmentAndCost(param);
        POIExcelUtils.exportExcel(response, excelVO, dataList);
    }
}