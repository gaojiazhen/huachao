package com.fjhcit.base.service.impl;

import com.fjhcit.base.dao.BaseMenuDAO;
import com.fjhcit.base.service.BaseMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * 菜单实现层
 * @author bule
 *
 */
@Service
public class BaseMenuServiceImpl implements BaseMenuService {

    @Autowired
    private BaseMenuDAO baseMenuDAO;

	public String getFindMenuCount(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return baseMenuDAO.getFindMenuCount(paramMap);
	}
	public List<Map<String, Object>> getFindMenuParentmenu(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return baseMenuDAO.getFindMenuParentmenu(paramMap);
	}
	public void deleteMenu(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		baseMenuDAO.deleteMenu(paramMap);
	}
	public void addMenu(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		baseMenuDAO.addMenu(paramMap);
	}
	public void updateMenu(Map paramMap) {
		// TODO Auto-generated method stub
		baseMenuDAO.updateMenu(paramMap);
	}
}