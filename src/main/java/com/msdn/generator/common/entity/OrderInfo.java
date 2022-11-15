package com.msdn.generator.common.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2022/9/8 5:16 下午
 * @description 排序条件
 */
@Getter
@Setter
public class OrderInfo {

  private boolean asc = true;

  private String column;
}
