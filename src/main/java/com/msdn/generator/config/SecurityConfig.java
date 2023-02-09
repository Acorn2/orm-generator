package com.msdn.generator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2022/9/15 4:21 下午
 * @description
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("/").permitAll();

        // 或者仅放行swagger相关的接口
//    http.csrf().disable().authorizeRequests()
//        .antMatchers("/swagger-ui.html").permitAll()
//        .antMatchers("/wabjars/**").permitAll()
//        .antMatchers("/swagger-resources/**").permitAll()
//        .antMatchers("/v3/**").permitAll();
    }
}
