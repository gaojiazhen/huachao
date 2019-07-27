package com.fjhcit.retire.service.impl;

import java.util.*;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fjhcit.common.kit.CommonUtils;
import com.fjhcit.constant.ParentIdContant;
import com.fjhcit.entity.RetireFamilyDO;
import com.fjhcit.entity.RetireUserDO;
import com.fjhcit.model.ResultVO;
import com.fjhcit.model.RetireUserExcelVO;
import com.fjhcit.retire.dao.RetireFamilyDAO;
import com.fjhcit.retire.dao.RetireUserDAO;
import com.fjhcit.retire.service.FeignZuulServer;
import com.fjhcit.retire.service.RetireUserService;

@Service
public class RetireUserServiceImpl implements RetireUserService {

    @Autowired
    private RetireUserDAO retireUserDAO;
    @Autowired
    private RetireFamilyDAO retireFamilyDAO;
    @Autowired
    private FeignZuulServer feignZuulServer;            //远程调用其他组件接口

    @Override
    public List<RetireUserDO> listRetireUser(Map<String, String> map) {
        return this.retireUserDAO.listRetireUser(map);
    }

    @Override
    public RetireUserDO getRetireUserDOById(String id) {
        return this.retireUserDAO.getRetireUserDOById(id);
    }

    @Override
    public void insertRetireUser(RetireUserDO retireUserDO) {
        this.retireUserDAO.insertRetireUser(retireUserDO);
    }

    @Override
    public void updateRetireUser(RetireUserDO retireUserDO) {
        this.retireUserDAO.updateRetireUser(retireUserDO);
    }

    @Override
    public void removeRetireUserByIdsArr(Map<String, Object> map) {
        this.retireUserDAO.removeRetireUserByIdsArr(map);
    }

    @Override
    public Integer findRetireUserCountByIdcard(Map<String, String> map) {
        return this.retireUserDAO.findRetireUserCountByIdcard(map);
    }

    @Override
    public List<Map<String, String>> listQuitCadreBiennialChange(Map<String, String> map) {
        return this.retireUserDAO.listQuitCadreBiennialChange(map);
    }

    @Override
    public List<Map<String, String>> listQuitCadreByRegion(Map<String, String> map) {
        return this.retireUserDAO.listQuitCadreByRegion(map);
    }

    @Override
    public List<Map<String, String>> listQuitCadreBasic(Map<String, String> map) {
        return this.retireUserDAO.listQuitCadreBasic(map);
    }

    @Override
    public List<Map<String, String>> listRetireCadreTreatment(Map<String, Object> map) {
        List treatmentIdArray = (List) map.get("treatmentIdArray");
        List cadres = (List) map.get("cadreIdArray");
        List cadreIdArray=new ArrayList();
        for (int i = 0; i < cadres.size(); i++) {
            cadreIdArray.add("'"+cadres.get(i)+"'");
        }
        String cadreIds = StringUtils.join(cadreIdArray, ",");
        String dynamicFields = "";
        for(int i=0;i<treatmentIdArray.size();i++) {
            dynamicFields += ",to_char((select count(1) from retire_user u where unit_id = d.id and (select nvl(special_mark,0) from base_code where code = u.HEALTH_STATUS) != 9 and RETIRE_NATURE_ID in ("+
                    cadreIds+") and NOW_TREATMENT_LEVEL_ID = '"+treatmentIdArray.get(i)+"' )) as TREATMENT_" + treatmentIdArray.get(i);
        }
        map.put("dynamicFields", dynamicFields);
        String unit_id = (String) map.get("unit_id");
        if(StringUtils.isEmpty(unit_id)) {
            map.put("parent_id", ParentIdContant.BASE_DEPARTMENT);
        }else {
            map.put("id", unit_id);
        }
        List<Map<String,String>> dataList = this.retireUserDAO.listRetireCadreTreatment(map);
        for(int i=0;i<dataList.size();i++) {
            int total = 0;
            Map<String,String> treatmentMap = dataList.get(i);
            for(int j=0;j<treatmentIdArray.size();j++) {
                total += Integer.valueOf(treatmentMap.get("TREATMENT_" + treatmentIdArray.get(j).toString().toUpperCase()));
            }
            treatmentMap.put("TOTAL", String.valueOf(total));
        }
        return dataList;
    }

