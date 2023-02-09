package ${package}.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *${tableComment}.
 *
 * @author ${author}
 * @since ${date}
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "${camelName}")
@Schema(name = "${tableName}对象", description = "${tableComment}")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ${pascalName} extends BaseDomain {

    private static final long serialVersionUID = 1L;

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list columns as column>
<#if column.isPrimaryKey>
</#if>
<#if !column.isCommonField>
    @Schema(name = "${column.comment}")
    @Column(name = "${column.fieldName}")
    private ${column.javaType} ${column.camelName};

</#if>
</#list>
}
