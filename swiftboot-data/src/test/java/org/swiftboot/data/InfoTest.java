package org.swiftboot.data;

import org.junit.jupiter.api.Test;
import org.swiftboot.data.model.aspect.EntityIdAspect;

/**
 * @author swiftech
 */
public class InfoTest {

    @Test
    public void test() {
        System.out.println(Info.get(EntityIdAspect.class, R.PARAM_NOT_IMPLEMENT_INTERFACE2, "param", "Testable"));
        System.out.println();
    }
}
