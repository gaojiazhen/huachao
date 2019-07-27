package com.fjhcit.retire.service;

import java.util.List;
import java.util.Map;

import com.fjhcit.entity.RetireContactDO;

/**
 * @description 离（退）休人员_在职人员通讯录库_业务逻辑接口
 * @author 陈麟
 * @date 2019年06月14日 上午08:17:13
 */
public interface RetireContactService {

	/**
	 * @description 查找离（退）休人员_在职人员通讯录库列表信息
	 * @param map
	 * @return
	 */
	List<RetireContactDO> listRetireContact(Map<String,String> map);
	
	/**
	 * @description 根据身份证查询人员是否存在
	 * @param map
	 * @return
	 */
	List<Integer> listRetireContactIdByIdcard(Map<String,String> map);

	/**
	 * @description 查找离（退）休人员_在职人员通讯录库单条信息
	 * @param id
	 * @return
	 */
	RetireContactDO getRetireContactDOById(String id);

	/**
	 * @description 添加离（退）休人员_在职人员通讯录库信息
	 * @param retireContactDO
	 * @return
	 */
	void insertRetireContact(RetireContactDO retireContactDO);

	/**
	 * @description 更新离（退）休人员_在职人员通讯录库信息
	 * @param retireContactDO
	 * @return
	 */
	void updateRetireContact(RetireContactDO retireContactDO);

	/**
	 * @description 删除离（退）休人员_在职人员通讯录库信息
	 * @param map
	 * @return
	 */
	void removeRetireContactByIdsArr(Map<String,Object> map);
	
	/**
	 * @description 查找离（退）休人员_在职人员通讯录库_最大排序号
	 * @param user_id
	 * @return
	 */
	String getRetireContactNextSortnum(String unit_id);
	
	/**
	 * @description 查询国网统计报表_离退休工作人员统计
	 * @param 
	 * @return
	 */
	List<Map<String,String>> listRetireContactStaff(Map<String,Object> param);
	
	/**
	 * @description 离退休人员登记_导入Excel解析的数据
	 * @param map
	 */
	Map<String, String> saveRetireContactByExcel(Map<String,Object> map);
}