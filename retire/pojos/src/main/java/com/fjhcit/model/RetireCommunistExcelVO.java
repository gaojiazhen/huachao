package com.fjhcit.model;

import com.fjhcit.entity.RetireCommunistDO;

/**
 * @description 离（退）休人员_中共党员基本情况登记表_导入Excel数据对象（注意：要和导入Excel模板顺序一致，导入Excel时会按顺序赋值；并且只写Excel有体现的字段 ）
 * @author 陈麟
 * @date 2019年07月14日 下午16:04:22
 */
public class RetireCommunistExcelVO extends RetireCommunistDO{
	private String idcard;				  	//身份号码
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
    
	@Override
	public String toString() {
		return "RetireCommunistExcelVO [idcard=" + idcard + ", category_personnel=" + category_personnel
				+ ", party_branch=" + party_branch + ", gmt_party=" + gmt_party + ", gmt_become=" + gmt_become
				+ ", operating_post=" + operating_post + ", membership_credentials_id=" + membership_credentials_id
				+ ", work_unit_post=" + work_unit_post + ", place_detailed=" + place_detailed + ", party_membership="
				+ party_membership + ", party_post=" + party_post + ", party_membership_dues=" + party_membership_dues
				+ ", gmt_paid_until=" + gmt_paid_until + ", is_missing=" + is_missing + ", gmt_missing=" + gmt_missing
				+ ", is_flow_communist=" + is_flow_communist + ", go_out_flow_direction=" + go_out_flow_direction + "]";
	}
	
	//以下是get和set方法
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
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
}