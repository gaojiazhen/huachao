package com.fjhcit.base;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Description：系统基础功能公共服务_启动器
 * @author：陈 麟
 * @date：2019年5月31日 上午9:49:50
 */
@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement
@MapperScan(value = "com.fjhcit.base.dao")
@ComponentScan("com.*")
public class ServerBaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerBaseApplication.class, args);
    }
}
