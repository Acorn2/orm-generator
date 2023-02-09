package com.msdn.generator.utils.jpa;

import cn.hutool.extra.spring.SpringUtil;
import com.msdn.generator.common.entity.PageSortInfo;
import com.msdn.generator.common.entity.OrderInfo;
import com.msdn.generator.utils.StringUtils;
import com.msdn.generator.utils.jpa.common.Connector;
import com.msdn.generator.utils.jpa.common.IFn;
import com.msdn.generator.utils.jpa.common.MatchCondition;
import com.msdn.generator.utils.jpa.common.Operator;
import com.msdn.generator.utils.jpa.common.QueryItem;
import com.msdn.generator.utils.jpa.common.QueryParam;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.CollectionUtils;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2022/9/9 2:59 下午
 * @description JPA动态查询工具类
 */
public class SpecificationBuilder<T> {

    private Class<? extends JpaSpecificationExecutor<T>> specificationExecutorClass;

    // 存放属性名和排序方式，ASC为true，DESC为false
    private final LinkedHashMap<String, Boolean> orderList = new LinkedHashMap<>();

    private final List<QueryParam> queryParams = new ArrayList<>();

    private SpecificationBuilder() {

    }

    public static <T, M extends JpaSpecificationExecutor<T>> SpecificationBuilder<T> create(
            Class<M> clazz) {
        SpecificationBuilder<T> specificationBuilder = new SpecificationBuilder<>();
        specificationBuilder.specificationExecutorClass = clazz;
        return specificationBuilder;
    }

    public SpecificationBuilder<T> andEqualTo(IFn<T, Object> fn, Object value) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .fieldValue(value).operator(Operator.AND).matchCondition(
                        MatchCondition.EQUAL).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    private void addQueryItemToWhereParam(QueryItem queryItem) {
        if (CollectionUtils.isEmpty(queryParams)) {
            queryParams.add(addQueryItem(queryItem));
        } else {
            Optional<QueryParam> queryParamOptional = queryParams.stream()
                    .filter(obj -> StringUtils.isEmpty(obj.getJoinName())).findFirst();
            if (queryParamOptional.isPresent()) {
                QueryParam queryParam = queryParamOptional.get();
                queryParam.getQueryItems().add(queryItem);
            } else {
                queryParams.add(addQueryItem(queryItem));
            }
        }
    }

    private QueryParam addQueryItem(QueryItem queryItem) {
        QueryParam queryParam = QueryParam.builder().connector(Connector.WHERE).build();
        List<QueryItem> queryItems = new ArrayList<>();
        queryItems.add(queryItem);
        queryParam.setQueryItems(queryItems);
        return queryParam;
    }

    private void addQueryItemToOnParam(QueryItem queryItem, String joinName, JoinType joinType) {
        if (StringUtils.isEmpty(joinName)) {
            return;
        }
        if (CollectionUtils.isEmpty(queryParams)) {
            queryParams.add(addQueryItem(queryItem, joinName, joinType));
        } else {
            Optional<QueryParam> queryParamOptional = queryParams.stream()
                    .filter(obj -> joinName.equals(obj.getJoinName())).findFirst();
            if (queryParamOptional.isPresent()) {
                QueryParam queryParam = queryParamOptional.get();
                queryParam.getQueryItems().add(queryItem);
            } else {
                queryParams.add(addQueryItem(queryItem, joinName, joinType));
            }
        }
    }

    private QueryParam addQueryItem(QueryItem queryItem, String joinName, JoinType joinType) {
        Connector connector;
        if (StringUtils.isNotEmpty(joinName)) {
            connector = Connector.ON;
        } else {
            connector = Connector.WHERE;
        }
        QueryParam queryParam = QueryParam.builder().connector(connector).joinName(joinName)
                .joinType(joinType).build();
        List<QueryItem> queryItems = new ArrayList<>();
        if (Objects.isNull(queryItem)) {
            return queryParam;
        }
        queryItems.add(queryItem);
        queryParam.setQueryItems(queryItems);
        return queryParam;
    }

