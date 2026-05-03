package org.swiftboot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link VersionUtils}.
 *
 * @author swiftech
 **/
public class VersionUtilsTest {

    @Test
    public void testCompareEqualVersions() {
        Assertions.assertEquals(0, VersionUtils.compare("1.0.0", "1.0.0"));
        Assertions.assertEquals(0, VersionUtils.compare("0.0.0", "0.0.0"));
        Assertions.assertEquals(0, VersionUtils.compare("10.20.30", "10.20.30"));
    }

    @Test
    public void testCompareDifferentMajor() {
        Assertions.assertTrue(VersionUtils.compare("2.0.0", "1.9.9") > 0);
        Assertions.assertTrue(VersionUtils.compare("1.9.9", "2.0.0") < 0);
    }

    @Test
    public void testCompareDifferentMinor() {
        Assertions.assertTrue(VersionUtils.compare("1.2.0", "1.1.9") > 0);
        Assertions.assertTrue(VersionUtils.compare("1.1.9", "1.2.0") < 0);
    }

    @Test
    public void testCompareDifferentPatch() {
        Assertions.assertTrue(VersionUtils.compare("1.0.2", "1.0.1") > 0);
        Assertions.assertTrue(VersionUtils.compare("1.0.1", "1.0.2") < 0);
    }

    @Test
    public void testCompareDifferentLengths() {
        // Missing segments are padded with 0, so 1.0 equals 1.0.0
        Assertions.assertEquals(0, VersionUtils.compare("1.0.0", "1.0"));
        Assertions.assertEquals(0, VersionUtils.compare("1.0", "1.0.0"));
        Assertions.assertEquals(0, VersionUtils.compare("2.1", "2.1.0"));
        // Different when there is a real numeric difference after padding
        Assertions.assertTrue(VersionUtils.compare("1.0.1", "1.0") > 0);
        Assertions.assertTrue(VersionUtils.compare("1.0", "1.0.1") < 0);
    }

    @Test
    public void testCompareWithVPrefix() {
        Assertions.assertEquals(0, VersionUtils.compare("v1.2.3", "1.2.3"));
        Assertions.assertEquals(0, VersionUtils.compare("V1.2.3", "1.2.3"));
        Assertions.assertTrue(VersionUtils.compare("v2.0.0", "v1.9.9") > 0);
    }

    @Test
    public void testCompareWithPreRelease() {
        // Pre-release versions are lower than release versions
        Assertions.assertTrue(VersionUtils.compare("1.0.0-alpha", "1.0.0") < 0);
        Assertions.assertTrue(VersionUtils.compare("1.0.0", "1.0.0-alpha") > 0);
        Assertions.assertTrue(VersionUtils.compare("1.0.0-SNAPSHOT", "1.0.0") < 0);
    }

    @Test
    public void testComparePreReleaseVersions() {
        Assertions.assertTrue(VersionUtils.compare("1.0.0-alpha", "1.0.0-beta") < 0);
        Assertions.assertTrue(VersionUtils.compare("1.0.0-beta", "1.0.0-alpha") > 0);
        Assertions.assertTrue(VersionUtils.compare("1.0.0-alpha.1", "1.0.0-alpha.2") < 0);
        Assertions.assertTrue(VersionUtils.compare("1.0.0-rc.1", "1.0.0-rc.2") < 0);
    }

    @Test
    public void testCompareWithBuildMetadata() {
        // Build metadata after '+' should be ignored in comparison
        Assertions.assertEquals(0, VersionUtils.compare("1.0.0+build123", "1.0.0+build456"));
        Assertions.assertTrue(VersionUtils.compare("1.0.1+build123", "1.0.0+build456") > 0);
    }

    @Test
    public void testCompareNullVersions() {
        Assertions.assertEquals(0, VersionUtils.compare(null, null));
        Assertions.assertTrue(VersionUtils.compare(null, "1.0.0") < 0);
        Assertions.assertTrue(VersionUtils.compare("1.0.0", null) > 0);
    }

    @Test
    public void testCompareDotSeparatedPreRelease() {
        Assertions.assertTrue(VersionUtils.compare("1.0.0.SNAPSHOT", "1.0.0") < 0);
        Assertions.assertTrue(VersionUtils.compare("1.0.0.alpha", "1.0.0.beta") < 0);
    }

    @Test
    public void testGreaterThan() {
        Assertions.assertTrue(VersionUtils.greaterThan("2.0.0", "1.0.0"));
        Assertions.assertFalse(VersionUtils.greaterThan("1.0.0", "1.0.0"));
        Assertions.assertFalse(VersionUtils.greaterThan("1.0.0", "2.0.0"));
    }

    @Test
    public void testGreaterThanOrEqual() {
        Assertions.assertTrue(VersionUtils.greaterThanOrEqual("2.0.0", "1.0.0"));
        Assertions.assertTrue(VersionUtils.greaterThanOrEqual("1.0.0", "1.0.0"));
        Assertions.assertFalse(VersionUtils.greaterThanOrEqual("1.0.0", "2.0.0"));
    }

    @Test
    public void testLessThan() {
        Assertions.assertTrue(VersionUtils.lessThan("1.0.0", "2.0.0"));
        Assertions.assertFalse(VersionUtils.lessThan("1.0.0", "1.0.0"));
        Assertions.assertFalse(VersionUtils.lessThan("2.0.0", "1.0.0"));
    }

    @Test
    public void testLessThanOrEqual() {
        Assertions.assertTrue(VersionUtils.lessThanOrEqual("1.0.0", "2.0.0"));
        Assertions.assertTrue(VersionUtils.lessThanOrEqual("1.0.0", "1.0.0"));
        Assertions.assertFalse(VersionUtils.lessThanOrEqual("2.0.0", "1.0.0"));
    }

    @Test
    public void testEquals() {
        Assertions.assertTrue(VersionUtils.equals("1.0.0", "1.0.0"));
        Assertions.assertTrue(VersionUtils.equals("v1.2.3", "1.2.3"));
        Assertions.assertFalse(VersionUtils.equals("1.0.0", "2.0.0"));
        Assertions.assertFalse(VersionUtils.equals("1.0.0", "1.0.0-alpha"));
    }

    @Test
    public void testComplexVersionComparisons() {
        Assertions.assertTrue(VersionUtils.compare("1.0.0-alpha+exp.sha.5114f85", "1.0.0-alpha") == 0);
        Assertions.assertTrue(VersionUtils.compare("1.0.0-alpha", "1.0.0-alpha.1") < 0);
        Assertions.assertTrue(VersionUtils.compare("1.0.0-alpha.1", "1.0.0-beta.2") < 0);
        Assertions.assertTrue(VersionUtils.compare("1.0.0-beta.2", "1.0.0-beta.11") < 0);
        Assertions.assertTrue(VersionUtils.compare("1.0.0-beta.11", "1.0.0-rc.1") < 0);
        Assertions.assertTrue(VersionUtils.compare("1.0.0-rc.1", "1.0.0") < 0);
    }
}
