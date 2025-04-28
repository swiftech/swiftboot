package org.swiftboot.demo.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.swiftboot.auth.config.SwiftbootAuthConfigBean;
import org.swiftboot.auth.service.SessionService;
import org.swiftboot.demo.request.AdminUserCreateCommand;
import org.swiftboot.demo.request.AdminUserSaveCommand;
import org.swiftboot.demo.request.AdminUserSigninCommand;
import org.swiftboot.demo.request.AdminUserSignoutCommand;
import org.swiftboot.demo.model.dao.AdminUserDao;
import org.swiftboot.demo.model.entity.AdminUserEntity;
import org.swiftboot.demo.result.*;
import org.swiftboot.demo.service.AdminUserService;
import org.swiftboot.web.request.IdListCommand;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.exception.ErrorCodeSupport;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Optional;

import static org.swiftboot.demo.constant.AuthConstants.MY_AUTH_SERVICE_NAME;

/**
 * 管理员服务接口实现
 *
 * @author swiftech 2020-01-06
 **/
@Service
public class AdminUserServiceImpl implements AdminUserService {

    private static final Logger log = LoggerFactory.getLogger(AdminUserServiceImpl.class);

    @Resource
    private AdminUserDao adminUserDao;

    @Resource
    private SessionService sessionService;

    @Resource
    private SwiftbootAuthConfigBean authConfigBean;

// Use AdminInit instead
//    @PostConstruct
//    public void initData() {
//        Optional<AdminUserEntity> optAdmin = adminUserDao.findByLoginName("admin");
//        if (!optAdmin.isPresent()) {
//            AdminUserEntity newEntity = new AdminUserEntity();
//            newEntity.setLoginName("admin");
//            newEntity.setLoginPwd(PasswordUtils.createPassword("12345678", "my-auth-service-name"));
//            adminUserDao.save(newEntity);
//        }
//    }

    @Override
    public AdminUserSigninResult adminUserSignin(AdminUserSigninCommand command) {
        AdminUserSigninResult ret = new AdminUserSigninResult();

        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.login(new UsernamePasswordToken(command.getLoginName(), command.getLoginPwd(), MY_AUTH_SERVICE_NAME));
            Session session = SecurityUtils.getSubject().getSession();
            ret.setSuccess(true);
            ret.setLoginName(command.getLoginName());
            ret.setToken(session.getId().toString());
            ret.setId(session.getAttribute("user_id").toString());
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new ErrMessageException(ErrorCodeSupport.CODE_SIGNIN_FAIL, e.getMessage());
        }
        return ret;
    }

    @Override
    public AdminUserSignoutResult adminUserSignout(AdminUserSignoutCommand command) {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return new AdminUserSignoutResult();
    }

    /**
     * 创建管理员
     *
     * @param cmd
     * @return
     */
    @Override
    public AdminUserCreateResult createAdminUser(AdminUserCreateCommand cmd) {
        AdminUserEntity p = cmd.createEntity();
        AdminUserEntity saved = adminUserDao.save(p);
        log.debug("创建管理员: " + saved.getId());
        return new AdminUserCreateResult(saved.getId());
    }

    /**
     * 保存对管理员的修改
     *
     * @param cmd
     * @return
     */
    @Override
    public AdminUserSaveResult saveAdminUser(AdminUserSaveCommand cmd) {
        AdminUserSaveResult ret = new AdminUserSaveResult();
        Optional<AdminUserEntity> optEntity = adminUserDao.findById(cmd.getId());
        if (optEntity.isPresent()) {
            AdminUserEntity p = optEntity.get();
            p = cmd.populateEntity(p);
            AdminUserEntity saved = adminUserDao.save(p);
            ret.setAdminUserId(saved.getId());
        }
        return ret;
    }

    /**
     * 逻辑删除管理员
     *
     * @param adminUserId
     */
    @Override
    public void deleteAdminUser(String adminUserId) {
        Optional<AdminUserEntity> optEntity = adminUserDao.findById(adminUserId);
        if (optEntity.isPresent()) {
            AdminUserEntity p = optEntity.get();
            p.setIsDelete(true);
            adminUserDao.save(p);
        }
    }

    /**
     * 批量逻辑删除管理员
     *
     * @param cmd
     */
    @Override
    public void deleteAdminUserList(IdListCommand cmd) {
        List<AdminUserEntity> entities = adminUserDao.findAllByIdIn(cmd.getIds());
        for (AdminUserEntity entity : entities) {
            entity.setIsDelete(true);
            adminUserDao.save(entity);
            // TODO 处理关联表的数据删除
        }
    }


    /**
     * 永久删除管理员
     *
     * @param adminUserId
     */
    @Override
    public void purgeAdminUser(String adminUserId) {
        if (StringUtils.isNotBlank(adminUserId)) {
            adminUserDao.deleteById(adminUserId);
        }
        else {
            throw new RuntimeException("");
        }
    }

    /**
     * 批量永久删除管理员
     *
     * @param cmd
     */
    @Override
    public void purgeAdminUserList(IdListCommand cmd) {
        List<AdminUserEntity> entities = adminUserDao.findAllByIdIn(cmd.getIds());
        for (AdminUserEntity entity : entities) {
            adminUserDao.deleteById(entity.getId());
            // TODO 处理关联表的数据删除
        }
    }


    /**
     * 查询管理员
     *
     * @param adminUserId
     * @return
     */
    @Override
    public AdminUserResult queryAdminUser(String adminUserId) {
        AdminUserResult ret = null;
        Optional<AdminUserEntity> optEntity = adminUserDao.findById(adminUserId);
        if (optEntity.isPresent()) {
            log.debug(optEntity.get().getId());
            ret = AdminUserResult.createResult(AdminUserResult.class, optEntity.get());
        }
        else {
            log.debug("没有查询到管理员, ID: " + adminUserId);
        }
        return ret;
    }

    /**
     * 查询所有管理员
     *
     * @return
     */
    @Override
    public AdminUserListResult queryAdminUserList() {
        AdminUserListResult ret = new AdminUserListResult();
        Iterable<AdminUserEntity> all = adminUserDao.findAll();
        if (all != null) {
            ret.populateByEntities(all);
            ret.setTotal(adminUserDao.count());
        }
        else {
            log.debug("没有查询到管理员");
        }
        return ret;
    }

    /**
     * 分页查询管理员
     *
     * @param page     页数，从0开始
     * @param pageSize 页大小
     * @return
     */
    @Override
    public AdminUserListResult queryAdminUserList(int page, int pageSize) {
        AdminUserListResult ret = new AdminUserListResult();
        Page<AdminUserEntity> allPagination = adminUserDao.findAll(PageRequest.of(page, pageSize));
        if (allPagination != null) {
            ret.populateByEntities(allPagination);
            ret.setTotal(adminUserDao.count());
        }
        else {
            log.debug("没有查到管理员");
        }
        return ret;
    }
}