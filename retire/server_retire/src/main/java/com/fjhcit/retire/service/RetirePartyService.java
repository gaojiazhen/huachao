package com.fjhcit.retire.service;

import java.util.List;
import java.util.Map;

import com.fjhcit.entity.BaseDepartmentDO;
import com.fjhcit.entity.RetirePartyDO;

/**
 * @description 离（退）休人员_党组织设置情况库_业务逻辑接口
 * @author 陈麟
 * @date 2019年06月17日 上午08:48:46
 */
public interface RetirePartyService {

	/**
	 * @description 查找离（退）休人员_党组织设置情况库列表信息
	 * @param map
	 * @return
	 */
	List<RetirePartyDO> listRetireParty(Map<String,String> map);

	/**
	 * @description 查找离（退）休人员_党组织设置情况库单条信息
	 * @param id
	 * @return
	 */
	RetirePartyDO getRetirePartyDOById(String id);

	/**
	 * @description 添加离（退）休人员_党组织设置情况库信息
	 * @param retirePartyDO
	 * @return
	 */
	void insertRetireParty(RetirePartyDO retirePartyDO);

	/**
	 * @description 更新离（退）休人员_党组织设置情况库信息
	 * @param retirePartyDO
	 * @return
	 */
	void updateRetireParty(RetirePartyDO retirePartyDO);

	/**
	 * @description 删除离（退）休人员_党组织设置情况库信息
	 * @param map
	 * @return
	 */
	void removeRetirePartyByIdsArr(Map<String,Object> map);
	
	/**
	 * @description 查找基础管理_组织机构列表信息
	 * @param map
	 * @return
	 */
	List<BaseDepartmentDO> listBaseDepartment(Map<String,String> map);
	
	/**
	 * @description 查询国网统计报表_离退休党组织设置统计
	 * @param map
	 * @return
	 */
	List<Map<String,String>> listRetirePartyOrganization(Map<String,String> map);
}