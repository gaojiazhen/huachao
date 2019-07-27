package com.fjhcit.model;

import com.fjhcit.entity.RetireArenaDO;

/**
 * @description 离（退）休人员_活动场所及老年大学建设情况统计表_导入Excel数据对象
 * （注意：要和导入Excel模板顺序一致，导入Excel时会按顺序赋值；并且只写Excel有体现的字段 ）
 * @author 陈麟
 * @date 2019年06月26日 上午08:55:44
 */
public class RetireArenaExcelVO extends RetireArenaDO {
    private String unit_id;                     //单位ID（base_department）
    private String existing_sum;                //现有活动场所_总数（个）
    private String existing_acreage;            //现有活动场所_总建筑面积（平方米）
    private String existing_everyday;           //现有活动场所_日均活动人数
    private String abuilding_sum;               //在建活动场所_总数（个）
    private String abuilding_acreage;           //在建活动场所_总建筑面积（平方米）
    private String independent_sum;             //退休人员独立使用_总数（个）
    private String independent_acreage;         //退休人员独立使用_总建筑面积（平方米）
    private String independent_original_value;	//退休人员独立使用_年末固定资产情况（万元）原值
    private String independent_net_value;     	//退休人员独立使用_年末固定资产情况（万元）净值
    private String common_sum;                  //与在职人员共同使用_总数（个）
    private String common_acreage;              //与在职人员共同使用_总建筑面积（平方米）
    private String common_original_value;   	//与在职人员共同使用_年末固定资产情况（万元）原值
    private String common_net_value;         	//与在职人员共同使用_年末固定资产情况（万元）净值
    private String eligible_sum;                //具备移交条件的活动场所_总数（个）
    private String eligible_acreage;            //具备移交条件的活动场所_总建筑面积（平方米）
    private String eligible_original_value;  	//具备移交条件的活动场所_年末固定资产情况（万元）原值
    private String eligible_net_value;     		//具备移交条件的活动场所_年末固定资产情况（万元）净值
    private String fixed_assets_original_value; //年末固定资产情况（万元）原值
    private String fixed_assets_net_value;      //年末固定资产情况（万元）净值
    private String university_sum;              //现有老年大学_总数（个）
    private String university_acreage;          //现有老年大学_总建筑面积（平方米）
    private String university_regular_staff;    //现有老年大学_正式工作人员
    private String university_students;         //现有老年大学_在读学员人数
    private String university_graduate;         //现有老年大学_累计结业人数
    private String abuilding_university_sum;    //在建老年大学_总数（个）
    private String abuilding_university_acreage;//在建老年大学_总建筑面积（平方米）
    private String attend_students;             //就读社会老年大学_在读学员人数
    private String attend_graduate;             //就读社会老年大学_累计结业人数
    
	@Override
	public String toString() {
		return "RetireArenaExcelVO [unit_id=" + unit_id + ", existing_sum=" + existing_sum + ", existing_acreage="
				+ existing_acreage + ", existing_everyday=" + existing_everyday + ", abuilding_sum=" + abuilding_sum
				+ ", abuilding_acreage=" + abuilding_acreage + ", independent_sum=" + independent_sum
				+ ", independent_acreage=" + independent_acreage + ", independent_original_value="
				+ independent_original_value + ", independent_net_value=" + independent_net_value + ", common_sum="
				+ common_sum + ", common_acreage=" + common_acreage + ", common_original_value=" + common_original_value
				+ ", common_net_value=" + common_net_value + ", eligible_sum=" + eligible_sum + ", eligible_acreage="
				+ eligible_acreage + ", eligible_original_value=" + eligible_original_value + ", eligible_net_value="
				+ eligible_net_value + ", fixed_assets_original_value=" + fixed_assets_original_value
				+ ", fixed_assets_net_value=" + fixed_assets_net_value + ", university_sum=" + university_sum
				+ ", university_acreage=" + university_acreage + ", university_regular_staff="
				+ university_regular_staff + ", university_students=" + university_students + ", university_graduate="
				+ university_graduate + ", abuilding_university_sum=" + abuilding_university_sum
				+ ", abuilding_university_acreage=" + abuilding_university_acreage + ", attend_students="
				+ attend_students + ", attend_graduate=" + attend_graduate + "]";
	}
	
