package com.msdn.generator.common.jpa.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author hresh
 * @date 2022/9/5 4:08 下午
 * @description
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@EqualsAndHashCode(of = "id")
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BaseDomain implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(name = "创建人代码")
    @CreatedBy
    @Column(name = "create_user_code")
    private String createUserCode;

    @Schema(name = "创建人姓名")
    @Column(name = "create_user_name")
    private String createUserName;

    @CreatedDate
    private LocalDateTime createdDate;

    @Schema(name = "修改人代码")
    @LastModifiedBy
    @Column(name = "last_modified_code")
    private String lastModifiedCode;

    @Schema(name = "修改人姓名")
    @Column(name = "last_modified_name")
    private String lastModifiedName;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @Schema(name = "")
    @Column(name = "del_flag")
    private Integer delFlag;

    @Schema(name = "版本号")
    @Version
    @Column(name = "version")
    private Integer version;
}