    public SpecificationBuilder<T> andEqualTo(boolean condition, IFn<T, Object> fn, Object value) {
        if (condition) {
            return andEqualTo(fn, value);
        }
        return this;
    }

    public SpecificationBuilder<T> andNotEqualTo(IFn<T, Object> fn, Object value) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .fieldValue(value).operator(Operator.AND).matchCondition(
                        MatchCondition.NOT_EQUAL).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> andNotEqualTo(boolean condition, IFn<T, Object> fn,
            Object value) {
        if (condition) {
            return andNotEqualTo(fn, value);
        }
        return this;
    }

    public SpecificationBuilder<T> andGreaterThan(IFn<T, Object> fn, Object value) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .fieldValue(value).operator(Operator.AND).matchCondition(
                        MatchCondition.GT).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> andGreaterThan(boolean condition, IFn<T, Object> fn,
            Object value) {
        if (condition) {
            return andGreaterThan(fn, value);
        }
        return this;
    }

    public SpecificationBuilder<T> andGreaterThanOrEqualTo(IFn<T, Object> fn, Object value) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .fieldValue(value).operator(Operator.AND).matchCondition(
                        MatchCondition.GE).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> andGreaterThanOrEqualTo(boolean condition, IFn<T, Object> fn,
            Object value) {
        if (condition) {
            return andGreaterThanOrEqualTo(fn, value);
        }
        return this;
    }

    public SpecificationBuilder<T> andLessThan(IFn<T, Object> fn, Object value) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .fieldValue(value).operator(Operator.AND).matchCondition(
                        MatchCondition.LT).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> andLessThan(boolean condition, IFn<T, Object> fn,
            Object value) {
        if (condition) {
            return andLessThan(fn, value);
        }
        return this;
    }

    public SpecificationBuilder<T> andLessThanOrEqualTo(IFn<T, Object> fn, Object value) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .fieldValue(value).operator(Operator.AND).matchCondition(
                        MatchCondition.LE).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> andLessThanOrEqualTo(boolean condition, IFn<T, Object> fn,
            Object value) {
        if (condition) {
            return andLessThanOrEqualTo(fn, value);
        }
        return this;
    }

    public SpecificationBuilder<T> andIn(IFn<T, Object> fn, Iterable<Object> values) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .iterable(values).operator(Operator.AND).matchCondition(
                        MatchCondition.IN).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> andIn(boolean condition, IFn<T, Object> fn,
            Iterable<Object> values) {
        if (condition) {
            return andIn(fn, values);
        }
        return this;
    }

    public SpecificationBuilder<T> andNotIn(IFn<T, Object> fn, Iterable<Object> values) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .iterable(values).operator(Operator.AND).matchCondition(
                        MatchCondition.NOT_IN).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> andNotIn(boolean condition, IFn<T, Object> fn,
            Iterable<Object> values) {
        if (condition) {
            return andNotIn(fn, values);
        }
        return this;
    }

    public SpecificationBuilder<T> andBetween(IFn<T, Object> fn, Object value1, Object value2) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .startValue(value1).endValue(value2).operator(Operator.AND).matchCondition(
                        MatchCondition.BETWEEN).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> andBetween(boolean condition, IFn<T, Object> fn, Object value1,
            Object value2) {
        if (condition) {
            return andBetween(fn, value1, value2);
        }
        return this;
    }

    public SpecificationBuilder<T> andNotBetween(IFn<T, Object> fn, Object value1, Object value2) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .startValue(value1).endValue(value2).operator(Operator.AND).matchCondition(
                        MatchCondition.NOT_BETWEEN).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> andNotBetween(boolean condition, IFn<T, Object> fn,
            Object value1,
            Object value2) {
        if (condition) {
            return andNotBetween(fn, value1, value2);
        }
        return this;
    }

