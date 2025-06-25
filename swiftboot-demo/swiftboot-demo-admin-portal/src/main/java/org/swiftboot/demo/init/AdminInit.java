package org.swiftboot.demo.init;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.swiftboot.demo.config.PermissionConfigBean;
import org.swiftboot.demo.config.RoleConfigBean;
import org.swiftboot.demo.config.SwiftbootDemoConfigBean;
import org.swiftboot.demo.config.UserConfigBean;
import org.swiftboot.demo.constant.AuthConstants;
import org.swiftboot.demo.model.*;
import org.swiftboot.demo.repository.*;
import org.swiftboot.util.PasswordUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 初始化管理用户数据, 例如角色, 权限等
 * 依赖于 Swiftboot-Shiro 模块并且必须启用
 *
 * @author swiftech
 */
@Component
@ConditionalOnProperty(value = "swiftboot.shiro.enabled", havingValue = "true")
public class AdminInit {

    private static final Logger log = LoggerFactory.getLogger(AdminInit.class);

    public static final String PERMISSION_CODE_SEPARATOR = ":";

    @Resource
    private SwiftbootDemoConfigBean demoConfig;

    @Resource
    private AdminUserRepository adminUserRepository;

    @Resource
    private AdminRoleRepository adminRoleRepository;

    @Resource
    private AdminPermissionRepository adminPermissionRepository;

    @Resource
    private AdminUserRoleRelRepository adminUserRoleRelRepository;

    @Resource
    private AdminRolePermissionRelRepository adminRolePermissionRelRepository;

//    @Resource
//    private PasswordManager passwordManager;

    @Resource
    protected PlatformTransactionManager txManager;

    private void createPermissionAndSubPermissions(AdminPermissionEntity parent, PermissionConfigBean permConfig) {
        if (parent == null) {
            throw new RuntimeException(String.format("%s 必须有父权限", permConfig.getCode()));
        }
        if (StringUtils.isAnyBlank(permConfig.getCode(), permConfig.getDesc())) {
            throw new RuntimeException(String.format("权限 %s 配置缺少", permConfig.getCode()));
        }
        // 所有code都以 ':*' 结尾
        permConfig.setCode(PermissionCodeUtils.standardPermCode(permConfig.getCode()));

        Optional<AdminPermissionEntity> exist = adminPermissionRepository.findByPermCode(permConfig.getCode());
        AdminPermissionEntity permissionEntity;
        if (exist.isEmpty()) {
            permissionEntity = new AdminPermissionEntity();
        }
        else {
            permissionEntity = exist.get();
            permissionEntity.setUpdateTime(LocalDateTime.now());
        }

        log.debug("新建或者更新权限：" + permConfig.getCode());
        permissionEntity.setPermCode(permConfig.getCode());
        permissionEntity.setPermDesc(permConfig.getDesc());
        permissionEntity.setParentPermission(parent);
        adminPermissionRepository.save(permissionEntity);
        // 子权限处理
        if (permConfig.getPermissions() != null && !permConfig.getPermissions().isEmpty()) {
            for (PermissionConfigBean subPermConfig : permConfig.getPermissions()) {
                this.createPermissionAndSubPermissions(permissionEntity, subPermConfig);
            }
        }

        // 添加到父权限中
        if (parent.getSubPermissions() == null) {
            Set<AdminPermissionEntity> subPerms = new HashSet<>();
            subPerms.add(permissionEntity);
            parent.setSubPermissions(subPerms);
            adminPermissionRepository.save(parent);
        }
    }

