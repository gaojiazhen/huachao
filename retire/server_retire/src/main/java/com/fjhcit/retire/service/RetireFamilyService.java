package com.fjhcit.retire.service;

import java.util.List;
import java.util.Map;

import com.fjhcit.entity.RetireFamilyDO;

/**
 * @description 离（退）休人员_家庭主要成员表_业务逻辑接口
 * @author 陈麟
 * @date 2019年06月13日 上午11:02:09
 */
public interface RetireFamilyService {

	/**
	 * @description 查找离（退）休人员_家庭主要成员表列表信息
	 * @param map
	 * @return
	 */
	List<RetireFamilyDO> listRetireFamily(Map<String,String> map);

	/**
	 * @description 查找离（退）休人员_家庭主要成员表单条信息
	 * @param id
	 * @return
	 */
	RetireFamilyDO getRetireFamilyDOById(String id);

	/**
	 * @description 添加离（退）休人员_家庭主要成员表信息
	 * @param retireFamilyDO
	 * @return
	 */
	void insertRetireFamily(RetireFamilyDO retireFamilyDO);

	/**
	 * @description 更新离（退）休人员_家庭主要成员表信息
	 * @param retireFamilyDO
	 * @return
	 */
	void updateRetireFamily(RetireFamilyDO retireFamilyDO);

	/**
	 * @description 删除离（退）休人员_家庭主要成员表信息
	 * @param map
	 * @return
	 */
	void removeRetireFamilyByIdsArr(Map<String,Object> map);
	
	/**
	 * @description 根据user_id删除离（退）休人员_家庭主要成员表信息
	 * @param map
	 */
	void removeRetireFamilyByUserid(Map<String,String> map);
}