    @Override
    public List<Map<String, String>> listRetireCadreBiennialChange(Map<String, Object> map) {
        String unit_id = (String) map.get("unit_id");
        if(StringUtils.isEmpty(unit_id)) {
            map.put("parent_id", ParentIdContant.BASE_DEPARTMENT);
        }else {
            map.put("id", unit_id);
        }
        return this.retireUserDAO.listRetireCadreBiennialChange(map);
    }

    @Override
    public List<Map<String, String>> listQuitCadreCurrentSituation(Map<String, Object> map) {

        List playARoleList = (List) map.get("playARoleList");

        List<String> cadreIdArray = (List) map.get("cadreIdArray");
        List array=new ArrayList();
        for (int i = 0; i < cadreIdArray.size(); i++) {
           array.add("'"+cadreIdArray.get(i)+"'");
        }

        String unit_id = (String) map.get("unit_id");
        if(StringUtils.isEmpty(unit_id)) {
            map.put("parent_id", ParentIdContant.BASE_DEPARTMENT);
        }else {
            map.put("id", unit_id);
        }
        String dynamicFields = "";
        for(int i=0;i<playARoleList.size();i++) {
            dynamicFields += ",to_char((select count(1) from retire_user u where u.unit_id = d.id and (select nvl(special_mark,0) from base_code where code = u.HEALTH_STATUS) != 9" +
                    " and retire_nature_id in ("+ StringUtils.join(array.toArray(),",") +") and instr(',' || PLAY_A_ROLE_IDS || ',' , ',' || '" +
                    playARoleList.get(i) + "' || ',') > 0 )) PLAY_A_ROLE_" + playARoleList.get(i);
        }
        //2、查列表数据
        map.put("dynamicFields", dynamicFields);
        //3、计算小计
        List<Map<String,String>> currentList = this.retireUserDAO.listQuitCadreCurrentSituation(map);
        for(int i=0;i<currentList.size();i++) {
            Map<String,String> currentMap = currentList.get(i);
            int subtotal = 0;
            for(int j=0;j<playARoleList.size();j++) {

                subtotal += Integer.valueOf(currentMap.get("PLAY_A_ROLE_"+playARoleList.get(j).toString().toUpperCase()));
            }
            currentMap.put("SUBTOTAL", String.valueOf(subtotal));
        }
        return currentList;
    }

    @Override
    public List<Map<String, String>> listRetirePartyRelation(Map<String, Object> map) {
        @SuppressWarnings("unchecked")
        List<String> locatedIdArray = (List<String>) map.get("locatedIdArray");
        String dynamicFields = "";
        for(int i=0;i<locatedIdArray.size();i++) {
            dynamicFields += ",to_char((select count(1) from retire_user u,RETIRE_COMMUNIST C where unit_id = d.id and u.id = c.user_id and MEMBERSHIP_CREDENTIALS_ID = '" +
                    locatedIdArray.get(i) + "' )) as LOCATED_" + locatedIdArray.get(i);
        }
        map.put("dynamicFields", dynamicFields);
        String unit_id = (String) map.get("unit_id");
        if(StringUtils.isEmpty(unit_id)) {
            map.put("parent_id", ParentIdContant.BASE_DEPARTMENT);
        }else {
            map.put("id", unit_id);
        }
        //计算合计
        List<Map<String,String>> dataList = this.retireUserDAO.listRetireCadreTreatment(map);
        for(int i=0;i<dataList.size();i++) {
            int total = 0;
            Map<String,String> organMap = dataList.get(i);
            for(int j=0;j<locatedIdArray.size();j++) {
                total += Integer.valueOf(organMap.get("LOCATED_"+locatedIdArray.get(j).toUpperCase()));
            }
            organMap.put("TOTAL", String.valueOf(total));
        }
        return dataList;
    }

