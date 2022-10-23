package org.swiftboot.demo.service;

import org.swiftboot.demo.config.PermissionConfigBean;

import java.util.Set;

/**
 *
 * @author swiftech
 */
public interface AdminPermissionService {

    /**
     *
     * @param loginName
     * @return
     */
    Set<PermissionConfigBean> queryAllPermissionForUser(String loginName);
}
