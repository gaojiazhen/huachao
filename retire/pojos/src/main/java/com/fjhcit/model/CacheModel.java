package com.fjhcit.model;

import java.util.List;
/**
 * 数据字典模型
 * 
 * @author cz
 *
 */
public class CacheModel {
	  private String value;
	  private String name;
	  private String kd_code;
	  private String special_mark;
	  private String code_superior;
	  private List<Object> children;
	  
	public String getCode_superior() {
		return code_superior;
	}
	public void setCode_superior(String code_superior) {
		this.code_superior = code_superior;
	}
	public String getValue() {
		return value;
	}
	public List<Object> getChildren() {
		return children;
	}
	public void setChildren(List<Object> children) {
		this.children = children;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKd_code() {
		return kd_code;
	}
	public void setKd_code(String kd_code) {
		this.kd_code = kd_code;
	}
	public String getSpecial_mark() {
		return special_mark;
	}
	public void setSpecial_mark(String special_mark) {
		this.special_mark = special_mark;
	}
}