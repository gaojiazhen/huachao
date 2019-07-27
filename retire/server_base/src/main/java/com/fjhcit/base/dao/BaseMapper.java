package com.fjhcit.base.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.fjhcit.entity.BaseUserDO;
import com.fjhcit.model.CacheModel;
import com.fjhcit.model.Users;
import org.apache.ibatis.annotations.Param;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author sunguoliang
 * @create 2018-09-25 下午2:17
 */
public interface BaseMapper {

    public List<Users> findAll();

	public BaseUserDO login(Map map);

	public List<Map<String, String>> getMuneListByUserId(String id);

	public List<Map> findRole();

	public List<Map> findMenu(Map<String, String> paramMap);
	public List<Map> findUser( Map<String, String> paramMap);

	public List<Map> findUser2(Map<String, String> paramMap, Page<Map> page2);

	public void addUser(Map<String, String> params);

	public void updateUser(Map<String, String> params);

	public void removeUser(String id);

	public List<String> getDataRangetByUserId(String id);
	
	public ArrayList<CacheModel> getCode(String code);
	
	/**
	 * 根据用户名改变用户状态
	 * @param id
	 */
	public void updateStateByUserName(@Param("userName") String userName,@Param("updateState") String updateState);
	/**
	 * 根据用户id改变登录时间
	 * @param userId
	 * @param date
	 */
	public void updateLastLoginTimeByUserId(Map<String, Object> params);
	
}