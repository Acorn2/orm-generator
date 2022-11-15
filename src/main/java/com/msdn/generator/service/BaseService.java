package com.msdn.generator.service;

import com.msdn.generator.entity.Column;
import com.msdn.generator.entity.GenerateParameter;
import com.msdn.generator.utils.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2021/5/13 21:33
 * @description
 */
public class BaseService {

  private static Connection connection;

  public static void setConnection(GenerateParameter generateParameter) throws Exception {
    connection = getConnection(generateParameter);
  }

  public static void closeConnection() throws SQLException {
    connection.close();
  }

  public static String getUrl(GenerateParameter generateParameter) {
    return "jdbc:mysql://" + generateParameter.getHost() + ":" + generateParameter.getPort() + "/"
        + generateParameter.getDatabase()
        + "?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8";
  }


  /**
   * 数据库连接，类似于：DriverManager.getConnection("jdbc:mysql://localhost:3306/test_demo?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC","root","password");
   *
   * @param generateParameter 请求参数
   * @return 数据库连接
   * @throws Exception
   */
  public static Connection getConnection(GenerateParameter generateParameter) throws Exception {
    return DriverManager.getConnection(getUrl(generateParameter), generateParameter.getUsername(),
        generateParameter.getPassword());
  }

  /**
   * 根据表具体位置，获取表中字段的具体信息，包括字段名，字段类型，备注等
   *
   * @param tableName
   * @return
   * @throws Exception
   */
  public List<Column> getColumns(String tableName, String[] commonColumns) throws Exception {
    // 获取表定义的字段信息
    ResultSet resultSet = connection.createStatement()
        .executeQuery("SHOW FULL COLUMNS FROM " + tableName);
    List<Column> columnList = new ArrayList<>();
    while (resultSet.next()) {
      String fieldName = resultSet.getString("Field");
      Column column = new Column();
      // 判断是否是主键
      column.setIsPrimaryKey("PRI".equals(resultSet.getString("Key")));
      // 获取字段名称
      column.setFieldName(fieldName);

      // 实体类特定字段从核心类里获取
      if (Objects.nonNull(commonColumns) && Arrays.asList(commonColumns).contains(fieldName)) {
        column.setIsCommonField(true);
      } else {
        column.setIsCommonField(false);
      }
      // 获取字段类型
      column.setFieldType(resultSet.getString("Type").replaceAll("\\(.*\\)", ""));
      switch (column.getFieldType()) {
        case "json":
        case "longtext":
        case "char":
        case "varchar":
        case "text":
          column.setJavaType("String");
          column.setIsNumber(false);
          break;
        case "date":
        case "datetime":
          column.setJavaType("Date");
          column.setIsNumber(false);
          break;
        case "timestamp":
          column.setJavaType("LocalDateTime");
          column.setIsNumber(false);
          break;
        case "bit":
          column.setJavaType("Boolean");
          column.setIsNumber(false);
          break;
        case "int":
        case "tinyint":
          column.setJavaType("Integer");
          column.setIsNumber(true);
          break;
        case "bigint":
          column.setJavaType("Long");
          column.setIsNumber(true);
          break;
        case "decimal":
          column.setJavaType("BigDecimal");
          column.setIsNumber(true);
          break;
        case "varbinary":
          column.setJavaType("byte[]");
          column.setIsNumber(false);
          break;
        default:
          throw new Exception(
              tableName + " " + column.getFieldName() + " " + column.getFieldType() + "类型没有解析");
      }
      // 转换字段名称,receipt_sign_name字段改为 receiptSignName
      column.setCamelName(StringUtils.underscoreToCamel(column.getFieldName()));
      // 首字母大写
      column.setPascalName(StringUtils.firstLetterUpperCase(column.getCamelName()));
      // 字段在数据库的注释
      column.setComment(resultSet.getString("Comment"));
      columnList.add(column);
    }
    return columnList;
  }

  /**
   * 获取表的描述
   *
   * @param tableName
   * @param parameter
   * @return
   * @throws Exception
   */
  public String getTableComment(String tableName, GenerateParameter parameter) throws Exception {
    Connection connection = getConnection(parameter);
    ResultSet resultSet = connection.createStatement().executeQuery(
        "SELECT table_comment FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = '" + parameter
            .getDatabase()
            + "' AND table_name = '" + tableName + "'");
    String tableComment = "";
    while (resultSet.next()) {
      tableComment = resultSet.getString("table_comment");
    }
    return tableComment;
  }

}