    public SpecificationBuilder<T> andLike(IFn<T, Object> fn, Object value) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .fieldValue(value).operator(Operator.AND).matchCondition(
                        MatchCondition.LIKE).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> andLike(boolean condition, IFn<T, Object> fn, Object value) {
        if (condition) {
            return andLike(fn, value);
        }
        return this;
    }

    public SpecificationBuilder<T> andNotLike(IFn<T, Object> fn, Object value) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .fieldValue(value).operator(Operator.AND).matchCondition(
                        MatchCondition.NOT_LIKE).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> andNotLike(boolean condition, IFn<T, Object> fn, Object value) {
        if (condition) {
            return andNotLike(fn, value);
        }
        return this;
    }


    public SpecificationBuilder<T> orEqualTo(IFn<T, Object> fn, Object value) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .fieldValue(value).operator(Operator.OR).matchCondition(
                        MatchCondition.EQUAL).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> orEqualTo(boolean condition, IFn<T, Object> fn, Object value) {
        if (condition) {
            return orEqualTo(fn, value);
        }
        return this;
    }

    public SpecificationBuilder<T> orNotEqualTo(IFn<T, Object> fn, Object value) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .fieldValue(value).operator(Operator.OR).matchCondition(
                        MatchCondition.NOT_EQUAL).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> orNotEqualTo(boolean condition, IFn<T, Object> fn,
            Object value) {
        if (condition) {
            return orNotEqualTo(fn, value);
        }
        return this;
    }

    public SpecificationBuilder<T> orGreaterThan(IFn<T, Object> fn, Object value) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .fieldValue(value).operator(Operator.OR).matchCondition(
                        MatchCondition.GT).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> orGreaterThan(boolean condition, IFn<T, Object> fn,
            Object value) {
        if (condition) {
            return orGreaterThan(fn, value);
        }
        return this;
    }

    public SpecificationBuilder<T> orGreaterThanOrEqualTo(IFn<T, Object> fn, Object value) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .fieldValue(value).operator(Operator.OR).matchCondition(
                        MatchCondition.GE).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> orGreaterThanOrEqualTo(boolean condition, IFn<T, Object> fn,
            Object value) {
        if (condition) {
            return orGreaterThanOrEqualTo(fn, value);
        }
        return this;
    }

    public SpecificationBuilder<T> orLessThan(IFn<T, Object> fn, Object value) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .fieldValue(value).operator(Operator.OR).matchCondition(
                        MatchCondition.LT).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> orLessThan(boolean condition, IFn<T, Object> fn, Object value) {
        if (condition) {
            return orLessThan(fn, value);
        }
        return this;
    }

    public SpecificationBuilder<T> orLessThanOrEqualTo(IFn<T, Object> fn, Object value) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .fieldValue(value).operator(Operator.OR).matchCondition(
                        MatchCondition.LE).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> orLessThanOrEqualTo(boolean condition, IFn<T, Object> fn,
            Object value) {
        if (condition) {
            return orLessThanOrEqualTo(fn, value);
        }
        return this;
    }

    public SpecificationBuilder<T> orIn(IFn<T, Object> fn, Iterable<Object> values) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .iterable(values).operator(Operator.OR).matchCondition(
                        MatchCondition.IN).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> orIn(boolean condition, IFn<T, Object> fn,
            Iterable<Object> values) {
        if (condition) {
            return orIn(fn, values);
        }
        return this;
    }

    public SpecificationBuilder<T> orNotIn(IFn<T, Object> fn, Iterable<Object> values) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .iterable(values).operator(Operator.OR).matchCondition(
                        MatchCondition.NOT_IN).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> orNotIn(boolean condition, IFn<T, Object> fn,
            Iterable<Object> values) {
        if (condition) {
            return orNotIn(fn, values);
        }
        return this;
    }

