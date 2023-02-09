package com.msdn.generator.utils.mybatis;

import cn.hutool.extra.spring.SpringUtil;
import com.msdn.generator.common.exception.BusinessException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.annotation.Version;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.weekend.Fn;
import tk.mybatis.mapper.weekend.WeekendSqls;
import tk.mybatis.mapper.weekend.reflection.Reflections;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2022/9/10
 * @description Mybatis工具类，除了动态查询，还可以实现修改、删除等
 */
public class ExampleBuilder<T> {

    private Class<T> entityClass;

    private Class<? extends Mapper<T>> mapperClass;

    private final WeekendSqls<T> weekendSqls = WeekendSqls.custom();

    // 存放属性名和排序方式，ASC为true，DESC为false
    private final LinkedHashMap<String, Boolean> orderList = new LinkedHashMap<>();

    private final Map<String, Object> setterList = new HashMap<>();

    private ExampleBuilder() {
    }

    public static <T, M extends Mapper<T>> ExampleBuilder<T> create(Class<M> clazz) {
        ExampleBuilder<T> exampleBuilder = new ExampleBuilder<>();
        exampleBuilder.mapperClass = clazz;
        exampleBuilder.entityClass = (Class<T>) ((ParameterizedType) clazz
                .getGenericInterfaces()[0])
                .getActualTypeArguments()[0];
        return exampleBuilder;
    }

    public ExampleBuilder<T> andIsNull(Fn<T, Object> fn) {
        weekendSqls.andIsNull(Reflections.fnToFieldName(fn));
        return this;
    }

    public ExampleBuilder<T> andIsNull(boolean condition, Fn<T, Object> fn) {
        if (condition) {
            weekendSqls.andIsNull(Reflections.fnToFieldName(fn));
        }
        return this;
    }

    public ExampleBuilder<T> andIsNotNull(Fn<T, Object> fn) {
        weekendSqls.andIsNotNull(Reflections.fnToFieldName(fn));
        return this;
    }

    public ExampleBuilder<T> andIsNotNull(boolean condition, Fn<T, Object> fn) {
        if (condition) {
            weekendSqls.andIsNotNull(Reflections.fnToFieldName(fn));
        }
        return this;
    }

