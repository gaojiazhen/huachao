package com.fjhcit.retire.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fjhcit.common.kit.CommonUtils;
import com.fjhcit.common.kit.StringUtils;
import com.fjhcit.constant.ParentIdContant;
import com.fjhcit.entity.RetireContactDO;
import com.fjhcit.model.ResultVO;
import com.fjhcit.model.RetireContactExcelVO;
import com.fjhcit.retire.dao.RetireContactDAO;
import com.fjhcit.retire.service.FeignZuulServer;
import com.fjhcit.retire.service.RetireContactService;

@Service
public class RetireContactServiceImpl implements RetireContactService {

    @Autowired
    private RetireContactDAO retireContactDAO;
    @Autowired
    private FeignZuulServer feignZuulServer;            //远程调用其他组件接口

    @Override
    public List<RetireContactDO> listRetireContact(Map<String, String> map) {
        return this.retireContactDAO.listRetireContact(map);
    }

    @Override
    public List<Integer> listRetireContactIdByIdcard(Map<String, String> map) {
        return this.retireContactDAO.listRetireContactIdByIdcard(map);
    }

    @Override
    public RetireContactDO getRetireContactDOById(String id) {
        return this.retireContactDAO.getRetireContactDOById(id);
    }

    @Override
    public void insertRetireContact(RetireContactDO retireContactDO) {
        this.retireContactDAO.insertRetireContact(retireContactDO);
    }

    @Override
    public void updateRetireContact(RetireContactDO retireContactDO) {
        this.retireContactDAO.updateRetireContact(retireContactDO);
    }

    @Override
    public void removeRetireContactByIdsArr(Map<String, Object> map) {
        this.retireContactDAO.removeRetireContactByIdsArr(map);
    }

    @Override
    public String getRetireContactNextSortnum(String unit_id) {
        return this.retireContactDAO.getRetireContactNextSortnum(unit_id);
    }

    @Override
    public List<Map<String, String>> listRetireContactStaff(Map<String, Object> param) {
        String unit_id = (String) param.get("unit_id");
        @SuppressWarnings("unchecked")
        List<LinkedHashMap<String, String>> educationList = (List<LinkedHashMap<String, String>>) param.get("educationList");
        String unit_id_sql = "";
        if (StringUtils.isNotEmpty(unit_id)) {
            unit_id_sql = " and unit_id = " + unit_id;
        }
        String dynamicFields = "";
        for (int i = 0; i < educationList.size(); i++) {
            LinkedHashMap<String, String> codeDO = educationList.get(i);
            dynamicFields += ",to_char((select count(1) from retire_contact where USER_NATURE_ID = c.code and EDUCATION_CODE = '" +
                    codeDO.get("value").toUpperCase() + unit_id_sql + "')) as EDUCATION_" + codeDO.get("value").toUpperCase();
        }
        param.put("dynamicFields", dynamicFields);
        //计算合计值
        List<Map<String, String>> staffList = this.retireContactDAO.listRetireContactStaff(param);
        for (int i = 0; i < staffList.size(); i++) {
            Map<String, String> staffMap = staffList.get(i);
            int TOTAL = 0;
            for (int j = 0; j < educationList.size(); j++) {
                LinkedHashMap<String, String> codeDO = educationList.get(j);
                TOTAL += Integer.valueOf(staffMap.get("EDUCATION_" + codeDO.get("value").toUpperCase()));
            }
            staffMap.put("TOTAL", TOTAL + "");
        }
        return staffList;
    }

