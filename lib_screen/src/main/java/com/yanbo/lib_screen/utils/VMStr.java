package com.yanbo.lib_screen.utils;

import com.yanbo.lib_screen.VApplication;

import java.util.Arrays;
import java.util.List;

/**
 * 描述：
 *
 * @author Yanbo
 * @date 2018/11/6
 */
public class VMStr {

    /**
     * 字符串转数组
     *
     * @param string 字符串
     * @param separator 分隔符
     * @return 数组
     */
    public static String[] strToArray(String string, String separator) {
        return string.split(separator);
    }

    /**
     * 字符串数组转字符串
     *
     * @param array 字符串数组
     * @param separator 分隔符
     * @return 字符串
     */
    public static String arrayToStr(String[] array, String separator) {
        if (array == null || array.length == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(array[i]);
        }
        return sb.toString();
    }

    /**
     * 集合转字符串拼接
     *
     * @param list 集合
     * @param splitStr 分隔符
     */
    public static String list2Str(List<String> list, String splitStr) {
        if (list == null || list.size() == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    /**
     * 字符串数组转集合
     *
     * @param array 字符串数组
     * @return 集合
     */
    public static List<String> arrayToList(String[] array) {
        return Arrays.asList(array);
    }

    /**
     * 字符串集合转数组
     *
     * @param list 字符串集合
     * @return 数组
     */
    public static String[] listToArray(List<String> list) {
        return list.toArray(new String[list.size()]);
    }

    /**
     * 根据资源 id 获取字符串
     */
    public static String strByResId(int resId) {
        return VApplication.getContext().getString(resId);
    }

    /**
     * 检测字符串是否为空白字符串
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }
}
