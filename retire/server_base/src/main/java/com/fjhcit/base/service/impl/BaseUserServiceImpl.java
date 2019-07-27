package com.fjhcit.base.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fjhcit.entity.BaseUserDO;
import com.fjhcit.base.dao.BaseUserDAO;
import com.fjhcit.base.service.BaseUserService;

@Service
public class BaseUserServiceImpl implements BaseUserService {

	@Autowired
	private BaseUserDAO baseUserDAO;
	
	@Override
	public BaseUserDO getLoginUser(Map<String, String> map) {
		return this.baseUserDAO.getLoginUser(map);
	}
	
	@Override
	public List<Map<String,Object>> listMenuByLoginUser(Map<String, String> map) {
		List<Map<String,Object>> menuList = this.baseUserDAO.listMenuByLoginUser(map);
		//1、key值转成小写
		List<Map<String,Object>> lowerCaseMenuList = new ArrayList<Map<String,Object>>();
		for (Map<String,Object> m : menuList) { 
			lowerCaseMenuList.add(transferToLowerCase(m)); 
		}
		//2、组织父子级菜单结构数据
		List<Map<String,Object>> stemp=new ArrayList<Map<String,Object>>();
		for (Map<String,Object> m : lowerCaseMenuList) {
			String pid = (String)m.get("pid");
			if("0".equals(pid)) {
				m.put("children", getChildren(m,lowerCaseMenuList));	
				stemp.add(m);
			}
		}
		return stemp;
	}

	/**
	 * @description 把Map对象的key值都转换成小写
	 * @param orgMap
	 * @return
	 */
	public static Map<String, Object> transferToLowerCase(Map<String, Object> orgMap) {
		Map<String, Object> resultMap = new HashMap<>();
		if (orgMap == null || orgMap.isEmpty()) {
			return resultMap;
		}
		Set<Entry<String,Object>> entrySet = orgMap.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			String key = entry.getKey();
			String value = (String) entry.getValue();
			resultMap.put(key.toLowerCase(), value);
		}
		return resultMap;
	}
	
	/**
	  * 迭代组装树状左菜单列表数据
	 */
	private List<Map<String,Object>> getChildren(Map<String,Object> map, List<Map<String,Object>> dataList) {
		List<Map<String,Object>> stemp=new ArrayList<Map<String,Object>>();
		for (Map<String,Object> d : dataList) {
			if(d.get("pid").equals(map.get("id"))) {
				List<Map<String,Object>> childrenList = getChildren(d,dataList);
				if(childrenList.size()>0) {
					d.put("children", childrenList);
				}
				stemp.add(d);
			}
		}
		return stemp;
	}

	@Override
	public List<BaseUserDO> listBaseUser(Map<String,String> map){
		return this.baseUserDAO.listBaseUser(map);
	}

	@Override
	public BaseUserDO getBaseUserDOById(String id){
		return this.baseUserDAO.getBaseUserDOById(id);
	}
	
	@Override
	public String insertBaseUser(BaseUserDO baseUserDO) {
		return this.baseUserDAO.insertBaseUser(baseUserDO);
	}

	@Override
	public void updateBaseUser(BaseUserDO baseUserDO) {
		this.baseUserDAO.updateBaseUser(baseUserDO);
	}
	
	@Override
	public void updateBaseUserByLoginName(BaseUserDO baseUserDO) {
		this.baseUserDAO.updateBaseUserByLoginName(baseUserDO);
	}

	@Override
	public void removeBaseUserByIds(Map<String,Object> map){
		this.baseUserDAO.removeBaseUserByIds(map);
	}
}