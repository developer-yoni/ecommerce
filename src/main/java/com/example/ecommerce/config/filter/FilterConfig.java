package com.example.ecommerce.config.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SecurityFilter Chain이 모두 끝난 후 -> Servlet Filter에서 걸리게 됨
 * */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyFilter2> myFilter2() {

        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*");
        bean.setOrder(0);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<MyFilter3> myFilter3() {

        FilterRegistrationBean<MyFilter3> bean = new FilterRegistrationBean<>(new MyFilter3());
        bean.addUrlPatterns("/*");
        bean.setOrder(1);
        return bean;
    }
}