    public SpecificationBuilder<T> orBetween(IFn<T, Object> fn, Object value1, Object value2) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .startValue(value1).endValue(value2).operator(Operator.OR).matchCondition(
                        MatchCondition.BETWEEN).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> orBetween(boolean condition, IFn<T, Object> fn, Object value1,
            Object value2) {
        if (condition) {
            return orBetween(fn, value1, value2);
        }
        return this;
    }

    public SpecificationBuilder<T> orNotBetween(IFn<T, Object> fn, Object value1, Object value2) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .startValue(value1).endValue(value2).operator(Operator.OR).matchCondition(
                        MatchCondition.NOT_BETWEEN).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> orNotBetween(boolean condition, IFn<T, Object> fn, Object value1,
            Object value2) {
        if (condition) {
            return orNotBetween(fn, value1, value2);
        }
        return this;
    }

    public SpecificationBuilder<T> orLike(IFn<T, Object> fn, Object value) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .fieldValue(value).operator(Operator.OR).matchCondition(
                        MatchCondition.LIKE).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> orLike(boolean condition, IFn<T, Object> fn, Object value) {
        if (condition) {
            return orLike(fn, value);
        }
        return this;
    }

    public SpecificationBuilder<T> orNotLike(IFn<T, Object> fn, Object value) {
        QueryItem queryItem = QueryItem.builder().fieldName(Reflections.fnToFieldName(fn))
                .fieldValue(value).operator(Operator.OR).matchCondition(
                        MatchCondition.NOT_LIKE).build();

        addQueryItemToWhereParam(queryItem);
        return this;
    }

    public SpecificationBuilder<T> orNotLike(boolean condition, IFn<T, Object> fn, Object value) {
        if (condition) {
            return orNotLike(fn, value);
        }
        return this;
    }

    public SpecificationBuilder<T> orderByAsc(IFn<T, Object> fn) {
        String fieldName = Reflections.fnToFieldName(fn);
        this.orderList.put(fieldName, true);
        return this;
    }

    public SpecificationBuilder<T> orderByDesc(IFn<T, Object> fn) {
        String fieldName = Reflections.fnToFieldName(fn);
        this.orderList.put(fieldName, false);
        return this;
    }

    public SpecificationBuilder<T> leftJoin(IFn<T, Object> fn) {
        addQueryItemToOnParam(null, Reflections.fnToFieldName(fn), JoinType.LEFT);
        return this;
    }

    public SpecificationBuilder<T> leftJoinAndOnEqualTo(IFn<T, Object> fn, String fieldName,
            Object value) {
        QueryItem queryItem = QueryItem.builder().fieldName(fieldName).fieldValue(value)
                .matchCondition(MatchCondition.EQUAL).operator(Operator.AND).build();

        addQueryItemToOnParam(queryItem, Reflections.fnToFieldName(fn), JoinType.LEFT);

        return this;
    }

    public SpecificationBuilder<T> innerJoin(IFn<T, Object> fn) {
        addQueryItemToOnParam(null, Reflections.fnToFieldName(fn), JoinType.INNER);
        return this;
    }

    public SpecificationBuilder<T> rightJoin(IFn<T, Object> fn) {
        addQueryItemToOnParam(null, Reflections.fnToFieldName(fn), JoinType.RIGHT);
        return this;
    }

