package org.swiftboot.demo.request;

import org.swiftboot.demo.model.entity.AdminUserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.web.request.BasePopulateCommand;

/**
 * Admin user Signout command
 *
 * @author swiftech
 **/
@Schema
public class AdminUserSignoutCommand extends BasePopulateCommand<AdminUserEntity> {


}
