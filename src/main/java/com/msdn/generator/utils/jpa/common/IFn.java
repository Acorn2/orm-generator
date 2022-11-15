package com.msdn.generator.utils.jpa.common;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2022/9/9 9:27 下午
 * @description
 */
@FunctionalInterface
public interface IFn<T, R> extends Function<T, R>, Serializable {

  R apply(T source);
}
