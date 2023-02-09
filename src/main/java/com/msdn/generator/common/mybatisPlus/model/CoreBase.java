package com.msdn.generator.common.mybatisPlus.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author hresh
 * @date 2021/4/18 11:45
 * @description 表中的公共字段根据设计来更改，这里将公共字段封装到一个基础实体类中
 */
@Data
@Schema(title = "核心基础实体类")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class CoreBase implements Serializable {

    @Schema(description = "是否删除 0未删除（默认），1已删除")
    @TableField(value = "del_flag")
    @TableLogic(delval = "1", value = "0")
    private Integer delFlag;

    @Schema(description = "创建人")
    @TableField(value = "create_user_code", fill = FieldFill.INSERT)
    private String createUserCode;

    @Schema(description = "创建人名称")
    @TableField(value = "create_user_name", fill = FieldFill.INSERT)
    private String createUserName;

    @Schema(description = "创建时间")
    @TableField(value = "created_date", fill = FieldFill.INSERT)
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime createdDate;

    @Schema(description = "修改人代码")
    @TableField(value = "last_modified_code", fill = FieldFill.INSERT_UPDATE)
    private String lastModifiedCode;

    @Schema(description = "修改人名称")
    @TableField(value = "last_modified_name", fill = FieldFill.INSERT_UPDATE)
    private String lastModifiedName;

    @Schema(description = "修改时间")
    @TableField(value = "last_modified_date", fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime lastModifiedDate;

    @Schema(description = "版本号")
    @Version
    @TableField(value = "version")
    private String version;
}
