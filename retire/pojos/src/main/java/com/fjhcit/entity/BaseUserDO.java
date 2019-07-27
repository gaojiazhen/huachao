package com.fjhcit.entity;

/**
 * @description 基础管理_登录人员表_实体类 
 * @author 陈麟
 * @date 2019年06月03日 上午08:14:20
 */
public class BaseUserDO{
    private String id;                  //ID（主键）
    private String user_type;           //账号类型（1长期用户 0临时用户）
    private String unit_id;         	//单位ID（base_department）
    private String department_id;      	//部门ID（base_department）
    private String login_name;          //登录账号
    private String user_name;           //姓名
    private String sex_id;              //性别ID（base_code）
    private String idcard;              //身份证号
    private String phone;               //手机号码
    private String email;               //邮箱
    private String login_password;      //登陆密码
    private String integrality_password;//完整性验证用密码
    private String update_password_time;//修改密码时间
    private String online_time;         //当前在线时间（下线时清空）
    private String last_login_time;     //最后一次登录时间
    private String login_ip;            //登录IP地址
    private String state;               //账号状态（1启用 -13锁定（连续登录错误） -15锁定（越权） -17锁定（管理员） -41休眠 -99注销）
    private String error_count;         //密码错误次数
    private String locked_time;         //账户锁定时间
    private String sleep_time;          //休眠开始时间
    private String limit_time;          //临时账号使用截止时间（二次激活后可以延长3个月）
    private String browser;             //登录浏览器
    private String sortnum;             //排序号
    private String create_user_id;      //创建人ID（base_user）
    private String gmt_create;          //创建时间
    private String modified_user_id;    //维护人ID（base_user）
    private String gmt_modified;        //维护时间
    private String age;					//年龄
    
    /**
     * @description 辅助字段
     */
    private String 	token;		//身份认证（令牌）
    private boolean timeFlag;	//修改密码时间超过预期
    private String 	unit_name;	//单位_名称

    @Override
    public String toString() {
        return "BaseUserDO [id=" + id + ",user_type=" + user_type + ",department_id=" + department_id
                + ",login_name=" + login_name + ",user_name=" + user_name + ",sex_id=" + sex_id
                + ",idcard=" + idcard + ",phone=" + phone + ",email=" + email
                + ",login_password=" + login_password + ",integrality_password=" + integrality_password + ",update_password_time=" + update_password_time
                + ",online_time=" + online_time + ",last_login_time=" + last_login_time + ",login_ip=" + login_ip
                + ",state=" + state + ",error_count=" + error_count + ",locked_time=" + locked_time
                + ",sleep_time=" + sleep_time + ",limit_time=" + limit_time + ",browser=" + browser
                + ",sortnum=" + sortnum + ",create_user_id=" + create_user_id + ",gmt_create=" + gmt_create
                + ",modified_user_id=" + modified_user_id + ",gmt_modified=" + gmt_modified + "]";
    }
	//以下是Get和Set方法
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}
	public String getLogin_name() {
		return login_name;
	}
	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}
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
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLogin_password() {
		return login_password;
	}
	public void setLogin_password(String login_password) {
		this.login_password = login_password;
	}
	public String getIntegrality_password() {
		return integrality_password;
	}
	public void setIntegrality_password(String integrality_password) {
		this.integrality_password = integrality_password;
	}
	public String getUpdate_password_time() {
		return update_password_time;
	}
	public void setUpdate_password_time(String update_password_time) {
		this.update_password_time = update_password_time;
	}
	public String getOnline_time() {
		return online_time;
	}
	public void setOnline_time(String online_time) {
		this.online_time = online_time;
	}
	public String getLast_login_time() {
		return last_login_time;
	}
	public void setLast_login_time(String last_login_time) {
		this.last_login_time = last_login_time;
	}
	public String getLogin_ip() {
		return login_ip;
	}
	public void setLogin_ip(String login_ip) {
		this.login_ip = login_ip;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getError_count() {
		return error_count;
	}
	public void setError_count(String error_count) {
		this.error_count = error_count;
	}
	public String getLocked_time() {
		return locked_time;
	}
	public void setLocked_time(String locked_time) {
		this.locked_time = locked_time;
	}
	public String getSleep_time() {
		return sleep_time;
	}
	public void setSleep_time(String sleep_time) {
		this.sleep_time = sleep_time;
	}
	public String getLimit_time() {
		return limit_time;
	}
	public void setLimit_time(String limit_time) {
		this.limit_time = limit_time;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public boolean isTimeFlag() {
		return timeFlag;
	}
	public void setTimeFlag(boolean timeFlag) {
		this.timeFlag = timeFlag;
	}
	public String getUnit_name() {
		return unit_name;
	}
	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}
	public String getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
}