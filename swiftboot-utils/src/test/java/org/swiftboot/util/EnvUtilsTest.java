package org.swiftboot.util;

import static org.swiftboot.util.EnvUtils.displaySystemPropsInStdout;

public class EnvUtilsTest {


    public static void main(String[] args) {
//        printSystemProperties();

        displaySystemPropsInStdout("", (propEntry, str) -> propEntry.getKey().toString().contains(str));
    }

}
