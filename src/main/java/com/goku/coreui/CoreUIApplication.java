package com.goku.coreui;

import com.goku.coreui.filter.OriginFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

/**
 * Created by nbfujx on 2017/12/25.
 */
@SpringBootApplication
@ServletComponentScan
@MapperScan("com.goku.coreui.**.mapper")
public class CoreUIApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CoreUIApplication.class);
    }

    @Bean
    public FilterRegistrationBean testFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new OriginFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("user_id", "123");
        registration.setName("originFilter");
        registration.setOrder(1);
        return registration;
    }

    /**
     * @date:2018/3/5 14:02
     * @author:hejr
     * @description: 文件上传配置
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize("5MB");
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("50MB");
        return factory.createMultipartConfig();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(CoreUIApplication.class, args);
    }

}
