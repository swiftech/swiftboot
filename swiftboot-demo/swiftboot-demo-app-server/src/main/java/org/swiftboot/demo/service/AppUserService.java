package org.swiftboot.demo.service;

import org.springframework.transaction.annotation.Transactional;
import org.swiftboot.demo.dto.AppUserDto;

/**
 * 管理员服务接口
 *
 * @author swiftech 2020-02-05
 **/
@Transactional
public interface AppUserService {

    /**
     * Get app user information.
     *
     * @param uid
     * @return
     */
    AppUserDto getUserInfo(String uid);

}