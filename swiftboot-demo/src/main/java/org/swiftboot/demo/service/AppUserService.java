package org.swiftboot.demo.service;

import org.springframework.transaction.annotation.Transactional;
import org.swiftboot.demo.command.AppUserSigninCommand;
import org.swiftboot.demo.result.AppUserSigninResult;

/**
 * 管理员服务接口
 *
 * @author swiftech 2020-02-05
 **/
@Transactional
public interface AppUserService {

    /**
     * App user signin
     *
     * @param command
     * @return
     */
    AppUserSigninResult appUserSignin(AppUserSigninCommand command);


}