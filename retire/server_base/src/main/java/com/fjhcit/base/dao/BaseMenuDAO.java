package com.fjhcit.base.dao;

import java.util.List;
import java.util.Map;

/**
 * 菜单持久层
 * @author bule
 *
 */
public interface BaseMenuDAO {
	/**
	 * 菜单数量
	 * @param paramMap
	 * @return
	 */
	public String getFindMenuCount(Map<String, String> paramMap);
	/**
	 * 菜单信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getFindMenuParentmenu(Map<String, String> paramMap);
	/**
	 * 删除菜单
	 * @param paramMap
	 */
	public void deleteMenu(Map<String, String> paramMap);
	/**
	 * 插入菜单
	 * @param paramMap
	 */
	public void addMenu(Map<String, String> paramMap);
	/**
	 * 修改菜单
	 * @param paramMap
	 */
	public void updateMenu(Map paramMap);
}