package com.msdn.generator.common.mybatis.config;

import com.msdn.generator.common.mybatis.AutoFillFieldInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * mybatis 配置类
 */
@Configuration
@ConditionalOnBean({SqlSessionFactory.class})
public class MybatisAutoConfiguration {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @PostConstruct
    public void init() {
        sqlSessionFactory.getConfiguration().addInterceptor(new AutoFillFieldInterceptor());
    }
}
