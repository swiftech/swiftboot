package org.swiftboot.demo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.swiftboot.demo.config.PermissionConfigBean;
import org.swiftboot.demo.model.entity.AdminUserPermissionView;
import org.swiftboot.demo.service.AdminPermissionService;
import org.swiftboot.demo.shiro.AdminUserPermissionDao;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author swiftech
 */
@Service
public class AdminPermissionServiceImpl implements AdminPermissionService {
    Logger log = LoggerFactory.getLogger(AdminPermissionServiceImpl.class);

    @Resource
    private AdminUserPermissionDao userPermissionDao;

    @Override
    public Set<PermissionConfigBean> queryAllPermissionForUser(String loginName) {
        Set<PermissionConfigBean> ret = new HashSet<>();
        List<? extends AdminUserPermissionView> allUserPermEntities =
                userPermissionDao.findPermissionsByLoginName(loginName);
        log.info(String.format("User %s has %d permissions found", loginName, allUserPermEntities.size()));
        for (AdminUserPermissionView userPermEntity : allUserPermEntities) {
            PermissionConfigBean permBean = new PermissionConfigBean(userPermEntity.getPermissionCode(), userPermEntity.getPermDesc());
            // 排除子权限（没有必要）
            if (ret.isEmpty()) {
                ret.add(permBean);
            }
            else {
                for (PermissionConfigBean permissionConfigBean : ret) {
                    if (!permissionConfigBean.isSubPermission(permBean)) {
                        ret.add(permBean);
                    }
                    else {
                        log.debug(String.format("  No need: %s", permBean.getCode()));
                    }
                }
            }
        }
        return ret;
    }
}
