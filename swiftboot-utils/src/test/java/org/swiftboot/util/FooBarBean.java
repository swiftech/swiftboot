package org.swiftboot.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Allen 2019-04-01
 **/
class FooBarBean {

    private String field1 = "field1";

    @JsonIgnore
    private String field2 = "field2";
}
