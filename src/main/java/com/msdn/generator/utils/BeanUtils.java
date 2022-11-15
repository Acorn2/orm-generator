package com.msdn.generator.utils;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2021/4/17 17:18
 * @description Bean相关工具类，在Spring项目中使用，则继承org.springframework.beans.BeanUtils
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

  public static void copyPropertiesIgnoreNull(Object source, Object target) throws BeansException {
    copyProperties(source, target, getNullPropertyNames(source));
  }

  public static <D> D copyProperties(Object source, Class<D> clazz) throws RuntimeException {
    Object object;
    try {
      object = clazz.newInstance();
    } catch (Exception var4) {
      throw new RuntimeException(var4);
    }

    copyProperties(source, object);
    return (D) object;
  }

  public static <S, D> List<D> copyProperties(List<S> source, Class<D> clazz) {
    try {
      List<D> destList = new ArrayList();
      Iterator value = source.iterator();

      while (value.hasNext()) {
        S s = (S) value.next();
        D d = clazz.newInstance();
        copyProperties(s, d);
        destList.add(d);
      }
      return destList;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }

  /**
   * 获取null值的字段
   *
   * @param source 源对象
   * @return 数组
   */
  private static String[] getNullPropertyNames(Object source) {
    BeanWrapper src = new BeanWrapperImpl(source);
    PropertyDescriptor[] pds = src.getPropertyDescriptors();
    Set emptyNames = new HashSet();

    for (int i = 0; i < pds.length; ++i) {
      PropertyDescriptor pd = pds[i];
      Object srcValue = src.getPropertyValue(pd.getName());
      if (srcValue == null) {
        emptyNames.add(pd.getName());
      }
    }

    String[] result = new String[emptyNames.size()];
    return (String[]) emptyNames.toArray(result);
  }

  public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
    return BeanUtil.mapToBean(map, clazz, false, null);
  }

  /**
   * null值设置默认值
   *
   * @param obj 对象
   */
  public static void setFieldValueIfNull(Object obj) {
    for (Field field : obj.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      try {
        if (null == field.get(obj)) {
          switch (field.getGenericType().toString()) {
            case "class java.lang.String":
              field.set(obj, "");
              break;
            case "class java.lang.Integer":
              field.set(obj, 0);
              break;
            case "class java.lang.Double":
              field.set(obj, 0.0);
              break;
            case "class java.lang.Long":
              field.set(obj, 0L);
              break;
            case "class java.lang.BigDecimal":
              field.set(obj, BigDecimal.ZERO);
              break;
            default:
              break;
          }
        }
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }
}
