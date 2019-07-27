package com.fjhcit.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 
/**
 * @Description：注册拦截器
 * @author：陈 麟
 * @date：2019年5月31日 下午3:18:38
 */
@Configuration
public class WebConfigurer  implements WebMvcConfigurer {
	@Autowired
	private LoginInterceptor loginInterceptor;
	@Autowired
	private LoginInterceptor indexloginInterceptor;   //单独为index.html页面做登录拦截器（因为index.html没有在page文件夹下）
	
	// 这个方法是用来配置静态资源的，比如html，js，css，等等
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//registry.addResourceHandler("/index").addResourceLocations("classpath:/webapp/page/commmom/index.html");
		//super.addResourceHandlers(registry);
	}
 
	//这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// addPathPatterns("/**") 表示拦截所有的请求，
		// excludePathPatterns("/login", "/register") 表示除了登陆与注册之外，因为登陆注册不需要登陆也可以访问
	    registry.addInterceptor(indexloginInterceptor).addPathPatterns("/index.html");
	    registry.addInterceptor(loginInterceptor).addPathPatterns("/page/**").excludePathPatterns("/page/login/login.html", "/page/405.html");
	}
}