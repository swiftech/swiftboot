package org.swiftboot.demo.command;

import org.swiftboot.demo.model.entity.AdminUserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.web.command.BasePopulateCommand;

/**
 * Admin user Signout command
 *
 * @author swiftech
 **/
@Schema
public class AdminUserSignoutCommand extends BasePopulateCommand<AdminUserEntity> {


}
