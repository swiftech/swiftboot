package org.swiftboot.web;

import org.junit.jupiter.api.Test;
import org.swiftboot.web.request.BasePopulateRequest;

/**
 * @author swiftech
 */
public class InfoTest {

    @Test
    public void test() {
        System.out.println(Info.get(BasePopulateRequest.class, R.REFLECT_TYPE_OF_ENTITY_FAIL));
        System.out.println();
    }
}
