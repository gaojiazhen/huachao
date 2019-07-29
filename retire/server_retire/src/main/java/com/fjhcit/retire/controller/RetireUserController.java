package com.fjhcit.retire.controller;

import java.util.*;

import javax.servlet.http.HttpServletResponse;

import jdk.nashorn.internal.runtime.linker.LinkerCallSite;
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
import com.fjhcit.model.ResultVO;
import com.fjhcit.model.RetireUserExcelVO;
import com.fjhcit.retire.service.FeignZuulServer;
import com.fjhcit.retire.service.RetireFamilyService;
import com.fjhcit.retire.service.RetireUserService;
import com.fjhcit.model.ExcelVO;
import com.fjhcit.utils.POIExcelUtils;
import com.fjhcit.entity.RetireFamilyDO;
import com.fjhcit.entity.RetireUserDO;

/**
 * @author 陈麟
 * @description 离（退）休人员基本信息库_控制器
 * @date 2019年06月05日 上午10:45:07
 */
@RestController
@RequestMapping("/retireUser")
public class RetireUserController {
    @Autowired
    private RetireUserService retireUserService;        //离（退）休人员基本信息库_业务接口
    @Autowired
    private RetireFamilyService retireFamilyService;        //离（退）休人员_家庭主要成员表_业务接口
    @Autowired
    private FeignZuulServer feignZuulServer;            //远程调用其他组件接口

    /**
     * @description 查离（退）休人员基本信息库列表数据
     */
    @RequestMapping(value = "/listRetireUser", method = RequestMethod.POST)
    public ResultVO listRetireUser(@RequestParam Map<String, String> param) {
        return new ResultVO(this.retireUserService.listRetireUser(param), true, "查询成功");
    }

    /**
     * @description 查离（退）休人员基本信息库分页列表数据
     */
    @RequestMapping(value = "/listRetireUserByPaging", method = RequestMethod.POST)
    public ResultVO listRetireUserByPaging(@RequestParam Map<String, String> param) {
        if (StringUtils.isEmpty(param.get("pageNum"))) {
            param.put("pageNum", "1");
        }
        if (StringUtils.isEmpty(param.get("pageSize"))) {
            param.put("pageSize", "10");
        }
        int pageNum = Integer.parseInt(param.get("pageNum"));        //当前页
        int pageSize = Integer.parseInt(param.get("pageSize"));    //每页条数
        Page<Object> p = PageHelper.startPage(pageNum, pageSize);
        List<RetireUserDO> personList = this.retireUserService.listRetireUser(param);
        //返回信息
        ResultVO result = new ResultVO(personList, true, "查询成功", p.getTotal(), pageNum, pageSize);
        return result;
    }

