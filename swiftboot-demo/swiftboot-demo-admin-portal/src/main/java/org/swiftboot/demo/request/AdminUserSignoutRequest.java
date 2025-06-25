package org.swiftboot.demo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.demo.model.AdminUserEntity;
import org.swiftboot.web.request.BasePopulateRequest;

/**
 * Admin user Signout command
 *
 * @author swiftech
 **/
@Schema
public class AdminUserSignoutRequest extends BasePopulateRequest<AdminUserEntity> {


}
