package com.fjhcit.base.service;

import com.fjhcit.entity.BaseUserDO;

/**
 * @Description：认证令牌_业务接口
 * @author：陈 麟
 * @date：2019年06月03日 上午10:35:03
 */
public interface AuthService {
	
	/**
	 * @description JWT生成HTTP header的Token认证令牌
	 * @param baseUserDO
	 * @return
	 */
	public String generateJwt(BaseUserDO baseUserDO);
}