    public Specification<T> build() {
        return (Specification<T>) (root, query, criteriaBuilder) -> {
            if (CollectionUtils.isEmpty(queryParams)) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> andPredicates = new ArrayList<>();
            List<Predicate> orPredicates = new ArrayList<>();
            QueryParam whereQueryParam = queryParams.stream()
                    .filter(obj -> StringUtils.isEmpty(obj.getJoinName())).findFirst().get();
            for (QueryItem queryItem : whereQueryParam.getQueryItems()) {
                Path<Object> path = root.get(queryItem.getFieldName());
                Object value = queryItem.getFieldValue();

                Predicate predicate;
                if (Operator.AND.equals(queryItem.getOperator())) {
                    switch (queryItem.getMatchCondition()) {
                        case EQUAL:
                            andPredicates.add(criteriaBuilder.equal(path, value));
                            break;
                        case NOT_EQUAL:
                            andPredicates.add(criteriaBuilder.notEqual(path, value));
                            break;
                        case GT:
                            predicate = getGtPredicate(criteriaBuilder, path, value);
                            andPredicates.add(predicate);
                            break;
                        case GE:
                            predicate = getGePredicate(criteriaBuilder, path, value);
                            andPredicates.add(predicate);
                            break;
                        case LT:
                            predicate = getLtPredicate(criteriaBuilder, path, value);
                            andPredicates.add(predicate);
                            break;
                        case LE:
                            predicate = getLePredicate(criteriaBuilder, path, value);
                            andPredicates.add(predicate);
                            break;
                        case IN:
                            In<Object> in = criteriaBuilder.in(path);
                            for (Object o : queryItem.getIterable()) {
                                in.value(o);
                            }
                            andPredicates.add(criteriaBuilder.and(in));
                            break;
                        case NOT_IN:
                            In<Object> notIn = criteriaBuilder.in(path);
                            for (Object o : queryItem.getIterable()) {
                                notIn.value(o);
                            }
                            andPredicates.add(criteriaBuilder.not(notIn));
                            break;
                        case BETWEEN:
                            Object value1 = queryItem.getStartValue();
                            Object value2 = queryItem.getEndValue();
                            predicate = getBetweenPredicate(criteriaBuilder, path, value1, value2);
                            andPredicates.add(predicate);
                            break;
                        case NOT_BETWEEN:
                            value1 = queryItem.getStartValue();
                            value2 = queryItem.getEndValue();
                            predicate = getBetweenPredicate(criteriaBuilder, path, value1, value2);
                            andPredicates.add(criteriaBuilder.not(predicate));
                            break;
                        case LIKE:
                            if (value instanceof String) {
                                // 内部不做%拼接，交由服务层自行处理，更加灵活
                                predicate = criteriaBuilder
                                        .like(path.as(String.class), (String) value);
                                andPredicates.add(predicate);
                            }
                            break;
                        case NOT_LIKE:
                            if (value instanceof String) {
                                predicate = criteriaBuilder
                                        .like(path.as(String.class), (String) value);
                                andPredicates.add(criteriaBuilder.not(predicate));
                            }
                            break;
                        default:
                    }

                } else if (Operator.OR.equals(queryItem.getOperator())) {
                    switch (queryItem.getMatchCondition()) {
                        case EQUAL:
                            orPredicates
                                    .add(criteriaBuilder.or(criteriaBuilder.equal(path, value)));
                            break;
                        case NOT_EQUAL:
                            orPredicates
                                    .add(criteriaBuilder.or(criteriaBuilder.notEqual(path, value)));
                            break;
                        case GT:
                            predicate = getGtPredicate(criteriaBuilder, path, value);
                            orPredicates.add(criteriaBuilder.or(predicate));
                            break;
                        case GE:
                            predicate = getGePredicate(criteriaBuilder, path, value);
                            orPredicates.add(criteriaBuilder.or(predicate));
                            break;
                        case LT:
                            predicate = getLtPredicate(criteriaBuilder, path, value);
                            orPredicates.add(criteriaBuilder.or(predicate));
                            break;
                        case LE:
                            predicate = getLePredicate(criteriaBuilder, path, value);
                            orPredicates.add(criteriaBuilder.or(predicate));
                            break;
                        case IN:
                            In<Object> in = criteriaBuilder.in(path);
                            for (Object o : queryItem.getIterable()) {
                                in.value(o);
                            }
                            orPredicates.add(criteriaBuilder.or(in));
                            break;
                        case NOT_IN:
                            In<Object> notIn = criteriaBuilder.in(path);
                            for (Object o : queryItem.getIterable()) {
                                notIn.value(o);
                            }
                            orPredicates.add(criteriaBuilder.or(criteriaBuilder.not(notIn)));
                            break;
                        case BETWEEN:
                            Object value1 = queryItem.getStartValue();
                            Object value2 = queryItem.getEndValue();
                            predicate = getBetweenPredicate(criteriaBuilder, path, value1, value2);
                            orPredicates.add(criteriaBuilder.or(predicate));
                            break;
                        case NOT_BETWEEN:
                            value1 = queryItem.getStartValue();
                            value2 = queryItem.getEndValue();
                            predicate = getBetweenPredicate(criteriaBuilder, path, value1, value2);
                            orPredicates.add(criteriaBuilder.or(criteriaBuilder.not(predicate)));
                            break;
                        case LIKE:
                            if (value instanceof String) {
                                predicate = criteriaBuilder
                                        .like(path.as(String.class), "%" + value + "%");
                                orPredicates.add(criteriaBuilder.or(predicate));
                            }
                            break;
                        case NOT_LIKE:
                            if (value instanceof String) {
                                predicate = criteriaBuilder
                                        .like(path.as(String.class), "%" + value + "%");
                                orPredicates
                                        .add(criteriaBuilder.or(criteriaBuilder.not(predicate)));
                            }
                            break;
                        default:
                    }
                }
            }

            // on 和where的区别
            /**
             * 连接查询中，on是用来确定两张表的关联关系，关联好之后生成一个临时表，之后where对这个临时表再进行过滤筛选。
             * 先执行on，后执行 where；on是建立关联关系在生成临时表时候执行，where是在临时表生成后对数据进行筛选的。
             * 所以优先执行on条件查询，效率更高
             */
            List<QueryParam> onQueryParams = queryParams.stream()
                    .filter(obj -> StringUtils.isNotEmpty(obj.getJoinName()))
                    .collect(Collectors.toList());
            for (QueryParam onQueryParam : onQueryParams) {
                List<QueryItem> queryItems = onQueryParam.getQueryItems();
                if (CollectionUtils.isEmpty(queryItems)) {
                    root.join(onQueryParam.getJoinName(), onQueryParam.getJoinType());
                } else {
                    Join<Object, Object> join = root
                            .join(onQueryParam.getJoinName(), onQueryParam.getJoinType());
                    for (QueryItem queryItem : queryItems) {
                        Object value = queryItem.getFieldValue();
                        switch (queryItem.getMatchCondition()) {
                            case EQUAL:
                                if (value instanceof String) {
                                    // 关联表where查询
//                  andPredicates.add(criteriaBuilder
//                      .equal(join.get(queryItem.getFieldName()).as(String.class), value));
                                    // 关联表on查询
                                    join.on(criteriaBuilder
                                            .equal(join.get(queryItem.getFieldName())
                                                    .as(String.class), value));
                                } else if (value instanceof Integer) {
//                  andPredicates.add(criteriaBuilder
//                      .equal(join.get(queryItem.getFieldName()).as(Integer.class), value));
                                    join.on(criteriaBuilder
                                            .equal(join.get(queryItem.getFieldName())
                                                    .as(Integer.class), value));
                                } else if (value instanceof Long) {
//                  andPredicates.add(criteriaBuilder
//                      .equal(join.get(queryItem.getFieldName()).as(Long.class), value));
                                    // on查询
                                    join.on(criteriaBuilder
                                            .equal(join.get(queryItem.getFieldName())
                                                    .as(Long.class), value));
                                }
                                break;
                            case LIKE:
                                if (value instanceof String) {
                                    join.on(criteriaBuilder
                                            .like(join.get(queryItem.getFieldName())
                                                            .as(String.class),
                                                    "%" + value + "%"));
                                }
                                break;
                            default:
                        }
                    }
                }
            }

            // 封装and语句
            Predicate predicateForAnd = criteriaBuilder
                    .and(andPredicates.toArray(new Predicate[0]));

            //封装or语句
            Predicate predicateForOr = criteriaBuilder
                    .or(orPredicates.toArray(new Predicate[0]));

            if (!CollectionUtils.isEmpty(andPredicates) && !CollectionUtils.isEmpty(orPredicates)) {
                return query.where(predicateForAnd, predicateForOr).distinct(true).getRestriction();
            } else if (!CollectionUtils.isEmpty(andPredicates)) {
                return query.where(predicateForAnd).getRestriction();
            } else if (!CollectionUtils.isEmpty(orPredicates)) {
                return query.where(predicateForOr).distinct(true).getRestriction();
            }

            return criteriaBuilder.conjunction();
        };
    }

