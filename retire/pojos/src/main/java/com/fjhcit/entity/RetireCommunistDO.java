package com.fjhcit.entity;

/**
 * @description 离（退）休人员_中共党员基本情况登记表_实体类 
 * @author 陈麟
 * @date 2019年07月14日 下午16:04:22
 */
public class RetireCommunistDO{
    private String id;                       //主键ID
    private String user_id;                  //人员ID（RETIRE_USER）
    private String category_personnel;       //人员类别（1正式党员） 
    private String party_branch;             //目前所在党支部
    private String gmt_party;                //入党日期
    private String gmt_become;               //转正日期
    private String operating_post;           //工作岗位
    private String membership_credentials_id;//组织关系所在单位（base_code）
    private String work_unit_post;           //工作单位及职务
    private String place_detailed;           //地方详细
    private String party_membership;         //党籍状态(1正常 0不正常)
    private String party_post;               //现任党内职务
    private String party_membership_dues;    //月应缴纳党费（元）
    private String gmt_paid_until;           //党费缴至年月
    private String is_missing;               //是否为失联党员（1是 0否）
    private String gmt_missing;              //失去联系日期
    private String is_flow_communist;        //是否为流动党员（1是 0否）
    private String go_out_flow_direction;    //外出流向
    private String create_user_id;           //创建人ID（base_user）
    private String gmt_create;               //创建时间
    private String modified_user_id;         //维护人ID（base_user）
    private String gmt_modified;             //维护时间

    /**
     * @description 辅助字段
     */
    private String unit_id;					//人员单位ID
    private String user_name;              	//姓名
	private String sex_id;			   		//性别_编码
    private String sex_name;			   	//性别_名称
    private String birth_date;             	//出生年月
	private String nation_name;            	//民族_名称
    private String native_place;           	//籍贯
    private String marital_status_name;	  	//婚姻状况_名称
    private String idcard;        		   	//身份号码
    private String create_user_name;	   	//创建人_姓名
    private String modified_user_name;	   	//维护人_姓名
    private String membership_credentials_name;		//党组织关系所在地名称
    private String membership_credentials_mark;		//党组织关系所在地ID（base_code）_特殊标志
    private String residence_address;      	//现居住地详细地址   
    
