package ${package}.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ${pascalName}VO {

<#list columns as column>
    private ${column.javaType} ${column.camelName};

</#list>
}
