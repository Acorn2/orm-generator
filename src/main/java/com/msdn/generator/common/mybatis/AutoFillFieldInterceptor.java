package com.msdn.generator.common.mybatis;

import com.msdn.generator.common.mybatis.annotation.CreatedName;
import com.msdn.generator.common.mybatis.annotation.LastModifiedName;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import tk.mybatis.mapper.annotation.LogicDelete;
import tk.mybatis.mapper.annotation.Version;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityField;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.util.MetaObjectUtil;

import javax.persistence.Table;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 */
@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class,
        Object.class})})
public class AutoFillFieldInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(AutoFillFieldInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        logger.info("执行intercept方法：{}", invocation.toString());
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if (sqlCommandType != SqlCommandType.INSERT && sqlCommandType != SqlCommandType.UPDATE) {
            return invocation.proceed();
        }
        Object parameter = args[1];
        Class<?> clazz = parameter.getClass();
        if (Map.class.isAssignableFrom(clazz)) {
            Map<String, Object> paramMap = (Map<String, Object>) parameter;
            if (paramMap.containsKey("param1")) {
                processData(sqlCommandType, paramMap.get("param1"));
            } else if (paramMap.containsKey("arg0")) {
                processData(sqlCommandType, paramMap.get("arg0"));
            } else if (paramMap.containsKey("list")) {
                processData(sqlCommandType, paramMap.get("list"));
            }
        } else {
            processData(sqlCommandType, parameter);
        }

        return invocation.proceed();
    }

    private boolean isSkipInject(Class clazz) {
        return clazz.getAnnotation(Table.class) == null;
    }

    private void processData(SqlCommandType sqlCommandType, Object parameter) {
        Class<?> clazz = parameter.getClass();
        if (Collection.class.isAssignableFrom(clazz)) {
            Collection<?> collection = (Collection<?>) parameter;
            for (Object object : collection) {
                processData(sqlCommandType, object);
            }
            return;
        }

        if (isSkipInject(clazz)) {
            return;
        }

        List<EntityField> entityFieldList = getFields(clazz);
        MetaObject metaObject = MetaObjectUtil.forObject(parameter);
        for (EntityField field : entityFieldList) {
            if (sqlCommandType == SqlCommandType.INSERT) {
                if (field.isAnnotationPresent(CreatedDate.class)) {
                    metaObject.setValue(field.getName(), new Date());
                }
                if (field.isAnnotationPresent(CreatedBy.class)) {
                    String id = "1";
                    metaObject.setValue(field.getName(), id);
                }
                if (field.isAnnotationPresent(CreatedName.class)) {
                    metaObject.setValue(field.getName(), "admin");
                }
                if (field.isAnnotationPresent(Version.class)) {
                    metaObject.setValue(field.getName(), 0);
                }
                if (field.isAnnotationPresent(LogicDelete.class)) {
                    metaObject.setValue(field.getName(), false);
                }
            }
            if (field.isAnnotationPresent(LastModifiedDate.class)) {
                metaObject.setValue(field.getName(), new Date());
            }
            if (field.isAnnotationPresent(LastModifiedBy.class)) {
                metaObject.setValue(field.getName(), "1");
            }
            if (field.isAnnotationPresent(LastModifiedName.class)) {
                metaObject.setValue(field.getName(), "admin");
            }
        }
    }

    private List<EntityField> getFields(Class clazz) {
        return EntityHelper.getColumns(clazz).stream().map(EntityColumn::getEntityField)
                .collect(Collectors.toList());
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
