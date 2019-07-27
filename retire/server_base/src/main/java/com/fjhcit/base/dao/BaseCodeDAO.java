package com.fjhcit.base.dao;

import java.util.List;
import java.util.Map;

import com.fjhcit.entity.BaseCodeDO;

/**
 * @description 基础管理_代码设置_数据库操作接口
 * @author 陈麟
 * @date 2019年06月05日 上午09:57:20
 */
public interface BaseCodeDAO {

	/**
	 * @description 查找基础管理_代码设置列表信息
	 * @param map
	 * @return
	 */
	List<BaseCodeDO> listBaseCode(Map<String,String> map);

	/**
	 * @description 查找基础管理_代码设置单条信息
	 * @param id
	 * @return
	 */
	BaseCodeDO getBaseCodeDOById(String id);

	/**
	 * @description 添加基础管理_代码设置信息
	 * @param baseCodeDO
	 * @return
	 */
	String insertBaseCode(BaseCodeDO baseCodeDO);

	/**
	 * @description 更新基础管理_代码设置信息
	 * @param baseCodeDO
	 * @return
	 */
	void updateBaseCode(BaseCodeDO baseCodeDO);

	/**
	 * @description 删除基础管理_代码设置信息
	 * @param map
	 * @return
	 */
	void removeBaseCodeByIdsArr(Map<String,Object> map);
	
	/**
	 * @description 查找基础管理_数据字典ID数组
	 * @param map
	 * @return
	 */
	String[] getBaseCodeIdArray(Map<String,String> map);
}