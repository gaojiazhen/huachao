package com.fjhcit.entity;

/**
 * @description 离（退）休人员_家庭主要成员表_实体类 
 * @author 陈麟
 * @date 2019年06月13日 上午11:02:09
 */
public class RetireFamilyDO{
    private String id;                //主键ID
    private String user_id;           //人员ID（RETIRE_USER）
    private String appellation_id;    //称谓ID（base_code）
    private String user_name;         //姓名
    private String age;               //年龄
    private String politics_status_id;//政治面貌ID（base_code）
    private String work_unit_post;    //工作单位及职务
    private String contact;           //联系方式
    private String remark;			  //备注
    private String create_user_id;    //创建人ID（base_user）
    private String gmt_create;        //创建时间
    private String modified_user_id;  //维护人ID（base_user）
    private String gmt_modified;      //维护时间

    @Override
    public String toString() {
        return "RetireFamilyDO [user_name=" + user_name + ",age=" + age + ",politics_status_id=" + politics_status_id
                + ",work_unit_post=" + work_unit_post + ",contact=" + contact + ",create_user_id=" + create_user_id
                + ",gmt_create=" + gmt_create + ",modified_user_id=" + modified_user_id + ",gmt_modified=" + gmt_modified
                + ",id=" + id + ",user_id=" + user_id + ",appellation_id=" + appellation_id + "]";
    }
	//以下是Get和Set方法
    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getPolitics_status_id() {
        return politics_status_id;
    }
    public void setPolitics_status_id(String politics_status_id) {
        this.politics_status_id = politics_status_id;
    }
    public String getWork_unit_post() {
        return work_unit_post;
    }
    public void setWork_unit_post(String work_unit_post) {
        this.work_unit_post = work_unit_post;
    }
    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
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
	public String getAppellation_id() {
		return appellation_id;
	}
	public void setAppellation_id(String appellation_id) {
		this.appellation_id = appellation_id;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}