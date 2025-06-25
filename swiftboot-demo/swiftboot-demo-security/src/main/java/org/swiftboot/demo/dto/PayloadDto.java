package org.swiftboot.demo.dto;

import org.swiftboot.web.dto.Dto;

/**
 * @author swiftech
 */
public class PayloadDto implements Dto {

    private String payload;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
