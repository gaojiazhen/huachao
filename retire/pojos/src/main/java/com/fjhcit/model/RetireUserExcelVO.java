package com.fjhcit.model;

import com.fjhcit.entity.RetireUserDO;

/**
 * @description 离（退）休人员基本信息库_导入Excel数据对象（注意：要和导入Excel模板顺序一致，导入Excel时会按顺序赋值；并且只写Excel有体现的字段 ）
 * @author 陈麟
 * @date 2019年06月05日 上午10:45:07
 */
public class RetireUserExcelVO extends RetireUserDO {
    private String user_name;                     //姓名
    private String sex_id;                        //性别ID（base_code）
    private String birth_date;                    //出生年月
    private String nation_id;                     //民族ID（base_code）
    private String native_place;                  //籍贯
    private String marital_status_id;             //婚姻状况ID（base_code）
    private String education_id;                  //最高学历ID（base_code）
    private String idcard;                        //身份号码
    private String registered_permanent_residence;//户口所在地
    private String residence_address;             //现居住地详细地址
    private String work_time;                     //参加工作时间
    private String retire_time;                   //离退休时间
    private String politics_status_id;            //政治面貌ID（base_code）
    private String retire_type_id;                //退休类型ID（base_code）
    private String retire_nature_id;              //退休性质ID（base_code）
    private String retirement_post;               //离退休岗位
    private String retirement_duty;               //离退休职务
    private String retirement_rank_id;            //离退休职级ID（base_code）
    private String now_treatment_level_id;        //现享受待遇级别ID（base_code）
    private String treatment_approval_time;       //待遇批准时间（文号）
    private String retirement_settlement;         //离退休安置地
    private String is_earthly_place;              //安置情况（1属地安置 5异地安置）
    private String is_beijing;                    //是否在京（1是 0否）
    private String is_socialized_pension;         //是否养老金社会化发放（1是 0否）
    private String is_medical_insurance;          //是否参加属地医疗保险（1是 0否）
    private String bank_card_number;              //银行卡及卡号
	private String child_working_sys;             //子女是否在系统工作
	private String phone;						  //手机号
	private String area_code;              		  //区号
	private String landline;              		  //固话
    private String social_security_area;          //社保关系所在地
    private String archives_area_id;              //人事档案存放地ID（base_code）
    private String receive_area_id;               //接收地ID（base_code）
    private String receive_area_address;          //接收地详细地址
    private String archives_book_number;          //档案册数
    private String health_status;                 //健康状况ID（base_code）
    private String deceased_time;                 //已故时间
    private String health_status_details;         //健康状况详情
    private String play_a_role_ids;               //发挥作用情况(可以多选）（base_code）
    private String play_a_role_specific;          //发挥作用具体情况（选择其他时）
    private String is_system_employees;           //是否电力系统双职工
    private String spouse_name;                   //配偶姓名
    private String spouse_unit;                   //配偶退休单位
    private String spouse_duty;                   //配偶退休部门及职务
    private String spouse_contact;                //配偶联系方式
    private String is_model_personnel;            //是否先模人员
    private String honor;                         //所获荣誉
    private String prizes_department;             //授奖单位
    private String award_level_id;                //奖项级别ID（base_code）
    private String is_soldier_cadre;              //是否军转干部
    private String military_level;                //转业时军职级
    private String special_crowd_ids;             //特殊人群标识ID串（base_code）
    private String special_remark;                //特殊情况具体说明
    //家庭主要成员表
    private String appellation_ids;    	//称谓_字符串
    private String user_names;         	//姓名_字符串
    private String age_arrs;           	//年龄_字符串
    private String work_unit_posts;    	//工作单位及职务_字符串
    private String contacts;  			//联系方式_字符串
    private String remarks;				//备注_字符串
    
