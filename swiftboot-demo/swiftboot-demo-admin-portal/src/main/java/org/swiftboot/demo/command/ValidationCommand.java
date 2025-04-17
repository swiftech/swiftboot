package org.swiftboot.demo.command;

import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.web.validate.constraint.PhoneNo;

/**
 * @author swiftech
 */
@Schema
public class ValidationCommand {

    @PhoneNo(prefix = "138")
    private String phoneNo;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
