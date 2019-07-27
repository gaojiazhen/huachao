package com.fjhcit.retire.service;

import java.util.List;
import java.util.Map;

import com.fjhcit.entity.RetireUserDO;

/**
 * @description 离（退）休人员基本信息库_业务逻辑接口
 * @author 陈麟
 * @date 2019年06月05日 上午10:45:07
 */
public interface RetireUserService {

	/**
	 * @description 查找离（退）休人员基本信息库列表信息
	 * @param map
	 * @return
	 */
	List<RetireUserDO> listRetireUser(Map<String,String> map);

	/**
	 * @description 查找离（退）休人员基本信息库单条信息
	 * @param id
	 * @return
	 */
	RetireUserDO getRetireUserDOById(String id);

	/**
	 * @description 添加离（退）休人员基本信息库信息
	 * @param retireUserDO
	 * @return
	 */
	void insertRetireUser(RetireUserDO retireUserDO);

	/**
	 * @description 更新离（退）休人员基本信息库信息
	 * @param retireUserDO
	 * @return
	 */
	void updateRetireUser(RetireUserDO retireUserDO);

	/**
	 * @description 删除离（退）休人员基本信息库信息
	 * @param map
	 * @return
	 */
	void removeRetireUserByIdsArr(Map<String,Object> map);
	
	/**
	 * @description 查询（身份证）是否重复
	 * @param map
	 * @return
	 */
	Integer findRetireUserCountByIdcard(Map<String,String> map);
	
	/**
	 * @description 根据身份证查询人员是否存在
	 * @param map
	 * @return
	 */
	List<Integer> listRetireUserIdByIdcard(Map<String,String> map);
	
	/**
	 * @description 查询国网统计报表_离休干部两年数字变化
	 * @param map
	 * @return
	 */
	List<Map<String,String>> listQuitCadreBiennialChange(Map<String,String> map);
	
	/**
	 * @description 查询国网统计报表_离休干部分地区情况统计表
	 * @param map
	 * @return
	 */
	List<Map<String,String>> listQuitCadreByRegion(Map<String,String> map);
	
	/**
	 * @description 查询国网统计报表_企业离休干部统计
	 * @param map
	 * @return
	 */
	List<Map<String,String>> listQuitCadreBasic(Map<String,String> map);
	
	/**
	 * @description 查询国网统计报表_退休干部享受待遇统计
	 * @param map
	 * @return
	 */
	List<Map<String,String>> listRetireCadreTreatment(Map<String,Object> map);
	
	/**
	 * @description 查询国网统计报表_退休干部两年数字变化
	 * 				查询国网统计报表_退休工人两年数字变化
	 * 				查询国网统计报表_退职人员两年数字变化
	 * @param map
	 * @return
	 */
	List<Map<String,String>> listRetireCadreBiennialChange(Map<String,Object> map);
	
	/**
	 * @description 查询国网统计报表_离休干部管理现状统计
	 * @param map
	 * @return
	 */
	List<Map<String,String>> listQuitCadreCurrentSituation(Map<String,Object> map);
	
	/**
	 * @description 查询国网统计报表_退休人员党组织关系统计
	 * @param map
	 * @return
	 */
	List<Map<String,String>> listRetirePartyRelation(Map<String,Object> map);
	
	/**
	 * 余昌增
	 * @description 查询退休人员基本情况数据统计
	 * @param map
	 * @return
	 */
	List<Map<String,String>> listRetireUserBasic(Map<String,Object> map);
	
	/**
	 * @description 查询国网统计报表_退休人员个人信息明细表
	 * @param map
	 * @return
	 */
	List<Map<String,String>> listRetireUserPersonalInformation(Map<String,Object> map);
	
	/**
	 * @description 离退休人员登记_导入Excel解析的数据
	 * @param map
	 */
	String saveRetireUserByExcel(Map<String,Object> map);
}