    public ExampleBuilder<T> andEqualTo(Fn<T, Object> fn, Object value) {
        weekendSqls.andEqualTo(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public ExampleBuilder<T> andEqualTo(boolean condition, Fn<T, Object> fn, Object value) {
        if (condition) {
            weekendSqls.andEqualTo(Reflections.fnToFieldName(fn), value);
        }
        return this;
    }

    public ExampleBuilder<T> andNotEqualTo(Fn<T, Object> fn, Object value) {
        weekendSqls.andNotEqualTo(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public ExampleBuilder<T> andNotEqualTo(boolean condition, Fn<T, Object> fn, Object value) {
        if (condition) {
            weekendSqls.andNotEqualTo(Reflections.fnToFieldName(fn), value);
        }
        return this;
    }

    public ExampleBuilder<T> andGreaterThan(Fn<T, Object> fn, Object value) {
        weekendSqls.andGreaterThan(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public ExampleBuilder<T> andGreaterThan(boolean condition, Fn<T, Object> fn, Object value) {
        if (condition) {
            weekendSqls.andGreaterThan(Reflections.fnToFieldName(fn), value);
        }
        return this;
    }

    public ExampleBuilder<T> andGreaterThanOrEqualTo(Fn<T, Object> fn, Object value) {
        weekendSqls.andGreaterThanOrEqualTo(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public ExampleBuilder<T> andGreaterThanOrEqualTo(boolean condition, Fn<T, Object> fn,
            Object value) {
        if (condition) {
            weekendSqls.andGreaterThanOrEqualTo(Reflections.fnToFieldName(fn), value);
        }
        return this;
    }

    public ExampleBuilder<T> andLessThan(Fn<T, Object> fn, Object value) {
        weekendSqls.andLessThan(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public ExampleBuilder<T> andLessThan(boolean condition, Fn<T, Object> fn, Object value) {
        if (condition) {
            weekendSqls.andLessThan(Reflections.fnToFieldName(fn), value);
        }
        return this;
    }

    public ExampleBuilder<T> andLessThanOrEqualTo(Fn<T, Object> fn, Object value) {
        weekendSqls.andLessThanOrEqualTo(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public ExampleBuilder<T> andLessThanOrEqualTo(boolean condition, Fn<T, Object> fn,
            Object value) {
        if (condition) {
            weekendSqls.andLessThanOrEqualTo(Reflections.fnToFieldName(fn), value);
        }
        return this;
    }

    public ExampleBuilder<T> andIn(Fn<T, Object> fn, Iterable<T> values) {
        weekendSqls.andIn(Reflections.fnToFieldName(fn), values);
        return this;
    }

    public ExampleBuilder<T> andIn(boolean condition, Fn<T, Object> fn, Iterable<T> values) {
        if (condition) {
            weekendSqls.andIn(Reflections.fnToFieldName(fn), values);
        }
        return this;
    }

    public ExampleBuilder<T> andNotIn(Fn<T, Object> fn, Iterable<T> values) {
        weekendSqls.andNotIn(Reflections.fnToFieldName(fn), values);
        return this;
    }

    public ExampleBuilder<T> andNotIn(boolean condition, Fn<T, Object> fn, Iterable<T> values) {
        if (condition) {
            weekendSqls.andNotIn(Reflections.fnToFieldName(fn), values);
        }
        return this;
    }

    public ExampleBuilder<T> andBetween(Fn<T, Object> fn, Object value1, Object value2) {
        weekendSqls.andBetween(Reflections.fnToFieldName(fn), value1, value2);
        return this;
    }

    public ExampleBuilder<T> andBetween(boolean condition, Fn<T, Object> fn, Object value1,
            Object value2) {
        if (condition) {
            weekendSqls.andBetween(Reflections.fnToFieldName(fn), value1, value2);
        }
        return this;
    }

    public ExampleBuilder<T> andNotBetween(Fn<T, Object> fn, Object value1, Object value2) {
        weekendSqls.andNotBetween(Reflections.fnToFieldName(fn), value1, value2);
        return this;
    }

    public ExampleBuilder<T> andNotBetween(boolean condition, Fn<T, Object> fn, Object value1,
            Object value2) {
        if (condition) {
            weekendSqls.andNotBetween(Reflections.fnToFieldName(fn), value1, value2);
        }
        return this;
    }

    public ExampleBuilder<T> andLike(Fn<T, Object> fn, String value) {
        weekendSqls.andLike(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public ExampleBuilder<T> andLike(boolean condition, Fn<T, Object> fn, String value) {
        if (condition) {
            weekendSqls.andLike(Reflections.fnToFieldName(fn), value);
        }
        return this;
    }

    public ExampleBuilder<T> andNotLike(Fn<T, Object> fn, String value) {
        weekendSqls.andNotLike(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public ExampleBuilder<T> andNotLike(boolean condition, Fn<T, Object> fn, String value) {
        if (condition) {
            weekendSqls.andNotLike(Reflections.fnToFieldName(fn), value);
        }
        return this;
    }

    public ExampleBuilder<T> orIsNull(Fn<T, Object> fn) {
        weekendSqls.orIsNull(Reflections.fnToFieldName(fn));
        return this;
    }

    public ExampleBuilder<T> orIsNull(boolean condition, Fn<T, Object> fn) {
        if (condition) {
            weekendSqls.orIsNull(Reflections.fnToFieldName(fn));
        }
        return this;
    }

    public ExampleBuilder<T> orIsNotNull(Fn<T, Object> fn) {
        weekendSqls.orIsNotNull(Reflections.fnToFieldName(fn));
        return this;
    }

    public ExampleBuilder<T> orIsNotNull(boolean condition, Fn<T, Object> fn) {
        if (condition) {
            weekendSqls.orIsNotNull(Reflections.fnToFieldName(fn));
        }
        return this;
    }

    public ExampleBuilder<T> orEqualTo(Fn<T, Object> fn, String value) {
        weekendSqls.orEqualTo(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public ExampleBuilder<T> orEqualTo(boolean condition, Fn<T, Object> fn, String value) {
        if (condition) {
            weekendSqls.orEqualTo(Reflections.fnToFieldName(fn), value);
        }
        return this;
    }

    public ExampleBuilder<T> orNotEqualTo(Fn<T, Object> fn, String value) {
        weekendSqls.orNotEqualTo(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public ExampleBuilder<T> orNotEqualTo(boolean condition, Fn<T, Object> fn, String value) {
        if (condition) {
            weekendSqls.orNotEqualTo(Reflections.fnToFieldName(fn), value);
        }
        return this;
    }

    public ExampleBuilder<T> orGreaterThan(Fn<T, Object> fn, String value) {
        weekendSqls.orGreaterThan(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public ExampleBuilder<T> orGreaterThan(boolean condition, Fn<T, Object> fn, String value) {
        if (condition) {
            weekendSqls.orGreaterThan(Reflections.fnToFieldName(fn), value);
        }
        return this;
    }

    public ExampleBuilder<T> orGreaterThanOrEqualTo(Fn<T, Object> fn, String value) {
        weekendSqls.orGreaterThanOrEqualTo(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public ExampleBuilder<T> orGreaterThanOrEqualTo(boolean condition, Fn<T, Object> fn,
            String value) {
        if (condition) {
            weekendSqls.orGreaterThanOrEqualTo(Reflections.fnToFieldName(fn), value);
        }
        return this;
    }

    public ExampleBuilder<T> orLessThan(Fn<T, Object> fn, String value) {
        weekendSqls.orLessThan(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public ExampleBuilder<T> orLessThan(boolean condition, Fn<T, Object> fn, String value) {
        if (condition) {
            weekendSqls.orLessThan(Reflections.fnToFieldName(fn), value);
        }
        return this;
    }

    public ExampleBuilder<T> orLessThanOrEqualTo(Fn<T, Object> fn, String value) {
        weekendSqls.orLessThanOrEqualTo(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public ExampleBuilder<T> orLessThanOrEqualTo(boolean condition, Fn<T, Object> fn,
            String value) {
        if (condition) {
            weekendSqls.orLessThanOrEqualTo(Reflections.fnToFieldName(fn), value);
        }
        return this;
    }

    public ExampleBuilder<T> orIn(Fn<T, Object> fn, Iterable<T> values) {
        weekendSqls.orIn(Reflections.fnToFieldName(fn), values);
        return this;
    }

    public ExampleBuilder<T> orIn(boolean condition, Fn<T, Object> fn, Iterable<T> values) {
        if (condition) {
            weekendSqls.orIn(Reflections.fnToFieldName(fn), values);
        }
        return this;
    }

    public ExampleBuilder<T> orNotIn(Fn<T, Object> fn, Iterable<T> values) {
        weekendSqls.orNotIn(Reflections.fnToFieldName(fn), values);
        return this;
    }

    public ExampleBuilder<T> orNotIn(boolean condition, Fn<T, Object> fn, Iterable<T> values) {
        if (condition) {
            weekendSqls.orNotIn(Reflections.fnToFieldName(fn), values);
        }
        return this;
    }

    public ExampleBuilder<T> orBetween(Fn<T, Object> fn, Object value1, Object value2) {
        weekendSqls.orBetween(Reflections.fnToFieldName(fn), value1, value2);
        return this;
    }

    public ExampleBuilder<T> orBetween(boolean condition, Fn<T, Object> fn, Object value1,
            Object value2) {
        if (condition) {
            weekendSqls.orBetween(Reflections.fnToFieldName(fn), value1, value2);
        }
        return this;
    }

    public ExampleBuilder<T> orNotBetween(Fn<T, Object> fn, Object value1, Object value2) {
        weekendSqls.orNotBetween(Reflections.fnToFieldName(fn), value1, value2);
        return this;
    }

    public ExampleBuilder<T> orNotBetween(boolean condition, Fn<T, Object> fn, Object value1,
            Object value2) {
        if (condition) {
            weekendSqls.orNotBetween(Reflections.fnToFieldName(fn), value1, value2);
        }
        return this;
    }

    public ExampleBuilder<T> orLike(Fn<T, Object> fn, String value) {
        weekendSqls.orLike(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public ExampleBuilder<T> orLike(boolean condition, Fn<T, Object> fn, String value) {
        if (condition) {
            weekendSqls.orLike(Reflections.fnToFieldName(fn), value);
        }
        return this;
    }

    public ExampleBuilder<T> orNotLike(Fn<T, Object> fn, String value) {
        weekendSqls.orNotLike(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public ExampleBuilder<T> orNotLike(boolean condition, Fn<T, Object> fn, String value) {
        if (condition) {
            weekendSqls.orNotLike(Reflections.fnToFieldName(fn), value);
        }
        return this;
    }

    public ExampleBuilder<T> orderByAsc(Fn<T, Object> fn) {
        String fieldName = Reflections.fnToFieldName(fn);
        this.orderList.put(fieldName, true);
        return this;
    }

    public ExampleBuilder<T> orderByDesc(Fn<T, Object> fn) {
        String fieldName = Reflections.fnToFieldName(fn);
        this.orderList.put(fieldName, false);
        return this;
    }

    public ExampleBuilder<T> set(Fn<T, Object> fn, Object value) {
        String fieldName = Reflections.fnToFieldName(fn);
        this.setterList.put(fieldName, value);
        return this;
    }

    public Example build() {
        Example.Builder builder = Example.builder(entityClass);
        if (!CollectionUtils.isEmpty(weekendSqls.getCriteria().getCriterions())) {
            builder.where(weekendSqls);
        }
        for (Map.Entry<String, Boolean> entry : this.orderList.entrySet()) {
            if (entry.getValue()) {
                builder.orderByAsc(entry.getKey());
            } else {
                builder.orderByDesc(entry.getKey());
            }
        }
        return builder.build();
    }

    public List<T> select() {
        Example example = this.build();
        return SpringUtil.getBean(mapperClass).selectByExample(example);
    }

    public List<T> select(int offset, int limit) {
        Example example = this.build();
        return SpringUtil.getBean(mapperClass)
                .selectByExampleAndRowBounds(example, new RowBounds(offset, limit));
    }

    public T selectOne() {
        Example example = this.build();
        return SpringUtil.getBean(mapperClass).selectOneByExample(example);
    }

    public int update() {
        if (CollectionUtils.isEmpty(setterList)) {
            return 0;
        }

        return doUpdate();
    }

    /**
     * 更新，带版本号
     *
     * @param version 当前数据的版本号
     * @return 受影响的行数
     */
    public int update(Integer version) {
        if (Objects.isNull(version)) {
            BusinessException.validateFailed("version不能为空");
        }

        if (CollectionUtils.isEmpty(setterList)) {
            BusinessException.validateFailed("没有更新任何字段");
        }

        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
        for (EntityColumn column : columns) {
            if (column.getEntityField().isAnnotationPresent(Version.class)) {
                this.weekendSqls.andEqualTo("version", version);
                this.setterList.put("version", version + 1);
            }
        }

        return doUpdate();
    }

    private int doUpdate() {
        T object = null;
        try {
            object = entityClass.newInstance();
            for (Map.Entry<String, Object> entry : this.setterList.entrySet()) {
                Field field = entityClass.getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(object, entry.getValue());
            }
        } catch (Exception e) {
            BusinessException.fail("类型错误，创建更新对象失败");
        }
        Example example = this.build();
        return SpringUtil.getBean(mapperClass).updateByExampleSelective(object, example);
    }

    public int delete() {
        Example example = this.build();
        return SpringUtil.getBean(mapperClass).deleteByExample(example);
    }

    public int updateSelective(T object) {
        Example example = this.build();
        return SpringUtil.getBean(mapperClass).updateByExampleSelective(object, example);
    }

    public List<T> query(Map<String, Object> queryMap) {
        for (Field field : entityClass.getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            String fieldName = field.getName();
            if (fieldType.equals(String.class)) {
                Object value = queryMap.get(fieldName);
                if (value != null && !"".equals(value)) {
                    weekendSqls.andEqualTo(fieldName, value);
                }
                Object listValue = queryMap.get(fieldName + "List");
                if (listValue != null) {
                    if (listValue.getClass().isArray()) {
                        Object[] arrayValue = (Object[]) listValue;
                        List<Object> list = new ArrayList<>();
                        Collections.addAll(list, arrayValue);
                        weekendSqls.andIn(fieldName, list);
                    } else {
                        weekendSqls.andIn(fieldName, (Iterable) listValue);
                    }
                }
                Object allLikeValue = queryMap.get(fieldName + "AllLike");
                if (allLikeValue != null && !"".equals(value)) {
                    weekendSqls.andLike(fieldName, "%" + allLikeValue + "%");
                }
                Object leftLikeValue = queryMap.get(fieldName + "LeftLike");
                if (leftLikeValue != null && !"".equals(value)) {
                    weekendSqls.andLike(fieldName, "%" + leftLikeValue);
                }
                Object rightLikeValue = queryMap.get(fieldName + "RightLike");
                if (rightLikeValue != null && !"".equals(value)) {
                    weekendSqls.andLike(fieldName, rightLikeValue + "%");
                }
            } else if (fieldType.equals(Integer.class) || fieldType.equals(BigDecimal.class)) {
                Object value = queryMap.get(fieldName);
                if (value != null) {
                    weekendSqls.andEqualTo(fieldName, value);
                }
                Object listValue = queryMap.get(fieldName + "List");
                if (listValue != null) {
                    if (listValue.getClass().isArray()) {
                        Object[] arrayValue = (Object[]) listValue;
                        List<Object> list = new ArrayList<>();
                        Collections.addAll(list, arrayValue);
                        weekendSqls.andIn(fieldName, list);
                    } else {
                        weekendSqls.andIn(fieldName, (Iterable) listValue);
                    }
                }
            } else if (fieldType.equals(Date.class)) {
                Object startValue = queryMap.get(fieldName + "Start");
                if (startValue != null) {
                    weekendSqls.andGreaterThanOrEqualTo(fieldName, startValue);
                }
                Object endValue = queryMap.get(fieldName + "End");
                if (endValue != null) {
                    weekendSqls.andLessThan(fieldName, endValue);
                }
            } else {
                // 其他类型仅支持精确搜索
                Object value = queryMap.get(fieldName);
                if (value != null) {
                    weekendSqls.andEqualTo(fieldName, value);
                }
            }
        }
        return select();
    }
}
