package com.msdn.generator.common.mybatis.model;

import com.msdn.generator.common.mybatis.annotation.CreatedName;
import com.msdn.generator.common.mybatis.annotation.LastModifiedName;
import com.msdn.generator.utils.IdUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.annotation.LogicDelete;
import tk.mybatis.mapper.annotation.Version;

/**
 * @author hresh
 * @date 2021/4/26 22:24
 * @description
 */
@Data
@Schema(title = "核心基础实体类")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @KeySql(genId = IdUtils.class)
  @Schema(name = "")
  private String id;

  @Schema(description = "删除标记")
  @LogicDelete
  @Column(name = "del_flag")
  private Boolean delFlag;

  @Schema(description = "创建人代码")
  @CreatedBy
  @Column(name = "create_user_code")
  private String createUserCode;

  @Schema(name = "创建人姓名")
  @CreatedName
  @Column(name = "create_user_name")
  private String createUserName;

  @Schema(name = "创建时间")
  @CreatedDate
  @Column(name = "created_date")
  private LocalDateTime createdDate;

  @Schema(name = "修改人代码")
  @LastModifiedBy
  @Column(name = "last_modified_code")
  private String lastModifiedCode;

  @Schema(name = "修改人姓名")
  @LastModifiedName
  @Column(name = "last_modified_name")
  private String lastModifiedName;

  @Schema(name = "修改时间")
  @LastModifiedDate
  @Column(name = "last_modified_date")
  private LocalDateTime lastModifiedDate;

  @Schema(name = "版本号")
  @Version
  @Column(name = "version")
  private Integer version;
}
