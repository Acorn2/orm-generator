package com.msdn.generator.utils.jpa;

import com.msdn.generator.utils.jpa.common.IFn;
import java.beans.Introspector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2022/9/9 9:29 下午
 * @description
 */
@Slf4j
public class Reflections {

    private static final Pattern GET_PATTERN = Pattern.compile("^get[A-Z].*");
    private static final Pattern IS_PATTERN = Pattern.compile("^is[A-Z].*");

    private Reflections() {
    }

    public static String fnToFieldName(IFn fn) {
        try {
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(fn);
            String getter = serializedLambda.getImplMethodName();
            if (GET_PATTERN.matcher(getter).matches()) {
                getter = getter.substring(3);
            } else if (IS_PATTERN.matcher(getter).matches()) {
                getter = getter.substring(2);
            }

            return Introspector.decapitalize(getter);
        } catch (ReflectiveOperationException e) {
            log.warn(String.format("%s:%s",
                    Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage()), e);
        }
        return "";
    }
}