    /**
     * @description 保存离（退）休人员基本信息库数据（新增、修改）
     */
    @RequestMapping(value = "/saveRetireUser", method = RequestMethod.POST)
    public ResultVO saveRetireUser(@RequestBody RetireUserDO retireUserDO) {
        ResultVO result;
        try {
            String idcard = retireUserDO.getIdcard();
            //1、查身份证是否重复
            Map<String, String> cparam = new HashMap<String, String>();
            cparam.put("idcard", idcard);
            cparam.put("id", retireUserDO.getId());
            Integer idcardCount = this.retireUserService.findRetireUserCountByIdcard(cparam);
            if (idcardCount > 0) {
                result = new ResultVO(-5, false, "身份证不能重复");
            } else {
                //2、保存人员基本信息
                if (StringUtils.isEmpty(retireUserDO.getId())) {
                    this.retireUserService.insertRetireUser(retireUserDO);
                } else {
                    this.retireUserService.updateRetireUser(retireUserDO);
                }
                //3、保存家庭主要成员信息
                Map<String, String> delFamilyParam = new HashMap<String, String>();
                delFamilyParam.put("user_id", retireUserDO.getId());
                this.retireFamilyService.removeRetireFamilyByUserid(delFamilyParam);
                String[] appellation_id_arr = retireUserDO.getAppellation_id_arr();
                if (appellation_id_arr != null) {
                    for (int i = 0; i < appellation_id_arr.length; i++) {
                        RetireFamilyDO retireFamilyDO = new RetireFamilyDO();
                        retireFamilyDO.setCreate_user_id(retireUserDO.getModified_user_id());
                        retireFamilyDO.setModified_user_id(retireUserDO.getModified_user_id());
                        retireFamilyDO.setUser_id(retireUserDO.getId());
                        retireFamilyDO.setAppellation_id(appellation_id_arr[i]);
                        retireFamilyDO.setUser_name(retireUserDO.getUser_name_arr()[i]);
                        retireFamilyDO.setAge(retireUserDO.getAge_arr()[i]);
                        retireFamilyDO.setWork_unit_post(retireUserDO.getWork_unit_post_arr()[i]);
                        retireFamilyDO.setContact(retireUserDO.getContact_arr()[i]);
                        retireFamilyDO.setRemark(retireUserDO.getRemark_arr()[i]);
                        this.retireFamilyService.insertRetireFamily(retireFamilyDO);
                    }
                }
                result = new ResultVO(retireUserDO.getId(), true, "保存成功");
            }
        } catch (Exception e) {
            result = new ResultVO(-1, false, "保存失败");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @description 删除离（退）休人员基本信息库数据
     */
    @RequestMapping(value = "/removeRetireUser", method = RequestMethod.POST)
    public ResultVO removeRetireUser(@RequestParam Map<String, String> param) {
        ResultVO result;
        try {
            String ids = param.get("ids");
            String[] idsArr = ids.split(",");
            //删除
            Map<String, Object> delParam = new HashMap<String, Object>();
            delParam.put("idsArr", idsArr);
            this.retireUserService.removeRetireUserByIdsArr(delParam);
            result = new ResultVO(true, true, "删除成功");
        } catch (Exception e) {
            result = new ResultVO(false, false, "删除失败");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @description 查询国网统计报表_离休干部两年数字变化
     */
    @RequestMapping(value = "/listQuitCadreBiennialChange", method = RequestMethod.POST)
    public ResultVO listQuitCadreBiennialChange(@RequestParam Map<String, String> param) {
        String unit_id = param.get("unit_id");
        if (StringUtils.isEmpty(unit_id)) {
            param.put("kd_code", ParentIdContant.BASE_DEPARTMENT);
        } else {
            param.put("id", unit_id);
        }
        param.put("retire_nature_id", ParentIdContant.BASE_CODE_NATURE_QUIT);
        return new ResultVO(this.retireUserService.listQuitCadreBiennialChange(param), true, "查询成功");
    }

    /**
     * @param response response对象
     * @description 导出国网统计报表_离休干部两年数字变化_Excel文件
     */
    @RequestMapping(value = "/excelQuitCadreBiennialChange", method = RequestMethod.GET)
    public void excelQuitCadreBiennialChange(HttpServletResponse response, @RequestParam Map<String, String> param) {
        ExcelVO excelVO = new ExcelVO();
        excelVO.setSheetName("离休干部两年数字变化");
        int[] columnWidthArr = {30, 10, 30, 30, 30, 30};
        String[] mergedRegionArr = {"0,0,0," + (columnWidthArr.length - 1)};
        excelVO.setMergedRegionArr(mergedRegionArr);
        excelVO.setTitleName("离休干部两年数字变化情况统计表");
        float[] rowHeightArr = {40f, 15f, 15f};
        excelVO.setRowHeight(rowHeightArr);
        String[] tableHeadArr = {"项目", "编号", "上年12月底离休干部总数", "本年度内去世离休干部数", "本年12月底应有离休干部数", "本年12月底实有离休干部数"};
        List<String[]> tableHeadList = new ArrayList<String[]>();
        tableHeadList.add(tableHeadArr);
        excelVO.setTableHeadList(tableHeadList);
        excelVO.setColumnWidthArr(columnWidthArr);
        String[] dataFieldArr = {"DEPARTMENT_NAME", "SEQUENCE", "LAST_YEAR_CADRE", "THIS_YEAR_CADRE_DEMISE", "THIS_YEAR_DESERVED", "THIS_YEAR_EXISTENCE"};
        excelVO.setDataFieldArr(dataFieldArr);
        excelVO.setFileName("离休干部两年数字变化情况统计表");
        //列表数据
        String unit_id = param.get("unit_id");
        if (StringUtils.isEmpty(unit_id)) {
            param.put("kd_code", ParentIdContant.BASE_DEPARTMENT);
        } else {
            param.put("id", unit_id);
        }
        param.put("retire_nature_id", ParentIdContant.BASE_CODE_NATURE_QUIT);
        List<Map<String, String>> dataList = this.retireUserService.listQuitCadreBiennialChange(param);
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, String> map = dataList.get(i);
            //map.put("SEQUENCE", String.valueOf(i+1));
            int thisYear = Integer.parseInt(map.get("LAST_YEAR_CADRE")) - Integer.parseInt(map.get("THIS_YEAR_CADRE_DEMISE"));
            map.put("THIS_YEAR_DESERVED", String.valueOf(thisYear));
            map.put("THIS_YEAR_EXISTENCE", String.valueOf(thisYear));
        }
        POIExcelUtils.exportExcel(response, excelVO, dataList);
    }

    /**
     * @description 查询国网统计报表_离休干部分地区情况统计表
     */
    @RequestMapping(value = "/listQuitCadreByRegion", method = RequestMethod.POST)
    public ResultVO listQuitCadreByRegion(@RequestParam Map<String, String> param) {
        param.put("retire_nature_id", ParentIdContant.BASE_CODE_NATURE_QUIT);
        return new ResultVO(this.retireUserService.listQuitCadreByRegion(param), true, "查询成功");
    }

    /**
     * @param response response对象
     * @description 导出国网统计报表_离休干部分地区情况统计表_Excel文件
     */
    @RequestMapping(value = "/excelQuitCadreByRegion", method = RequestMethod.GET)
    public void excelQuitCadreByRegion(HttpServletResponse response, @RequestParam Map<String, String> param) {
        ExcelVO excelVO = new ExcelVO();
        excelVO.setSheetName("离休干部分地区统计");
        int[] columnWidthArr = {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10};
        String[] mergedRegionArr = {"0,0,0," + (columnWidthArr.length - 1), "1,2,0,0", "1,2,1,1", "1,2,2,2", "1,1,3,12"};
        excelVO.setMergedRegionArr(mergedRegionArr);
        excelVO.setTitleName("中央单位离休干部分地区情况统计表");
        float[] rowHeightArr = {40f, 15f, 15f};
        excelVO.setRowHeight(rowHeightArr);
        String[] tableHeadArr1 = {"项目", "编号", "总数", "分布在各地", "", "", "", "", "", "", "", "", ""};  //合并单元格不输出空格，会没有边框线条
        String[] tableHeadArr2 = {"", "", "", "小计", "福州", "厦门", "莆田", "泉州", "龙岩", "漳州", "南平", "宁德", "三明"};
        List<String[]> tableHeadList = new ArrayList<String[]>();
        tableHeadList.add(tableHeadArr1);
        tableHeadList.add(tableHeadArr2);
        excelVO.setTableHeadList(tableHeadList);
        excelVO.setColumnWidthArr(columnWidthArr);
        String[] dataFieldArr = {"ITEM", "SEQUENCE", "TOTAL", "SUBTOTAL", "FZ", "XM", "PT", "QZ", "LY", "ZZ", "NP", "ND", "SM"};
        excelVO.setDataFieldArr(dataFieldArr);
        excelVO.setFileName("中央单位离休干部分地区情况统计表");
        //列表数据
        param.put("retire_nature_id", ParentIdContant.BASE_CODE_NATURE_QUIT);
        List<Map<String, String>> dataList = this.retireUserService.listQuitCadreByRegion(param);
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, String> map = dataList.get(i);
            map.put("ITEM", "总计");
            //map.put("SEQUENCE", String.valueOf(i+1));
            int subtotal = Integer.parseInt(map.get("FZ")) + Integer.parseInt(map.get("XM")) + Integer.parseInt(map.get("PT")) + Integer.parseInt(map.get("QZ")) +
                    Integer.parseInt(map.get("LY")) + Integer.parseInt(map.get("ZZ")) + Integer.parseInt(map.get("NP")) + Integer.parseInt(map.get("ND")) + Integer.parseInt(map.get("SM"));
            map.put("TOTAL", String.valueOf(subtotal));
            map.put("SUBTOTAL", String.valueOf(subtotal));
        }
        POIExcelUtils.exportExcel(response, excelVO, dataList);
    }

    /**
     * @description 查询国网统计报表_企业离休干部统计
     */
    @RequestMapping(value = "/listQuitCadreBasic", method = RequestMethod.POST)
    public ResultVO listQuitCadreBasic(@RequestParam Map<String, String> param) {
        String unit_id = param.get("unit_id");
        if (StringUtils.isEmpty(unit_id)) {
            param.put("parent_id", ParentIdContant.BASE_DEPARTMENT);
        } else {
            param.put("id", unit_id);
        }
        param.put("retire_nature_id", ParentIdContant.BASE_CODE_NATURE_QUIT);
        param.put("politics_status_id", ParentIdContant.BASE_CODE_POLITICS_STATUS_COMMUNIST);
        param.put("health_status", ParentIdContant.BASE_CODE_HEALTH_STATUS_CANNOT_CARE_ONESELF);
        return new ResultVO(this.retireUserService.listQuitCadreBasic(param), true, "查询成功");
    }

    /**
     * @description 导出国网统计报表_企业离休干部统计_Excel文件
     */
    @RequestMapping(value = "/excelQuitCadreBasic", method = RequestMethod.GET)
    public void excelQuitCadreBasic(HttpServletResponse response, @RequestParam Map<String, String> param) {
        ExcelVO excelVO = new ExcelVO();
        excelVO.setSheetName("离休干部基本情况");
        int[] columnWidthArr = {30, 10, 10, 10, 15, 15, 15, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10};
        String[] mergedRegionArr = {"0,0,0," + (columnWidthArr.length - 1), "1,3,0,0", "1,3,1,1", "1,3,2,2", "1,3,3,3", "1,3,4,4", "1,3,5,5", "1,3,6,6",
                "1,1,7," + +(columnWidthArr.length - 1), "2,2,7,8", "2,2,9,10", "2,2,11,12", "2,2,13,14", "2,2,15,16"};
        excelVO.setMergedRegionArr(mergedRegionArr);
        excelVO.setTitleName("企业离休干部基本情况统计表");
        float[] rowHeightArr = {40f, 15f, 15f};
        excelVO.setRowHeight(rowHeightArr);
        String[] tableHeadArr1 = {"项目", "编号", "总数", "中共党员", "生活不能自理", "70岁至79岁", "80岁及以上", "参 加 革 命 工 作 时 间", "", "", "", "", "", "", "", "", ""};  //合并单元格不输出空格，会没有边框线条
        String[] tableHeadArr2 = {"", "", "", "", "", "", "", "1927年7月底以前", "", "1927年8月1日至1937年7月6日", "",
                "1937年7月7日至1942年12月底", "", "1943年1月1日至1945年9月2日", "", "1945年9月3日至9月底", ""};
        String[] tableHeadArr3 = {"", "", "", "", "", "", "", "", "在京", "", "在京", "", "在京", "", "在京", "", "在京"};
        List<String[]> tableHeadList = new ArrayList<String[]>();
        tableHeadList.add(tableHeadArr1);
        tableHeadList.add(tableHeadArr2);
        tableHeadList.add(tableHeadArr3);
        excelVO.setTableHeadList(tableHeadList);
        excelVO.setColumnWidthArr(columnWidthArr);
        String[] dataFieldArr = {"DEPARTMENT_NAME", "SEQUENCE", "TOTAL", "COMMUNIST", "CANNOT_CARE_ONESELF", "SEVENTY_AGE", "EIGHTY_AGE",
                "WORK_TIME_19270731", "BEIJING_19270731", "WORK_TIME_19370706", "BEIJING_19370706", "WORK_TIME_19421231", "BEIJING_19421231",
                "WORK_TIME_19450902", "BEIJING_19450902", "WORK_TIME_19450930", "BEIJING_19450930"};
        excelVO.setDataFieldArr(dataFieldArr);
        excelVO.setFileName("企业离休干部基本情况统计表");
        //列表数据
        String unit_id = param.get("unit_id");
        if (StringUtils.isEmpty(unit_id)) {
            param.put("kd_code", ParentIdContant.BASE_DEPARTMENT);
        } else {
            param.put("id", unit_id);
        }
        param.put("retire_nature_id", ParentIdContant.BASE_CODE_NATURE_QUIT);
        param.put("politics_status_id", ParentIdContant.BASE_CODE_POLITICS_STATUS_COMMUNIST);
        param.put("health_status", ParentIdContant.BASE_CODE_HEALTH_STATUS_CANNOT_CARE_ONESELF);
        List<Map<String, String>> dataList = this.retireUserService.listQuitCadreBasic(param);
        POIExcelUtils.exportExcel(response, excelVO, dataList);
    }

    /**
     * @description 查询国网统计报表_退休干部享受待遇统计
     */
    @RequestMapping(value = "/listRetireCadreTreatment", method = RequestMethod.POST)
    public ResultVO listRetireCadreTreatment(@RequestParam Map<String, Object> param) {
        Map codeparam = new HashMap();
        codeparam.put("code", ParentIdContant.BASE_CODE_NATURE + "," + ParentIdContant.BASE_CODE_NOW_TREATMENT_LEVEL);
        ResultVO code = this.feignZuulServer.getCode(codeparam);
        Map<String, List<LinkedHashMap<String, String>>> dataMap = (Map<String, List<LinkedHashMap<String, String>>>) code.getData();
        List<LinkedHashMap<String, String>> nature = dataMap.get(ParentIdContant.BASE_CODE_NATURE);
        List cadreIdArray = new ArrayList();

        for (int i = 0; i < nature.size(); i++) {
            if (nature.get(i).get("special_mark").equals(ParentIdContant.BASE_CODE_TYPE1_RETIRE) && nature.get(i).get("name").contains("干部")) {
                cadreIdArray.add(nature.get(i).get("value"));
            }
        }
        //1、查数据字典_干部ID列表
        //2、查数据字典_待遇ID列表
        Map<String, String> treatmentParam = new HashMap<String, String>();
        treatmentParam.put("parent_id", ParentIdContant.BASE_CODE_NOW_TREATMENT_LEVEL);
        List<LinkedHashMap<String, String>> treatmentIdArray = dataMap.get(ParentIdContant.BASE_CODE_NOW_TREATMENT_LEVEL);
        List treatmentIdArrays = new ArrayList();
        for (int i = 0; i < treatmentIdArray.size(); i++) {
            treatmentIdArrays.add(treatmentIdArray.get(i).get("value"));
        }
        //3、查统计结果
        param.put("cadreIdArray", cadreIdArray);
        param.put("treatmentIdArray", treatmentIdArrays);
        return new ResultVO(this.retireUserService.listRetireCadreTreatment(param), true, "查询成功");

    }

    /**
     * @description 导出国网统计报表_退休干部享受待遇统计_Excel文件
     */
    @RequestMapping(value = "/excelRetireCadreTreatment", method = RequestMethod.GET)
    public void excelRetireCadreTreatment(HttpServletResponse response, @RequestParam Map<String, Object> param) {
//    	//1、查数据字典_干部ID列表
//    	Map<String,String> cadreParam = new HashMap<String, String>();
//    	cadreParam.put("parent_id", ParentIdContant.BASE_CODE_NATURE);
//    	cadreParam.put("special_mark", ParentIdContant.BASE_CODE_TYPE1_RETIRE);
//    	cadreParam.put("is_available", "1");
//    	cadreParam.put("keyword", "code_name");
//    	cadreParam.put("code_name", "干部");
//    	ResultVO cadreIdArrayVO = this.feignZuulServer.getCode(codeParam);//更改
//    	@SuppressWarnings("unchecked")
//		List<String> cadreIdArray = (List<String>)cadreIdArrayVO.getData();
//    	//3、查数据字典_待遇ID列表
//    	Map<String,String> treatmentParam = new HashMap<String, String>();
//    	treatmentParam.put("code", ParentIdContant.BASE_CODE_NOW_TREATMENT_LEVEL);
//    	treatmentParam.put("is_available", "1");
//    	ResultVO treatmentVO = this.feignZuulServer.getCode(treatmentParam);
//    	@SuppressWarnings("unchecked")
//		List<LinkedHashMap<String,String>> treatmentList = (List<LinkedHashMap<String,String>>)treatmentVO.getData();

        //1、查询数据字典数据
        Map<String, String> codeParam = new HashMap<String, String>();
        codeParam.put("code", ParentIdContant.BASE_CODE_NATURE + "," + ParentIdContant.BASE_CODE_NOW_TREATMENT_LEVEL);
        ResultVO resultVO = this.feignZuulServer.getCode(codeParam);
        @SuppressWarnings("unchecked")
        Map<String, List<LinkedHashMap<String, String>>> dataMap = (Map<String, List<LinkedHashMap<String, String>>>) resultVO.getData();
        //2、发挥作用情况类别
        List<LinkedHashMap<String, String>> playNatureList = dataMap.get(ParentIdContant.BASE_CODE_NATURE);
        List<String> cadreIdArray = new ArrayList<>();
        for (LinkedHashMap<String, String> cadre : playNatureList) {
            if (ParentIdContant.BASE_CODE_TYPE1_RETIRE.equals(cadre.get("special_mark")) && cadre.get("name").contains("干部")) {
                cadreIdArray.add(cadre.get("value"));
            }
        }
        //3、查数据字典_待遇ID列表
        List<LinkedHashMap<String, String>> treatmentList = dataMap.get(ParentIdContant.BASE_CODE_NOW_TREATMENT_LEVEL);
        List<String> treatmentArray = new ArrayList<>();
        for (LinkedHashMap<String, String> role : treatmentList) {
            if (!"0".equals(role.get("special_mark"))) {
                treatmentArray.add(role.get("value"));
            }
        }
        ExcelVO excelVO = new ExcelVO();

        excelVO.setSheetName("退休干部享受待遇统计");
        int[] columnWidthArr = new int[3 + treatmentList.size()];
        columnWidthArr[0] = 30;
        for (int i = 1; i < columnWidthArr.length; i++) {
            columnWidthArr[i] = 12;
        }
        String[] mergedRegionArr = {"0,0,0," + (columnWidthArr.length - 1), "1,2,0,0", "1,2,1,1", "1,2,2,2", "1,1,3," + (columnWidthArr.length - 1)};
        excelVO.setMergedRegionArr(mergedRegionArr);
        excelVO.setTitleName("退休干部享受待遇情况统计表");
        float[] rowHeightArr = {40f, 15f, 15f};
        excelVO.setRowHeight(rowHeightArr);
        String[] tableHeadArr1 = new String[3 + treatmentList.size()];
        tableHeadArr1[0] = "项目";
        tableHeadArr1[1] = "编号";
        tableHeadArr1[2] = "总数";
        tableHeadArr1[3] = "享受待遇情况";
        //合并单元格不输出空格，会没有边框线条
        for (int i = 4; i < columnWidthArr.length; i++) {
            tableHeadArr1[i] = "";
        }
        String[] tableHeadArr2 = new String[3 + treatmentList.size()];
        tableHeadArr2[0] = "";
        tableHeadArr2[1] = "";
        tableHeadArr2[2] = "";
        List<String> treatmentIdArray = new ArrayList<String>();
        String[] dataFieldArr = new String[3 + treatmentList.size()];
        dataFieldArr[0] = "DEPARTMENT_NAME";
        dataFieldArr[1] = "SEQUENCE";
        dataFieldArr[2] = "TOTAL";
        for (int i = 0; i < treatmentList.size(); i++) {
            LinkedHashMap<String, String> codeDO = treatmentList.get(i);
            treatmentIdArray.add(codeDO.get("id"));
            tableHeadArr2[i + 3] = codeDO.get("code_val");
            dataFieldArr[i + 3] = "TREATMENT_" + codeDO.get("id");
        }
        List<String[]> tableHeadList = new ArrayList<String[]>();
        tableHeadList.add(tableHeadArr1);
        tableHeadList.add(tableHeadArr2);
        excelVO.setTableHeadList(tableHeadList);
        excelVO.setColumnWidthArr(columnWidthArr);
        excelVO.setDataFieldArr(dataFieldArr);
        excelVO.setFileName("退休干部享受待遇情况统计表");
        //3、查统计结果
        param.put("cadreIdArray", cadreIdArray);
        param.put("treatmentArray", treatmentArray);
        param.put("treatmentIdArray", treatmentIdArray);
        List<Map<String, String>> dataList = this.retireUserService.listRetireCadreTreatment(param);
        POIExcelUtils.exportExcel(response, excelVO, dataList);
    }

    /**
     * @description 查询国网统计报表_退休干部两年数字变化
     */
    @RequestMapping(value = "/listRetireCadreBiennialChange", method = RequestMethod.POST)
    public ResultVO listRetireCadreBiennialChange(@RequestParam Map<String, Object> param) {
//    	//1、查数据字典_干部ID列表
//    	Map<String,String> cadreParam = new HashMap<String, String>();
//    	cadreParam.put("parent_id", ParentIdContant.BASE_CODE_NATURE);
//    	cadreParam.put("special_mark", ParentIdContant.BASE_CODE_TYPE1_RETIRE);
//    	cadreParam.put("is_available", "1");
//    	cadreParam.put("keyword", "code_name");
//    	cadreParam.put("code_name", "干部");
//        ResultVO cadreIdArrayVO = this.feignZuulServer.getCode(cadreParam);//更改
//    	@SuppressWarnings("unchecked")
//		List<String>  cadreIdArray = (List<String>)cadreIdArrayVO.getData();

        //1、查询数据字典数据
        Map<String, String> codeParam = new HashMap<String, String>();
        codeParam.put("code", ParentIdContant.BASE_CODE_NATURE);
        ResultVO resultVO = this.feignZuulServer.getCode(codeParam);
        @SuppressWarnings("unchecked")
        Map<String, List<LinkedHashMap<String, String>>> dataMap = (Map<String, List<LinkedHashMap<String, String>>>) resultVO.getData();
        List<LinkedHashMap<String, String>> cadreIdAllList = dataMap.get(ParentIdContant.BASE_CODE_NATURE);
        List<String> cadreIdArray = new ArrayList<>();
        for (LinkedHashMap<String, String> cadre : cadreIdAllList) {
            if (ParentIdContant.BASE_CODE_TYPE1_RETIRE.equals(cadre.get("special_mark")) && cadre.get("name").contains("干部")) {
                cadreIdArray.add(cadre.get("value"));
            }
        }
        param.put("cadreIdArray", cadreIdArray);
        param.put("BASE_CODE_POLITICS_STATUS_COMMUNIST", ParentIdContant.BASE_CODE_POLITICS_STATUS_COMMUNIST);
        param.put("founding_state", "1");
        return new ResultVO(this.retireUserService.listRetireCadreBiennialChange(param), true, "查询成功");
    }

    /**
     * @description 导出国网统计报表_退休干部两年数字变化_Excel文件
     */
    @RequestMapping(value = "/excelRetireCadreBiennialChange", method = RequestMethod.GET)
    public void excelRetireCadreBiennialChange(HttpServletResponse response, @RequestParam Map<String, Object> param) {
        ExcelVO excelVO = new ExcelVO();
        excelVO.setSheetName("退休干部两年数字变化");
        int[] columnWidthArr = {30, 8, 15, 15, 15, 15, 15, 10, 10, 20};
        String[] mergedRegionArr = {"0,0,0," + (columnWidthArr.length - 1), "1,2,0,0", "1,2,1,1", "1,2,2,2", "1,2,3,3", "1,2,4,4", "1,2,5,5", "1,2,6,6",
                "1,1,7," + +(columnWidthArr.length - 1)};
        excelVO.setMergedRegionArr(mergedRegionArr);
        excelVO.setTitleName("退休干部两年数字变化情况统计表");
        float[] rowHeightArr = {40f, 15f, 15f};
        excelVO.setRowHeight(rowHeightArr);
        String[] tableHeadArr1 = {"项目", "编号", "上年12月底\012退休干部总数", "本年度内办理\012退休手续干部数", "本年度内\012去世退休干部数",
                "本年12月底\012应有退休干部数", "本年12月底\012实有退休干部数", "其中", "", ""};  //合并单元格不输出空格，会没有边框线条
        String[] tableHeadArr2 = {"", "", "", "", "", "", "", "在京", "中共党员", "建国前参加革命工作"};
        List<String[]> tableHeadList = new ArrayList<String[]>();
        tableHeadList.add(tableHeadArr1);
        tableHeadList.add(tableHeadArr2);
        excelVO.setTableHeadList(tableHeadList);
        excelVO.setColumnWidthArr(columnWidthArr);
        String[] dataFieldArr = {"DEPARTMENT_NAME", "SEQUENCE", "PREVIOUS_YEAR_CADRE", "THIS_YEAR_RETIRE", "THIS_YEAR_DECEASED",
                "THIS_YEAR_DESERVED", "THIS_YEAR_EXISTENCE", "IN_BEIJING", "COMMUNIST", "FOUNDING_STATE_WORK"};
        excelVO.setDataFieldArr(dataFieldArr);
        excelVO.setFileName("退休干部两年数字变化情况统计表");
//    	//1、查数据字典_干部ID列表
//    	Map<String,String> cadreParam = new HashMap<String, String>();
//    	cadreParam.put("parent_id", ParentIdContant.BASE_CODE_NATURE);
//    	cadreParam.put("special_mark", ParentIdContant.BASE_CODE_TYPE1_RETIRE);
//    	cadreParam.put("is_available", "1");
//    	cadreParam.put("keyword", "code_name");
//    	cadreParam.put("code_name", "干部");
//    	ResultVO cadreIdArrayVO = this.feignZuulServer.getCode(codeParam);//更改
//    	@SuppressWarnings("unchecked")
//		List<String> cadreIdArray = (List<String>)cadreIdArrayVO.getData();

        //1、查询数据字典数据
        Map<String, String> codeParam = new HashMap<String, String>();
        codeParam.put("code", ParentIdContant.BASE_CODE_NATURE);
        ResultVO resultVO = this.feignZuulServer.getCode(codeParam);
        @SuppressWarnings("unchecked")
        Map<String, List<LinkedHashMap<String, String>>> dataMap = (Map<String, List<LinkedHashMap<String, String>>>) resultVO.getData();
        List<LinkedHashMap<String, String>> cadreIdAllList = dataMap.get(ParentIdContant.BASE_CODE_NATURE);
        List<String> cadreIdArray = new ArrayList<>();
        for (LinkedHashMap<String, String> cadre : cadreIdAllList) {
            if (ParentIdContant.BASE_CODE_TYPE1_RETIRE.equals(cadre.get("special_mark")) && cadre.get("name").contains("干部")) {
                cadreIdArray.add(cadre.get("value"));
            }
        }
        param.put("cadreIdArray", cadreIdArray);
        param.put("BASE_CODE_POLITICS_STATUS_COMMUNIST", ParentIdContant.BASE_CODE_POLITICS_STATUS_COMMUNIST);
        param.put("founding_state", "1");
        List<Map<String, String>> dataList = this.retireUserService.listRetireCadreBiennialChange(param);
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, String> map = dataList.get(i);
            int deserved = Integer.valueOf(map.get("PREVIOUS_YEAR_CADRE")) + Integer.valueOf(map.get("THIS_YEAR_RETIRE")) - Integer.valueOf(map.get("THIS_YEAR_DECEASED"));
            map.put("THIS_YEAR_DESERVED", String.valueOf(deserved));
            map.put("THIS_YEAR_EXISTENCE", String.valueOf(deserved));
        }
        POIExcelUtils.exportExcel(response, excelVO, dataList);
    }

    /**
     * @description 查询国网统计报表_退休工人两年数字变化
     */
    @RequestMapping(value = "/listRetireWorkerBiennialChange", method = RequestMethod.POST)
    public ResultVO listRetireWorkerBiennialChange(@RequestParam Map<String, Object> param) {
        Map<String, String> cadreParam = new HashMap<String, String>();
        //1、查数据字典_工人ID列表
        cadreParam.put("code", ParentIdContant.BASE_CODE_NATURE);
        ResultVO code = this.feignZuulServer.getCode(cadreParam);
        cadreParam.put("parent_id", ParentIdContant.BASE_CODE_NATURE);
        cadreParam.put("special_mark", ParentIdContant.BASE_CODE_TYPE1_RETIRE);
        cadreParam.put("is_available", "1");
        cadreParam.put("keyword", "code_name");
        cadreParam.put("code_name", "工人");

        ResultVO cadreIdArrayVO = this.feignZuulServer.getCode(cadreParam);
        @SuppressWarnings("unchecked")
        Map<String, List<LinkedHashMap<String, String>>> dataMap = (Map<String, List<LinkedHashMap<String, String>>>) cadreIdArrayVO.getData();
        List<LinkedHashMap<String, String>> cadreIds = dataMap.get(ParentIdContant.BASE_CODE_NATURE);
        List cadreIdArray = new ArrayList();

        for (int i = 0; i < cadreIds.size(); i++) {
            if (cadreIds.get(i).get("name").contains("工人") && ParentIdContant.BASE_CODE_TYPE1_RETIRE.equals(cadreIds.get(i).get("special_mark"))) {
                cadreIdArray.add(cadreIds.get(i).get("value"));
            }
        }
        param.put("cadreIdArray", cadreIdArray);
        param.put("BASE_CODE_POLITICS_STATUS_COMMUNIST", ParentIdContant.BASE_CODE_POLITICS_STATUS_COMMUNIST);
        param.put("founding_state", "1");
        return new ResultVO(this.retireUserService.listRetireCadreBiennialChange(param), true, "查询成功");
    }

    /**
     * @description 导出国网统计报表_退休工人两年数字变化_Excel文件
     */
    @RequestMapping(value = "/excelRetireWorkerBiennialChange", method = RequestMethod.GET)
    public void excelRetireWorkerBiennialChange(HttpServletResponse response, @RequestParam Map<String, Object> param) {
        ExcelVO excelVO = new ExcelVO();
        excelVO.setSheetName("退休工人两年数字变化");
        int[] columnWidthArr = {30, 8, 15, 15, 15, 15, 15, 10, 10, 20};
        String[] mergedRegionArr = {"0,0,0," + (columnWidthArr.length - 1), "1,2,0,0", "1,2,1,1", "1,2,2,2", "1,2,3,3", "1,2,4,4", "1,2,5,5", "1,2,6,6",
                "1,1,7," + +(columnWidthArr.length - 1)};
        excelVO.setMergedRegionArr(mergedRegionArr);
        excelVO.setTitleName("退休工人两年数字变化情况统计表");
        float[] rowHeightArr = {40f, 15f, 15f};
        excelVO.setRowHeight(rowHeightArr);
        String[] tableHeadArr1 = {"项目", "编号", "上年12月底\012退休工人总数", "本年度内办理\012退休手续工人数", "本年度内\012去世退休工人数",
                "本年12月底\012应有退休工人数", "本年12月底\012实有退休工人数", "其中", "", ""};  //合并单元格不输出空格，会没有边框线条
        String[] tableHeadArr2 = {"", "", "", "", "", "", "", "在京", "中共党员", "建国前参加革命工作"};
        List<String[]> tableHeadList = new ArrayList<String[]>();
        tableHeadList.add(tableHeadArr1);
        tableHeadList.add(tableHeadArr2);
        excelVO.setTableHeadList(tableHeadList);
        excelVO.setColumnWidthArr(columnWidthArr);
        String[] dataFieldArr = {"DEPARTMENT_NAME", "SEQUENCE", "PREVIOUS_YEAR_CADRE", "THIS_YEAR_RETIRE", "THIS_YEAR_DECEASED",
                "THIS_YEAR_DESERVED", "THIS_YEAR_EXISTENCE", "IN_BEIJING", "COMMUNIST", "FOUNDING_STATE_WORK"};
        excelVO.setDataFieldArr(dataFieldArr);
        excelVO.setFileName("退休工人两年数字变化情况统计表");
//    	//1、查数据字典_工人ID列表
//    	Map<String,String> cadreParam = new HashMap<String, String>();
//    	cadreParam.put("parent_id", ParentIdContant.BASE_CODE_NATURE);
//    	cadreParam.put("special_mark", ParentIdContant.BASE_CODE_TYPE1_RETIRE);
//    	cadreParam.put("is_available", "1");
//    	cadreParam.put("keyword", "code_name");
//    	cadreParam.put("code_name", "工人");
//    	ResultVO cadreIdArrayVO = this.feignZuulServer.getCode(cadreParam);//更改
//    	@SuppressWarnings("unchecked")
//		List<String> cadreIdArray = (List<String>)cadreIdArrayVO.getData();

        //1、查询数据字典数据
        Map<String, String> codeParam = new HashMap<String, String>();
        codeParam.put("code", ParentIdContant.BASE_CODE_NATURE);
        ResultVO resultVO = this.feignZuulServer.getCode(codeParam);
        @SuppressWarnings("unchecked")
        Map<String, List<LinkedHashMap<String, String>>> dataMap = (Map<String, List<LinkedHashMap<String, String>>>) resultVO.getData();
        List<LinkedHashMap<String, String>> cadreIdAllList = dataMap.get(ParentIdContant.BASE_CODE_NATURE);
        List<String> cadreIdArray = new ArrayList<>();
        for (LinkedHashMap<String, String> cadre : cadreIdAllList) {
            if (ParentIdContant.BASE_CODE_TYPE1_RETIRE.equals(cadre.get("special_mark")) && cadre.get("name").contains("工人")) {
                cadreIdArray.add(cadre.get("value"));
            }
        }
        param.put("cadreIdArray", cadreIdArray);
        param.put("BASE_CODE_POLITICS_STATUS_COMMUNIST", ParentIdContant.BASE_CODE_POLITICS_STATUS_COMMUNIST);
        param.put("founding_state", "1");
        List<Map<String, String>> dataList = this.retireUserService.listRetireCadreBiennialChange(param);
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, String> map = dataList.get(i);
            int deserved = Integer.valueOf(map.get("PREVIOUS_YEAR_CADRE")) + Integer.valueOf(map.get("THIS_YEAR_RETIRE")) - Integer.valueOf(map.get("THIS_YEAR_DECEASED"));
            map.put("THIS_YEAR_DESERVED", String.valueOf(deserved));
            map.put("THIS_YEAR_EXISTENCE", String.valueOf(deserved));
        }
        POIExcelUtils.exportExcel(response, excelVO, dataList);
    }