    @PostConstruct
    public void init() {
        log.info("初始化用户权限角色(RBAC)数据");

        TransactionTemplate transactionTemplate = new TransactionTemplate(txManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                doInit();
            }
        });
    }

    private void doInit() {

        // 权限（至少有一个所有权限）
        Optional<AdminPermissionEntity> optRootPerm = adminPermissionRepository.findByPermCode("*");
        AdminPermissionEntity rootPermission;
        if (optRootPerm.isPresent()) {
            rootPermission = optRootPerm.get();
        }
        else {
            rootPermission = new AdminPermissionEntity();
            rootPermission.setPermCode("*");
            rootPermission.setPermDesc("所有权限");
            adminPermissionRepository.save(rootPermission);
        }
        Set<PermissionConfigBean> permissions = demoConfig.getInit().getPermissions();
        if (permissions != null) {
            log.info("权限：" + permissions.size());
            for (PermissionConfigBean permission : permissions) {
                this.createPermissionAndSubPermissions(rootPermission, permission);
            }
        }


//        Set<PermissionConfigBean> permissions = demoConfig.getInit().getPermissions();
//        if (permissions != null) {
//            log.info("权限：" + permissions.size());
//            for (PermissionConfigBean permission : permissions) {
//                AdminPermissionEntity exist = adminPermissionDao.findByPermCode(permission.getCode());
//                if (exist == null) {
//                    log.info("创建权限：" + permission.getCode());
//                    AdminPermissionEntity permissionEntity = new AdminPermissionEntity();
//                    permissionEntity.setPermCode(permission.getCode());
//                    permissionEntity.setPermDesc(permission.getDesc());
//                    adminPermissionDao.save(permissionEntity);
//                }
//            }
//        }

        // 角色
        Set<RoleConfigBean> roles = demoConfig.getInit().getRoles();
        if (roles != null) {
            log.info("角色：" + roles.size());
            for (RoleConfigBean role : roles) {
                Optional<AdminRoleEntity> exist = adminRoleRepository.findByRoleName(role.getName());
                if (exist.isEmpty()) {
                    log.info("  创建角色：" + role.getName());
                    AdminRoleEntity roleEntity = new AdminRoleEntity();
                    roleEntity.setRoleName(role.getName());
                    roleEntity.setRoleDesc(role.getDesc());
                    adminRoleRepository.save(roleEntity);
                }
            }
        }

        // 用户
        Set<UserConfigBean> users = demoConfig.getInit().getUsers();
        if (users != null) {
            log.info("用户：%d".formatted(users.size()));
            for (UserConfigBean user : users) {
                Optional<AdminUserEntity> exist = adminUserRepository.findByLoginName(user.getLoginName());
                if (exist.isEmpty()) {
                    log.info("  创建用户：%s".formatted(user.getLoginName()));
                    AdminUserEntity userEntity = new AdminUserEntity();
                    userEntity.setLoginName(user.getLoginName());
                    userEntity.setLoginPwd(PasswordUtils.createPassword(user.getLoginPwd(), AuthConstants.MY_AUTH_SERVICE_NAME));
                    adminUserRepository.save(userEntity);
                }
            }
        }

        // 权限和角色关系
        Map<String, List<String>> rolePermsRels = demoConfig.getInit().getRolePermRels();
        if (rolePermsRels != null) {
            log.info(String.format("%d 个角色需要初始化权限关联", rolePermsRels.size()));
            for (String roleName : rolePermsRels.keySet()) {
                List<String> permCodeList = rolePermsRels.get(roleName);
                Optional<AdminRoleEntity> optAdminRole = adminRoleRepository.findByRoleName(roleName);
                // 排除不存在的角色
                if (optAdminRole.isPresent()) {
                    for (String permCode : permCodeList) {
                        permCode = PermissionCodeUtils.standardPermCode(permCode);

                        Optional<AdminPermissionEntity> optAdminPerm = adminPermissionRepository.findByPermCode(permCode);
                        // 排除不存在的权限
                        if (optAdminPerm.isPresent()) {
                            Optional<AdminRolePermissionRelEntity> exist = adminRolePermissionRelRepository.findByAdminRoleAndAdminPermission(
                                    optAdminRole.get(), optAdminPerm.get());
                            if (!exist.isPresent()) {
                                log.info(String.format("  创建角色权限关联：%s - %s", roleName, permCode));
                                AdminRolePermissionRelEntity rolePermRelEntity = new AdminRolePermissionRelEntity();
                                rolePermRelEntity.setAdminRole(optAdminRole.get());
                                rolePermRelEntity.setAdminPermission(optAdminPerm.get());
                                adminRolePermissionRelRepository.save(rolePermRelEntity);
                            }
                        }
                        else {
                            log.warn(String.format("  权限 %s 不存在", permCode));
                        }
                    }
                }
                else {
                    log.warn(String.format("  角色 %s 不存在", roleName));
                }
            }
        }

        // 用户和角色关系
        Map<String, List<String>> roleUserRels = demoConfig.getInit().getRoleUserRels();
        if (roleUserRels != null) {
            log.info(String.format("%d 个用户需要初始化角色关联", roleUserRels.size()));
            for (String roleName : roleUserRels.keySet()) {
                List<String> userLoginNameList = roleUserRels.get(roleName);
                Optional<AdminRoleEntity> optAdminRole = adminRoleRepository.findByRoleName(roleName);
                if (userLoginNameList != null) {
                    for (String userLoginName : userLoginNameList) {
                        Optional<AdminUserEntity> optAdminUser = adminUserRepository.findByIsDeleteFalseAndLoginName(userLoginName);
                        if (optAdminUser.isPresent()) {
                            Optional<AdminUserRoleRelEntity> exist = adminUserRoleRelRepository.findByAdminRoleAndAdminUser(optAdminRole.get(), optAdminUser.get());
                            if (!exist.isPresent()) {
                                log.info(String.format("  创建用户角色关联：%s - %s", userLoginName, roleName));
                                AdminUserRoleRelEntity userRoleRelEntity = new AdminUserRoleRelEntity();
                                userRoleRelEntity.setAdminRole(optAdminRole.get());
                                userRoleRelEntity.setAdminUser(optAdminUser.get());
                                adminUserRoleRelRepository.save(userRoleRelEntity);
                            }
                        }
                    }
                }
            }
        }

    }
}
