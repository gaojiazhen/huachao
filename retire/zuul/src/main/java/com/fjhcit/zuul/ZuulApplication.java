package com.fjhcit.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description：zuul 服务映射网关_启动器
 * @author：陈 麟
 * @date：2019年5月31日 上午9:49:50
 */
@EnableFeignClients(basePackages = {"com.fjhcit.service"})
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan("com.fjhcit.*")
public class ZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }
}