    /**
     * @description 查询国网统计报表_退职人员两年数字变化
     */
    @RequestMapping(value = "/listQuitWorkingBiennialChange", method = RequestMethod.POST)
    public ResultVO listQuitWorkingBiennialChange(@RequestParam Map<String, Object> param) {
        Map<String,String> cadreParam = new HashMap<String, String>();
        //1、查数据字典_退职ID列表
       cadreParam.put("code",ParentIdContant.BASE_CODE_NATURE);
      //  cadreParam.put("parent_id", ParentIdContant.BASE_CODE_NATURE);
      //  cadreParam.put("special_mark", ParentIdContant.BASE_CODE_TYPE1_RETIRE);
      //  cadreParam.put("is_available", "1");
      //  cadreParam.put("code_name", "退职");
        ResultVO cadreIdArrayVO = this.feignZuulServer.getCode(cadreParam);
        @SuppressWarnings("unchecked")
        Map<String,List<LinkedHashMap<String,String>>> dataMap = (Map<String, List<LinkedHashMap<String, String>>>) cadreIdArrayVO.getData();
        List<LinkedHashMap<String, String>> cadreIds = dataMap.get(ParentIdContant.BASE_CODE_NATURE);
        List cadreIdArray=new ArrayList();
        for (int i = 0; i < cadreIds.size(); i++) {
            if (ParentIdContant.BASE_CODE_TYPE1_RETIRE.equals(cadreIds.get(i).get("special_mark")) && cadreIds.get(i).get("name").contains("退职")){
                cadreIdArray.add(cadreIds.get(i).get("value"));
            }
        }
        param.put("cadreIdArray", cadreIdArray);
        param.put("BASE_CODE_POLITICS_STATUS_COMMUNIST", ParentIdContant.BASE_CODE_POLITICS_STATUS_COMMUNIST);
        return new ResultVO(this.retireUserService.listRetireCadreBiennialChange(param), true, "查询成功");
    }

