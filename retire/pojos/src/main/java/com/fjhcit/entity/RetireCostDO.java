package com.fjhcit.entity;

/**
 * @description 离（退）休人员_单位运行成本费用_实体类 
 * @author 陈麟
 * @date 2019年06月24日 上午11:57:51
 */
public class RetireCostDO{
    private String id;                       //ID主键
    private String unit_id;                  //单位ID（base_department）
    private String year;                     //年度
    private String utilities;                //水电气暖费用
    private String emolument;                //薪酬及附加
    private String depreciation;             //折旧及摊销
    private String chummage;                 //房租
    private String office_allowance;         //办公费
    private String travel_expense;           //交通费（差旅费）
    private String convention_expense;       //会议费
    private String publicity_expense;       //宣传费
    
    private String train_expense;       	//培训费
    private String train_count;       		//培训情况_次数
    private String train_man_time;       	//培训情况_人次
    private String train_other;       		//培训情况_参加其他培训人次

    private String other;                    //其他
    private String remark;                   //备注
    private String expense_total;            //退休人员相关费用情况合计（万元）
    private String supplementary_medical;    //在成本中列支的补充医疗保险费（万元）
    private String monthly_living_allowance; //统筹外按月生活补贴（万元）
    private String one_time_living_allowance;//统筹外一次性生活补贴（万元）
    private String medical_fee;              //统筹外医疗费（保险费）（万元）
    private String modified_user_id;         //维护人ID（base_user）
    private String activity_funds;           //统筹外活动经费（万元）
    private String subsidies_for_heating;    //统筹外供暖费补贴（万元）
    private String other_expenses;           //统筹外其他费用（万元）
    private String retire_basic_pension;	 //退休人员基本养老金（元/月）
    private String basic_local_pension;     //地方基本养老金水平（元/月）
    private String create_user_id;           //创建人ID（base_user）
    private String gmt_create;               //创建时间
    private String gmt_modified;             //维护时间

