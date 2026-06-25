package org.swiftboot.util;

import java.util.Map;

/**
 * @author swiftech
 */
class SysUtilsTest {

    public static void main(String[] args) {

        Map<String, Object> props = SysUtils.getSysProps("java");
        SysUtils.printSysProps(props);

        props = SysUtils.getSysProps(e -> e.getKey().toString().contains("vm"));
        SysUtils.printSysProps(props);
    }
}