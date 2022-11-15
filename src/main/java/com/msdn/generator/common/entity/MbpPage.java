package com.msdn.generator.common.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2021/5/2 20:55
 * @description Mybatis Plus分页封装接口
 */
public class MbpPage<T> implements IPage<T> {
    private List<T> records;
    private long total;
    private long pageSize;
    private long pageNum;
    private List<OrderItem> orders;
    private boolean optimizeCountSql;
    private boolean isSearchCount;
    private boolean hitCount;

    public MbpPage() {
        this.records = Collections.emptyList();
        this.total = 0L;
        this.pageSize = 10L;
        this.pageNum = 1L;
        this.orders = new ArrayList();
        this.optimizeCountSql = true;
        this.isSearchCount = true;
        this.hitCount = false;
    }

    public MbpPage(long page, long size) {
        this(page, size, 0L);
    }

    public MbpPage(long page, long size, long total) {
        this(page, size, total, true);
    }

    public MbpPage(long page, long size, boolean isSearchCount) {
        this(page, size, 0L, isSearchCount);
    }

    public MbpPage(long page, long size, long total, boolean isSearchCount) {
        this.records = Collections.emptyList();
        this.total = 0L;
        this.pageSize = 10L;
        this.pageNum = 1L;
        this.orders = new ArrayList();
        this.optimizeCountSql = true;
        this.isSearchCount = true;
        this.hitCount = false;
        if (page > 1L) {
            this.pageNum = page;
        }

        this.pageSize = size;
        this.total = total;
        this.isSearchCount = isSearchCount;
    }

    public boolean hasPrevious() {
        return this.pageNum > 1L;
    }

    public boolean hasNext() {
        return this.pageNum < this.getPages();
    }

    @Override
    public List<T> getRecords() {
        return this.records;
    }

    @Override
    public MbpPage<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    @Override
    public long getTotal() {
        return this.total;
    }

    @Override
    public MbpPage<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public long getSize() {
        return this.pageSize;
    }

    @Override
    public MbpPage<T> setSize(long size) {
        this.pageSize = size;
        return this;
    }

    @Override
    public long getCurrent() {
        return this.pageNum;
    }

    @Override
    public MbpPage<T> setCurrent(long current) {
        this.pageNum = current;
        return this;
    }

    @Override
    public List<OrderItem> orders() {
        return this.getOrders();
    }

    public List<OrderItem> getOrders() {
        return this.orders;
    }

    public void setOrders(List<OrderItem> orders) {
        this.orders = orders;
    }
}
