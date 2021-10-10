package org.swiftboot.demo.command;

import io.swagger.annotations.ApiModel;
import org.swiftboot.web.validate.constraint.PhoneNo;

/**
 * @author allen
 */
@ApiModel
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
