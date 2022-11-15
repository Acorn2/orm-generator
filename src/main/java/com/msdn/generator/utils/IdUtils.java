package com.msdn.generator.utils;

import tk.mybatis.mapper.genid.GenId;

import java.util.UUID;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2021/4/17 17:18
 */
public class IdUtils implements GenId<String> {

  @Override
  public String genId(String table, String column) {
    return UUID.randomUUID().toString().replace("-", "");
  }

  public static String genId() {
    return UUID.randomUUID().toString().replace("-", "");
  }

}
