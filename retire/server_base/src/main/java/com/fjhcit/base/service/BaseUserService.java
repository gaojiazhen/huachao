package com.fjhcit.base.service;

import java.util.List;
import java.util.Map;

import com.fjhcit.entity.BaseUserDO;

/**
 * @description 基础管理_登录人员表_业务逻辑接口
 * @author 陈麟
 * @date 2019年06月03日 上午08:14:20
 */
public interface BaseUserService {
	
	/**
	 * @description 查找登录人员信息
	 * @param map
	 * @return
	 */
	BaseUserDO getLoginUser(Map<String,String> map);
	
	/**
	 * @description 查登录人员的权限菜单列表
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> listMenuByLoginUser(Map<String,String> map);

	/**
	 * @description 查找基础管理_登录人员表列表信息
	 * @param map
	 * @return
	 */
	List<BaseUserDO> listBaseUser(Map<String,String> map);

	/**
	 * @description 查找基础管理_登录人员表单条信息
	 * @param id
	 * @return
	 */
	BaseUserDO getBaseUserDOById(String id);
	
	/**
	 * @description 添加基础管理_登录人员表信息
	 * @param baseUserDO
	 * @return
	 */
	String insertBaseUser(BaseUserDO baseUserDO);

	/**
	 * @description 根据用户ID_更新基础管理_登录人员表信息
	 * @param baseUserDO
	 * @return
	 */
	void updateBaseUser(BaseUserDO baseUserDO);
	
	/**
	 * @description 根据用户名_更新基础管理_登录人员表信息
	 * @param baseUserDO
	 * @return
	 */
	void updateBaseUserByLoginName(BaseUserDO baseUserDO);

	/**
	 * @description 删除基础管理_登录人员表信息
	 * @param map
	 * @return
	 */
	void removeBaseUserByIds(Map<String,Object> map);
}