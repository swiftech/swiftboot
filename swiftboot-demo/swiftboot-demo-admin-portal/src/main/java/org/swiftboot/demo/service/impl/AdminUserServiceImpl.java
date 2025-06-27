package org.swiftboot.demo.service.impl;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.swiftboot.common.auth.JwtService;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.common.auth.token.AccessToken;
import org.swiftboot.demo.dto.*;
import org.swiftboot.demo.model.AdminUserEntity;
import org.swiftboot.demo.repository.AdminUserRepository;
import org.swiftboot.demo.request.AdminUserRequest;
import org.swiftboot.demo.request.AdminUserSigninRequest;
import org.swiftboot.demo.request.AdminUserSignoutRequest;
import org.swiftboot.demo.security.AuthUser;
import org.swiftboot.demo.service.AdminUserService;
import org.swiftboot.web.dto.PopulatableDto;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.request.IdListRequest;
import org.swiftboot.web.response.ResponseCode;

import java.util.List;
import java.util.Optional;

/**
 * 管理员服务接口实现
 *
 * @author swiftech 2020-01-06
 **/
@Service
public class AdminUserServiceImpl implements AdminUserService {

    private static final Logger log = LoggerFactory.getLogger(AdminUserServiceImpl.class);
    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtService jwtService;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Resource
    private AdminUserRepository adminUserRepository;

//    @Resource
//    private SessionService sessionService;
//
//    @Resource
//    private SwiftbootAuthConfigBean authConfigBean;

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
    public AdminUserSigninResult adminUserSignin(AdminUserSigninRequest req) {
        String loginName = req.getLoginName();
        String loginPwd = req.getLoginPwd();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginName, loginPwd);
        log.info("user %s login...".formatted(loginName));
        // authenticate by Spring Security.
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);

        if (authentication.getPrincipal() instanceof AuthUser user) {
            AccessToken accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername());
            jwtService.saveJwtAuthentication(accessToken, null); // TODO 为什么必须调用这个？
            AdminUserSigninResult ret = new AdminUserSigninResult();
            ret.setAccessToken(accessToken.tokenValue());
            ret.setExpiresAt(accessToken.expiresAt());
            return ret;
        }
        else {
            throw new ErrMessageException(ResponseCode.CODE_SIGNIN_FAIL, "登录失败");
        }


//        Subject currentUser = SecurityUtils.getSubject();
//        try {
//            currentUser.login(new UsernamePasswordToken(request.getLoginName(), request.getLoginPwd(), MY_AUTH_SERVICE_NAME));
//            Session session = SecurityUtils.getSubject().getSession();
//            ret.setSuccess(true);
//            ret.setLoginName(request.getLoginName());
//            ret.setToken(session.getId().toString());
//            ret.setId(session.getAttribute("user_id").toString());
//        } catch (AuthenticationException e) {
//            e.printStackTrace();
//            throw new ErrMessageException(ResponseCode.CODE_SIGNIN_FAIL, e.getMessage());
//        }
//        return ret;
    }

    @Override
    public AdminUserSignoutResult adminUserSignout(AdminUserSignoutRequest command) {
//        Subject currentUser = SecurityUtils.getSubject();
//        currentUser.logout();
        return new AdminUserSignoutResult();
    }

    /**
     * 创建管理员
     *
     * @param request
     * @return
     */
    @Override
    public AdminUserCreateResult createAdminUser(AdminUserRequest request) {
        AdminUserEntity p = request.createEntity();
        AdminUserEntity saved = adminUserRepository.save(p);
        log.debug("创建管理员: " + saved.getId());
        return new AdminUserCreateResult(saved.getId());
    }

    /**
     * 保存对管理员的修改
     *
     * @param request
     * @return
     */
    @Override
    public AdminUserSaveResult saveAdminUser(String userId, AdminUserRequest request) {
        AdminUserSaveResult ret = new AdminUserSaveResult();
        Optional<AdminUserEntity> optEntity = adminUserRepository.findById(userId);
        if (optEntity.isPresent()) {
            AdminUserEntity p = optEntity.get();
            p = request.populateEntity(p);
            AdminUserEntity saved = adminUserRepository.save(p);
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
        Optional<AdminUserEntity> optEntity = adminUserRepository.findById(adminUserId);
        if (optEntity.isPresent()) {
            AdminUserEntity p = optEntity.get();
            p.setIsDelete(true);
            adminUserRepository.save(p);
        }
    }

    /**
     * 批量逻辑删除管理员
     *
     * @param request
     */
    @Override
    public void deleteAdminUserList(IdListRequest request) {
        List<AdminUserEntity> entities = adminUserRepository.findAllByIdIn(request.getIds());
        for (AdminUserEntity entity : entities) {
            entity.setIsDelete(true);
            adminUserRepository.save(entity);
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
            adminUserRepository.deleteById(adminUserId);
        }
        else {
            throw new RuntimeException("");
        }
    }

    /**
     * 批量永久删除管理员
     *
     * @param request
     */
    @Override
    public void purgeAdminUserList(IdListRequest request) {
        List<AdminUserEntity> entities = adminUserRepository.findAllByIdIn(request.getIds());
        for (AdminUserEntity entity : entities) {
            adminUserRepository.deleteById(entity.getId());
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
        Optional<AdminUserEntity> optEntity = adminUserRepository.findById(adminUserId);
        if (optEntity.isPresent()) {
            log.debug(optEntity.get().getId());
            ret = PopulatableDto.createDto(AdminUserResult.class, optEntity.get());
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
        Iterable<AdminUserEntity> all = adminUserRepository.findAll();
        if (all != null) {
            ret.populateByEntities(all);
            ret.setTotal(adminUserRepository.count());
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
        Page<AdminUserEntity> allPagination = adminUserRepository.findAll(PageRequest.of(page, pageSize));
        if (allPagination != null) {
            ret.populateByEntities(allPagination);
            ret.setTotal(adminUserRepository.count());
        }
        else {
            log.debug("没有查到管理员");
        }
        return ret;
    }
}