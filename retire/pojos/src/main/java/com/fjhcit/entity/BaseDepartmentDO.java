package com.fjhcit.entity;

/**
 * @description 基础管理_组织机构表_实体类 
 * @author 陈麟
 * @date 2019年06月04日 下午19:49:25
 */
public class BaseDepartmentDO{
    private String id;              //ID（主键）
    private String department_name; //单位名称
    private String short_name;      //单位简称
    private String parent_id;       //父级单位ID（base_department）
    private String is_available;    //启用标志（1启用 0禁止）
    private String sortnum;         //排序号
    private String create_user_id;  //创建人ID（base_user）
    private String gmt_create;      //创建时间
    private String modified_user_id;//维护人ID（base_user）
    private String gmt_modified;    //维护时间
    
    /**
     * @description 辅助字段
     */
    private String party_id;		//党组织情况设置表ID

    @Override
    public String toString() {
        return "BaseDepartmentDO [id=" + id + ",department_name=" + department_name + ",short_name=" + short_name
                + ",parent_id=" + parent_id + ",is_available=" + is_available + ",sortnum=" + sortnum
                + ",create_user_id=" + create_user_id + ",gmt_create=" + gmt_create + ",modified_user_id=" + modified_user_id
                + ",gmt_modified=" + gmt_modified + "]";
    }
	//以下是Get和Set方法
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDepartment_name() {
        return department_name;
    }
    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }
    public String getShort_name() {
        return short_name;
    }
    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }
    public String getParent_id() {
        return parent_id;
    }
    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }
    public String getIs_available() {
        return is_available;
    }
    public void setIs_available(String is_available) {
        this.is_available = is_available;
    }
    public String getSortnum() {
        return sortnum;
    }
    public void setSortnum(String sortnum) {
        this.sortnum = sortnum;
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
	public String getParty_id() {
		return party_id;
	}
	public void setParty_id(String party_id) {
		this.party_id = party_id;
	}
}