    @Override
    public List<Map<String, String>> listRetireUserBasic(Map<String, Object> map) {
        if (null == map || map.isEmpty()) {
            map = new HashMap<String, Object>();
        }
        String unit_id = (String) map.get("unit_id");
        if (StringUtils.isEmpty(unit_id)) {
            map.put("parent_id", ParentIdContant.BASE_DEPARTMENT);
        } else {
            map.put("id", unit_id);
        }
        map.put("base_code_parent_id", ParentIdContant.BASE_CODE_NATURE);
        //Map<String, String> parameter=new HashedMap<String, String>();
        //parameter.put("parent_id", ParentIdContant.BASE_CODE_RETIREMENT_RANK);
        //parameter.put("is_available", ParentIdContant.BASE_CODE_IS_AVAILABLE_YES);
        @SuppressWarnings("unchecked")
        List<LinkedHashMap<String, String>> list = (List<LinkedHashMap<String, String>>) map.get("list");
        String dynamicFieldsSubtotal = "";
        String dynamicFieldsRetire = "";
        String dynamicFieldsResign = "";
        String dynamicFieldsEarlyRetire = "";
        for (int i = 0; i < list.size(); i++) {
            dynamicFieldsSubtotal += ",to_char((select count(1) from retire_user u where unit_id = d.id  and (select nvl(special_mark,0) from base_code c where c.code = u.HEALTH_STATUS) != 9 and RETIREMENT_RANK_ID ='" + list.get(i) + "' and RETIRE_NATURE_ID in " + ParentIdContant.BASE_CODE_ID_SUBTOTAL + " )) as RETIREMENT_RANK_ID_" + list.get(i);
            dynamicFieldsRetire += ",to_char((select count(1) from retire_user u where unit_id = d.id and (select nvl(special_mark,0) from base_code c where c.code = u.HEALTH_STATUS) != 9 and RETIREMENT_RANK_ID ='" + list.get(i) + "' and RETIRE_NATURE_ID in " + ParentIdContant.BASE_CODE_ID_RETIRE + "  )) as RETIREMENT_RANK_ID_" + list.get(i);
            dynamicFieldsResign += ",to_char((select count(1) from retire_user u where unit_id = d.id and (select nvl(special_mark,0) from base_code c where c.code = u.HEALTH_STATUS) != 9 and RETIREMENT_RANK_ID ='" + list.get(i) + "' and RETIRE_NATURE_ID in " + ParentIdContant.BASE_CODE_ID_RESIGN + " )) as RETIREMENT_RANK_ID_" + list.get(i);
            dynamicFieldsEarlyRetire += ",to_char((select count(1) from retire_user u where unit_id = d.id  and (select nvl(special_mark,0) from base_code c where c.code = u.HEALTH_STATUS) != 9 and RETIREMENT_RANK_ID ='" + list.get(i) + "' and RETIRE_NATURE_ID in " + ParentIdContant.BASE_CODE_ID_EARLY_RETIRE + " )) as RETIREMENT_RANK_ID_" + list.get(i);
        }
        map.put("dynamicFieldsSubtotal", dynamicFieldsSubtotal);
        map.put("dynamicFieldsRetire", dynamicFieldsRetire);
        map.put("dynamicFieldsResign", dynamicFieldsResign);
        map.put("dynamicFieldsEarlyRetire", dynamicFieldsEarlyRetire);

        return retireUserDAO.listRetireUserBasic(map);
    }

    @Override
    public List<Map<String, String>> listRetireUserPersonalInformation(Map<String, Object> map) {
//        @SuppressWarnings("unchecked")
//        List<LinkedHashMap<String, String>> locatedList = (List<LinkedHashMap<String, String>>) map.get("locatedList");
        @SuppressWarnings("unchecked")
        List cadreIdArray = new ArrayList();
        @SuppressWarnings("unchecked")
        List<String> cadreIds = (List<String>) map.get("locatedList");
        for (int i = 0; i < cadreIds.size(); i++) {
//            String code = "'"+cadreIds.get(i)+"'";
            cadreIdArray.add(cadreIds.get(i));

        }
        String dynamicFields = "";
        for (int i = 0; i < cadreIds.size(); i++) {
            dynamicFields += ",case when u.receive_area_id = '" + cadreIds.get(i) + "' then RECEIVE_AREA_ADDRESS else '' end as AREA_ADDRESS_" + cadreIds.get(i);
        }
        map.put("dynamicFields", dynamicFields);
        return this.retireUserDAO.listRetireUserPersonalInformation(map);
    }