    private Predicate getBetweenPredicate(CriteriaBuilder criteriaBuilder, Path<Object> path,
            Object value1, Object value2) {
        if (value1 instanceof LocalDateTime) {
            return criteriaBuilder
                    .between(path.as(LocalDateTime.class), (LocalDateTime) value1,
                            (LocalDateTime) value2);
        } else if (value1 instanceof Integer) {
            return criteriaBuilder
                    .between(path.as(Integer.class), (Integer) value1, (Integer) value2);
        } else if (value1 instanceof Double) {
            return criteriaBuilder
                    .between(path.as(Double.class), (Double) value1, (Double) value2);
        } else if (value1 instanceof Float) {
            return criteriaBuilder
                    .between(path.as(Float.class), (Float) value1, (Float) value2);
        } else if (value1 instanceof Long) {
            return criteriaBuilder
                    .between(path.as(Long.class), (Long) value1, (Long) value2);
        } else if (value1 instanceof String) {
            return criteriaBuilder
                    .between(path.as(String.class), (String) value1, (String) value2);
        } else if (value1 instanceof BigDecimal) {
            return criteriaBuilder
                    .between(path.as(BigDecimal.class), (BigDecimal) value1, (BigDecimal) value2);
        }
        return null;
    }

    private Predicate getGtPredicate(CriteriaBuilder criteriaBuilder, Path<Object> path,
            Object value) {
        if (value instanceof LocalDateTime) {
            return criteriaBuilder
                    .greaterThan(path.as(LocalDateTime.class), (LocalDateTime) value);
        } else if (value instanceof Integer) {
            return criteriaBuilder.greaterThan(path.as(Integer.class), (Integer) value);
        } else if (value instanceof Double) {
            return criteriaBuilder.greaterThan(path.as(Double.class), (Double) value);
        } else if (value instanceof Float) {
            return criteriaBuilder.greaterThan(path.as(Float.class), (Float) value);
        } else if (value instanceof Long) {
            return criteriaBuilder.greaterThan(path.as(Long.class), (Long) value);
        } else if (value instanceof BigDecimal) {
            return criteriaBuilder
                    .greaterThan(path.as(BigDecimal.class), (BigDecimal) value);
        } else if (value instanceof String) {
            return criteriaBuilder.greaterThan(path.as(String.class), (String) value);
        }
        return null;
    }

