package com.msdn.generator.common.entity;

import cn.hutool.json.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 日志封装类
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2021/5/2 17:08
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RequestLog {

  // 请求ip
  private String ip;
  // 访问url
  private String url;
  // 请求类型
  private String httpMethod;
  // 请求方法名（绝对路径）
  private String classMethod;
  // 请求方法描述
  private String methodDesc;
  // 请求参数
  private Object requestParams;
  // 返回结果
  private Object result;
  // 操作时间
  private Long operateTime;
  // 消耗时间
  private Long timeCost;
  // 错误信息
  private JSONObject errorMessage;
}