    @Override
    public Map<String, String> saveRetireContactByExcel(Map<String, Object> map) {
        //1、查询数据字典数据
        Map<String, String> codeParam = new HashMap<String, String>();
        codeParam.put("code", ParentIdContant.BASE_CODE_SEX + "," + ParentIdContant.BASE_CODE_NATION + "," + ParentIdContant.BASE_CODE_MARITAL_STATUS + "," +
                ParentIdContant.BASE_CODE_EDUCATION + "," + ParentIdContant.BASE_CODE_POLITICS_STATUS + "," + ParentIdContant.BASE_CODE_TYPE1 + "," +
                ParentIdContant.BASE_CODE_NATURE + "," + ParentIdContant.BASE_CODE_RETIREMENT_RANK + "," + ParentIdContant.BASE_CODE_NOW_TREATMENT_LEVEL + "," +
                ParentIdContant.BASE_CODE_ARCHIVES_AREA + "," + ParentIdContant.BASE_CODE_RECEIVE_AREA + "," + ParentIdContant.BASE_CODE_HEALTH_STATUS + "," +
                ParentIdContant.BASE_CODE_PLAY_A_ROLE + "," + ParentIdContant.BASE_CODE_AWARD_LEVEL + "," + ParentIdContant.BASE_CODE_SPECIAL_CROWD + "," +
                ParentIdContant.BASE_CODE_APPELLATION+","+ParentIdContant.BASE_CODE_USER_NATURE+","+ParentIdContant.BASE_CODE_USER_RANK);
        ResultVO resultVO = this.feignZuulServer.getCode(codeParam);
        @SuppressWarnings("unchecked")
        Map<String, List<LinkedHashMap<String, String>>> codeData = (Map<String, List<LinkedHashMap<String, String>>>) resultVO.getData();
        String unit_id = (String) map.get("unit_id");
        Map<String, String> retMap = new HashMap<>();
        @SuppressWarnings("unchecked")
        List<Object> dataList = (List<Object>) map.get("dataList");


        //1、查部门名称
        Map<String, String> departmentParam = new HashMap<String, String>();
        departmentParam.put("department_id", ParentIdContant.BASE_DEPARTMENT_PROVINCIAL);
        departmentParam.put("parent_id", unit_id);
        departmentParam.put("is_available", "1");
        ResultVO departmentVO = this.feignZuulServer.listBaseDepartment(departmentParam);
        @SuppressWarnings("unchecked")
        List<LinkedHashMap<String, String>> departmentList = (List<LinkedHashMap<String, String>>) departmentVO.getData();
        //2、查性别
        List<LinkedHashMap<String, String>> sexList = codeData.get(ParentIdContant.BASE_CODE_SEX);
        //3、学历
        List<LinkedHashMap<String, String>> educationList = codeData.get(ParentIdContant.BASE_CODE_EDUCATION);
        //4、查人员性质
        List<LinkedHashMap<String, String>> natureList = codeData.get(ParentIdContant.BASE_CODE_USER_NATURE);
        //5、人员职级
        List<LinkedHashMap<String, String>> rankList = codeData.get(ParentIdContant.BASE_CODE_USER_RANK);
        //6、排序号
        String sortnum = this.retireContactDAO.getRetireContactNextSortnum(unit_id);

        //开始处理数据
        String errorMessageHtml = "";
        int addNum = 0;
        int updateNum = 0;
        int errorNum = 0;
        for (int i = 0; i < dataList.size(); i++) {
            RetireContactExcelVO excelVO = (RetireContactExcelVO) dataList.get(i);
            String userinfo = "第" + (i + 1) + "条记录：" + excelVO.getUser_name() + "的";
            String errorMessage = "";
            String department = excelVO.getDepartment_id();
            if (StringUtils.isNotEmpty(department)) {
                String codeid = isDictionaryByDepartmentName(department, departmentList);
                excelVO.setDepartment_id(codeid);
                if (StringUtils.isEmpty(codeid)) {
                    errorMessage += "部门错误；";
                }
            }
            if (StringUtils.isEmpty(excelVO.getUser_name())) {
                errorMessage += "姓名必填；";
            }
            String sex_code = excelVO.getSex_code();
            if (StringUtils.isNotEmpty(sex_code)) {
                String codeid = isDictionaryByCodename(sex_code, sexList);
                excelVO.setSex_code(codeid);
                if (StringUtils.isEmpty(codeid)) {
                    errorMessage += "性别错误；";
                }
            }
            String education_code = excelVO.getEducation_code();
            if (StringUtils.isNotEmpty(education_code)) {
                String codeid = isDictionaryByCodename(education_code, educationList);
                excelVO.setEducation_code(codeid);
                if (StringUtils.isEmpty(codeid)) {
                    errorMessage += "最高学历错误；";
                }
            }
            if (StringUtils.isEmpty(excelVO.getIdcard())) {
                errorMessage += "身份号码必填；";
            } else if (excelVO.getIdcard().length() != 18) {
                errorMessage += "身份号码必需18位；";
            }
            String birth_date = excelVO.getBirth_date();
            if (StringUtils.isNotEmpty(birth_date)) {
                if (!com.fjhcit.common.kit.StringUtils.isDate(birth_date, "yyyy-MM-dd")) {
                    errorMessage += "出生年月格式错误；";
                }
            }
            String user_nature_id = excelVO.getUser_nature_id();
            if (StringUtils.isNotEmpty(user_nature_id)) {
                String codeid = isDictionaryByCodename(user_nature_id, natureList);
                excelVO.setUser_nature_id(codeid);
                if (StringUtils.isEmpty(codeid)) {
                    errorMessage += "人员性质错误；";
                }
            }
            String user_rank_id = excelVO.getUser_rank_id();
            if (StringUtils.isNotEmpty(user_rank_id)) {
                String codeid = isDictionaryByCodename(user_rank_id, rankList);
                excelVO.setUser_rank_id(codeid);
                if (StringUtils.isEmpty(codeid)) {
                    errorMessage += "人员职级错误；";
                }
            }
            String work_seniority = excelVO.getWork_seniority();
            if (StringUtils.isNotEmpty(work_seniority)) {
                if (!com.fjhcit.common.kit.StringUtils.isNumeric(work_seniority)) {
                    errorMessage += "从事离退办工作年限必须为整数；";
                }
            }
            //拼装最终输出的错误提示消息
            if (StringUtils.isNotEmpty(errorMessage)) {
                errorNum++;
                errorMessageHtml += userinfo + errorMessage + "<br>";
            } else {   //人员数据没有格式问题
                excelVO.setUnit_id(unit_id);
                String userId = CommonUtils.getCurrentUserId();
                excelVO.setModified_user_id(userId);
                excelVO.setSortnum((Integer.valueOf(sortnum) + i + 1) + "");
                //根据身份证判断人员是否存在
                Map<String, String> cparam = new HashMap<String, String>();
                cparam.put("idcard", excelVO.getIdcard());
                List<Integer> userList = this.retireContactDAO.listRetireContactIdByIdcard(cparam);
                if (StringUtils.isNotEmpty(userList)) {    //修改
                    for (int j = 0; j < userList.size(); j++) {
                        excelVO.setId(userList.get(j) + "");
                        this.retireContactDAO.updateRetireContact(excelVO);
                    }
                    updateNum++;
                } else {        //新增
                    excelVO.setCreate_user_id(userId);
                    this.retireContactDAO.insertRetireContact(excelVO);
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
     *
     * @param excelVal
     * @param codeList
     * @return
     */
    private String isDictionaryByDepartmentName(String excelVal, List<LinkedHashMap<String, String>> codeList) {
        String codeid = "";
        if (StringUtils.isNotEmpty(excelVal)) {
            for (int j = 0; j < codeList.size(); j++) {
                if (excelVal.equals(codeList.get(j).get("department_name"))) {
                    codeid = codeList.get(j).get("id");
                }
            }
        }
        return codeid;
    }

    /**
     * 判断Excel数据是否在数据字典中存在（存在有效，不存在无效）
     *
     * @param excelVal
     * @param codeList
     * @return
     */
    private String isDictionaryByCodename(String excelVal, List<LinkedHashMap<String, String>> codeList) {
        String codeid = "";
        if (StringUtils.isNotEmpty(excelVal)) {
            for (int j = 0; j < codeList.size(); j++) {
                if (excelVal.equals(codeList.get(j).get("name"))) {
                    codeid = codeList.get(j).get("value");
                }
            }
        }
        return codeid;
    }
}