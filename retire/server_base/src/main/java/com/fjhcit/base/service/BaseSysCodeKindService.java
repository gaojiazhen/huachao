package com.fjhcit.base.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.fjhcit.model.CacheModel;

/**
 * 数据字典服务层
 * @author bule
 *
 */
public interface BaseSysCodeKindService {
	/**
	 * 数据字典数据数
	 * @param paramMap
	 * @return
	 */
	public String getSysCodeKindCount(Map<String, String> paramMap);
	/**
	 * 数据字典详细
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getSysCodeKindInfo(Map<String, String> paramMap);
	/**
	 * 删除数据字典数据
	 * @param paramMap
	 */
	public void deleteSysCodeKind(Map<String, String> paramMap);
	/**
	 * 删除数据字典对应编码明细
	 * @param paramMap
	 */
	public void deleteSysCode(Map<String, String> paramMap);
	/**
	 * 新增数据字典数据
	 * @param paramMap
	 */
	public void addSysCodeKind(Map<String, String> paramMap);
	/**
	 * 修改数据字典数据
	 * @param paramMap
	 */
	public void updateSysCodeKind(Map<String, String> paramMap);
	/**
	 * 数据字典明细数据数量
	 * @param paramMap
	 * @return
	 */
	public String getSysCodeCount(Map<String, String> paramMap);
	/**
	 * 数据字典明细数据
	 * @param paramMap
	 * @return
	 */
	public Map<String, List<Map>> getSysCodeInfo(Map<String, String> paramMap);
	/**
	 * 新增数据字典明细数据
	 * @param paramMap
	 */
	public void addSysCode(Map<String, String> paramMap);
	/**
	 * 编码验证
	 * @param paramMap
	 */
	public String checkSysCodeKindCount(Map<String, String> paramMap);
	/**
	 * 缓存
	 * @param code
	 * @return
	 */
	public ArrayList<CacheModel> getCode(String code);
	/**
	 * 删除编码附加属性的值
	 * @param kdCode
	 */
	public void deleteCodeAddiVal(String kdCode);
	/**
	 * 删除编码拓展属性
	 * @param kdCode
	 */
	public void deleteKdCodeAddIByKdcode(String kdCode);
	/**
	 * 插入新增编码附加属的值
	 * @param paramMap
	 */
	public void addCodeAddiVal(Map<String, String> paramMap);
	/**
	 * 插入新增编码拓展属性
	 * @param paramMap
	 */
	public void addSysCodeAddI(Map<String, String> paramMap);
}