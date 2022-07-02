package com.erahub.common.utils;

import java.util.regex.Pattern;

/**
 * @Author lipeng
 * @Date 2022/4/25 19:05
 * @Version 1.0
 **/
public class RegexUtils {
    /**
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[+]?(0|([1-9]\\d*))?$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为零开头整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isStringInteger(String str) {
        Pattern pattern = Pattern.compile("^[+]?(0|([0-9]\\d*))?$");
        return pattern.matcher(str).matches();
    }
}
