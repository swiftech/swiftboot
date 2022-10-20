package org.swiftboot.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author swiftech 2019-04-01
 **/
class FooBarBean {

    private final String field1 = "field1";

    @JsonIgnore
    private final String field2 = "field2";
}
