package com.msdn.generator.utils.jpa.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2022/9/8 5:05 下午
 * @description
 */
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryCondition {

  /**
   * 数据库中字段名,默认为空字符串,则Query类中的字段要与数据库中字段一致
   */
  String column() default "";

  /**
   * @see MatchCondition
   */
  MatchCondition func() default MatchCondition.EQUAL;
}
