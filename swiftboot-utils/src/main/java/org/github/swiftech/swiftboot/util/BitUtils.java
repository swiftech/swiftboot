package org.github.swiftech.swiftboot.util;

import java.util.Collection;

/**
 * @author swiftech 2015-06-05
 **/
public class BitUtils {

    /**
     * 判断一个数字的二进制位是否和另外一个数字的二进制位重叠
     * @param target
     * @param body
     * @param <T>
     * @return
     */
    public static <T extends Number> boolean contains(T target, T body) {
        return (target.longValue() & body.longValue()) > 0;
    }

    /**
     * 对列表中的所有长整数做逻辑或操作，相当于合并所有二进制位
     *
     * @param longs
     * @return 返回操作结果
     */
    public static long bitwiseOr(Collection<Long> longs) {
        long result = 0;
        if (longs == null)
            return result;
        for (Long lon : longs) {
            if (lon != null) {
                result = result | lon;
            }
        }
        return result;
    }
}