	//以下是Get和Set方法
    public String getUnit_id() {
        return unit_id;
    }
    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }
    public String getExisting_sum() {
        return existing_sum;
    }
    public void setExisting_sum(String existing_sum) {
        this.existing_sum = existing_sum;
    }
    public String getExisting_acreage() {
        return existing_acreage;
    }
    public void setExisting_acreage(String existing_acreage) {
        this.existing_acreage = existing_acreage;
    }
    public String getExisting_everyday() {
        return existing_everyday;
    }
    public void setExisting_everyday(String existing_everyday) {
        this.existing_everyday = existing_everyday;
    }
    public String getAbuilding_sum() {
        return abuilding_sum;
    }
    public void setAbuilding_sum(String abuilding_sum) {
        this.abuilding_sum = abuilding_sum;
    }
    public String getAbuilding_acreage() {
        return abuilding_acreage;
    }
    public void setAbuilding_acreage(String abuilding_acreage) {
        this.abuilding_acreage = abuilding_acreage;
    }
    public String getIndependent_sum() {
        return independent_sum;
    }
    public void setIndependent_sum(String independent_sum) {
        this.independent_sum = independent_sum;
    }
    public String getIndependent_acreage() {
        return independent_acreage;
    }
    public void setIndependent_acreage(String independent_acreage) {
        this.independent_acreage = independent_acreage;
    }
    public String getCommon_sum() {
        return common_sum;
    }
    public void setCommon_sum(String common_sum) {
        this.common_sum = common_sum;
    }
    public String getCommon_acreage() {
        return common_acreage;
    }
    public void setCommon_acreage(String common_acreage) {
        this.common_acreage = common_acreage;
    }
    public String getEligible_sum() {
        return eligible_sum;
    }
    public void setEligible_sum(String eligible_sum) {
        this.eligible_sum = eligible_sum;
    }
    public String getEligible_acreage() {
        return eligible_acreage;
    }
    public void setEligible_acreage(String eligible_acreage) {
        this.eligible_acreage = eligible_acreage;
    }
    public String getFixed_assets_original_value() {
        return fixed_assets_original_value;
    }
    public void setFixed_assets_original_value(String fixed_assets_original_value) {
        this.fixed_assets_original_value = fixed_assets_original_value;
    }
    public String getFixed_assets_net_value() {
        return fixed_assets_net_value;
    }
    public void setFixed_assets_net_value(String fixed_assets_net_value) {
        this.fixed_assets_net_value = fixed_assets_net_value;
    }
    public String getUniversity_sum() {
        return university_sum;
    }
    public void setUniversity_sum(String university_sum) {
        this.university_sum = university_sum;
    }
    public String getUniversity_acreage() {
        return university_acreage;
    }
    public void setUniversity_acreage(String university_acreage) {
        this.university_acreage = university_acreage;
    }
    public String getUniversity_regular_staff() {
        return university_regular_staff;
    }
    public void setUniversity_regular_staff(String university_regular_staff) {
        this.university_regular_staff = university_regular_staff;
    }
    public String getUniversity_students() {
        return university_students;
    }
    public void setUniversity_students(String university_students) {
        this.university_students = university_students;
    }
    public String getUniversity_graduate() {
        return university_graduate;
    }
    public void setUniversity_graduate(String university_graduate) {
        this.university_graduate = university_graduate;
    }
    public String getAbuilding_university_sum() {
        return abuilding_university_sum;
    }
    public void setAbuilding_university_sum(String abuilding_university_sum) {
        this.abuilding_university_sum = abuilding_university_sum;
    }
    public String getAbuilding_university_acreage() {
        return abuilding_university_acreage;
    }
    public void setAbuilding_university_acreage(String abuilding_university_acreage) {
        this.abuilding_university_acreage = abuilding_university_acreage;
    }
    public String getAttend_students() {
        return attend_students;
    }
    public void setAttend_students(String attend_students) {
        this.attend_students = attend_students;
    }
    public String getAttend_graduate() {
        return attend_graduate;
    }
    public void setAttend_graduate(String attend_graduate) {
        this.attend_graduate = attend_graduate;
    }
	public String getIndependent_original_value() {
		return independent_original_value;
	}
	public void setIndependent_original_value(String independent_original_value) {
		this.independent_original_value = independent_original_value;
	}
	public String getIndependent_net_value() {
		return independent_net_value;
	}
	public void setIndependent_net_value(String independent_net_value) {
		this.independent_net_value = independent_net_value;
	}
	public String getCommon_original_value() {
		return common_original_value;
	}
	public void setCommon_original_value(String common_original_value) {
		this.common_original_value = common_original_value;
	}
	public String getCommon_net_value() {
		return common_net_value;
	}
	public void setCommon_net_value(String common_net_value) {
		this.common_net_value = common_net_value;
	}
	public String getEligible_original_value() {
		return eligible_original_value;
	}
	public void setEligible_original_value(String eligible_original_value) {
		this.eligible_original_value = eligible_original_value;
	}
	public String getEligible_net_value() {
		return eligible_net_value;
	}
	public void setEligible_net_value(String eligible_net_value) {
		this.eligible_net_value = eligible_net_value;
	}
}