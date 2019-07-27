package com.fjhcit.retire;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Description：离退休业务服务_启动器
 * @author：陈 麟
 * @date：2019年5月31日 上午9:49:50
 */
@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@EnableTransactionManagement
@MapperScan(value = "com.fjhcit.retire.dao")
@ComponentScan("com.*")
public class ServerRetireApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerRetireApplication.class, args);
        //SpringApplication springApplication = new SpringApplication(ServerRetireApplication.class);
        //springApplication.setBannerMode(Banner.Mode.CONSOLE);
        //springApplication.run(args);
    }
    
}
