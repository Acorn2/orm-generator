package com.msdn.generator.common.mybatis;

import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

@RegisterMapper
public interface ListMapper<T, PK> {

    /**
     * 批量插入，支持批量插入的数据库可以使用
     *
     * @param recordList
     * @return
     */
    @InsertProvider(type = ListProvider.class, method = "dynamicSQL")
    int insertList(List<? extends T> recordList);

    /**
     * 批量更新
     *
     * @return
     */
    @UpdateProvider(type = ListProvider.class, method = "dynamicSQL")
    int updateBatchByPrimaryKeySelective(List<? extends T> recordList);

    /**
     * 根据主键字符串进行查询，类中只有存在一个带有@Id注解的字段
     *
     * @param idList
     * @return
     */
    @SelectProvider(type = ListProvider.class, method = "dynamicSQL")
    List<T> selectByIdList(@Param("idList") List<PK> idList);

    /**
     * 根据主键字符串进行删除，类中只有存在一个带有@Id注解的字段
     *
     * @param idList
     * @return
     */
    @DeleteProvider(type = ListProvider.class, method = "dynamicSQL")
    int deleteByIdList(@Param("idList") List<PK> idList);
}
