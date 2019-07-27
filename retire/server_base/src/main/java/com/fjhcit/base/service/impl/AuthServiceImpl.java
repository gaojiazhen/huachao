package com.fjhcit.base.service.impl;

import com.fjhcit.base.service.AuthService;
import com.fjhcit.common.kit.CommonUtils;
import com.fjhcit.entity.BaseUserDO;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
 
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service(value = "authService")
public class AuthServiceImpl implements AuthService{

	@Override
	public String generateJwt(BaseUserDO baseUserDO) {
		byte[] secretKey = Base64.getEncoder().encode(CommonUtils.SECURITY_KEY.getBytes());
        // 设置失效时间
        DateTime expirationDate = new DateTime().plusDays(1);
        //DateTime expirationDate = new DateTime().plusMinutes(30);
        // Claims是需要保存到token中的信息，可以自定义，需要存什么就放什么，会保存到token的payload中
        Map<String, Object> claims = new HashMap<>();
        // 用户角色
        claims.put("role", "user");
        // 用户名
        claims.put("userName", baseUserDO.getLogin_name());
        claims.put("id", baseUserDO.getId());
        claims.put("uuid",UUID.randomUUID().toString());
        String compactJws = Jwts.builder()
                // 设置subject，一般是用户的唯一标识，比如用户对象的ID，用户名等，目前设置的是userCode
                .setSubject(baseUserDO.getLogin_name())
                // 设置失效时间
                .setExpiration(expirationDate.toDate())
                .addClaims(claims)
                // 加密算法是HS512，加密解密统一就可以
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        return compactJws;
	}
}