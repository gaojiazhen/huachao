package com.fjhcit.model;

import com.fjhcit.entity.RetireContactDO;

/**
 * @description 离（退）休人员_在职人员通讯录库_导入Excel数据对象（注意：要和导入Excel模板顺序一致，导入Excel时会按顺序赋值；并且只写Excel有体现的字段 ）
 * @author 陈麟
 * @date 2019年06月14日 上午08:17:13
 */
public class RetireContactExcelVO extends RetireContactDO {
    private String department_id;   //部门ID（base_department）
    private String user_name;       //姓名
    private String sex_code;		//性别_编码（base_code）
    private String education_code;	//学历_编码（base_code）
    private String idcard;          //身份证号码
    private String birth_date;      //出生年月
    private String user_nature_id;  //人员性质ID
    private String user_rank_id;    //人员职级ID
    private String office_duty;     //处室/职务
    private String work_seniority;  //从事离退办工作年限（年）
    private String dial_directly;   //办公电话-直拨
    private String system_number;   //办公电话-系统号
    private String phone;           //手机

	@Override
	public String toString() {
		return "RetireContactExcelVO [department_id=" + department_id + ", user_name=" + user_name + ", sex_code="
				+ sex_code + ", education_code=" + education_code + ", idcard=" + idcard + ", birth_date=" + birth_date
				+ ", user_nature_id=" + user_nature_id + ", user_rank_id=" + user_rank_id + ", office_duty="
				+ office_duty + ", work_seniority=" + work_seniority + ", dial_directly=" + dial_directly
				+ ", system_number=" + system_number + ", phone=" + phone + "]";
	}
	
	//以下是get和set方法
	public String getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getSex_code() {
		return sex_code;
	}

	public void setSex_code(String sex_code) {
		this.sex_code = sex_code;
	}

	public String getEducation_code() {
		return education_code;
	}

	public void setEducation_code(String education_code) {
		this.education_code = education_code;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getBirth_date() {
		return birth_date;
	}

	public void setBirth_date(String birth_date) {
		this.birth_date = birth_date;
	}

	public String getUser_nature_id() {
		return user_nature_id;
	}

	public void setUser_nature_id(String user_nature_id) {
		this.user_nature_id = user_nature_id;
	}

	public String getUser_rank_id() {
		return user_rank_id;
	}

	public void setUser_rank_id(String user_rank_id) {
		this.user_rank_id = user_rank_id;
	}

	public String getOffice_duty() {
		return office_duty;
	}

	public void setOffice_duty(String office_duty) {
		this.office_duty = office_duty;
	}

	public String getWork_seniority() {
		return work_seniority;
	}

	public void setWork_seniority(String work_seniority) {
		this.work_seniority = work_seniority;
	}

	public String getDial_directly() {
		return dial_directly;
	}

	public void setDial_directly(String dial_directly) {
		this.dial_directly = dial_directly;
	}

	public String getSystem_number() {
		return system_number;
	}

	public void setSystem_number(String system_number) {
		this.system_number = system_number;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}