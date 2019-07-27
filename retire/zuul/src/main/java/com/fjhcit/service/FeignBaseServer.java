package com.fjhcit.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Map;

/**
 * @Description：网关公共服务统一配置地址（子模块服务需注册可能用）
 * @author：陈 麟
 * @date：2019年5月31日 上午10:13:45
 */
@FeignClient(name = "audit")
public interface FeignBaseServer {

    /**
     * @Description：插入审计日志
     * @param paramMap
     * @return
     */
	@RequestMapping("/audit/insertAudit")
    boolean insertAudit(@RequestBody Map<String,String> paramMap);

}
