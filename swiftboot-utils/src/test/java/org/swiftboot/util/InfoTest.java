package org.swiftboot.util;

import org.junit.jupiter.api.Test;

public class InfoTest {

    @Test
    public void testJustWorking() {
        System.out.println();
        System.out.println("  # " + Info.get("org.swiftboot.util.BeanUtils.no_field_by_type2"));
    }


    public static void main(String[] args) {
        System.out.println(Info.get(IdUtils.class, org.swiftboot.util.R.ID_FAILED1));
//        Info.validateForAllLocale();
        System.out.println();
        System.out.println(Info.get(IdUtils.class, org.swiftboot.util.R.ID_FAILED1));
    }
}
