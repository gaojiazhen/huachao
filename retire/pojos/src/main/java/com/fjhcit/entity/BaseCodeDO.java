package com.fjhcit.entity;

/**
 * @description 基础管理_代码设置_实体类 
 * @author 陈麟
 * @date 2019年06月05日 上午09:57:20
 */
public class BaseCodeDO{
    private String kd_code;     	//编码
    private String code;			//编码值
    private String code_val;		//编码值名称
    private String code_superior; 	//父级ID（base_code）
    private String special_mark;    //特殊标志
    private String fl_available;    //启用标志（1启用，0禁止）
    private String seqDisplay;   	//排序号
    private String create_user_id;  //创建人ID（base_user）
    private String gmt_create;      //创建时间
    private String modified_user_id;//维护人ID（base_user）
    private String gmt_modified;    //维护时间
    private String width;			//长度
    private String opr_code;     	//操作员
    
	@Override
	public String toString() {
		return "BaseCodeDO [kd_code=" + kd_code + ", code=" + code + ", code_val=" + code_val + ", code_superior="
				+ code_superior + ", special_mark=" + special_mark + ", fl_available=" + fl_available + ", seqDisplay="
				+ seqDisplay + ", create_user_id=" + create_user_id + ", gmt_create=" + gmt_create
				+ ", modified_user_id=" + modified_user_id + ", gmt_modified=" + gmt_modified + ", width=" + width
				+ ", opr_code=" + opr_code + "]";
	}
	
	//以下是Get和Set方法
	
	public String getKd_code() {
		return kd_code;
	}
	public void setKd_code(String kd_code) {
		this.kd_code = kd_code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCode_val() {
		return code_val;
	}
	public void setCode_val(String code_val) {
		this.code_val = code_val;
	}
	public String getCode_superior() {
		return code_superior;
	}
	public void setCode_superior(String code_superior) {
		this.code_superior = code_superior;
	}
	public String getSpecial_mark() {
		return special_mark;
	}
	public void setSpecial_mark(String special_mark) {
		this.special_mark = special_mark;
	}
	public String getFl_available() {
		return fl_available;
	}
	public void setFl_available(String fl_available) {
		this.fl_available = fl_available;
	}
	public String getSeqDisplay() {
		return seqDisplay;
	}
	public void setSeqDisplay(String seqDisplay) {
		this.seqDisplay = seqDisplay;
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
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getOpr_code() {
		return opr_code;
	}
	public void setOpr_code(String opr_code) {
		this.opr_code = opr_code;
	}
    
}