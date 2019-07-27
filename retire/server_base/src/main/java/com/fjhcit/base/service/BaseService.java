package com.fjhcit.base.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.fjhcit.entity.BaseUserDO;
import com.fjhcit.model.Users;
import com.fjhcit.model.CacheModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;


/**
 * @author sunguoliang
 * @create 2018-09-25 下午2:15
 */
public interface BaseService {

    public List<Users> findAll();

	public BaseUserDO login(String userName, String pwd);

	public List<Map<String, String>> getMuneListByUserId(String string);

	public List<Map> findRole();

	public List<Map> findMenu(Map<String, String> params);

	public List<Map> findUser(Map<String, String> paramMap);
	public Page findUserbypage(Map<String, String> paramMap,Page<Map> page2);

	public void addUser(Map<String, String> params);

	public void updateUser(Map<String, String> params);
    @Transactional
	public void removeUser(String[] tmp);

	public List<String> getDataRangetByUserId(String string);
	
	public ArrayList<CacheModel> getCode(String code);
	
	/**
	 * 根据用户名改变用户状态
	 * @param userName
	 */
	public void updateStateByUserName(String userName,String updateState);
	/**
	 * 根据用户id改变登录时间
	 * @param userId
	 * @param date
	 */
	public void updateLastLoginTimeByUserId(Map<String, Object> params);

}


