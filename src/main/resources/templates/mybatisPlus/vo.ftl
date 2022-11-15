package ${package}.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ${pascalName}VO {

<#list columns as column>
  <#if !column.isCommonField>
  private ${column.javaType} ${column.camelName};
  </#if>
</#list>
}