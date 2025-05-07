package org.swiftboot.demo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.web.validate.constraint.PhoneNo;

/**
 * @author swiftech
 */
@Schema
public class ValidationRequest {

    @PhoneNo(prefix = "138")
    private String phoneNo;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
