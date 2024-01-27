package org.swiftboot.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author swiftech 2019-04-01
 **/
class FooBarBean extends BaseBean {

    private String field1 = "field 1";

    @JsonIgnore
    private String field2 = "field 2";

    // test primitive type
    private int field3 = 3;
}