    private Predicate getGePredicate(CriteriaBuilder criteriaBuilder, Path<Object> path,
            Object value) {
        if (value instanceof LocalDateTime) {
            return criteriaBuilder
                    .greaterThanOrEqualTo(path.as(LocalDateTime.class), (LocalDateTime) value);
        } else if (value instanceof Integer) {
            return criteriaBuilder
                    .greaterThanOrEqualTo(path.as(Integer.class), (Integer) value);
        } else if (value instanceof Double) {
            return criteriaBuilder
                    .greaterThanOrEqualTo(path.as(Double.class), (Double) value);
        } else if (value instanceof Float) {
            return criteriaBuilder
                    .greaterThanOrEqualTo(path.as(Float.class), (Float) value);
        } else if (value instanceof Long) {
            return criteriaBuilder.greaterThanOrEqualTo(path.as(Long.class), (Long) value);
        } else if (value instanceof BigDecimal) {
            return criteriaBuilder
                    .greaterThanOrEqualTo(path.as(BigDecimal.class), (BigDecimal) value);
        } else if (value instanceof String) {
            return criteriaBuilder
                    .greaterThanOrEqualTo(path.as(String.class), (String) value);
        }
        return null;
    }

    private Predicate getLtPredicate(CriteriaBuilder criteriaBuilder, Path<Object> path,
            Object value) {
        if (value instanceof LocalDateTime) {
            return criteriaBuilder
                    .lessThan(path.as(LocalDateTime.class), (LocalDateTime) value);
        } else if (value instanceof Integer) {
            return criteriaBuilder.lessThan(path.as(Integer.class), (Integer) value);
        } else if (value instanceof Double) {
            return criteriaBuilder.lessThan(path.as(Double.class), (Double) value);
        } else if (value instanceof Float) {
            return criteriaBuilder.lessThan(path.as(Float.class), (Float) value);
        } else if (value instanceof Long) {
            return criteriaBuilder.lessThan(path.as(Long.class), (Long) value);
        } else if (value instanceof BigDecimal) {
            return criteriaBuilder.lessThan(path.as(BigDecimal.class), (BigDecimal) value);
        } else if (value instanceof String) {
            return criteriaBuilder.lessThan(path.as(String.class), (String) value);
        }
        return null;
    }

