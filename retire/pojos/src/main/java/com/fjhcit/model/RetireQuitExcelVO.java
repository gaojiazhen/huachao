package com.fjhcit.model;

import com.fjhcit.entity.RetireQuitDO;

/**
 * @description 离（退）休人员_离休人员库_导入Excel数据对象（注意：要和导入Excel模板顺序一致，导入Excel时会按顺序赋值；并且只写Excel有体现的字段 ）
 * @author 陈麟
 * @date 2019年07月13日 下午16:19:27
 */
public class RetireQuitExcelVO extends RetireQuitDO {
	private String idcard;				  //身份号码
    private String monthly_income;        //月收入总额
    private String basic_expenses;        //基本离休费
    private String province_subsidy;      //省（部）规定补贴
    private String other_subsidy;         //其它各项补贴
    private String self_employed_fee;     //自雇费
    private String nursing_fee;           //护理费
    private String car_fare;              //交通费
    private String children_number;       //子女总人数
    private String raising_a_child_number;//无劳动能力需抚养子女人数
    private String spouse_situation;      //配偶状况（1健在 9死亡）
    private String spouse_name;           //配偶姓名
    private String spouse_birth_date;     //配偶出生年月
    private String spouse_is_work;        //配偶有无工作情况（0无工作 1有工作）
    private String spouse_department_name;//配偶工作单位名称
    private String spouse_settlement;     //配偶现安置地
    private String regular_subsidy;       //定期补助/月（元）
    private String spouse_contact;        //配偶联系方式
 
	@Override
	public String toString() {
		return "RetireQuitExcelVO [idcard=" + idcard + ", monthly_income=" + monthly_income + ", basic_expenses="
				+ basic_expenses + ", province_subsidy=" + province_subsidy + ", other_subsidy=" + other_subsidy
				+ ", self_employed_fee=" + self_employed_fee + ", nursing_fee=" + nursing_fee + ", car_fare=" + car_fare
				+ ", children_number=" + children_number + ", raising_a_child_number=" + raising_a_child_number
				+ ", spouse_situation=" + spouse_situation + ", spouse_name=" + spouse_name + ", spouse_birth_date="
				+ spouse_birth_date + ", spouse_is_work=" + spouse_is_work + ", spouse_department_name="
				+ spouse_department_name + ", spouse_settlement=" + spouse_settlement + ", regular_subsidy="
				+ regular_subsidy + ", spouse_contact=" + spouse_contact + ", getMonthly_income()="
				+ getMonthly_income() + ", getBasic_expenses()=" + getBasic_expenses() + ", getProvince_subsidy()="
				+ getProvince_subsidy() + ", getOther_subsidy()=" + getOther_subsidy() + ", getSelf_employed_fee()="
				+ getSelf_employed_fee() + ", getNursing_fee()=" + getNursing_fee() + ", getCar_fare()=" + getCar_fare()
				+ ", getChildren_number()=" + getChildren_number() + ", getRaising_a_child_number()="
				+ getRaising_a_child_number() + ", getSpouse_situation()=" + getSpouse_situation()
				+ ", getSpouse_name()=" + getSpouse_name() + ", getSpouse_birth_date()=" + getSpouse_birth_date()
				+ ", getSpouse_is_work()=" + getSpouse_is_work() + ", getSpouse_department_name()="
				+ getSpouse_department_name() + ", getSpouse_settlement()=" + getSpouse_settlement()
				+ ", getRegular_subsidy()=" + getRegular_subsidy() + ", getSpouse_contact()=" + getSpouse_contact()
				+ ", getIdcard()=" + getIdcard() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	//以下是Get和Set方法
	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getMonthly_income() {
		return monthly_income;
	}

	public void setMonthly_income(String monthly_income) {
		this.monthly_income = monthly_income;
	}

	public String getBasic_expenses() {
		return basic_expenses;
	}

	public void setBasic_expenses(String basic_expenses) {
		this.basic_expenses = basic_expenses;
	}

	public String getProvince_subsidy() {
		return province_subsidy;
	}

	public void setProvince_subsidy(String province_subsidy) {
		this.province_subsidy = province_subsidy;
	}

	public String getOther_subsidy() {
		return other_subsidy;
	}

	public void setOther_subsidy(String other_subsidy) {
		this.other_subsidy = other_subsidy;
	}

	public String getSelf_employed_fee() {
		return self_employed_fee;
	}

	public void setSelf_employed_fee(String self_employed_fee) {
		this.self_employed_fee = self_employed_fee;
	}

	public String getNursing_fee() {
		return nursing_fee;
	}

	public void setNursing_fee(String nursing_fee) {
		this.nursing_fee = nursing_fee;
	}

	public String getCar_fare() {
		return car_fare;
	}

	public void setCar_fare(String car_fare) {
		this.car_fare = car_fare;
	}

	public String getChildren_number() {
		return children_number;
	}

	public void setChildren_number(String children_number) {
		this.children_number = children_number;
	}

	public String getRaising_a_child_number() {
		return raising_a_child_number;
	}

	public void setRaising_a_child_number(String raising_a_child_number) {
		this.raising_a_child_number = raising_a_child_number;
	}

	public String getSpouse_situation() {
		return spouse_situation;
	}

	public void setSpouse_situation(String spouse_situation) {
		this.spouse_situation = spouse_situation;
	}

	public String getSpouse_name() {
		return spouse_name;
	}

	public void setSpouse_name(String spouse_name) {
		this.spouse_name = spouse_name;
	}

	public String getSpouse_birth_date() {
		return spouse_birth_date;
	}

	public void setSpouse_birth_date(String spouse_birth_date) {
		this.spouse_birth_date = spouse_birth_date;
	}

	public String getSpouse_is_work() {
		return spouse_is_work;
	}

	public void setSpouse_is_work(String spouse_is_work) {
		this.spouse_is_work = spouse_is_work;
	}

	public String getSpouse_department_name() {
		return spouse_department_name;
	}

	public void setSpouse_department_name(String spouse_department_name) {
		this.spouse_department_name = spouse_department_name;
	}

	public String getSpouse_settlement() {
		return spouse_settlement;
	}

	public void setSpouse_settlement(String spouse_settlement) {
		this.spouse_settlement = spouse_settlement;
	}

	public String getRegular_subsidy() {
		return regular_subsidy;
	}

	public void setRegular_subsidy(String regular_subsidy) {
		this.regular_subsidy = regular_subsidy;
	}

	public String getSpouse_contact() {
		return spouse_contact;
	}

	public void setSpouse_contact(String spouse_contact) {
		this.spouse_contact = spouse_contact;
	}
}