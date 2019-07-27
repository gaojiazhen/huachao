package com.fjhcit.base.service;

import java.util.List;
import java.util.Map;

/**
 * 菜单服务层
 * @author bule
 *
 */
public interface BaseMenuService {
    /**
     * 菜单数
     * @param paramMap
     * @return
     */
    public String getFindMenuCount(Map<String, String> paramMap);
    /**
     * 菜单详细
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
     * 新增菜单
     * @param paramMap
     */
    public void addMenu(Map<String, String> paramMap);
    /**
     * 修改菜单
     * @param paramMap
     */
    public void updateMenu(Map paramMap);

}