    private Predicate getLePredicate(CriteriaBuilder criteriaBuilder, Path<Object> path,
            Object value) {
        if (value instanceof LocalDateTime) {
            return criteriaBuilder
                    .lessThanOrEqualTo(path.as(LocalDateTime.class), (LocalDateTime) value);
        } else if (value instanceof Integer) {
            return criteriaBuilder
                    .lessThanOrEqualTo(path.as(Integer.class), (Integer) value);
        } else if (value instanceof Double) {
            return criteriaBuilder
                    .lessThanOrEqualTo(path.as(Double.class), (Double) value);
        } else if (value instanceof Float) {
            return criteriaBuilder.lessThanOrEqualTo(path.as(Float.class), (Float) value);
        } else if (value instanceof Long) {
            return criteriaBuilder.lessThanOrEqualTo(path.as(Long.class), (Long) value);
        } else if (value instanceof BigDecimal) {
            return criteriaBuilder
                    .lessThanOrEqualTo(path.as(BigDecimal.class), (BigDecimal) value);
        } else if (value instanceof String) {
            return criteriaBuilder
                    .lessThanOrEqualTo(path.as(String.class), (String) value);
        }
        return null;
    }

    public Sort getSortOrder() {
        List<Order> sortOrders = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderList)) {
            for (Entry<String, Boolean> entry : orderList.entrySet()) {
                Order sortOrder = new Order(
                        Boolean.TRUE.equals(entry.getValue()) ? Sort.Direction.ASC
                                : Sort.Direction.DESC,
                        entry.getKey());
                sortOrders.add(sortOrder);
            }
        }
        return Sort.by(sortOrders);
    }

    public static Pageable getPageable(PageSortInfo pageSortInfo) {
        Sort sort = getOrders(pageSortInfo.getOrderInfos());
        return PageRequest
                .of(pageSortInfo.getPageNum() - 1, pageSortInfo.getPageSize(), sort);
    }

    public static Sort getOrders(List<OrderInfo> orderInfos) {
        List<Order> sortOrders = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderInfos)) {
            for (OrderInfo order : orderInfos) {
                String column = order.getColumn();
                Order sortOrder = new Order(
                        order.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC,
                        column);
                sortOrders.add(sortOrder);
            }
        }
        return Sort.by(sortOrders);
    }

    public List<T> select() {
        Specification<T> specification = build();
        return SpringUtil.getBean(specificationExecutorClass)
                .findAll(specification, getSortOrder());
    }

    public Page<T> select(Pageable pageable) {
        Specification<T> specification = build();
        return SpringUtil.getBean(specificationExecutorClass).findAll(specification, pageable);
    }

}