	@Override
	public String toString() {
		return "RetireCostDO [id=" + id + ", unit_id=" + unit_id + ", year=" + year + ", utilities=" + utilities
				+ ", emolument=" + emolument + ", depreciation=" + depreciation + ", chummage=" + chummage
				+ ", office_allowance=" + office_allowance + ", travel_expense=" + travel_expense
				+ ", convention_expense=" + convention_expense + ", publicity_expense=" + publicity_expense
				+ ", train_expense=" + train_expense + ", train_count=" + train_count + ", train_man_time="
				+ train_man_time + ", train_other=" + train_other + ", other=" + other + ", remark=" + remark
				+ ", expense_total=" + expense_total + ", supplementary_medical=" + supplementary_medical
				+ ", monthly_living_allowance=" + monthly_living_allowance + ", one_time_living_allowance="
				+ one_time_living_allowance + ", medical_fee=" + medical_fee + ", modified_user_id=" + modified_user_id
				+ ", activity_funds=" + activity_funds + ", subsidies_for_heating=" + subsidies_for_heating
				+ ", other_expenses=" + other_expenses + ", retire_basic_pension=" + retire_basic_pension
				+ ", basic_local_pension=" + basic_local_pension + ", create_user_id=" + create_user_id
				+ ", gmt_create=" + gmt_create + ", gmt_modified=" + gmt_modified + ", getGmt_modified()="
				+ getGmt_modified() + ", getId()=" + getId() + ", getUnit_id()=" + getUnit_id() + ", getYear()="
				+ getYear() + ", getUtilities()=" + getUtilities() + ", getEmolument()=" + getEmolument()
				+ ", getDepreciation()=" + getDepreciation() + ", getChummage()=" + getChummage()
				+ ", getOffice_allowance()=" + getOffice_allowance() + ", getTravel_expense()=" + getTravel_expense()
				+ ", getConvention_expense()=" + getConvention_expense() + ", getOther()=" + getOther()
				+ ", getRemark()=" + getRemark() + ", getExpense_total()=" + getExpense_total()
				+ ", getSupplementary_medical()=" + getSupplementary_medical() + ", getMonthly_living_allowance()="
				+ getMonthly_living_allowance() + ", getOne_time_living_allowance()=" + getOne_time_living_allowance()
				+ ", getMedical_fee()=" + getMedical_fee() + ", getModified_user_id()=" + getModified_user_id()
				+ ", getActivity_funds()=" + getActivity_funds() + ", getSubsidies_for_heating()="
				+ getSubsidies_for_heating() + ", getOther_expenses()=" + getOther_expenses() + ", getCreate_user_id()="
				+ getCreate_user_id() + ", getGmt_create()=" + getGmt_create() + ", getRetire_basic_pension()="
				+ getRetire_basic_pension() + ", getTrain_expense()=" + getTrain_expense() + ", getTrain_count()="
				+ getTrain_count() + ", getTrain_man_time()=" + getTrain_man_time() + ", getTrain_other()="
				+ getTrain_other() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	//以下是Get和Set方法
    public String getGmt_modified() {
        return gmt_modified;
    }
    public void setGmt_modified(String gmt_modified) {
        this.gmt_modified = gmt_modified;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUnit_id() {
        return unit_id;
    }
    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public String getUtilities() {
        return utilities;
    }
    public void setUtilities(String utilities) {
        this.utilities = utilities;
    }
    public String getEmolument() {
        return emolument;
    }
    public void setEmolument(String emolument) {
        this.emolument = emolument;
    }
    public String getDepreciation() {
        return depreciation;
    }
    public void setDepreciation(String depreciation) {
        this.depreciation = depreciation;
    }
    public String getChummage() {
        return chummage;
    }
    public void setChummage(String chummage) {
        this.chummage = chummage;
    }
    public String getOffice_allowance() {
        return office_allowance;
    }
    public void setOffice_allowance(String office_allowance) {
        this.office_allowance = office_allowance;
    }
    public String getTravel_expense() {
        return travel_expense;
    }
    public void setTravel_expense(String travel_expense) {
        this.travel_expense = travel_expense;
    }
    public String getConvention_expense() {
        return convention_expense;
    }
    public void setConvention_expense(String convention_expense) {
        this.convention_expense = convention_expense;
    }
    public String getOther() {
        return other;
    }
    public void setOther(String other) {
        this.other = other;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getExpense_total() {
        return expense_total;
    }
    public void setExpense_total(String expense_total) {
        this.expense_total = expense_total;
    }
    public String getSupplementary_medical() {
        return supplementary_medical;
    }
    public void setSupplementary_medical(String supplementary_medical) {
        this.supplementary_medical = supplementary_medical;
    }
    public String getMonthly_living_allowance() {
        return monthly_living_allowance;
    }
    public void setMonthly_living_allowance(String monthly_living_allowance) {
        this.monthly_living_allowance = monthly_living_allowance;
    }
    public String getOne_time_living_allowance() {
        return one_time_living_allowance;
    }
    public void setOne_time_living_allowance(String one_time_living_allowance) {
        this.one_time_living_allowance = one_time_living_allowance;
    }
    public String getMedical_fee() {
        return medical_fee;
    }
    public void setMedical_fee(String medical_fee) {
        this.medical_fee = medical_fee;
    }
    public String getModified_user_id() {
        return modified_user_id;
    }
    public void setModified_user_id(String modified_user_id) {
        this.modified_user_id = modified_user_id;
    }
    public String getActivity_funds() {
        return activity_funds;
    }
    public void setActivity_funds(String activity_funds) {
        this.activity_funds = activity_funds;
    }
    public String getSubsidies_for_heating() {
        return subsidies_for_heating;
    }
    public void setSubsidies_for_heating(String subsidies_for_heating) {
        this.subsidies_for_heating = subsidies_for_heating;
    }
    public String getOther_expenses() {
        return other_expenses;
    }
    public void setOther_expenses(String other_expenses) {
        this.other_expenses = other_expenses;
    }
    public String getCreate_user_id() {
        return create_user_id;
    }
    public void setCreate_user_id(String create_user_id) {
        this.create_user_id = create_user_id;
    }
    public String getGmt_create() {
        return gmt_create;
    }
    public void setGmt_create(String gmt_create) {
        this.gmt_create = gmt_create;
    }
	public String getRetire_basic_pension() {
		return retire_basic_pension;
	}
	public void setRetire_basic_pension(String retire_basic_pension) {
		this.retire_basic_pension = retire_basic_pension;
	}
	public String getTrain_expense() {
		return train_expense;
	}
	public void setTrain_expense(String train_expense) {
		this.train_expense = train_expense;
	}
	public String getTrain_count() {
		return train_count;
	}
	public void setTrain_count(String train_count) {
		this.train_count = train_count;
	}
	public String getTrain_man_time() {
		return train_man_time;
	}
	public void setTrain_man_time(String train_man_time) {
		this.train_man_time = train_man_time;
	}
	public String getTrain_other() {
		return train_other;
	}
	public void setTrain_other(String train_other) {
		this.train_other = train_other;
	}
	public String getPublicity_expense() {
		return publicity_expense;
	}
	public void setPublicity_expense(String publicity_expense) {
		this.publicity_expense = publicity_expense;
	}
	public String getBasic_local_pension() {
		return basic_local_pension;
	}
	public void setBasic_local_pension(String basic_local_pension) {
		this.basic_local_pension = basic_local_pension;
	}
}