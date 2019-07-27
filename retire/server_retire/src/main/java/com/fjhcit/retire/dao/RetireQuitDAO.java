package com.fjhcit.retire.dao;

import java.util.List;
import java.util.Map;

import com.fjhcit.entity.RetireQuitDO;

/**
 * @description 离（退）休人员_离休人员库_数据库操作接口
 * @author 陈麟
 * @date 2019年06月28日 上午09:19:27
 */
public interface RetireQuitDAO {
	
	/**
	 * @description 查找信息库管理_离休人员登记_列表数据
	 * @param map
	 * @return
	 */
	List<RetireQuitDO> listRetireQuitUser(Map<String,String> map);
	
	/**
	 * @description 查找离（退）休人员_离休人员库列表信息
	 * @param map
	 * @return
	 */
	List<RetireQuitDO> listRetireQuit(Map<String,String> map);

	/**
	 * @description 查找离（退）休人员_离休人员库单条信息
	 * @param id
	 * @return
	 */
	RetireQuitDO getRetireQuitDOById(String id);
	
	/**
	 * @description 查找离（退）休人员_离休人员库单条信息
	 * @param userid
	 * @return
	 */
	RetireQuitDO getRetireQuitDOByUserid(String userid);

	/**
	 * @description 添加离（退）休人员_离休人员库信息
	 * @param retireQuitDO
	 * @return
	 */
	void insertRetireQuit(RetireQuitDO retireQuitDO);

	/**
	 * @description 更新离（退）休人员_离休人员库信息
	 * @param retireQuitDO
	 * @return
	 */
	void updateRetireQuit(RetireQuitDO retireQuitDO);

	/**
	 * @description 删除离（退）休人员_离休人员库信息
	 * @param map
	 * @return
	 */
	void removeRetireQuitByIdsArr(Map<String,Object> map);
}