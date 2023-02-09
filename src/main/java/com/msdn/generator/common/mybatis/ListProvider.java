package com.msdn.generator.common.mybatis;

import cn.hutool.core.util.IdUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.reflection.MetaObject;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.util.MetaObjectUtil;

/**
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 */
public class ListProvider extends MapperTemplate {

    public ListProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 填充主键值
     *
     * @param list
     */
    public static void fillId(List<?> list, String fieldName) {
        for (Object object : list) {
            MetaObject metaObject = MetaObjectUtil.forObject(object);
            if (metaObject.getValue(fieldName) == null) {
                metaObject.setValue(fieldName, IdUtil.fastSimpleUUID());
            }
        }
    }

    /**
     * 批量插入
     *
     * @param ms
     */
    public String insertList(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        List<EntityColumn> pkColumns = new ArrayList<>(EntityHelper.getPKColumns(entityClass));
        sql.append(
                "<bind name=\"listNotEmptyCheck\" value=\"@tk.mybatis.mapper.util.OGNL@notEmptyCollectionCheck(list, '"
                        + ms.getId() + " 方法参数为空')\"/>");
        sql.append(
                "<bind name=\"fillIdProcess\" value=\"@com.zhifeng.common.mybatis.ListProvider@fillId(list, '"
                        + pkColumns.get(0).getProperty() + "')\"/>");
        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass), "list[0]"));
        sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
        sql.append(" VALUES ");
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnList) {
            if (column.isInsertable()) {
                sql.append(column.getColumnHolder("record") + ",");
            }
        }
        sql.append("</trim>");
        sql.append("</foreach>");

        // 反射把MappedStatement中的设置主键名
        EntityHelper.setKeyProperties(EntityHelper.getPKColumns(entityClass), ms);

        return sql.toString();
    }

    /**
     * 拼update sql, 使用case when方式，id为主键
     *
     * @param ms
     * @return
     */
    public String updateBatchByPrimaryKeySelective(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
        sql.append("<trim prefix=\"set\" suffixOverrides=\",\">");
        List<EntityColumn> pkColumns = new ArrayList<>(EntityHelper.getPKColumns(entityClass));

        StringBuilder idSql = new StringBuilder();

        //拼装主键
        for (int i = 0; i < pkColumns.size(); i++) {
            EntityColumn entityColumn = pkColumns.get(i);
            if (i > 0) {
                idSql.append(" and ");
            }
            idSql.append(entityColumn.getColumn()).
                    append("=#{i.").append(entityColumn.getEntityField().getName() + "}");
        }

        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        for (EntityColumn column : columnList) {
            if (!column.isId() && column.isUpdatable()) {
                sql.append("  <trim prefix=\"" + column.getColumn() + " =case\" suffix=\"end,\">");
                sql.append("    <foreach collection=\"list\" item=\"i\" index=\"index\">");
                sql.append("      <if test=\"i." + column.getEntityField().getName() + "!=null\">");
                sql.append(
                        "         when " + idSql.toString() + " then #{i." + column.getEntityField()
                                .getName() + "}");
                sql.append("      </if>");
                sql.append("    </foreach>");
                sql.append("  </trim>");
            }
        }

        sql.append("</trim>");
        sql.append("WHERE ");
        for (int i = 0; i < pkColumns.size(); i++) {
            EntityColumn entityColumn = pkColumns.get(i);
            if (i > 0) {
                sql.append(" and ");
            }
            sql.append(entityColumn.getColumn() + " IN ");
            sql.append("<trim prefix=\"(\" suffix=\")\">");
            sql.append(
                    "<foreach collection=\"list\" separator=\", \" item=\"i\" index=\"index\" >");
            sql.append("#{i." + entityColumn.getEntityField().getName() + "}");
            sql.append("</foreach>");
            sql.append("</trim>");
        }

        return sql.toString();
    }

    /**
     * 根据主键字符串进行删除，类中只有存在一个带有@Id注解的字段
     *
     * @param ms
     * @return
     */
    public String deleteByIdList(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        // 如果是逻辑删除，则修改为更新表，修改逻辑删除字段的值
        if (SqlHelper.hasLogicDeleteColumn(entityClass)) {
            sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
            sql.append("<set>");
            sql.append(SqlHelper.logicDeleteColumnEqualsValue(entityClass, true));
            sql.append("</set>");
            MetaObjectUtil.forObject(ms).setValue("sqlCommandType", SqlCommandType.UPDATE);
        } else {
            sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
        }
        appendWhereIdList(sql, entityClass);
        return sql.toString();
    }

    /**
     * 根据主键字符串进行查询，类中只有存在一个带有@Id注解的字段
     *
     * @param ms
     * @return
     */
    public String selectByIdList(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //将返回值修改为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        appendWhereIdList(sql, entityClass);
        return sql.toString();
    }

    /**
     * 保证 idList 不能为空
     *
     * @param list
     */
    public static List<?> notEmpty(List<?> list) {
        if (list == null || list.size() == 0) {
            list = new ArrayList<>();
            list.add(null);
        }
        return list;
    }

    /**
     * 拼接条件
     *
     * @param sql
     * @param entityClass
     */
    private void appendWhereIdList(StringBuilder sql, Class<?> entityClass) {
        Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
        if (columnList.size() == 1) {
            EntityColumn column = columnList.iterator().next();
            sql.append(
                    "<bind name=\"_idList\" value=\"@com.zhifeng.common.mybatis.ListProvider@notEmpty(idList)\"/>");
            sql.append("<where>");
            sql.append("<foreach collection=\"_idList\" item=\"id\" separator=\",\" open=\"");
            sql.append(column.getColumn());
            sql.append(" in ");
            sql.append("(\" close=\")\">");
            sql.append("#{id}");
            sql.append("</foreach>");
            sql.append("</where>");
        } else {
            throw new MapperException("继承 ByIdList 方法的实体类[" + entityClass.getCanonicalName()
                    + "]中必须只有一个带有 @Id 注解的字段");
        }
    }
}
