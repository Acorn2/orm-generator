package ${package}.model;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msdn.mall.common.model.CoreBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
*
${tableComment}
*
* @author ${author}
* @since ${date}
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("${tableName}")
@Schema(name="${tableName}对象", description="${tableComment}")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ${pascalName} extends CoreBase{

  private static final long serialVersionUID = 1L;

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list columns as column>
  <#if column.isPrimaryKey>
<#--    @TableId(value = "${column.fieldName}", type = IdType.${column.primaryKeyType})-->
  <#else>
  <#if !column.isCommonField>
  @TableField("${column.fieldName}")
  </#if>
  </#if>
  <#if !column.isCommonField>
  @Schema(name = "${column.comment}")
  private ${column.javaType} ${column.camelName};

  </#if>
</#list>
}
