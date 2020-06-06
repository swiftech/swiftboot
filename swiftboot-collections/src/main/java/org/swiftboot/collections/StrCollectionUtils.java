package org.swiftboot.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * 字符串集合的工具类
 *
 * @author swiftech
 * @since 1.1.1
 */
public class StrCollectionUtils {

    /**
     * 集合中最长的字符串，有多个的情况取第一个
     *
     * @param strList
     * @return
     */
    public static String longestStr(Collection<String> strList) {
        return Collections.max(strList, Comparator.comparing(String::length));
    }

    /**
     * 集合中最短的字符串，有多个的情况取第一个
     *
     * @param strList
     * @return
     */
    public static String shortestStr(Collection<String> strList) {
        return Collections.min(strList, Comparator.comparing(String::length));
    }

    /**
     * 集合中最长的字符串长度
     *
     * @param strList
     * @return
     */
    public static int maxStrLength(Collection<String> strList) {
        int ret = 0;
        for (String s : strList) {
            if (s.length() > ret) {
                ret = s.length();
            }
        }
        return ret;
    }

    /**
     * 集合中最短的字符串长度
     *
     * @param strList
     * @return
     */
    public static int minStrLength(Collection<String> strList) {
        int ret = Integer.MAX_VALUE;
        for (String s : strList) {
            if (s.length() < ret) {
                ret = s.length();
            }
        }
        return ret;
    }
}
