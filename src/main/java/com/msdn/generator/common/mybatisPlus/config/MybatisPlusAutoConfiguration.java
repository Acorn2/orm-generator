package com.msdn.generator.common.mybatisPlus.config;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2021/4/18 17:38
 * @description Mybatis plus 配置类
 */
@ConditionalOnBean({com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration.class})
public class MybatisPlusAutoConfiguration {

    public MybatisPlusAutoConfiguration() {
    }

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MybatisPlusAutoConfiguration.FillFieldConfiguration();
    }

//    @Bean
//    public PaginationInterceptor paginationInterceptor() {
//        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
//        List<ISqlParser> sqlParserList = new ArrayList();
//        sqlParserList.add(new BlockAttackSqlParser());
//        paginationInterceptor.setSqlParserList(sqlParserList);
//        return paginationInterceptor;
//    }

    public static class FillFieldConfiguration implements MetaObjectHandler {

        public FillFieldConfiguration() {
        }

        @Override
        public void insertFill(MetaObject metaObject) {
            DateTime now = DateUtil.date();
            metaObject.setValue("createUserCode", "1");
            metaObject.setValue("createUserName", "admin");
            metaObject.setValue("createDate", now);
            metaObject.setValue("lastModifiedCode", "1");
            metaObject.setValue("lastModifiedName", "admin");
            metaObject.setValue("lastModifiedDate", now);
            // 其它公共字段
//            this.strictInsertFill(metaObject, "orgId", String.class, "xxx");
//            this.strictInsertFill(metaObject, "platformId", String.class, "xxx");
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            DateTime now = DateUtil.date();
            metaObject.setValue("lastModifiedCode", "1");
            metaObject.setValue("lastModifiedName", "admin");
            metaObject.setValue("lastModifiedDate", now);
        }
    }

    /**
     * 分页插件
     *
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(
                DbType.MYSQL);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInnerInterceptor.setMaxLimit(500L);
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);

        return mybatisPlusInterceptor;
    }
}
