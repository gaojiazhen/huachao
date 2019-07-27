package com.fjhcit.base.enums;

/**
 * @Description：用户账号状态_枚举类
 * @author：陈 麟
 * @date：2019年06月03日 上午10:20:49
 */
public enum AccountEnum {

	ACCSTATUS_ACTIVE(1,"启用"),
	
	ACCSTATUS_LOCKED(-13,"锁定（连续登录错误）"),
	
	ACCSTATUS_ULOCKED(-15,"锁定（越权）"),
	
	ACCSTATUS_SLOCKED(-17,"锁定（管理员）"),
	
	ACCSTATUS_SLEEP(-41,"休眠"),
	
	ACCSTATUS_CANCEL(-99,"注销");
	
	private int key;
	private String desc;
	
	/**
	 * 构造方法
	 * @param key
	 * @param desc
	 */
	private AccountEnum(int key,String desc){
		this.key = key;
		this.desc = desc;
	}
	
	//以下是Get和Set方法
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}