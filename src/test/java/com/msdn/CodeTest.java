package com.msdn;

import cn.hutool.core.date.DateUtil;
import com.msdn.generator.utils.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * @author hresh
 * @date 2021/4/17 11:25
 * @description
 */

public class CodeTest {
    @Test
    public void test(){
        String value = "contract_detail";
        System.out.println(underscoreToCamel(value));
        System.out.println(StringUtils.firstLetterUpperCase(underscoreToCamel((value))));

        System.out.println(DateUtil.now());
    }

    public String underscoreToCamel(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '_') {
                i++;
                c = str.charAt(i);
                sb.append(Character.toUpperCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
