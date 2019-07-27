package com.fjhcit.model;

import java.io.Serializable;

/**
 * @Description：SpringBoot定义统一返回result对象
 * @author：陈 麟
 * @date：2019年5月31日 下午4:32:37
 */
public class ResultVO implements Serializable {

	private static final long serialVersionUID = 1537865612667875802L;

	private Object 	data; 		//返回数据
	private boolean success; 	//操作是否成功
	private String 	message; 	//提示信息
	/**
	 * 分页时才用到
	 */
	private Long 	total;		//总条数
	private Integer pageNum;	//第几页
	private Integer pageSize;	//每页多少条

	public ResultVO() {

	}

	public ResultVO(Object data) {
		this.success = true;
		this.data = data;
	}

	public ResultVO(boolean success) {
		this.success = success;
	}

	public ResultVO(Object data, boolean success, String message) {
		this.data = data;
		this.success = success;
		this.message = message;
	}
	
	public ResultVO(Object data, boolean success, String message, Long total, Integer pageNum, Integer pageSize) {
		this.data = data;
		this.success = success;
		this.message = message;
		this.total = total;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	//以下是get和set方法
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}