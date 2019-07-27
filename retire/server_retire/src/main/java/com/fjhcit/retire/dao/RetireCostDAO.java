package com.fjhcit.retire.dao;

import java.util.List;
import java.util.Map;

import com.fjhcit.entity.RetireCostDO;

/**
 * @description 离（退）休人员_单位运行成本费用_数据库操作接口
 * @author 陈麟
 * @date 2019年06月24日 上午11:57:51
 */
public interface RetireCostDAO {

	/**
	 * @description 查找离（退）休人员_单位运行成本费用列表信息
	 * @param map
	 * @return
	 */
	List<RetireCostDO> listRetireCost(Map<String,String> map);

	/**
	 * @description 查找离（退）休人员_单位运行成本费用单条信息
	 * @param id
	 * @return
	 */
	RetireCostDO getRetireCostDOById(String id);

	/**
	 * @description 添加离（退）休人员_单位运行成本费用信息
	 * @param retireCostDO
	 * @return
	 */
	void insertRetireCost(RetireCostDO retireCostDO);

	/**
	 * @description 更新离（退）休人员_单位运行成本费用信息
	 * @param retireCostDO
	 * @return
	 */
	void updateRetireCost(RetireCostDO retireCostDO);

	/**
	 * @description 删除离（退）休人员_单位运行成本费用信息
	 * @param map
	 * @return
	 */
	void removeRetireCostByIdsArr(Map<String,Object> map);
	
	/**
	 * @description 查找国网统计报表_退休人员管理费用统计
	 * @param map
	 * @return
	 */
	List<Map<String,String>> listRetireEmployeeCost(Map<String,Object> map);
	
	/**
	 * @description 根据单位和年度查找离（退）休人员_单位运行成本费用单条信息
	 * @param map
	 * @return
	 */
	RetireCostDO getRetireCostDOByUnitidAndYear(Map<String,String> map);
	
	/**
	 * @description 查找国网统计报表_基本养老保险及档案统计
	 * @param map
	 * @return
	 */
	List<Map<String,String>> listRetireBasicEndowmentInsurance(Map<String,Object> map);
	
	/**
	 * @description 查找国网统计报表_退休人员相关费用情况表
	 * @param map
	 * @return
	 */
	List<Map<String,String>> listRetireUserCorrelativeCharges(Map<String,String> map);
	
	/**
	 * @description 国网统计报表_离退休工作机构情况
	 * @param map
	 * @return
	 */
	List<Map<String,String>> listRetireDepartmentAndCost(Map<String,Object> map);
}