package com.msdn.generator.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2021/4/17 18:12
 * @description
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class PageSortInfo extends SimplePageInfo {

  @Schema(name = "排序信息")
  private List<OrderInfo> orderInfos;

  public String parseSort() {
    if (CollectionUtils.isEmpty(orderInfos)) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    for (OrderInfo orderInfo : orderInfos) {
      sb.append(orderInfo.getColumn()).append(" ");
      sb.append(orderInfo.isAsc() ? " ASC," : " DESC,");
    }
    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }
}