    @Override
    public String saveRetireUserByExcel(Map<String, Object> map) {
        String unit_id = (String) map.get("unit_id");
        @SuppressWarnings("unchecked")
        List<Object> dataList = (List<Object>) map.get("dataList");
        //1、查询数据字典数据
        Map<String, String> codeParam = new HashMap<String, String>();
        codeParam.put("code", ParentIdContant.BASE_CODE_SEX + "," + ParentIdContant.BASE_CODE_NATION + "," + ParentIdContant.BASE_CODE_MARITAL_STATUS + "," +
                ParentIdContant.BASE_CODE_EDUCATION + "," + ParentIdContant.BASE_CODE_POLITICS_STATUS + "," + ParentIdContant.BASE_CODE_TYPE1 + "," +
                ParentIdContant.BASE_CODE_NATURE + "," + ParentIdContant.BASE_CODE_RETIREMENT_RANK + "," + ParentIdContant.BASE_CODE_NOW_TREATMENT_LEVEL + "," +
                ParentIdContant.BASE_CODE_ARCHIVES_AREA + "," + ParentIdContant.BASE_CODE_RECEIVE_AREA + "," + ParentIdContant.BASE_CODE_HEALTH_STATUS + "," +
                ParentIdContant.BASE_CODE_PLAY_A_ROLE + "," + ParentIdContant.BASE_CODE_AWARD_LEVEL + "," + ParentIdContant.BASE_CODE_SPECIAL_CROWD + "," +
                ParentIdContant.BASE_CODE_APPELLATION);
        ResultVO resultVO = this.feignZuulServer.getCode(codeParam);
        @SuppressWarnings("unchecked")
        Map<String, List<LinkedHashMap<String, String>>> dataMap = (Map<String, List<LinkedHashMap<String, String>>>) resultVO.getData();
        //1.1、性别
        List<LinkedHashMap<String, String>> sexList = dataMap.get(ParentIdContant.BASE_CODE_SEX);
        //1.2、民族
        List<LinkedHashMap<String, String>> nationList = dataMap.get(ParentIdContant.BASE_CODE_NATION);
        //1.3、查婚姻状况
        List<LinkedHashMap<String, String>> maritalList = dataMap.get(ParentIdContant.BASE_CODE_MARITAL_STATUS);
        //1.4、最高学历
        List<LinkedHashMap<String, String>> educationList = dataMap.get(ParentIdContant.BASE_CODE_EDUCATION);
        //1.5、政治面貌
        List<LinkedHashMap<String, String>> politicsList = dataMap.get(ParentIdContant.BASE_CODE_POLITICS_STATUS);
        //1.6、离退休类型
        List<LinkedHashMap<String, String>> type1List = dataMap.get(ParentIdContant.BASE_CODE_TYPE1);
        //1.7、退休性质
        List<LinkedHashMap<String, String>> natureList = dataMap.get(ParentIdContant.BASE_CODE_NATURE);
        //1.8、离退休职级
        List<LinkedHashMap<String, String>> retirementRankList = dataMap.get(ParentIdContant.BASE_CODE_RETIREMENT_RANK);
        //1.9、现享受待遇级别
        List<LinkedHashMap<String, String>> treatmentLevelList = dataMap.get(ParentIdContant.BASE_CODE_NOW_TREATMENT_LEVEL);
        //1.10、人事档案存放地
        List<LinkedHashMap<String, String>> archivesAreaList = dataMap.get(ParentIdContant.BASE_CODE_ARCHIVES_AREA);
        //1.11、接收地
        List<LinkedHashMap<String, String>> receiveAreaList = dataMap.get(ParentIdContant.BASE_CODE_RECEIVE_AREA);
        //1.12、健康状况
        List<LinkedHashMap<String, String>> healthStatusList = dataMap.get(ParentIdContant.BASE_CODE_HEALTH_STATUS);
        //1.13、发挥作用情况
        List<LinkedHashMap<String, String>> playRoleList = dataMap.get(ParentIdContant.BASE_CODE_PLAY_A_ROLE);
        //1.14、奖项级别
        List<LinkedHashMap<String, String>> awardLevelList = dataMap.get(ParentIdContant.BASE_CODE_AWARD_LEVEL);
        //1.15、特殊人群标识
        List<LinkedHashMap<String, String>> specialCrowdList = dataMap.get(ParentIdContant.BASE_CODE_SPECIAL_CROWD);
        //1.16、特殊人群标识
        List<LinkedHashMap<String, String>> appellationList = dataMap.get(ParentIdContant.BASE_CODE_APPELLATION);

        //开始处理数据
        String errorMessageHtml = "";
        int addNum = 0;
        int updateNum = 0;
        int errorNum = 0;
        for (int i = 0; i < dataList.size(); i++) {
            RetireUserExcelVO excelVO = (RetireUserExcelVO) dataList.get(i);
            String userinfo = "第" + (i + 1) + "条记录：" + excelVO.getUser_name() + "（" + excelVO.getIdcard() + "）的";
            String errorMessage = "";
            if (StringUtils.isEmpty(excelVO.getUser_name())) {
                errorMessage += "姓名必填；";
            }
            String sexid = excelVO.getSex_id();
            if (StringUtils.isNotEmpty(sexid)) {
                String codeid = isDictionaryByCodename(sexid, sexList);
                excelVO.setSex_id(codeid);
                if (StringUtils.isEmpty(codeid)) {
                    errorMessage += "性别错误；";
                }
            }
            String birth_date = excelVO.getBirth_date();
            if (StringUtils.isNotEmpty(birth_date)) {
                if (!com.fjhcit.common.kit.StringUtils.isDate(birth_date, "yyyy-MM-dd")) {
                    errorMessage += "出生年月格式错误；";
                }

            }
            String nationid = excelVO.getNation_id();
            if (StringUtils.isNotEmpty(nationid)) {
                String codeid = isDictionaryByCodename(nationid, nationList);
                excelVO.setNation_id(codeid);
                if (StringUtils.isEmpty(codeid)) {
                    errorMessage += "民族错误；";
                }
            }
            String maritalid = excelVO.getMarital_status_id();
            if (StringUtils.isNotEmpty(maritalid)) {
                String codeid = isDictionaryByCodename(maritalid, maritalList);
                excelVO.setMarital_status_id(codeid);
                if (StringUtils.isEmpty(codeid)) {
                    errorMessage += "婚姻状况错误；";
                }
            }
            String educationid = excelVO.getEducation_id();
            if (StringUtils.isNotEmpty(educationid)) {
                String codeid = isDictionaryByCodename(educationid, educationList);
                excelVO.setEducation_id(codeid);
                if (StringUtils.isEmpty(codeid)) {
                    errorMessage += "最高学历错误；";
                }
            }
            String work_time = excelVO.getWork_time();
            if (StringUtils.isNotEmpty(work_time)) {
                if (!com.fjhcit.common.kit.StringUtils.isDate(work_time, "yyyy-MM-dd")) {
                    errorMessage += "参加工作时间格式错误；";
                }
            }
            String retire_time = excelVO.getRetire_time();
            if (StringUtils.isNotEmpty(retire_time)) {
                if (!com.fjhcit.common.kit.StringUtils.isDate(retire_time, "yyyy-MM-dd")) {
                    errorMessage += "离退休时间格式错误；";
                }
            }
            if (StringUtils.isEmpty(excelVO.getIdcard())) {
                errorMessage += "身份号码必填；";
            } else if (excelVO.getIdcard().length() != 18) {
                errorMessage += "身份号码必需18位；";
            }
            String politicsid = excelVO.getPolitics_status_id();
            if (StringUtils.isNotEmpty(politicsid)) {
                String codeid = isDictionaryByCodename(politicsid, politicsList);
                excelVO.setPolitics_status_id(codeid);
                if (StringUtils.isEmpty(codeid)) {
                    errorMessage += "政治面貌错误；";
                }
            }
            String retire_type_id = excelVO.getRetire_type_id();
            if (StringUtils.isNotEmpty(retire_type_id)) {
                String codeid = isDictionaryByCodename(retire_type_id, type1List);
                excelVO.setRetire_type_id(codeid);
                if (StringUtils.isEmpty(codeid)) {
                    errorMessage += "离退休类型错误；";
                }
            }
            String retire_nature_id = excelVO.getRetire_nature_id();
            if (StringUtils.isNotEmpty(retire_nature_id)) {
                String codeid = isDictionaryByCodename(retire_nature_id, natureList);
                excelVO.setRetire_nature_id(codeid);
                if (StringUtils.isEmpty(codeid)) {
                    errorMessage += "退休性质错误；";
                }
            }
            String retirement_rank_id = excelVO.getRetirement_rank_id();
            if (StringUtils.isNotEmpty(retirement_rank_id)) {
                String codeid = isDictionaryByCodename(retirement_rank_id, retirementRankList);
                excelVO.setRetirement_rank_id(codeid);
                if (StringUtils.isEmpty(codeid)) {
                    errorMessage += "离退休职级错误；";
                }
            }
            String now_treatment_level_id = excelVO.getNow_treatment_level_id();
            if (StringUtils.isNotEmpty(now_treatment_level_id)) {
                String codeid = isDictionaryByCodename(now_treatment_level_id, treatmentLevelList);
                excelVO.setNow_treatment_level_id(codeid);
                if (StringUtils.isEmpty(codeid)) {
                    errorMessage += "现享受待遇级别错误；";
                }
            }
            String treatment_approval_time = excelVO.getTreatment_approval_time();
            if (StringUtils.isNotEmpty(treatment_approval_time)) {
                if (!com.fjhcit.common.kit.StringUtils.isDate(treatment_approval_time, "yyyy-MM-dd")) {
                    errorMessage += "待遇批准时间（文号）格式错误；";
                }
            }
            String is_earthly_place = excelVO.getIs_earthly_place();
            if (StringUtils.isNotEmpty(is_earthly_place)) {
                if ("属地安置".equals(is_earthly_place)) {
                    excelVO.setIs_earthly_place("1");
                } else if ("异地安置".equals(is_earthly_place)) {
                    excelVO.setIs_earthly_place("5");
                } else {
                    errorMessage += "安置情况错误；";
                }
            }
            String is_beijing = excelVO.getIs_beijing();
            if (StringUtils.isNotEmpty(is_beijing)) {
                if ("是".equals(is_beijing)) {
                    excelVO.setIs_beijing("1");
                } else if ("否".equals(is_beijing)) {
                    excelVO.setIs_beijing("0");
                } else {
                    errorMessage += "是否在京错误；";
                }
            }
            String is_socialized_pension = excelVO.getIs_socialized_pension();
            if (StringUtils.isNotEmpty(is_socialized_pension)) {
                if ("是".equals(is_socialized_pension)) {
                    excelVO.setIs_socialized_pension("1");
                } else if ("否".equals(is_socialized_pension)) {
                    excelVO.setIs_socialized_pension("0");
                } else {
                    errorMessage += "是否养老金社会化发放错误；";
                }
            }
            String is_medical_insurance = excelVO.getIs_medical_insurance();
            if (StringUtils.isNotEmpty(is_medical_insurance)) {
                if ("是".equals(is_medical_insurance)) {
                    excelVO.setIs_medical_insurance("1");
                } else if ("否".equals(is_medical_insurance)) {
                    excelVO.setIs_medical_insurance("0");
                } else {
                    errorMessage += "是否参加属地医疗保险错误；";
                }
            }
            String child_working_sys = excelVO.getChild_working_sys();
            if (StringUtils.isNotEmpty(child_working_sys)) {
                if ("是".equals(child_working_sys)) {
                    excelVO.setChild_working_sys("1");
                } else if ("否".equals(child_working_sys)) {
                    excelVO.setChild_working_sys("0");
                } else {
                    errorMessage += "是否有子女在系统工作错误；";
                }
            }
            String archives_area_id = excelVO.getArchives_area_id();
            if (StringUtils.isNotEmpty(archives_area_id)) {
                String codeid = isDictionaryByCodename(archives_area_id, archivesAreaList);
                excelVO.setArchives_area_id(codeid);
                if (StringUtils.isEmpty(codeid)) {
                    errorMessage += "人事档案存放地错误；";
                }
            }
            String receive_area_id = excelVO.getReceive_area_id();
            if (StringUtils.isNotEmpty(receive_area_id)) {
                String codeid = isDictionaryByCodename(receive_area_id, receiveAreaList);
                excelVO.setReceive_area_id(codeid);
                if (StringUtils.isEmpty(codeid)) {
                    errorMessage += "接收地错误；";
                }
            }
            String health_status = excelVO.getHealth_status();
            if (StringUtils.isNotEmpty(health_status)) {
                String codeid = isDictionaryByCodename(health_status, healthStatusList);
                excelVO.setHealth_status(codeid);
                if (StringUtils.isEmpty(codeid)) {
                    errorMessage += "健康状况错误；";
                }
            }
            String deceased_time = excelVO.getDeceased_time();
            if (StringUtils.isNotEmpty(deceased_time)) {
                if (!com.fjhcit.common.kit.StringUtils.isDate(deceased_time, "yyyy-MM-dd")) {
                    errorMessage += "已故时间错误；";
                }
            }
            String play_a_role_ids = excelVO.getPlay_a_role_ids();
            if (StringUtils.isNotEmpty(play_a_role_ids)) {
                String codeid = isDictionaryByCodename(play_a_role_ids, playRoleList);
                excelVO.setPlay_a_role_ids(codeid);
                if (StringUtils.isEmpty(codeid)) {
                    errorMessage += "发挥作用情况错误；";
                }
            }
            String is_system_employees = excelVO.getIs_system_employees();
            if (StringUtils.isNotEmpty(is_system_employees)) {
                if ("是".equals(is_system_employees)) {
                    excelVO.setIs_system_employees("1");
                } else if ("否".equals(is_system_employees)) {
                    excelVO.setIs_system_employees("0");
                } else {
                    errorMessage += "是否电力系统双职工错误；";
                }
            }
            String is_model_personnel = excelVO.getIs_model_personnel();
            if (StringUtils.isNotEmpty(is_model_personnel)) {
                if ("是".equals(is_model_personnel)) {
                    excelVO.setIs_model_personnel("1");
                } else if ("否".equals(is_model_personnel)) {
                    excelVO.setIs_model_personnel("0");
                } else {
                    errorMessage += "是否先模人员错误；";
                }
            }
            String award_level_id = excelVO.getAward_level_id();
            if (StringUtils.isNotEmpty(award_level_id)) {
                String codeid = isDictionaryByCodename(award_level_id, awardLevelList);
                excelVO.setAward_level_id(codeid);
                if (StringUtils.isEmpty(codeid)) {
                    errorMessage += "奖项级别错误；";
                }
            }
            String is_soldier_cadre = excelVO.getIs_soldier_cadre();
            if (StringUtils.isNotEmpty(is_soldier_cadre)) {
                if ("是".equals(is_soldier_cadre)) {
                    excelVO.setIs_soldier_cadre("1");
                } else if ("否".equals(is_soldier_cadre)) {
                    excelVO.setIs_soldier_cadre("0");
                } else {
                    errorMessage += "是否军转干部错误；";
                }
            }
            String special_crowd_ids = excelVO.getSpecial_crowd_ids();
            if (StringUtils.isNotEmpty(special_crowd_ids)) {
                String codeid = isDictionaryByCodename(special_crowd_ids, specialCrowdList);
                excelVO.setSpecial_crowd_ids(codeid);
                if (StringUtils.isEmpty(codeid)) {
                    errorMessage += "特殊人群标识错误；";
                }
            }
            String appellation_ids = excelVO.getAppellation_ids();
            String[] appellation_idArr = null;
            if (StringUtils.isNotEmpty(appellation_ids)) {
                appellation_ids = appellation_ids.replaceAll(" ", "");
                appellation_ids = appellation_ids.replaceAll("，", ",");
                String[] appellationArr = appellation_ids.split(",");
                appellation_idArr = new String[appellationArr.length];
                for (int j = 0; j < appellationArr.length; j++) {
                    String codeid = isDictionaryByCodename(appellationArr[j], appellationList);
                    appellation_idArr[j] = codeid;
                    if (StringUtils.isEmpty(codeid)) {
                        errorMessage += "家庭主要成员称谓错误；";
                    }
                }
                excelVO.setAppellation_ids(StringUtils.join(appellation_idArr, ","));
            }
            String user_names = excelVO.getUser_names();                //姓名_字符串
            String[] user_nameArr = null;
            if (StringUtils.isNotEmpty(user_names)) {
                user_names = user_names.replaceAll(" ", "");
                user_names = user_names.replaceAll("，", ",");
                user_nameArr = user_names.split(",");
            }
            String age_arrs = excelVO.getAge_arrs();
            String[] ageArr = null;
            if (StringUtils.isNotEmpty(age_arrs)) {
                age_arrs = age_arrs.replaceAll(" ", "");
                age_arrs = age_arrs.replaceAll("，", ",");
                ageArr = age_arrs.split(",");
                for (int j = 0; j < ageArr.length; j++) {
                    if (!com.fjhcit.common.kit.StringUtils.isNumeric(ageArr[j])) {
                        errorMessage += "家庭主要成员年龄错误；";
                    }
                }
            }
            String work_unit_posts = excelVO.getWork_unit_posts();        //工作单位及职务_字符串
            String[] work_unit_postArr = null;
            if (StringUtils.isNotEmpty(work_unit_posts)) {
                work_unit_posts = work_unit_posts.replaceAll(" ", "");
                work_unit_posts = work_unit_posts.replaceAll("，", ",");
                work_unit_postArr = work_unit_posts.split(",");
            }
            String contacts = excelVO.getContacts();                    //联系方式_字符串
            String[] contactArr = null;
            if (StringUtils.isNotEmpty(contacts)) {
                contacts = contacts.replaceAll(" ", "");
                contacts = contacts.replaceAll("，", ",");
                contactArr = contacts.split(",");
            }
            if (appellation_idArr != null && appellation_idArr.length > 0) {
                if (appellation_idArr.length != user_nameArr.length || appellation_idArr.length != ageArr.length ||
                        appellation_idArr.length != work_unit_postArr.length || appellation_idArr.length != contactArr.length) {
                    errorMessage += "家庭主要成员的称谓、姓名、年龄、工作单位及职务、联系方式数量不一致；";
                }
            }
            String remarks = excelVO.getRemarks();                        //备注_字符串
            String[] remarkArr = null;
            if (StringUtils.isNotEmpty(remarks)) {
                remarks = remarks.replaceAll(" ", "");
                remarks = remarks.replaceAll("，", ",");
                remarkArr = remarks.split(",");
            }

            //拼装最终输出的错误提示消息
            if (StringUtils.isNotEmpty(errorMessage)) {
                errorNum++;
                errorMessageHtml += userinfo + errorMessage + "<br>";
            } else {   //人员数据没有格式问题
                excelVO.setUnit_id(unit_id);
                String userId = CommonUtils.getCurrentUserId();
                excelVO.setModified_user_id(userId);
                //根据身份证判断人员是否存在
                Map<String, String> cparam = new HashMap<String, String>();
                cparam.put("idcard", excelVO.getIdcard());
                List<Integer> userList = this.retireUserDAO.listRetireUserIdByIdcard(cparam);
                if (userList.size() > 0) {    //修改
                    for (int j = 0; j < userList.size(); j++) {
                        excelVO.setId(userList.get(j) + "");
                        this.retireUserDAO.updateRetireUser(excelVO);
                        //删除家庭成员
                        Map<String, String> delFamilyParam = new HashMap<String, String>();
                        delFamilyParam.put("user_id", userList.get(j) + "");
                        this.retireFamilyDAO.removeRetireFamilyByUserid(delFamilyParam);
                        //新增家庭成员
                        if (appellation_idArr != null && appellation_idArr.length > 0) {
                            for (int k = 0; k < appellation_idArr.length; k++) {
                                RetireFamilyDO retireFamilyDO = new RetireFamilyDO();
                                retireFamilyDO.setCreate_user_id(userId);
                                retireFamilyDO.setModified_user_id(userId);
                                retireFamilyDO.setUser_id(userList.get(j) + "");
                                retireFamilyDO.setAppellation_id(appellation_idArr[k]);
                                retireFamilyDO.setUser_name(user_nameArr[k]);
                                retireFamilyDO.setAge(ageArr[k]);
                                retireFamilyDO.setWork_unit_post(work_unit_postArr[k]);
                                retireFamilyDO.setContact(contactArr[k]);
                                if (null != remarkArr && remarkArr.length - 1 >= k) {
                                    retireFamilyDO.setRemark(remarkArr[k]);
                                }
                                this.retireFamilyDAO.insertRetireFamily(retireFamilyDO);
                            }
                        }
                    }
                    updateNum++;
                } else {        //新增
                    excelVO.setCreate_user_id(userId);
                    this.retireUserDAO.insertRetireUser(excelVO);
                    addNum++;
                    //新增家庭成员
                    if (appellation_idArr != null && appellation_idArr.length > 0) {
                        for (int j = 0; j < appellation_idArr.length; j++) {
                            RetireFamilyDO retireFamilyDO = new RetireFamilyDO();
                            retireFamilyDO.setCreate_user_id(userId);
                            retireFamilyDO.setModified_user_id(userId);
                            retireFamilyDO.setUser_id(excelVO.getId());
                            retireFamilyDO.setAppellation_id(appellation_idArr[j]);
                            retireFamilyDO.setUser_name(user_nameArr[j]);
                            retireFamilyDO.setAge(ageArr[j]);
                            retireFamilyDO.setWork_unit_post(work_unit_postArr[j]);
                            retireFamilyDO.setContact(contactArr[j]);
                            if (null != remarkArr && remarkArr.length - 1 >= j) {
                                retireFamilyDO.setRemark(remarkArr[j]);
                            }
                            this.retireFamilyDAO.insertRetireFamily(retireFamilyDO);
                        }
                    }
                }
            }
        }
        /*
         * retMap.put("errorMessageHtml", "<b>成功新增" + addNum + "条数据，成功更新" + updateNum +
         * "条数据，以下" + errorNum + "条错误数据未入库：</b><br>" + errorMessageHtml);
         */
        return "<b>成功新增" + addNum + "条数据，成功更新" + updateNum + "条数据，以下" + errorNum +
                "条错误数据未入库：</b><br>" + errorMessageHtml;
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

    public List<Integer> listRetireUserIdByIdcard(Map<String, String> map) {
        return this.retireUserDAO.listRetireUserIdByIdcard(map);
    }
}