package org.swiftboot.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 数字格式转换工具类
 * @author switech
 **/
public class NumberFormatUtils {

    /**
     * 格式化为常用的货币（保留两位小数，四舍五入）
     *
     * @param number
     * @return
     */
    public static String formatCurrency(Number number) {
        return new DecimalFormat("#0.00").format(number);
    }

    /**
     * 转换为百分比数值（保留两位小数，四舍五入）
     * @param number
     * @return
     */
    public static Double toPercent(Number number) {
        return new BigDecimal(number.doubleValue())
                .multiply(new BigDecimal(100))
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    /**
     * 转换为百分比数值（四舍五入）
     * @param number
     * @param scale 保留小数位数
     * @return
     */
    public static Double toPercent(Number number, int scale) {
        return new BigDecimal(number.doubleValue())
                .multiply(new BigDecimal(100))
                .setScale(scale, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }
}