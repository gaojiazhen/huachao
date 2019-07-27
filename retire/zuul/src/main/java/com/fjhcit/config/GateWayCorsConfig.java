package com.fjhcit.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Description：Spring Boot 过滤器（zuul启动时加载）
 * @author：陈 麟
 * @date：2019年5月31日 上午9:57:16
 */
@Configuration
public class GateWayCorsConfig {
	/**
	 * 过滤所有资源
	 * @return
	 */
	@Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        //这个请求头在https中会出现,但是有点问题，下面我会说
        //config.addExposedHeader("X-forwared-port, X-forwarded-host"); 
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
        //代表这个过滤器在众多过滤器中级别最高，也就是过滤的时候最先执行
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE); 
        return bean;
    }
}