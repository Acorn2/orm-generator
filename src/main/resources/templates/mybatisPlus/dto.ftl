package ${package}.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ${pascalName}DTO {

<#list columns as column>
<#if !column.isCommonField>
    @Schema(name = "${column.comment}")
    private ${column.javaType} ${column.camelName};

    </#if>
</#list>
}
