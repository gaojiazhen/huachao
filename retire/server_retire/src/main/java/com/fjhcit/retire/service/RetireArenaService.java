package com.fjhcit.retire.service;

import java.util.List;
import java.util.Map;

import com.fjhcit.entity.RetireArenaDO;

/**
 * @description 离（退）休人员_活动场所及老年大学建设情况统计表_业务逻辑接口
 * @author 陈麟
 * @date 2019年06月25日 下午21:46:27
 */
public interface RetireArenaService {

	/**
	 * @description 查找离（退）休人员_活动场所及老年大学建设情况统计表列表信息
	 * @param map
	 * @return
	 */
	List<RetireArenaDO> listRetireArena(Map<String,String> map);

	/**
	 * @description 查找离（退）休人员_活动场所及老年大学建设情况统计表单条信息
	 * @param id
	 * @return
	 */
	RetireArenaDO getRetireArenaDOById(String id);

	/**
	 * @description 添加离（退）休人员_活动场所及老年大学建设情况统计表信息
	 * @param retireArenaDO
	 * @return
	 */
	void insertRetireArena(RetireArenaDO retireArenaDO);

	/**
	 * @description 更新离（退）休人员_活动场所及老年大学建设情况统计表信息
	 * @param retireArenaDO
	 * @return
	 */
	void updateRetireArena(RetireArenaDO retireArenaDO);

	/**
	 * @description 删除离（退）休人员_活动场所及老年大学建设情况统计表信息
	 * @param map
	 * @return
	 */
	void removeRetireArenaByIdsArr(Map<String,Object> map);
	
	/**
	 * @description 查询国网统计报表_活动场所及老年大学录入
	 * @param map
	 * @return
	 */
	List<Map<String,String>> listRetireArenaAndUniversity(Map<String,String> map);
	
	/**
	 * @description 查询国网统计报表_退休人员活动场所统计
	 * @param map
	 * @return
	 */
	List<Map<String,String>> listRetireArenaServiceCondition(Map<String,String> map);
	
	/**
	 * @description 活动场所及老年大学录入_导入Excel解析的数据
	 * @param map
	 */
	Map<String, String> saveRetireArenaByExcel(Map<String,Object> map);
	
	/**
	 * @description 根据单位+年度判断数据是否存在 
	 * @param map
	 * @return
	 */
	List<String> listRetireArenaIdByUnitidAndYear(Map<String,String> map);
}