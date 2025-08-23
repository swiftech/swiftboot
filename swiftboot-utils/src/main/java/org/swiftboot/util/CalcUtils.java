package org.swiftboot.util;

import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utils for calculating.
 *
 */
public class CalcUtils {

    /**
     * Limit an integer value between min and max.
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
     * Limit a BigDecimal value between min and max.
     *
     * @param v
     * @param min
     * @param max
     * @return
     */
    public static BigDecimal limitIn(BigDecimal v, BigDecimal min, BigDecimal max) {
        return max(min(v, max), min);
    }

    /**
     * Limit a float value between 0 and 1.
     *
     * @param v
     * @return
     */
    public static float limitInZeroToOne(float v) {
        return limitIn(v, 0f, 1f);
    }

    /**
     * Limit a double value between 0 and 1.
     *
     * @param v
     * @return
     */
    public static double limitInZeroToOne(double v) {
        return limitIn(v, 0f, 1f);
    }

    /**
     * Limit a BigDecimal value between 0 and 1.
     *
     * @param v
     * @return
     */
    public static BigDecimal limitInZeroToOne(BigDecimal v) {
        return limitIn(v, BigDecimal.ZERO, BigDecimal.ONE);
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

    /**
     * Add 2 {@link BigDecimal}s even if one of them is null.
     *
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        if (a == null && b == null) {
            return BigDecimal.ZERO;
        }
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return a.add(b);
    }

    /**
     * Subtract the {@link BigDecimal} b from the {@link BigDecimal} a even if a or b is null.
     *
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        if (a == null && b == null) {
            return BigDecimal.ZERO;
        }
        if (a == null) {
            return BigDecimal.ZERO.subtract(b);
        }
        if (b == null) {
            return a;
        }
        return a.subtract(b);
    }

    /**
     * Return maximum one between two {@link BigDecimal}s, if equals, return the a, if any is null, return null.
     *
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal max(BigDecimal a, BigDecimal b) {
        if (ObjectUtils.anyNull(a, b)) {
            return null;
        }
        if (a.compareTo(b) >= 0) {
            return a;
        }
        else {
            return b;
        }
    }

    /**
     * Return minimum one between two {@link BigDecimal}s, if equals, return the a, if any is null, return null.
     *
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal min(BigDecimal a, BigDecimal b) {
        if (ObjectUtils.anyNull(a, b)) {
            return null;
        }
        if (a.compareTo(b) > 0) {
            return b;
        }
        else {
            return a;
        }
    }
}
