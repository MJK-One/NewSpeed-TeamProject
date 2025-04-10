package com.newspeed.newspeed.common.config;


import com.newspeed.newspeed.common.filter.LoginFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<LoginFilter> loggingFilter() {
        FilterRegistrationBean<LoginFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LoginFilter()); // Filter 등록
        registrationBean.addUrlPatterns("/api/*"); // 필터를 적용할 URL 패턴
        return registrationBean;
    }
}