	@Override
	public String toString() {
		return "RetireUserExcelVO [user_name=" + user_name + ", sex_id=" + sex_id
				+ ", birth_date=" + birth_date + ", nation_id=" + nation_id + ", native_place=" + native_place
				+ ", marital_status_id=" + marital_status_id + ", education_id=" + education_id + ", idcard=" + idcard
				+ ", registered_permanent_residence=" + registered_permanent_residence + ", residence_address="
				+ residence_address + ", work_time=" + work_time + ", retire_time=" + retire_time
				+ ", politics_status_id=" + politics_status_id + ", retire_type_id=" + retire_type_id
				+ ", retire_nature_id=" + retire_nature_id + ", retirement_post=" + retirement_post
				+ ", retirement_duty=" + retirement_duty + ", retirement_rank_id=" + retirement_rank_id
				+ ", now_treatment_level_id=" + now_treatment_level_id + ", treatment_approval_time="
				+ treatment_approval_time + ", retirement_settlement=" + retirement_settlement + ", is_earthly_place="
				+ is_earthly_place + ", is_beijing=" + is_beijing + ", is_socialized_pension=" + is_socialized_pension
				+ ", is_medical_insurance=" + is_medical_insurance + ", bank_card_number=" + bank_card_number
				+ ", child_working_sys=" + child_working_sys + ", phone=" + phone + ", area_code=" + area_code
				+ ", landline=" + landline + ", social_security_area=" + social_security_area + ", archives_area_id="
				+ archives_area_id + ", receive_area_id=" + receive_area_id + ", receive_area_address="
				+ receive_area_address + ", archives_book_number=" + archives_book_number + ", health_status="
				+ health_status + ", deceased_time=" + deceased_time + ", health_status_details="
				+ health_status_details + ", play_a_role_ids=" + play_a_role_ids + ", play_a_role_specific="
				+ play_a_role_specific + ", is_system_employees=" + is_system_employees + ", spouse_name=" + spouse_name
				+ ", spouse_unit=" + spouse_unit + ", spouse_duty=" + spouse_duty + ", spouse_contact=" + spouse_contact
				+ ", is_model_personnel=" + is_model_personnel + ", honor=" + honor + ", prizes_department="
				+ prizes_department + ", award_level_id=" + award_level_id + ", is_soldier_cadre=" + is_soldier_cadre
				+ ", military_level=" + military_level + ", special_crowd_ids=" + special_crowd_ids
				+ ", special_remark=" + special_remark + ", appellation_ids=" + appellation_ids + ", user_names="
				+ user_names + ", age_arrs=" + age_arrs + ", work_unit_posts=" + work_unit_posts + ", contacts="
				+ contacts + ", remarks=" + remarks + "]";
	}

	//以下是get和set方法
	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getSex_id() {
		return sex_id;
	}

	public void setSex_id(String sex_id) {
		this.sex_id = sex_id;
	}

	public String getBirth_date() {
		return birth_date;
	}

	public void setBirth_date(String birth_date) {
		this.birth_date = birth_date;
	}

	public String getNation_id() {
		return nation_id;
	}

	public void setNation_id(String nation_id) {
		this.nation_id = nation_id;
	}

	public String getNative_place() {
		return native_place;
	}

	public void setNative_place(String native_place) {
		this.native_place = native_place;
	}

	public String getMarital_status_id() {
		return marital_status_id;
	}

	public void setMarital_status_id(String marital_status_id) {
		this.marital_status_id = marital_status_id;
	}

	public String getEducation_id() {
		return education_id;
	}

