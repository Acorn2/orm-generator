package com.msdn.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author hresh
 * @date 2021/4/16 23:58
 * @description
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GeneratorApplication {

  /**
   * 测试的时候添加参数 -h 127.0.0.1 -P 3306 -d db_tl_sale -u root -p 123456 -m sale -g base -t
   * t_xs_sale_contract,t_xs_sale_contract_detail
   *
   * @param args
   */
  public static void main(String[] args) {
    SpringApplication.run(GeneratorApplication.class, args);
  }
}