	@Override
	public String toString() {
		return "RetireCommunistDO [id=" + id + ", user_id=" + user_id + ", category_personnel=" + category_personnel
				+ ", party_branch=" + party_branch + ", gmt_party=" + gmt_party + ", gmt_become=" + gmt_become
				+ ", operating_post=" + operating_post + ", membership_credentials_id=" + membership_credentials_id
				+ ", work_unit_post=" + work_unit_post + ", place_detailed=" + place_detailed + ", party_membership="
				+ party_membership + ", party_post=" + party_post + ", party_membership_dues=" + party_membership_dues
				+ ", gmt_paid_until=" + gmt_paid_until + ", is_missing=" + is_missing + ", gmt_missing=" + gmt_missing
				+ ", is_flow_communist=" + is_flow_communist + ", go_out_flow_direction=" + go_out_flow_direction
				+ ", create_user_id=" + create_user_id + ", gmt_create=" + gmt_create + ", modified_user_id="
				+ modified_user_id + ", gmt_modified=" + gmt_modified + ", unit_id=" + unit_id + ", user_name="
				+ user_name + ", sex_name=" + sex_name + ", birth_date=" + birth_date + ", nation_name=" + nation_name
				+ ", native_place=" + native_place + ", marital_status_name=" + marital_status_name + ", idcard="
				+ idcard + ", create_user_name=" + create_user_name + ", modified_user_name=" + modified_user_name
				+ ", membership_credentials_name=" + membership_credentials_name + ", membership_credentials_mark="
				+ membership_credentials_mark + ", residence_address=" + residence_address + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	//以下是get和set方法
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getCategory_personnel() {
		return category_personnel;
	}
	public void setCategory_personnel(String category_personnel) {
		this.category_personnel = category_personnel;
	}
	public String getParty_branch() {
		return party_branch;
	}
	public void setParty_branch(String party_branch) {
		this.party_branch = party_branch;
	}
	public String getGmt_party() {
		return gmt_party;
	}
	public void setGmt_party(String gmt_party) {
		this.gmt_party = gmt_party;
	}
	public String getGmt_become() {
		return gmt_become;
	}
	public void setGmt_become(String gmt_become) {
		this.gmt_become = gmt_become;
	}
	public String getOperating_post() {
		return operating_post;
	}
	public void setOperating_post(String operating_post) {
		this.operating_post = operating_post;
	}
	public String getMembership_credentials_id() {
		return membership_credentials_id;
	}
	public void setMembership_credentials_id(String membership_credentials_id) {
		this.membership_credentials_id = membership_credentials_id;
	}
	public String getWork_unit_post() {
		return work_unit_post;
	}
	public void setWork_unit_post(String work_unit_post) {
		this.work_unit_post = work_unit_post;
	}
	public String getPlace_detailed() {
		return place_detailed;
	}
	public void setPlace_detailed(String place_detailed) {
		this.place_detailed = place_detailed;
	}
	public String getParty_membership() {
		return party_membership;
	}
	public void setParty_membership(String party_membership) {
		this.party_membership = party_membership;
	}
	public String getParty_post() {
		return party_post;
	}
	public void setParty_post(String party_post) {
		this.party_post = party_post;
	}
	public String getParty_membership_dues() {
		return party_membership_dues;
	}
	public void setParty_membership_dues(String party_membership_dues) {
		this.party_membership_dues = party_membership_dues;
	}
	public String getGmt_paid_until() {
		return gmt_paid_until;
	}
	public void setGmt_paid_until(String gmt_paid_until) {
		this.gmt_paid_until = gmt_paid_until;
	}
	public String getIs_missing() {
		return is_missing;
	}
	public void setIs_missing(String is_missing) {
		this.is_missing = is_missing;
	}
	public String getGmt_missing() {
		return gmt_missing;
	}
	public void setGmt_missing(String gmt_missing) {
		this.gmt_missing = gmt_missing;
	}
	public String getIs_flow_communist() {
		return is_flow_communist;
	}
	public void setIs_flow_communist(String is_flow_communist) {
		this.is_flow_communist = is_flow_communist;
	}
	public String getGo_out_flow_direction() {
		return go_out_flow_direction;
	}
	public void setGo_out_flow_direction(String go_out_flow_direction) {
		this.go_out_flow_direction = go_out_flow_direction;
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
	public String getModified_user_id() {
		return modified_user_id;
	}
	public void setModified_user_id(String modified_user_id) {
		this.modified_user_id = modified_user_id;
	}
	public String getGmt_modified() {
		return gmt_modified;
	}
	public void setGmt_modified(String gmt_modified) {
		this.gmt_modified = gmt_modified;
	}
	public String getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getSex_name() {
		return sex_name;
	}
	public void setSex_name(String sex_name) {
		this.sex_name = sex_name;
	}
	public String getBirth_date() {
		return birth_date;
	}
	public void setBirth_date(String birth_date) {
		this.birth_date = birth_date;
	}
	public String getNation_name() {
		return nation_name;
	}
	public void setNation_name(String nation_name) {
		this.nation_name = nation_name;
	}
	public String getNative_place() {
		return native_place;
	}
	public void setNative_place(String native_place) {
		this.native_place = native_place;
	}
	public String getMarital_status_name() {
		return marital_status_name;
	}
	public void setMarital_status_name(String marital_status_name) {
		this.marital_status_name = marital_status_name;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getCreate_user_name() {
		return create_user_name;
	}
	public void setCreate_user_name(String create_user_name) {
		this.create_user_name = create_user_name;
	}
	public String getModified_user_name() {
		return modified_user_name;
	}
	public void setModified_user_name(String modified_user_name) {
		this.modified_user_name = modified_user_name;
	}
	public String getMembership_credentials_name() {
		return membership_credentials_name;
	}
	public void setMembership_credentials_name(String membership_credentials_name) {
		this.membership_credentials_name = membership_credentials_name;
	}
	public String getMembership_credentials_mark() {
		return membership_credentials_mark;
	}
	public void setMembership_credentials_mark(String membership_credentials_mark) {
		this.membership_credentials_mark = membership_credentials_mark;
	}
	public String getResidence_address() {
		return residence_address;
	}
	public void setResidence_address(String residence_address) {
		this.residence_address = residence_address;
	}

	public String getSex_id() {
		return sex_id;
	}

	public void setSex_id(String sex_id) {
		this.sex_id = sex_id;
	}
}