package com.msdn.generator.utils.jpa.common;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2022/9/8 5:04 下午
 * @description 查询条件关系匹配
 */
public enum MatchCondition {

  /**
   * equal-相等，notEqual-不等于，like-模糊匹配，notLike-， gt-大于，ge-大于等于，lt-小于，le-小于等于
   */
  EQUAL,
  NOT_EQUAL,
  LIKE,
  NOT_LIKE,

  GT,
  GE,
  LT,
  LE,

  IN,
  NOT_IN,
  BETWEEN,
  NOT_BETWEEN,
  LEFT_JOIN
}
