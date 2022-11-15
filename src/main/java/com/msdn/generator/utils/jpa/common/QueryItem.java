package com.msdn.generator.utils.jpa.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2022/9/14 10:46 上午
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryItem {

  private String fieldName;
  private Object fieldValue;
  private MatchCondition matchCondition;

  // between使用
  private Object startValue;
  private Object endValue;

  // in查询
  private Iterable<Object> iterable;

  private Operator operator;
}