    /**
     * @description 导出国网统计报表_退职人员两年数字变化_Excel文件
     */
    @RequestMapping(value = "/excelQuitWorkingBiennialChange", method = RequestMethod.GET)
    public void excelQuitWorkingBiennialChange(HttpServletResponse response, @RequestParam Map<String, Object> param) {
        ExcelVO excelVO = new ExcelVO();
        excelVO.setSheetName("退职人员两年数字变化");
        int[] columnWidthArr = {30, 8, 15, 15, 15, 15, 15, 10, 10};
        String[] mergedRegionArr = {"0,0,0," + (columnWidthArr.length - 1), "1,2,0,0", "1,2,1,1", "1,2,2,2", "1,2,3,3", "1,2,4,4", "1,2,5,5", "1,2,6,6",
                "1,1,7," + +(columnWidthArr.length - 1)};
        excelVO.setMergedRegionArr(mergedRegionArr);
        excelVO.setTitleName("退职人员两年数字变化情况统计表");
        float[] rowHeightArr = {40f, 15f, 15f};
        excelVO.setRowHeight(rowHeightArr);
        String[] tableHeadArr1 = {"项目", "编号", "上年12月底\012退职人员总数", "本年度内办理\012退职手续人员数", "本年度内\012去世退职人员数",
                "本年12月底\012应有退职人员数", "本年12月底\012实有退职人员数", "其中", ""};  //合并单元格不输出空格，会没有边框线条
        String[] tableHeadArr2 = {"", "", "", "", "", "", "", "在京", "中共党员"};
        List<String[]> tableHeadList = new ArrayList<String[]>();
        tableHeadList.add(tableHeadArr1);
        tableHeadList.add(tableHeadArr2);
        excelVO.setTableHeadList(tableHeadList);
        excelVO.setColumnWidthArr(columnWidthArr);
        String[] dataFieldArr = {"DEPARTMENT_NAME", "SEQUENCE", "PREVIOUS_YEAR_CADRE", "THIS_YEAR_RETIRE", "THIS_YEAR_DECEASED",
                "THIS_YEAR_DESERVED", "THIS_YEAR_EXISTENCE", "IN_BEIJING", "COMMUNIST"};
        excelVO.setDataFieldArr(dataFieldArr);
        excelVO.setFileName("退职人员两年数字变化情况统计表");

//    	//1、查数据字典_退职ID列表
//    	Map<String,String> cadreParam = new HashMap<String, String>();
//    	cadreParam.put("parent_id", ParentIdContant.BASE_CODE_NATURE);
//    	cadreParam.put("special_mark", ParentIdContant.BASE_CODE_TYPE1_RETIRE);
//    	cadreParam.put("is_available", "1");
//    	cadreParam.put("keyword", "code_name");
//    	cadreParam.put("code_name", "退职");
//    	ResultVO cadreIdArrayVO = this.feignZuulServer.getCode(cadreParam);//更改
//    	@SuppressWarnings("unchecked")
//		List<String> cadreIdArray = (List<String>)cadreIdArrayVO.getData();

        //1、查询数据字典数据
        Map<String, String> codeParam = new HashMap<String, String>();
        codeParam.put("code", ParentIdContant.BASE_CODE_NATURE);
        ResultVO resultVO = this.feignZuulServer.getCode(codeParam);
        @SuppressWarnings("unchecked")
        Map<String, List<LinkedHashMap<String, String>>> dataMap = (Map<String, List<LinkedHashMap<String, String>>>) resultVO.getData();
        List<LinkedHashMap<String, String>> cadreIdAllList = dataMap.get(ParentIdContant.BASE_CODE_NATURE);
        List<String> cadreIdArray = new ArrayList<>();
        for (LinkedHashMap<String, String> cadre : cadreIdAllList) {
            if (ParentIdContant.BASE_CODE_TYPE1_RETIRE.equals(cadre.get("special_mark")) && cadre.get("name").contains("退职")) {
                cadreIdArray.add(cadre.get("value"));
            }
        }
        param.put("cadreIdArray", cadreIdArray);
        param.put("BASE_CODE_POLITICS_STATUS_COMMUNIST", ParentIdContant.BASE_CODE_POLITICS_STATUS_COMMUNIST);
        List<Map<String, String>> dataList = this.retireUserService.listRetireCadreBiennialChange(param);
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, String> map = dataList.get(i);
            int deserved = Integer.valueOf(map.get("PREVIOUS_YEAR_CADRE")) + Integer.valueOf(map.get("THIS_YEAR_RETIRE")) - Integer.valueOf(map.get("THIS_YEAR_DECEASED"));
            map.put("THIS_YEAR_DESERVED", String.valueOf(deserved));
            map.put("THIS_YEAR_EXISTENCE", String.valueOf(deserved));
        }
        POIExcelUtils.exportExcel(response, excelVO, dataList);
    }

    /**
     * @description 查询国网统计报表_离休干部管理现状统计
     */
    @RequestMapping(value = "/listQuitCadreCurrentSituation", method = RequestMethod.POST)
    public ResultVO listQuitCadreCurrentSituation(@RequestParam Map<String, Object> param) {
        //1、查发挥作用情况类别
        Map<String, String> codeParam = new HashMap<String, String>();
        codeParam.put("code", ParentIdContant.BASE_CODE_PLAY_A_ROLE);
        //   codeParam.put("parent_id", ParentIdContant.BASE_CODE_PLAY_A_ROLE);
        //   codeParam.put("is_available", "1");
        codeParam.put("unequal_special_mark", "0");
        ResultVO playARoleVO = this.feignZuulServer.getCode(codeParam);
        @SuppressWarnings("unchecked")
        Map<String, List<LinkedHashMap<String, String>>> dataMap = (Map<String, List<LinkedHashMap<String, String>>>) playARoleVO.getData();
        List<LinkedHashMap<String, String>> playARoles = dataMap.get(ParentIdContant.BASE_CODE_PLAY_A_ROLE);
        List playARoleList = new ArrayList();
        for (int i = 0; i < playARoles.size(); i++) {
            if (!"0".equals(playARoles.get(i).get("special_mark"))) {
                playARoleList.add(playARoles.get(i).get("value"));
            }
        }
        //2、查列表数据
        param.put("playARoleList", playARoleList);
        List<String> cadreIdArray = new ArrayList<>();
        cadreIdArray.add(ParentIdContant.BASE_CODE_NATURE_QUIT);
        //Integer[] cadreIdArray = {Integer.valueOf(ParentIdContant.BASE_CODE_NATURE_QUIT)};
        param.put("cadreIdArray", cadreIdArray);
        param.put("party_located_id", ParentIdContant.BASE_CODE_PARTY_LOCATED_PLACE);
        return new ResultVO(this.retireUserService.listQuitCadreCurrentSituation(param), true, "查询成功");
    }

    /**
     * @description 导出国网统计报表_离休干部管理现状统计_Excel文件
     */
    @RequestMapping(value = "/excelQuitCadreCurrentSituation", method = RequestMethod.GET)
    public void excelQuitCadreCurrentSituation(HttpServletResponse response, @RequestParam Map<String, Object> param) {
        //1、查发挥作用情况类别
        Map<String, String> codeParam = new HashMap<String, String>();
        codeParam.put("kd_code", ParentIdContant.BASE_CODE_PLAY_A_ROLE);
        codeParam.put("FL_AVAILABLE", "1");
        codeParam.put("unequal_special_mark", "0");
        ResultVO playARoleVO = this.feignZuulServer.getCode(codeParam);
        @SuppressWarnings("unchecked")
        List<LinkedHashMap<String, String>> playARoleList = (List<LinkedHashMap<String, String>>) playARoleVO.getData();


        //2、导出Excel
        ExcelVO excelVO = new ExcelVO();
        excelVO.setSheetName("离休干部管理现状统计");
        int[] columnWidthArr = new int[playARoleList.size() + 8];
        columnWidthArr[0] = 30;
        columnWidthArr[1] = 8;
        columnWidthArr[2] = 8;
        columnWidthArr[3] = 11;
        columnWidthArr[4] = 11;
        columnWidthArr[5] = 11;
        columnWidthArr[6] = 11;
        columnWidthArr[7] = 8;
        String[] tableHeadArr1 = new String[playARoleList.size() + 8];
        tableHeadArr1[0] = "项目";
        tableHeadArr1[1] = "编号";
        tableHeadArr1[2] = "总数";
        tableHeadArr1[3] = "基本管理情况";
        tableHeadArr1[4] = "";
        tableHeadArr1[5] = "";
        tableHeadArr1[6] = "";
        tableHeadArr1[7] = "发挥作用情况";
        String[] tableHeadArr2 = new String[playARoleList.size() + 8];
        tableHeadArr2[0] = "";
        tableHeadArr2[1] = "";
        tableHeadArr2[2] = "";
        tableHeadArr2[3] = "养老金社\012会化发放";
        tableHeadArr2[4] = "参加属地\012离休干部\012医疗保险";
        tableHeadArr2[5] = "党组织关\012系移交地\012方管理";
        tableHeadArr2[6] = "人事档案\012移交地方\012管理";
        tableHeadArr2[7] = "小计";
        tableHeadArr2[8] = "其中";
        String[] tableHeadArr3 = new String[playARoleList.size() + 8];
        for (int i = 0; i <= 8; i++) {
            tableHeadArr3[i] = "";
        }
        String[] dataFieldArr = new String[playARoleList.size() + 8];
        dataFieldArr[0] = "DEPARTMENT_NAME";
        dataFieldArr[1] = "SEQUENCE";
        dataFieldArr[2] = "TOTAL";
        dataFieldArr[3] = "SOCIALIZED_PENSION";
        dataFieldArr[4] = "MEDICAL_INSURANCE";
        dataFieldArr[5] = "PARTY_TRANSFER_LOCAL";
        dataFieldArr[6] = "ARCHIVES_TRANSFER_LOCAL";
        dataFieldArr[7] = "SUBTOTAL";
        for (int i = 0; i < playARoleList.size(); i++) {
            LinkedHashMap<String, String> code = playARoleList.get(i);
            columnWidthArr[i + 8] = 12;
            tableHeadArr1[i + 8] = "";
            if (i != 0) {
                tableHeadArr2[i + 8] = "";
            }
            tableHeadArr3[i + 8] = code.get("code_val");
            dataFieldArr[i + 8] = "PLAY_A_ROLE_" + code.get("id");
        }
        String[] mergedRegionArr = {"0,0,0," + (columnWidthArr.length - 1),
                "1,3,0,0", "1,3,1,1", "1,3,2,2", "1,1,3,6", "1,1,7," + (columnWidthArr.length - 1),
                "2,3,3,3", "2,3,4,4", "2,3,5,5", "2,3,6,6", "2,3,7,7", "2,2,8," + (columnWidthArr.length - 1)};
        excelVO.setMergedRegionArr(mergedRegionArr);
        excelVO.setTitleName("离休干部基本管理现状及发挥作用情况统计表");
        float[] rowHeightArr = {40f, 26f, 15f};
        excelVO.setRowHeight(rowHeightArr);
        List<String[]> tableHeadList = new ArrayList<String[]>();
        tableHeadList.add(tableHeadArr1);
        tableHeadList.add(tableHeadArr2);
        tableHeadList.add(tableHeadArr3);
        excelVO.setTableHeadList(tableHeadList);
        excelVO.setColumnWidthArr(columnWidthArr);
        excelVO.setDataFieldArr(dataFieldArr);
        excelVO.setFileName("离休干部基本管理现状及发挥作用情况统计表");
        //2、查列表数据
        param.put("playARoleList", playARoleList);
        List<Integer> cadreIdArray = new ArrayList<>();
        cadreIdArray.add(Integer.valueOf(ParentIdContant.BASE_CODE_NATURE_QUIT));
        //Integer[] cadreIdArray = {Integer.valueOf(ParentIdContant.BASE_CODE_NATURE_QUIT)};
        param.put("cadreIdArray", cadreIdArray);
        param.put("retire_nature_id", ParentIdContant.BASE_CODE_NATURE_QUIT);
        param.put("party_located_id", ParentIdContant.BASE_CODE_PARTY_LOCATED_PLACE);
        List<Map<String, String>> dataList = this.retireUserService.listQuitCadreCurrentSituation(param);
        POIExcelUtils.exportExcel(response, excelVO, dataList);
    }


    /**
     * @description 查询国网统计报表_退休干部管理现状统计
     */
    @RequestMapping(value = "/listRetireCadreCurrentSituation", method = RequestMethod.POST)
    public ResultVO listRetireCadreCurrentSituation(@RequestParam Map<String, Object> param) {
        Map<String, String> codeParam = new HashMap<String, String>();
        //1、查数据字典_发挥作用情况类别
        codeParam.put("code", ParentIdContant.BASE_CODE_PLAY_A_ROLE + "," + ParentIdContant.BASE_CODE_NATURE);
        // codeParam.put("parent_id", ParentIdContant.BASE_CODE_PLAY_A_ROLE);
        // codeParam.put("is_available", "1");
        codeParam.put("unequal_special_mark", "0");
        ResultVO playARoleVO = this.feignZuulServer.getCode(codeParam);
        @SuppressWarnings("unchecked")
        Map<String, List<LinkedHashMap<String, String>>> dataMap = (Map<String, List<LinkedHashMap<String, String>>>) playARoleVO.getData();
        List<LinkedHashMap<String, String>> playARoles = dataMap.get(ParentIdContant.BASE_CODE_PLAY_A_ROLE);
        List<String> playARoleList = new ArrayList<>();
        for (int i = 0; i < playARoles.size(); i++) {
            if (!"0".equals(playARoles.get(i).get("special_mark"))) {
                playARoleList.add(playARoles.get(i).get("value"));
            }
        }
        List<LinkedHashMap<String, String>> cadreIds = dataMap.get(ParentIdContant.BASE_CODE_NATURE);
        List cadreIdArray = new ArrayList();
        for (int i = 0; i < cadreIds.size(); i++) {
            if (ParentIdContant.BASE_CODE_TYPE1_RETIRE.equals(cadreIds.get(i).get("special_mark")) && cadreIds.get(i).get("name").contains("干部")) {
                cadreIdArray.add(cadreIds.get(i).get("value"));
            }
        }
        param.put("cross_district", "1");
        param.put("cadreIdArray", cadreIdArray);
        //3、查列表数据
        param.put("playARoleList", playARoleList);
        param.put("party_located_id", ParentIdContant.BASE_CODE_PARTY_LOCATED_PLACE);
        return new ResultVO(this.retireUserService.listQuitCadreCurrentSituation(param), true, "查询成功");
    }

    /**
     * @description 导出国网统计报表_退休干部管理现状统计_Excel文件
     */
    @RequestMapping(value = "/excelRetireCadreCurrentSituation", method = RequestMethod.GET)
    public void excelRetireCadreCurrentSituation(HttpServletResponse response, @RequestParam Map<String, Object> param) {
        //1、查发挥作用情况类别
        Map<String, String> codeParam = new HashMap<String, String>();
        codeParam.put("kd_code", ParentIdContant.BASE_CODE_PLAY_A_ROLE);
        codeParam.put("FL_AVAILABLE", "1");
        codeParam.put("unequal_special_mark", "0");
        ResultVO playARoleVO = this.feignZuulServer.getCode(codeParam);
        @SuppressWarnings("unchecked")
        List<LinkedHashMap<String, String>> playARoleList = (List<LinkedHashMap<String, String>>) playARoleVO.getData();
        //2、导出Excel
        ExcelVO excelVO = new ExcelVO();
        excelVO.setSheetName("退休干部管理现状统计");
        int[] columnWidthArr = new int[playARoleList.size() + 9];
        columnWidthArr[0] = 30;
        columnWidthArr[1] = 8;
        columnWidthArr[2] = 8;
        columnWidthArr[3] = 11;
        columnWidthArr[4] = 11;
        columnWidthArr[5] = 11;
        columnWidthArr[6] = 11;
        columnWidthArr[7] = 10;
        columnWidthArr[8] = 8;
        String[] tableHeadArr1 = new String[playARoleList.size() + 9];
        tableHeadArr1[0] = "项目";
        tableHeadArr1[1] = "编号";
        tableHeadArr1[2] = "总数";
        tableHeadArr1[3] = "基本管理情况";
        tableHeadArr1[4] = "";
        tableHeadArr1[5] = "";
        tableHeadArr1[6] = "";
        tableHeadArr1[7] = "";
        tableHeadArr1[8] = "发挥作用情况";
        String[] tableHeadArr2 = new String[playARoleList.size() + 9];
        tableHeadArr2[0] = "";
        tableHeadArr2[1] = "";
        tableHeadArr2[2] = "";
        tableHeadArr2[3] = "养老金社\012会化发放";
        tableHeadArr2[4] = "参加属地\012离休干部\012医疗保险";
        tableHeadArr2[5] = "党组织关\012系移交地\012方管理";
        tableHeadArr2[6] = "人事档案\012移交地方\012管理";
        tableHeadArr2[7] = "异地管理";
        tableHeadArr2[8] = "小计";
        tableHeadArr2[9] = "其中";
        String[] tableHeadArr3 = new String[playARoleList.size() + 9];
        for (int i = 0; i <= 9; i++) {
            tableHeadArr3[i] = "";
        }
        String[] dataFieldArr = new String[playARoleList.size() + 9];
        dataFieldArr[0] = "DEPARTMENT_NAME";
        dataFieldArr[1] = "SEQUENCE";
        dataFieldArr[2] = "TOTAL";
        dataFieldArr[3] = "SOCIALIZED_PENSION";
        dataFieldArr[4] = "MEDICAL_INSURANCE";
        dataFieldArr[5] = "PARTY_TRANSFER_LOCAL";
        dataFieldArr[6] = "ARCHIVES_TRANSFER_LOCAL";
        dataFieldArr[7] = "CROSS_DISTRICT";
        dataFieldArr[8] = "SUBTOTAL";
        for (int i = 0; i < playARoleList.size(); i++) {
            LinkedHashMap<String, String> code = playARoleList.get(i);
            columnWidthArr[i + 9] = 12;
            tableHeadArr1[i + 9] = "";
            if (i != 0) {
                tableHeadArr2[i + 9] = "";
            }
            tableHeadArr3[i + 9] = code.get("code_val");
            dataFieldArr[i + 9] = "PLAY_A_ROLE_" + code.get("id");
        }
        String[] mergedRegionArr = {"0,0,0," + (columnWidthArr.length - 1),
                "1,3,0,0", "1,3,1,1", "1,3,2,2", "1,1,3,7", "1,1,8," + (columnWidthArr.length - 1),
                "2,3,3,3", "2,3,4,4", "2,3,5,5", "2,3,6,6", "2,3,7,7", "2,3,8,8", "2,2,9," + (columnWidthArr.length - 1)};
        excelVO.setMergedRegionArr(mergedRegionArr);
        excelVO.setTitleName("退休干部基本管理现状及发挥作用情况统计表");
        float[] rowHeightArr = {40f, 26f, 15f};
        excelVO.setRowHeight(rowHeightArr);
        List<String[]> tableHeadList = new ArrayList<String[]>();
        tableHeadList.add(tableHeadArr1);
        tableHeadList.add(tableHeadArr2);
        tableHeadList.add(tableHeadArr3);
        excelVO.setTableHeadList(tableHeadList);
        excelVO.setColumnWidthArr(columnWidthArr);
        excelVO.setDataFieldArr(dataFieldArr);
        excelVO.setFileName("退休干部基本管理现状及发挥作用情况统计表");
        //2、查数据字典_干部ID列表
        Map<String, String> cadreParam = new HashMap<String, String>();
        cadreParam.put("kd_code", ParentIdContant.BASE_CODE_NATURE);
        cadreParam.put("special_mark", ParentIdContant.BASE_CODE_TYPE1_RETIRE);
        cadreParam.put("FL_AVAILABLE", "1");
        cadreParam.put("keyword", "code_val");
        cadreParam.put("code_val", "干部");
        param.put("cross_district", "1");


        ResultVO cadreIdArrayVO = this.feignZuulServer.getCode(cadreParam);//更改
        @SuppressWarnings("unchecked")
        List<String> cadreIdArray = (List<String>) cadreIdArrayVO.getData();
        param.put("cadreIdArray", cadreIdArray);
        //3、查列表数据
        param.put("playARoleList", playARoleList);
        param.put("party_located_id", ParentIdContant.BASE_CODE_PARTY_LOCATED_PLACE);
        List<Map<String, String>> dataList = this.retireUserService.listQuitCadreCurrentSituation(param);
        POIExcelUtils.exportExcel(response, excelVO, dataList);
    }

    /**
     * @description 查询国网统计报表_退休工人管理现状统计
     */
    @RequestMapping(value = "/listRetireWorkerCurrentSituation", method = RequestMethod.POST)
    public ResultVO listRetireWorkerCurrentSituation(@RequestParam Map<String, Object> param) {
        Map<String, String> codeParam = new HashMap<String, String>();
        //1、查数据字典_发挥作用情况类别
        codeParam.put("code", ParentIdContant.BASE_CODE_PLAY_A_ROLE + "," + ParentIdContant.BASE_CODE_NATURE);
        //codeParam.put("parent_id", ParentIdContant.BASE_CODE_PLAY_A_ROLE);
        // codeParam.put("is_available", "1");
        // codeParam.put("unequal_special_mark", "0");
        ResultVO playARoleVO = this.feignZuulServer.getCode(codeParam);
        @SuppressWarnings("unchecked")
        Map<String, List<LinkedHashMap<String, String>>> dataMap = (Map<String, List<LinkedHashMap<String, String>>>) playARoleVO.getData();
        List<LinkedHashMap<String, String>> playARoles = dataMap.get(ParentIdContant.BASE_CODE_PLAY_A_ROLE);
        List playARoleList = new ArrayList();
        for (int i = 0; i < playARoles.size(); i++) {
            if (!"0".equals(playARoles.get(i).get("special_mark"))) {
                playARoleList.add(playARoles.get(i).get("value"));
            }
        }
        //2、查数据字典_干部ID列表
        //    Map<String,String> cadreParam = new HashMap<String, String>();
        //   cadreParam.put("parent_id", ParentIdContant.BASE_CODE_NATURE);
        //    cadreParam.put("special_mark", ParentIdContant.BASE_CODE_TYPE1_RETIRE);
        //    cadreParam.put("is_available", "1");
        //   cadreParam.put("code_name", "工人");
        param.put("cross_district", "1");
        List<LinkedHashMap<String, String>> cadreIds = dataMap.get(ParentIdContant.BASE_CODE_NATURE);
        List cadreIdArray = new ArrayList();
        for (int i = 0; i < cadreIds.size(); i++) {
            if (cadreIds.get(i).get("name").contains("工人") && cadreIds.get(i).get("special_mark").equals(ParentIdContant.BASE_CODE_TYPE1_RETIRE)) {
                cadreIdArray.add(cadreIds.get(i).get("value"));
            }
        }
        param.put("cadreIdArray", cadreIdArray);
        //3、查列表数据
        param.put("playARoleList", playARoleList);
        param.put("party_located_id", ParentIdContant.BASE_CODE_PARTY_LOCATED_PLACE);
        return new ResultVO(this.retireUserService.listQuitCadreCurrentSituation(param), true, "查询成功");
    }

    /**
     * @description 导出国网统计报表_退休工人管理现状统计_Excel文件
     */
    @RequestMapping(value = "/excelRetireWorkerCurrentSituation", method = RequestMethod.GET)
    public void excelRetireWorkerCurrentSituation(HttpServletResponse response, @RequestParam Map<String, Object> param) {
        //1、查发挥作用情况类别
        Map<String, String> codeParam = new HashMap<String, String>();
        codeParam.put("kd_code", ParentIdContant.BASE_CODE_PLAY_A_ROLE);
        codeParam.put("FL_AVAILABLE", "1");
        codeParam.put("unequal_special_mark", "0");
        ResultVO playARoleVO = this.feignZuulServer.getCode(codeParam);
        @SuppressWarnings("unchecked")
        List<LinkedHashMap<String, String>> playARoleList = (List<LinkedHashMap<String, String>>) playARoleVO.getData();
        //2、导出Excel
        ExcelVO excelVO = new ExcelVO();
        excelVO.setSheetName("退休工人管理现状统计");
        int[] columnWidthArr = new int[playARoleList.size() + 9];
        columnWidthArr[0] = 30;
        columnWidthArr[1] = 8;
        columnWidthArr[2] = 8;
        columnWidthArr[3] = 11;
        columnWidthArr[4] = 11;
        columnWidthArr[5] = 11;
        columnWidthArr[6] = 11;
        columnWidthArr[7] = 10;
        columnWidthArr[8] = 8;
        String[] tableHeadArr1 = new String[playARoleList.size() + 9];
        tableHeadArr1[0] = "项目";
        tableHeadArr1[1] = "编号";
        tableHeadArr1[2] = "总数";
        tableHeadArr1[3] = "基本管理情况";
        tableHeadArr1[4] = "";
        tableHeadArr1[5] = "";
        tableHeadArr1[6] = "";
        tableHeadArr1[7] = "";
        tableHeadArr1[8] = "发挥作用情况";
        String[] tableHeadArr2 = new String[playARoleList.size() + 9];
        tableHeadArr2[0] = "";
        tableHeadArr2[1] = "";
        tableHeadArr2[2] = "";
        tableHeadArr2[3] = "养老金社\012会化发放";
        tableHeadArr2[4] = "参加属地\012离休干部\012医疗保险";
        tableHeadArr2[5] = "党组织关\012系移交地\012方管理";
        tableHeadArr2[6] = "人事档案\012移交地方\012管理";
        tableHeadArr2[7] = "异地管理";
        tableHeadArr2[8] = "小计";
        tableHeadArr2[9] = "其中";
        String[] tableHeadArr3 = new String[playARoleList.size() + 9];
        for (int i = 0; i <= 9; i++) {
            tableHeadArr3[i] = "";
        }
        String[] dataFieldArr = new String[playARoleList.size() + 9];
        dataFieldArr[0] = "DEPARTMENT_NAME";
        dataFieldArr[1] = "SEQUENCE";
        dataFieldArr[2] = "TOTAL";
        dataFieldArr[3] = "SOCIALIZED_PENSION";
        dataFieldArr[4] = "MEDICAL_INSURANCE";
        dataFieldArr[5] = "PARTY_TRANSFER_LOCAL";
        dataFieldArr[6] = "ARCHIVES_TRANSFER_LOCAL";
        dataFieldArr[7] = "CROSS_DISTRICT";
        dataFieldArr[8] = "SUBTOTAL";
        for (int i = 0; i < playARoleList.size(); i++) {
            LinkedHashMap<String, String> code = playARoleList.get(i);
            columnWidthArr[i + 9] = 12;
            tableHeadArr1[i + 9] = "";
            if (i != 0) {
                tableHeadArr2[i + 9] = "";
            }
            tableHeadArr3[i + 9] = code.get("code_val");
            dataFieldArr[i + 9] = "PLAY_A_ROLE_" + code.get("id");
        }
        String[] mergedRegionArr = {"0,0,0," + (columnWidthArr.length - 1),
                "1,3,0,0", "1,3,1,1", "1,3,2,2", "1,1,3,7", "1,1,8," + (columnWidthArr.length - 1),
                "2,3,3,3", "2,3,4,4", "2,3,5,5", "2,3,6,6", "2,3,7,7", "2,3,8,8", "2,2,9," + (columnWidthArr.length - 1)};
        excelVO.setMergedRegionArr(mergedRegionArr);
        excelVO.setTitleName("退休工人基本管理现状及发挥作用情况统计表");
        float[] rowHeightArr = {40f, 26f, 15f};
        excelVO.setRowHeight(rowHeightArr);
        List<String[]> tableHeadList = new ArrayList<String[]>();
        tableHeadList.add(tableHeadArr1);
        tableHeadList.add(tableHeadArr2);
        tableHeadList.add(tableHeadArr3);
        excelVO.setTableHeadList(tableHeadList);
        excelVO.setColumnWidthArr(columnWidthArr);
        excelVO.setDataFieldArr(dataFieldArr);
        excelVO.setFileName("退休工人基本管理现状及发挥作用情况统计表");
        //2、查数据字典_干部ID列表
        Map<String, String> cadreParam = new HashMap<String, String>();
        cadreParam.put("kd_code", ParentIdContant.BASE_CODE_NATURE);
        cadreParam.put("special_mark", ParentIdContant.BASE_CODE_TYPE1_RETIRE);
        cadreParam.put("FL_AVAILABLE", "1");
        cadreParam.put("keyword", "code_val");
        cadreParam.put("code_val", "工人");
        param.put("cross_district", "1");


        ResultVO cadreIdArrayVO = this.feignZuulServer.getCode(cadreParam);//更改
        @SuppressWarnings("unchecked")
        List<String> cadreIdArray = (List<String>) cadreIdArrayVO.getData();
        param.put("cadreIdArray", cadreIdArray);
        //3、查列表数据
        param.put("playARoleList", playARoleList);
        param.put("party_located_id", ParentIdContant.BASE_CODE_PARTY_LOCATED_PLACE);
        List<Map<String, String>> dataList = this.retireUserService.listQuitCadreCurrentSituation(param);
        POIExcelUtils.exportExcel(response, excelVO, dataList);
    }

    /**
     * @description 查询国网统计报表_退休人员党组织关系统计
     */
    @RequestMapping(value = "/listRetirePartyRelation", method = RequestMethod.POST)
    public ResultVO listRetirePartyRelation(@RequestParam Map<String, Object> param) {
        //1、查数据字典_党组织关系ID串
        Map<String,String> locatedParam = new HashMap<String, String>();
        locatedParam.put("code",ParentIdContant.BASE_CODE_PARTY_LOCATED);
       // locatedParam.put("parent_id", ParentIdContant.BASE_CODE_PARTY_LOCATED);
      //  locatedParam.put("is_available", "1");
        ResultVO locatedIdArrayVO = this.feignZuulServer.getCode(locatedParam);
        @SuppressWarnings("unchecked")
        Map<String,List<LinkedHashMap<String,String>>> dataMap = (Map<String, List<LinkedHashMap<String, String>>>) locatedIdArrayVO.getData();
        List<LinkedHashMap<String, String>> locatedIds = dataMap.get(ParentIdContant.BASE_CODE_PARTY_LOCATED);
        List locatedIdArray=new ArrayList();
        for (int i = 0; i < locatedIds.size(); i++) {
            locatedIdArray.add(locatedIds.get(i).get("value"));
        }
        //2、查统计结果
        param.put("locatedIdArray", locatedIdArray);
        return new ResultVO(this.retireUserService.listRetirePartyRelation(param), true, "查询成功");
    }

    /**
     * @description 导出国网统计报表_退休人员党组织关系统计_Excel文件
     */
    @RequestMapping(value = "/excelRetirePartyRelation", method = RequestMethod.GET)
    public void excelRetirePartyRelation(HttpServletResponse response, @RequestParam Map<String, Object> param) {
        //1、查数据字典_党组织关系列表
        Map<String, String> locatedParam = new HashMap<String, String>();
        locatedParam.put("code", ParentIdContant.BASE_CODE_PARTY_LOCATED);
//    	locatedParam.put("is_available", "1");
        ResultVO locatedVO = this.feignZuulServer.getCode(locatedParam);
        @SuppressWarnings("unchecked")
        List<LinkedHashMap<String, String>> locatedList = (List<LinkedHashMap<String, String>>) locatedVO.getData();
        ExcelVO excelVO = new ExcelVO();
        excelVO.setSheetName("退休人员党组织关系统计");
        int[] columnWidthArr = new int[3 + locatedParam.size()];
        columnWidthArr[0] = 30;
        columnWidthArr[1] = 20;
        String[] mergedRegionArr = {"0,0,0," + (columnWidthArr.length - 1), "1,2,0,0",
                "1,1,1," + (locatedParam.size() + 1), "1,2," + (columnWidthArr.length - 1) + "," + (columnWidthArr.length - 1)};
        excelVO.setMergedRegionArr(mergedRegionArr);
        excelVO.setTitleName("退休人员党组织关系情况表");
        float[] rowHeightArr = {40f, 15f, 15f};
        excelVO.setRowHeight(rowHeightArr);
        String[] tableHeadArr1 = new String[3 + locatedList.size()];
        tableHeadArr1[0] = "企业名称";
        tableHeadArr1[1] = "党组织关系管理";
        String[] tableHeadArr2 = new String[3 + locatedList.size()];
        tableHeadArr2[0] = "";
        tableHeadArr2[1] = "合计";
        List<String> locatedIdArray = new ArrayList<String>();
        String[] dataFieldArr = new String[3 + locatedList.size()];
        dataFieldArr[0] = "DEPARTMENT_NAME";
        dataFieldArr[1] = "TOTAL";
        for (int i = 0; i < locatedList.size(); i++) {
            LinkedHashMap<String, String> codeDO = locatedList.get(i);
            columnWidthArr[i + 2] = 20;
            tableHeadArr1[i + 2] = "";
            tableHeadArr2[i + 2] = codeDO.get("code_val");
            locatedIdArray.add(codeDO.get("id"));
            dataFieldArr[i + 2] = "LOCATED_" + codeDO.get("id");
        }
        columnWidthArr[locatedList.size() + 2] = 20;
        tableHeadArr1[locatedList.size() + 2] = "备注";
        tableHeadArr2[locatedList.size() + 2] = "";
        dataFieldArr[locatedList.size() + 2] = "REMARK";
        List<String[]> tableHeadList = new ArrayList<String[]>();
        tableHeadList.add(tableHeadArr1);
        tableHeadList.add(tableHeadArr2);
        excelVO.setTableHeadList(tableHeadList);
        excelVO.setColumnWidthArr(columnWidthArr);
        excelVO.setDataFieldArr(dataFieldArr);
        excelVO.setFileName("退休人员党组织关系情况表");
        //3、查统计结果
        param.put("locatedIdArray", locatedIdArray);
        List<Map<String, String>> dataList = this.retireUserService.listRetirePartyRelation(param);
        POIExcelUtils.exportExcel(response, excelVO, dataList);
    }

    /**
     * 余昌增
     * 查询退休人员基本情况数据统计
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/listRetireUserBasic", method = RequestMethod.POST)
    public ResultVO listRetireUserBasic(@RequestParam Map<String, Object> param) {
        //1、获取职级数据
        Map<String, String> codeParam = new HashMap<String, String>();
        codeParam.put("code", ParentIdContant.BASE_CODE_RETIREMENT_RANK);
        //  codeParam.put("parent_id", ParentIdContant.BASE_CODE_RETIREMENT_RANK);
        //    codeParam.put("is_available", "1");
        ResultVO listVO = this.feignZuulServer.getCode(codeParam);
        @SuppressWarnings("unchecked")
        Map<String, List<LinkedHashMap<String, String>>> list = (Map<String, List<LinkedHashMap<String, String>>>) listVO.getData();
        List<LinkedHashMap<String, String>> linkedHashMaps = list.get(ParentIdContant.BASE_CODE_RETIREMENT_RANK);
        List list1 = new ArrayList();
        for (int i = 0; i < linkedHashMaps.size(); i++) {
            list1.add(linkedHashMaps.get(i).get("value"));
        }
        param.put("list", list1);
        return new ResultVO(retireUserService.listRetireUserBasic(param), true, "查询成功");
    }

    /**
     * 余昌增
     * 退休人员基本情况数据统计导出
     *
     * @param response
     * @param param
     */
    @RequestMapping(value = "/excelRetireUserBasic", method = RequestMethod.GET)
    public void excelRetireUserBasic(HttpServletResponse response, @RequestParam Map<String, Object> param) {
        //1、获取职级数据
        Map<String, String> codeParam = new HashMap<String, String>();

        codeParam.put("code", ParentIdContant.BASE_CODE_RETIREMENT_RANK);
//    	codeParam.put("is_available", "1");
        ResultVO listVO = this.feignZuulServer.getCode(codeParam);
        @SuppressWarnings("unchecked")
        List<LinkedHashMap<String, String>> list = (List<LinkedHashMap<String, String>>) listVO.getData();
        param.put("list", list);
        //查单位条数，计算表格内容跨行
        Map<String, String> departmentParam = new HashMap<String, String>();
        departmentParam.put("department_id", ParentIdContant.BASE_DEPARTMENT_PROVINCIAL);
        departmentParam.put("kd_code", ParentIdContant.BASE_DEPARTMENT);
        departmentParam.put("FL_AVAILABLE", "1");
        ResultVO departmentVO = this.feignZuulServer.listBaseDepartment(departmentParam);
        @SuppressWarnings("unchecked")
        List<LinkedHashMap<String, String>> departmentList = (List<LinkedHashMap<String, String>>) departmentVO.getData();
        ExcelVO excelVO = new ExcelVO();
        excelVO.setSheetName("退休人员基本情况统计");
        //第一行表头
        String[] tableHeadArr1 = new String[list.size() + 13];
        tableHeadArr1[0] = "企业名称";
        tableHeadArr1[1] = "退休人\012员性质";
        tableHeadArr1[2] = "年龄结构";
        tableHeadArr1[3] = "";
        tableHeadArr1[4] = "";
        tableHeadArr1[5] = "";
        tableHeadArr1[6] = "";
        tableHeadArr1[7] = "";
        tableHeadArr1[8] = "";
        tableHeadArr1[9] = "";
        tableHeadArr1[10] = "";
        tableHeadArr1[11] = "";
        tableHeadArr1[12] = "";
        tableHeadArr1[13] = "职级";
        //第二行表头
        String[] tableHeadArr2 = new String[list.size() + 13];
        tableHeadArr2[0] = "";
        tableHeadArr2[1] = "";
        tableHeadArr2[2] = "总人数";
        tableHeadArr2[3] = "平均年龄";
        tableHeadArr2[4] = "45岁及\012以下";
        tableHeadArr2[5] = "46岁-50岁";
        tableHeadArr2[6] = "51岁-55岁";
        tableHeadArr2[7] = "56岁-60岁";
        tableHeadArr2[8] = "61岁-70岁";
        tableHeadArr2[9] = "71岁-80岁";
        tableHeadArr2[10] = "81岁-90岁";
        tableHeadArr2[11] = "91岁-99岁";
        tableHeadArr2[12] = "100岁\012及以上";
        //对应列表字段信息
        String[] dataFieldArr = new String[list.size() + 13];
        dataFieldArr[0] = "DEPARTMENT_NAME";
        dataFieldArr[1] = "code_val";
        dataFieldArr[2] = "TOTAL_NUM";
        dataFieldArr[3] = "AGE_VAG";
        dataFieldArr[4] = "AGE_45";
        dataFieldArr[5] = "AGE_46_50";
        dataFieldArr[6] = "AGE_51_55";
        dataFieldArr[7] = "AGE_56_60";
        dataFieldArr[8] = "AGE_61_70";
        dataFieldArr[9] = "AGE_71_80";
        dataFieldArr[10] = "AGE_81_90";
        dataFieldArr[11] = "AGE_91_99";
        dataFieldArr[12] = "AGE_100";
        //每列宽度
        int[] columnWidthArr = new int[list.size() + 13];
        columnWidthArr[0] = 30;
        columnWidthArr[1] = 10;
        columnWidthArr[2] = 10;
        columnWidthArr[3] = 10;
        columnWidthArr[4] = 10;
        columnWidthArr[5] = 10;
        columnWidthArr[6] = 10;
        columnWidthArr[7] = 10;
        columnWidthArr[8] = 10;
        columnWidthArr[9] = 10;
        columnWidthArr[10] = 10;
        columnWidthArr[11] = 10;
        columnWidthArr[12] = 10;

        //动态拼接数据
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                tableHeadArr1[i + 13] = "";
            }
            tableHeadArr2[i + 13] = list.get(i).get("code_val");
            dataFieldArr[i + 13] = "RETIREMENT_RANK_ID_" + list.get(i).get("id");
            columnWidthArr[i + 13] = 10;
        }
        excelVO.setTitleName("退休人员基本情况统计");
        float[] rowHeightArr = {40f, 26f};
        //行高
        excelVO.setRowHeight(rowHeightArr);
        //跨度
        String[] mergedRegionArr = new String[5 + departmentList.size()];
        mergedRegionArr[0] = "0,0,0," + (columnWidthArr.length - 1);
        mergedRegionArr[1] = "1,2,0,0";
        mergedRegionArr[2] = "1,2,1,1";
        mergedRegionArr[3] = "1,1,2,12";
        mergedRegionArr[4] = "1,1,13," + (columnWidthArr.length - 1);
        for (int i = 0; i < departmentList.size(); i++) {
            mergedRegionArr[5 + i] = (3 + (i * 4)) + "," + (6 + (i * 4)) + ",0,0";
        }
        excelVO.setMergedRegionArr(mergedRegionArr);
        List<String[]> tableHeadList = new ArrayList<String[]>();
        tableHeadList.add(tableHeadArr1);
        tableHeadList.add(tableHeadArr2);

        excelVO.setTableHeadList(tableHeadList);
        excelVO.setColumnWidthArr(columnWidthArr);
        excelVO.setDataFieldArr(dataFieldArr);
        excelVO.setFileName("退休人员基本情况统计表");

        List<Map<String, String>> dataList = retireUserService.listRetireUserBasic(param);

        POIExcelUtils.exportExcel(response, excelVO, dataList);
    }

    /**
     * @description 查询国网统计报表_退休人员个人信息明细表
     */
    @RequestMapping(value = "/listRetireUserPersonalInformation", method = RequestMethod.POST)
    public ResultVO listRetireUserPersonalInformation(@RequestParam Map<String, Object> param) {
        Map<String, String> receiveParam = new HashMap<String, String>();
        //1、查数据字典_档案接收地
        receiveParam.put("code", ParentIdContant.BASE_CODE_RECEIVE_AREA);
        // receiveParam.put("parent_id",ParentIdContant.BASE_CODE_RECEIVE_AREA );
        //  receiveParam.put("is_available", "1");
        ResultVO locatedVO = this.feignZuulServer.getCode(receiveParam);
        @SuppressWarnings("unchecked")
        Map<String, List<LinkedHashMap<String, String>>> dataMap = (Map<String, List<LinkedHashMap<String, String>>>) locatedVO.getData();
        List<LinkedHashMap<String, String>> locateds = dataMap.get(ParentIdContant.BASE_CODE_RECEIVE_AREA);
        List locatedList = new ArrayList();
        for (int i = 0; i < locateds.size(); i++) {
            locatedList.add(locateds.get(i).get("value"));
        }
        param.put("locatedList", locatedList);
        param.put("retire_type_id", ParentIdContant.BASE_CODE_TYPE1_RETIRE);
        if (StringUtils.isEmpty((String) param.get("pageNum"))) {
            param.put("pageNum", "1");
        }
        if (StringUtils.isEmpty((String) param.get("pageSize"))) {
            param.put("pageSize", "10");
        }
        int pageNum = Integer.parseInt((String) param.get("pageNum"));        //当前页
        int pageSize = Integer.parseInt((String) param.get("pageSize"));    //每页条数
        Page<Object> p = PageHelper.startPage(pageNum, pageSize);
        return new ResultVO(this.retireUserService.listRetireUserPersonalInformation(param), true, "查询成功", p.getTotal(), pageNum, pageSize);

    }

    /**
     * @description 导出国网统计报表_退休人员个人信息明细表_Excel文件
     */
    @RequestMapping(value = "/excelRetireUserPersonalInformation", method = RequestMethod.GET)
    public void excelRetireUserPersonalInformation(HttpServletResponse response, @RequestParam Map<String, Object> param) {
        //1、查数据字典_档案接收地
        Map<String, String> receiveParam = new HashMap<String, String>();
        receiveParam.put("code", ParentIdContant.BASE_CODE_RECEIVE_AREA);
//    	receiveParam.put("is_available", "1");
        ResultVO locatedVO = this.feignZuulServer.getCode(receiveParam);
        @SuppressWarnings("unchecked")
        List<LinkedHashMap<String, String>> locatedList = (List<LinkedHashMap<String, String>>) locatedVO.getData();
        param.put("locatedList", locatedList);
        //2、导出Excel
        ExcelVO excelVO = new ExcelVO();
        excelVO.setSheetName("退休人员个人信息明细表");
        int[] columnWidthArr = new int[32 + locatedList.size()];
        List<String[]> tableHeadList = new ArrayList<String[]>();
        String[] tableHeadArr1 = new String[columnWidthArr.length];
        String[] tableHeadArr2 = new String[columnWidthArr.length];
        String[] tableHeadArr3 = new String[columnWidthArr.length];
        String[] tableHeadArr4 = new String[columnWidthArr.length];
        for (int i = 0; i < 32 + locatedList.size(); i++) {
            columnWidthArr[i] = 15;
            tableHeadArr1[i] = "";
            tableHeadArr2[i] = (i + 1) + "";
            tableHeadArr4[i] = "";
        }
        columnWidthArr[0] = 6;
        columnWidthArr[1] = 8;
        columnWidthArr[2] = 20;
        columnWidthArr[3] = 8;
        columnWidthArr[4] = 8;
        columnWidthArr[5] = 8;
        columnWidthArr[26] = 20;
        columnWidthArr[27] = 20;
        columnWidthArr[34] = 30;
        excelVO.setColumnWidthArr(columnWidthArr);
        String[] mergedRegionArr = {"0,0,0," + (columnWidthArr.length - 1),
                "1,1,0," + (18 + locatedList.size()), "1,1," + (19 + locatedList.size()) + "," + (23 + locatedList.size()),
                "1,1," + (24 + locatedList.size()) + "," + (columnWidthArr.length - 1),
                "3,4,0,0", "3,4,1,1", "3,4,2,2", "3,4,3,3", "3,4,4,4", "3,4,5,5", "3,4,6,6", "3,4,7,7", "3,4,8,8",
                "3,4,9,9", "3,4,10,10", "3,4,11,11", "3,4,12,12", "3,4,13,13", "3,4,14,14", "3,4,15,15", "3,4,16,16", "3,4,17,17",
                "3,3,18," + (17 + locatedList.size()), "3,4," + (18 + locatedList.size()) + "," + (18 + locatedList.size()),
                "3,4," + (19 + locatedList.size()) + "," + (19 + locatedList.size()), "3,4," + (20 + locatedList.size()) + "," + (20 + locatedList.size()), "3,4," + (21 + locatedList.size()) + "," + (21 + locatedList.size()),
                "3,4," + (22 + locatedList.size()) + "," + (22 + locatedList.size()), "3,4," + (23 + locatedList.size()) + "," + (23 + locatedList.size()), "3,4," + (24 + locatedList.size()) + "," + (24 + locatedList.size()),
                "3,4," + (25 + locatedList.size()) + "," + (25 + locatedList.size()), "3,4," + (26 + locatedList.size()) + "," + (26 + locatedList.size()), "3,4," + (27 + locatedList.size()) + "," + (27 + locatedList.size()),
                "3,3," + (28 + locatedList.size()) + "," + (30 + locatedList.size()),
                "3,4," + (31 + locatedList.size()) + "," + (31 + locatedList.size())};
        excelVO.setMergedRegionArr(mergedRegionArr);
        excelVO.setTitleName("退休人员个人信息明细表");
        float[] rowHeightArr = {40f, 20f, 30f};
        excelVO.setRowHeight(rowHeightArr);
        tableHeadArr1[0] = "基础信息";
        tableHeadArr1[(19 + locatedList.size())] = "党组织关系";
        tableHeadArr1[(24 + locatedList.size())] = "其他信息";
        tableHeadList.add(tableHeadArr1);
        tableHeadList.add(tableHeadArr2);
        tableHeadArr3[0] = "序号";
        tableHeadArr3[1] = "姓名";
        tableHeadArr3[2] = "身份证号码";
        tableHeadArr3[3] = "性别";
        tableHeadArr3[4] = "籍贯";
        tableHeadArr3[5] = "民族";
        tableHeadArr3[6] = "出生年月";
        tableHeadArr3[7] = "政治面貌";
        tableHeadArr3[8] = "最高学历";
        tableHeadArr3[9] = "资格职称";
        tableHeadArr3[10] = "参加工作时间";
        tableHeadArr3[11] = "退休年月";
        tableHeadArr3[12] = "退休时单位及岗位";
        tableHeadArr3[13] = "退休时职务（职级）";
        tableHeadArr3[14] = "联系电话";
        tableHeadArr3[15] = "社保关系所在地";
        tableHeadArr3[16] = "户口所在地址";
        tableHeadArr3[17] = "现居住地详细地址";
        tableHeadArr3[18] = "接收地";
        tableHeadArr3[19] = "";
        tableHeadArr3[20] = "";
        tableHeadArr3[21] = "入党时间";
        tableHeadArr3[22] = "党组织关系所在地";
        tableHeadArr3[23] = "党费月缴额(元)";
        tableHeadArr3[24] = "党费缴至年月";
        tableHeadArr3[25] = "婚姻状况";
        tableHeadArr3[26] = "劳模情况标识";
        tableHeadArr3[27] = "特殊人群标示";
        tableHeadArr3[28] = "是否有子女在系统工作";
        tableHeadArr3[29] = "档案存放地";
        tableHeadArr3[30] = "档案册数";
        tableHeadArr3[31] = "其他联系人";
        tableHeadArr3[32] = "";
        tableHeadArr3[33] = "";
        tableHeadArr3[34] = "备注";
        tableHeadList.add(tableHeadArr3);
        String[] dataFieldArr = new String[columnWidthArr.length];
        dataFieldArr[0] = "SEQUENCE";
        dataFieldArr[1] = "USER_NAME";
        dataFieldArr[2] = "IDCARD";
        dataFieldArr[3] = "SEX_ID";
        dataFieldArr[4] = "NATIVE_PLACE";
        dataFieldArr[5] = "NATION_ID";
        dataFieldArr[6] = "BIRTH_DATE";
        dataFieldArr[7] = "POLITICS_STATUS_ID";
        dataFieldArr[8] = "EDUCATION_ID";
        dataFieldArr[9] = "NOW_TREATMENT_LEVEL_ID";
        dataFieldArr[10] = "WORK_TIME";
        dataFieldArr[11] = "RETIRE_TIME";
        dataFieldArr[12] = "RETIREMENT_POST";
        dataFieldArr[13] = "RETIREMENT_DUTY";
        dataFieldArr[14] = "CONTACT_NUMBER";
        dataFieldArr[15] = "SOCIAL_SECURITY_AREA";
        dataFieldArr[16] = "REGISTERED_PERMANENT_RESIDENCE";
        dataFieldArr[17] = "RESIDENCE_ADDRESS";
        for (int i = 0; i < locatedList.size(); i++) {
            LinkedHashMap<String, String> codeDO = locatedList.get(i);
            tableHeadArr4[18 + i] = codeDO.get("code_val");
            dataFieldArr[18 + i] = "AREA_ADDRESS_" + codeDO.get("id");
        }
        tableHeadArr4[28 + locatedList.size()] = "姓名";
        tableHeadArr4[29 + locatedList.size()] = "关系";
        tableHeadArr4[30 + locatedList.size()] = "电话";
        tableHeadList.add(tableHeadArr4);
        excelVO.setTableHeadList(tableHeadList);
        dataFieldArr[18 + locatedList.size()] = "GMT_PARTY";
        dataFieldArr[19 + locatedList.size()] = "MEMBERSHIP_CREDENTIALS_ID";
        dataFieldArr[20 + locatedList.size()] = "PARTY_MEMBERSHIP_DUES";
        dataFieldArr[21 + locatedList.size()] = "GMT_PAID_UNTIL";
        dataFieldArr[22 + locatedList.size()] = "MARITAL_STATUS_ID";
        dataFieldArr[23 + locatedList.size()] = "AWARD_LEVEL_ID";
        dataFieldArr[24 + locatedList.size()] = "SPECIAL_CROWD_IDS";
        dataFieldArr[25 + locatedList.size()] = "CHILD_WORKING_SYS";   //是否有子女在系统工作
        dataFieldArr[26 + locatedList.size()] = "ARCHIVES_AREA_ID";
        dataFieldArr[27 + locatedList.size()] = "ARCHIVES_BOOK_NUMBER";
        dataFieldArr[28 + locatedList.size()] = "SOCIALIZED_PENSION";
        dataFieldArr[29 + locatedList.size()] = "MEDICAL_INSURANCE";
        dataFieldArr[30 + locatedList.size()] = "PARTY_TRANSFER_LOCAL";
        dataFieldArr[31 + locatedList.size()] = "SPECIAL_REMARK";
        excelVO.setDataFieldArr(dataFieldArr);
        excelVO.setFileName("退休人员党组织关系情况表");
        //3、查统计结果
        param.put("retire_type_id", ParentIdContant.BASE_CODE_TYPE1_RETIRE);
        List<Map<String, String>> dataList = this.retireUserService.listRetireUserPersonalInformation(param);
        POIExcelUtils.exportExcel(response, excelVO, dataList);
    }

    /**
     * @param file
     * @return
     * @description 离退休人员登记_导入Excel
     */
    @RequestMapping(value = "/saveRetireUserByExcel", method = RequestMethod.POST)
    @ResponseBody
    public ResultVO saveRetireUserByExcel(@RequestParam("file") MultipartFile file, @RequestParam Map<String, Object> param) {
        //Map<String,String> retMap = new HashMap<>();
        String errorMessageHtml = "";
        ResultVO resultVO = new ResultVO();
        try {
            //上传附件到Minio服务器
//			String fileName = file.getOriginalFilename();
//			String contentType = file.getContentType();
//			InputStream inputStram = file.getInputStream();
//			return MinioUtil.uploadfile(inputStram, fileName, contentType);
            POIExcelUtils poi = new POIExcelUtils();
            List<Object> dataList = poi.importExcel(file, new RetireUserExcelVO(), 2, 1);
            param.put("dataList", dataList);
            errorMessageHtml = this.retireUserService.saveRetireUserByExcel(param);
            if (StringUtils.isEmpty(errorMessageHtml)) {
                resultVO.setData(1);
                resultVO.setSuccess(true);
            } else {
                resultVO.setData(-9);
                resultVO.setSuccess(false);
            }
        } catch (Exception e) {
            resultVO.setSuccess(false);
            if ("当前Excel表和模板不匹配！".equals(e.getMessage())) {
                errorMessageHtml = "当前Excel表和模板不匹配！";
                resultVO.setData(-1);
            } else {
                errorMessageHtml = "Excel解析错误！";
                resultVO.setData(-5);
            }
            e.printStackTrace();
        }
        resultVO.setMessage(errorMessageHtml);
        return resultVO;
    }
}