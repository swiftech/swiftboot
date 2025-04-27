package org.swiftboot.demo.dto;

import org.swiftboot.web.dto.Dto;

/**
 * @author swiftech
 */
public class PayloadDto implements Dto {

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
