package org.swiftboot.demo.request;

import org.swiftboot.demo.constant.FooBarType;

/**
 * Testing enum in request object.
 * @since 3.1.5
 */
public class EnumRequest {

    private FooBarType fooBarType;

    public FooBarType getFooBarType() {
        return fooBarType;
    }

    public void setFooBarType(FooBarType fooBarType) {
        this.fooBarType = fooBarType;
    }
}
