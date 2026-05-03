package org.swiftboot.util;

/**
 * Utility class for comparing software version numbers.
 *
 * <p>Supports common version formats, for example:</p>
 * <ul>
 *   <li>1.0.0</li>
 *   <li>2.1.3-SNAPSHOT</li>
 *   <li>3.0.0-alpha.1</li>
 *   <li>v1.2.3</li>
 * </ul>
 *
 * @since 3.1.3
 */
public class VersionUtils {

    /**
     * Compare two version numbers.
     *
     * @param v1 version number 1
     * @param v2 version number 2
     * @return negative if v1 < v2, zero if v1 == v2, positive if v1 > v2
     */
    public static int compare(String v1, String v2) {
        if (v1 == null && v2 == null) {
            return 0;
        }
        if (v1 == null) {
            return -1;
        }
        if (v2 == null) {
            return 1;
        }

        v1 = normalize(v1);
        v2 = normalize(v2);

        String[] parts1 = splitVersion(v1);
        String[] parts2 = splitVersion(v2);

        String[] numeric1 = parts1[0].split("\\.");
        String[] numeric2 = parts2[0].split("\\.");

        int maxLength = Math.max(numeric1.length, numeric2.length);
        for (int i = 0; i < maxLength; i++) {
            String s1 = i < numeric1.length ? numeric1[i] : "0";
            String s2 = i < numeric2.length ? numeric2[i] : "0";

            int cmp = compareNumericPart(s1, s2);
            if (cmp != 0) {
                return cmp;
            }
        }

        String pre1 = parts1.length > 1 ? parts1[1] : null;
        String pre2 = parts2.length > 1 ? parts2[1] : null;

        if (pre1 == null && pre2 == null) {
            return 0;
        }
        if (pre1 == null) {
            return 1;
        }
        if (pre2 == null) {
            return -1;
        }

        return comparePreRelease(pre1, pre2);
    }

    /**
     * Check if v1 is greater than v2.
     */
    public static boolean greaterThan(String v1, String v2) {
        return compare(v1, v2) > 0;
    }

    /**
     * Check if v1 is greater than or equal to v2.
     */
    public static boolean greaterThanOrEqual(String v1, String v2) {
        return compare(v1, v2) >= 0;
    }

    /**
     * Check if v1 is less than v2.
     */
    public static boolean lessThan(String v1, String v2) {
        return compare(v1, v2) < 0;
    }

    /**
     * Check if v1 is less than or equal to v2.
     */
    public static boolean lessThanOrEqual(String v1, String v2) {
        return compare(v1, v2) <= 0;
    }

    /**
     * Check if two version numbers are equal.
     */
    public static boolean equals(String v1, String v2) {
        return compare(v1, v2) == 0;
    }

    private static String normalize(String version) {
        if (version.startsWith("v") || version.startsWith("V")) {
            return version.substring(1);
        }
        return version;
    }

    private static String[] splitVersion(String version) {
        int idx = version.indexOf('+');
        if (idx != -1) {
            version = version.substring(0, idx);
        }

        idx = version.indexOf('-');
        if (idx != -1) {
            return new String[]{version.substring(0, idx), version.substring(idx + 1)};
        }

        // Handle dot-separated pre-release identifiers like 1.0.0.SNAPSHOT or 1.0.0.alpha
        int lastDot = version.lastIndexOf('.');
        if (lastDot != -1) {
            String lastPart = version.substring(lastDot + 1);
            if (!isNumeric(lastPart)) {
                int preStart = version.lastIndexOf('.', lastDot - 1);
                if (preStart != -1) {
                    String prePart = version.substring(preStart + 1);
                    if (!isNumeric(prePart.substring(0, prePart.indexOf('.')))) {
                        return new String[]{version.substring(0, preStart), prePart};
                    }
                }
                return new String[]{version.substring(0, lastDot), lastPart};
            }
        }

        return new String[]{version};
    }

    private static boolean isNumeric(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return !s.isEmpty();
    }

    private static int compareNumericPart(String s1, String s2) {
        boolean n1 = isNumeric(s1);
        boolean n2 = isNumeric(s2);

        if (n1 && n2) {
            return Integer.compare(Integer.parseInt(s1), Integer.parseInt(s2));
        }
        if (n1) {
            return 1;
        }
        if (n2) {
            return -1;
        }
        return s1.compareTo(s2);
    }

    private static int comparePreRelease(String pre1, String pre2) {
        String[] parts1 = pre1.split("\\.");
        String[] parts2 = pre2.split("\\.");

        int maxLength = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < maxLength; i++) {
            String s1 = i < parts1.length ? parts1[i] : null;
            String s2 = i < parts2.length ? parts2[i] : null;

            if (s1 == null && s2 == null) {
                return 0;
            }
            if (s1 == null) {
                return -1;
            }
            if (s2 == null) {
                return 1;
            }

            int cmp = compareNumericPart(s1, s2);
            if (cmp != 0) {
                return cmp;
            }
        }

        return 0;
    }
}
