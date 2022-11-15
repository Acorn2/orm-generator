package com.msdn.generator.entity;

import java.io.File;

/**
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * 公共字段
 */
public class Config {

  public static final String OUTPUT_PATH = "." + File.separator + "output";

  public static final String AUTHOR = "hresh";

  // 公共实体类字段
  public static final String[] JPA_COMMON_COLUMNS = new String[]{
      "create_user_code", "create_user_name", "created_date", "last_modified_code",
      "last_modified_name"
      , "last_modified_date", "version", "id", "del_flag"
  };

  public static final String[] MYBATIS_COMMON_COLUMNS = new String[]{
      "create_user_code", "create_user_name", "created_date", "last_modified_code",
      "last_modified_name"
      , "last_modified_date", "version", "id", "del_flag"
  };

  public static final String[] MYBATIS_PLUS_COMMON_COLUMNS = new String[]{
      "create_user_code", "create_user_name", "created_date", "last_modified_code",
      "last_modified_name"
      , "last_modified_date", "version", "id", "del_flag"
  };
}
