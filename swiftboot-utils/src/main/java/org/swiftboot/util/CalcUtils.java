package org.swiftboot.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utils for calculating.
 *
 */
public class CalcUtils {
    /**
     * Limit a integer value between min and max.
     *
     * @param v
     * @param min
     * @param max
     * @return
     */
    public static int limitIn(int v, int min, int max) {
        return Math.max(Math.min(v, max), min);
    }

    /**
     * Limit a long value between min and max.
     *
     * @param v
     * @param min
     * @param max
     * @return
     */
    public static long limitIn(long v, long min, long max) {
        return Math.max(Math.min(v, max), min);
    }

    /**
     * Limit a float value between min and max.
     *
     * @param v
     * @param min
     * @param max
     * @return
     */
    public static float limitIn(float v, float min, float max) {
        return Math.max(Math.min(v, max), min);
    }

    /**
     * Limit a double value between min and max.
     *
     * @param v
     * @param min
     * @param max
     * @return
     */
    public static double limitIn(double v, double min, double max) {
        return Math.max(Math.min(v, max), min);
    }

    /**
     * Limit a float value between 0 and 1,
     * @param v
     * @return
     */
    public static float limitInZeroToOne(float v){
        return limitIn(v, 0f, 1f);
    }

    /**
     * Limit a double value between 0 and 1,
     * @param v
     * @return
     */
    public static double limitInZeroToOne(double v){
        return limitIn(v, 0f, 1f);
    }

    /**
     * Check whether 2 decimals are equals after rounding down by {@code scale}.
     *
     * @param a
     * @param b
     * @param scale
     * @return
     */
    public static boolean equalsIgnoreScale(double a, double b, int scale) {
        return BigDecimal.valueOf(a).setScale(scale, RoundingMode.DOWN)
                .equals(BigDecimal.valueOf(b).setScale(scale, RoundingMode.DOWN));
    }

    /**
     * Check whether 2 decimals are equals after rounding down by {@code scale}.
     *
     * @param a
     * @param b
     * @param scale
     * @return
     */
    public static boolean equalsIgnoreScale(float a, float b, int scale) {
        return BigDecimal.valueOf(a).setScale(scale, RoundingMode.DOWN)
                .equals(BigDecimal.valueOf(b).setScale(scale, RoundingMode.DOWN));
    }

    /**
     * Check whether 2 decimals are equals after rounding down by {@code scale}.
     *
     * @param a
     * @param b
     * @param scale
     * @return
     */
    public static boolean equalsIgnoreScale(BigDecimal a, BigDecimal b, int scale) {
        return a.setScale(scale, RoundingMode.DOWN)
                .equals(b.setScale(scale, RoundingMode.DOWN));
    }
}
