package com.fjhcit.common.kit;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Description：Spring上下文环境工具类
 * @author：陈 麟
 * @date：2019年06月04日 下午21:40:17
 */
@Component
public class SpringContextUtils implements ApplicationContextAware{
	private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringContextUtils.applicationContext == null) {
        	SpringContextUtils.applicationContext = applicationContext;
        }
//        System.out.println("  福  建  华  超  信  息  科  技  有  限  公  司");
//        System.out.println("███████╗         ██╗    ██╗  ██╗     ██████╗    ██╗    ████████╗");
//        System.out.println("██╔════╝         ██║    ██║  ██║    ██╔════╝    ██║    ╚══██╔══╝");
//        System.out.println("█████╗           ██║    ███████║    ██║         ██║       ██║   ");
//        System.out.println("██╔══╝      ██   ██║    ██╔══██║    ██║         ██║       ██║   ");
//        System.out.println("██║         ╚█████╔╝    ██║  ██║    ╚██████╗    ██║       ██║   ");
//        System.out.println("╚═╝          ╚════╝     ╚═╝  ╚═╝     ╚═════╝    ╚═╝       ╚═╝   ");
//        System.out.println("系统：离退休信息档案管理系统V1.0     作者：陈麟       时间：2019年6月 ");
//        System.out.println("");
        System.out.println("------------------------------ 正在加载Spring上下文环境工具类 ----------------------------");
        System.out.println("---------- commons/src/main/java/com/fjhcit/common/kit/SpringContextUtils.java ----------");
        System.out.println("----------- 在普通类可以调用SpringUtils.getAppContext()获取applicationContext对象---------");
        System.out.println("applicationContext=" + SpringContextUtils.applicationContext);
    }

    //获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过name获取 Bean.
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    //通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }
}