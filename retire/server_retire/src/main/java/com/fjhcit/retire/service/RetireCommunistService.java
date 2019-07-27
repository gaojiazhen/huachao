package com.fjhcit.retire.service;

import java.util.List;
import java.util.Map;

import com.fjhcit.entity.RetireCommunistDO;

/**
 * @description 离（退）休人员_中共党员基本情况登记表_业务逻辑接口
 * @author 陈麟
 * @date 2019年06月11日 下午20:53:24
 */
public interface RetireCommunistService {

	/**
	 * @description 查找离（退）休人员_中共党员基本情况登记表列表信息
	 * @param map
	 * @return
	 */
	List<RetireCommunistDO> listRetireCommunist(Map<String,String> map);

	/**
	 * @description 查找离（退）休人员_中共党员基本情况登记表单条信息
	 * @param id
	 * @return
	 */
	RetireCommunistDO getRetireCommunistDOById(String id);
	
	/**
	 * @description 根据userid查找离（退）休人员_中共党员基本情况登记表单条信息
	 * @param userid
	 * @return
	 */
	RetireCommunistDO getRetireCommunistDOByUserid(String userid);

	/**
	 * @description 添加离（退）休人员_中共党员基本情况登记表信息
	 * @param retireCommunistDO
	 * @return
	 */
	void insertRetireCommunist(RetireCommunistDO retireCommunistDO);

	/**
	 * @description 更新离（退）休人员_中共党员基本情况登记表信息
	 * @param retireCommunistDO
	 * @return
	 */
	void updateRetireCommunist(RetireCommunistDO retireCommunistDO);

	/**
	 * @description 删除离（退）休人员_中共党员基本情况登记表信息
	 * @param map
	 * @return
	 */
	void removeRetireCommunistByIdsArr(Map<String,Object> map);
	
	/**
	 * @description 离休人员登记_导入Excel解析的数据
	 * @param map
	 */
	Map<String, String> saveRetireCommunistByExcel(List<Object> dataList);
}