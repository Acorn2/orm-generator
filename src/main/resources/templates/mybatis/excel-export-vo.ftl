package ${package}.vo;

import com.sirius.poi.annotation.Excel;
import com.sirius.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Excel("${tableComment}")
public class ${pascalName}ExcelVO {

<#list columns as column>
    @ApiModelProperty("${column.comment}")
    @ExcelField(value = "${column.comment}")
    private ${column.javaType} ${column.camelName};

</#list>
}
