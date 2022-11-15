package com.msdn.generator.common.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2021/5/3 10:00
 * @description 解析分页数据，Page和PageInfo是针对Mybatis查询到的分页数据进行处理；
 * <p> org.springframework.data.domain.Page是针对Jpa查询的分页数据进行处理
 * Ipage是针对Mybatis Plus查询的分页数据进行处理，实际上我们解析MbpPage
 */
@Getter
@Setter
public class PageResult<T> {

  /**
   * 总条数
   */
  private Long total;
  /**
   * 总页数
   */
  private Integer pageCount;
  /**
   * 每页数量
   */
  private Integer pageSize;
  /**
   * 当前页码
   */
  private Integer pageNum;

  /**
   * 分页数据
   */
  private List<T> data;

  /**
   * 处理Mybatis分页结果
   */
  public static <T> PageResult<T> ok(Page<T> page) {
    PageResult<T> result = new PageResult<T>();
    result.setPageCount(page.getPages());
    result.setPageNum(page.getPageNum());
    result.setPageSize(page.getPageSize());
    result.setTotal(page.getTotal());
    result.setData(page.getResult());
    return result;
  }

  /**
   * 处理Mybatis分页结果
   */
  public static <T> PageResult<T> ok(PageInfo<T> pageInfo) {
    PageResult<T> result = new PageResult<T>();
    result.setPageCount(pageInfo.getPages());
    result.setPageNum(pageInfo.getPageNum());
    result.setPageSize(pageInfo.getSize());
    result.setTotal(pageInfo.getTotal());
    result.setData(pageInfo.getList());
    return result;
  }

  /**
   * 处理Mybatis Plus分页结果
   */
  public static <T> PageResult<T> ok(IPage<T> iPage) {
    PageResult<T> result = new PageResult<T>();
    result.setPageCount((int) iPage.getPages());
    result.setPageNum((int) iPage.getCurrent());
    result.setPageSize((int) iPage.getSize());
    result.setTotal(iPage.getTotal());
    result.setData(iPage.getRecords());
    return result;
  }

  /**
   * 处理Jpa分页结果，Jpa页码默认从0开始，所以返回结果加1
   */
  public static <T> PageResult<T> ok(org.springframework.data.domain.Page<T> page) {
    PageResult<T> result = new PageResult<T>();
    result.setPageCount(page.getTotalPages());
    result.setPageNum(page.getNumber() + 1);
    result.setPageSize(page.getSize());
    result.setTotal(page.getTotalElements());
    result.setData(page.getContent());
    return result;
  }
}