	public void setEducation_id(String education_id) {
		this.education_id = education_id;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getRegistered_permanent_residence() {
		return registered_permanent_residence;
	}

	public void setRegistered_permanent_residence(String registered_permanent_residence) {
		this.registered_permanent_residence = registered_permanent_residence;
	}

	public String getResidence_address() {
		return residence_address;
	}

	public void setResidence_address(String residence_address) {
		this.residence_address = residence_address;
	}

	public String getWork_time() {
		return work_time;
	}

	public void setWork_time(String work_time) {
		this.work_time = work_time;
	}

	public String getRetire_time() {
		return retire_time;
	}

	public void setRetire_time(String retire_time) {
		this.retire_time = retire_time;
	}

	public String getPolitics_status_id() {
		return politics_status_id;
	}

	public void setPolitics_status_id(String politics_status_id) {
		this.politics_status_id = politics_status_id;
	}

	public String getRetire_type_id() {
		return retire_type_id;
	}

	public void setRetire_type_id(String retire_type_id) {
		this.retire_type_id = retire_type_id;
	}

	public String getRetire_nature_id() {
		return retire_nature_id;
	}

	public void setRetire_nature_id(String retire_nature_id) {
		this.retire_nature_id = retire_nature_id;
	}

	public String getRetirement_post() {
		return retirement_post;
	}

	public void setRetirement_post(String retirement_post) {
		this.retirement_post = retirement_post;
	}

	public String getRetirement_duty() {
		return retirement_duty;
	}

	public void setRetirement_duty(String retirement_duty) {
		this.retirement_duty = retirement_duty;
	}

	public String getRetirement_rank_id() {
		return retirement_rank_id;
	}

	public void setRetirement_rank_id(String retirement_rank_id) {
		this.retirement_rank_id = retirement_rank_id;
	}

	public String getNow_treatment_level_id() {
		return now_treatment_level_id;
	}

	public void setNow_treatment_level_id(String now_treatment_level_id) {
		this.now_treatment_level_id = now_treatment_level_id;
	}

	public String getTreatment_approval_time() {
		return treatment_approval_time;
	}

	public void setTreatment_approval_time(String treatment_approval_time) {
		this.treatment_approval_time = treatment_approval_time;
	}

	public String getRetirement_settlement() {
		return retirement_settlement;
	}

	public void setRetirement_settlement(String retirement_settlement) {
		this.retirement_settlement = retirement_settlement;
	}

	public String getIs_earthly_place() {
		return is_earthly_place;
	}

	public void setIs_earthly_place(String is_earthly_place) {
		this.is_earthly_place = is_earthly_place;
	}

	public String getIs_beijing() {
		return is_beijing;
	}

	public void setIs_beijing(String is_beijing) {
		this.is_beijing = is_beijing;
	}

	public String getIs_socialized_pension() {
		return is_socialized_pension;
	}

	public void setIs_socialized_pension(String is_socialized_pension) {
		this.is_socialized_pension = is_socialized_pension;
	}

	public String getIs_medical_insurance() {
		return is_medical_insurance;
	}

	public void setIs_medical_insurance(String is_medical_insurance) {
		this.is_medical_insurance = is_medical_insurance;
	}

	public String getBank_card_number() {
		return bank_card_number;
	}

	public void setBank_card_number(String bank_card_number) {
		this.bank_card_number = bank_card_number;
	}

	public String getChild_working_sys() {
		return child_working_sys;
	}

	public void setChild_working_sys(String child_working_sys) {
		this.child_working_sys = child_working_sys;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getArea_code() {
		return area_code;
	}

	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}

	public String getLandline() {
		return landline;
	}

	public void setLandline(String landline) {
		this.landline = landline;
	}

	public String getSocial_security_area() {
		return social_security_area;
	}

	public void setSocial_security_area(String social_security_area) {
		this.social_security_area = social_security_area;
	}

	public String getArchives_area_id() {
		return archives_area_id;
	}

	public void setArchives_area_id(String archives_area_id) {
		this.archives_area_id = archives_area_id;
	}

	public String getReceive_area_id() {
		return receive_area_id;
	}

	public void setReceive_area_id(String receive_area_id) {
		this.receive_area_id = receive_area_id;
	}

	public String getReceive_area_address() {
		return receive_area_address;
	}

	public void setReceive_area_address(String receive_area_address) {
		this.receive_area_address = receive_area_address;
	}

	public String getArchives_book_number() {
		return archives_book_number;
	}

	public void setArchives_book_number(String archives_book_number) {
		this.archives_book_number = archives_book_number;
	}

	public String getHealth_status() {
		return health_status;
	}

	public void setHealth_status(String health_status) {
		this.health_status = health_status;
	}

	public String getDeceased_time() {
		return deceased_time;
	}

	public void setDeceased_time(String deceased_time) {
		this.deceased_time = deceased_time;
	}

	public String getHealth_status_details() {
		return health_status_details;
	}

	public void setHealth_status_details(String health_status_details) {
		this.health_status_details = health_status_details;
	}

	public String getPlay_a_role_ids() {
		return play_a_role_ids;
	}

	public void setPlay_a_role_ids(String play_a_role_ids) {
		this.play_a_role_ids = play_a_role_ids;
	}

	public String getPlay_a_role_specific() {
		return play_a_role_specific;
	}

	public void setPlay_a_role_specific(String play_a_role_specific) {
		this.play_a_role_specific = play_a_role_specific;
	}

	public String getIs_system_employees() {
		return is_system_employees;
	}

	public void setIs_system_employees(String is_system_employees) {
		this.is_system_employees = is_system_employees;
	}

	public String getSpouse_name() {
		return spouse_name;
	}

	public void setSpouse_name(String spouse_name) {
		this.spouse_name = spouse_name;
	}

	public String getSpouse_unit() {
		return spouse_unit;
	}

	public void setSpouse_unit(String spouse_unit) {
		this.spouse_unit = spouse_unit;
	}

	public String getSpouse_duty() {
		return spouse_duty;
	}

	public void setSpouse_duty(String spouse_duty) {
		this.spouse_duty = spouse_duty;
	}

	public String getSpouse_contact() {
		return spouse_contact;
	}

	public void setSpouse_contact(String spouse_contact) {
		this.spouse_contact = spouse_contact;
	}

	public String getIs_model_personnel() {
		return is_model_personnel;
	}

	public void setIs_model_personnel(String is_model_personnel) {
		this.is_model_personnel = is_model_personnel;
	}

	public String getHonor() {
		return honor;
	}

	public void setHonor(String honor) {
		this.honor = honor;
	}

	public String getPrizes_department() {
		return prizes_department;
	}

	public void setPrizes_department(String prizes_department) {
		this.prizes_department = prizes_department;
	}

	public String getAward_level_id() {
		return award_level_id;
	}

	public void setAward_level_id(String award_level_id) {
		this.award_level_id = award_level_id;
	}

	public String getIs_soldier_cadre() {
		return is_soldier_cadre;
	}

	public void setIs_soldier_cadre(String is_soldier_cadre) {
		this.is_soldier_cadre = is_soldier_cadre;
	}

	public String getMilitary_level() {
		return military_level;
	}

	public void setMilitary_level(String military_level) {
		this.military_level = military_level;
	}

	public String getSpecial_crowd_ids() {
		return special_crowd_ids;
	}

	public void setSpecial_crowd_ids(String special_crowd_ids) {
		this.special_crowd_ids = special_crowd_ids;
	}

	public String getSpecial_remark() {
		return special_remark;
	}

	public void setSpecial_remark(String special_remark) {
		this.special_remark = special_remark;
	}

	public String getAppellation_ids() {
		return appellation_ids;
	}

	public void setAppellation_ids(String appellation_ids) {
		this.appellation_ids = appellation_ids;
	}

	public String getUser_names() {
		return user_names;
	}

	public void setUser_names(String user_names) {
		this.user_names = user_names;
	}

	public String getAge_arrs() {
		return age_arrs;
	}

	public void setAge_arrs(String age_arrs) {
		this.age_arrs = age_arrs;
	}

	public String getWork_unit_posts() {
		return work_unit_posts;
	}

	public void setWork_unit_posts(String work_unit_posts) {
		this.work_unit_posts = work_unit_posts;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}