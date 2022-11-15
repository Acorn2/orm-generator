package com.msdn.generator.utils.jpa.common;

import java.util.List;
import javax.persistence.criteria.JoinType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2022/9/11 10:22 下午
 * @description
 * 理论上会有多个QueryParam对象，一个是where条件，当Connector为on时，根据joinName的不同，会生成不同的QueryParam对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryParam {

  private Connector connector;

  private List<QueryItem> queryItems;

  private String joinName;

  private JoinType joinType;
}
