package org.swiftboot.demo.result;

import org.swiftboot.web.result.Result;

/**
 * @author allen
 */
public class SomeResult implements Result {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
