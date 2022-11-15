package com.msdn.generator.entity;

import lombok.Data;

/**
 * 数据表的解析内容
 */
@Data
public class Column {
    /**
     * 是否是主键
     */
    private Boolean isPrimaryKey;

    /**
     * Mybatis plus生成类主键类型，默认为ASSIGN_ID(3)
     */
    private String primaryKeyType = "ASSIGN_ID";
    /**
     * 数据库表名称
     */
    private String tableName;
    /**
     * 表描述
     */
    private String tableDesc;
    /**
     * 数据库字段名称
     **/
    private String fieldName;
    /**
     * 数据库字段类型
     **/
    private String fieldType;
    /**
     * Java类型
     */
    private String javaType;
    /**
     * 是否是数字类型
     */
    private Boolean isNumber;
    /**
     * 数据库字段驼峰命名，saleBooke
     **/
    private String camelName;
    /**
     * 数据库字段Pascal命名，SaleBook
     **/
    private String pascalName;
    /**
     * 数据库字段注释
     **/
    private String comment;

    private String field;

    private String key;

    /**
     * 是否是公共字段
     */
    private Boolean isCommonField;
}
