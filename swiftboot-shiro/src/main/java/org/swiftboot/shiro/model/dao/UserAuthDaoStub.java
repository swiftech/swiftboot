package org.swiftboot.shiro.model.dao;

import org.swiftboot.shiro.model.entity.UserEntityStub;

/**
 * 用户认证接口
 *
 * @param <T> 用户实体类类型
 * @since 1.2
 */
public interface UserAuthDaoStub<T extends UserEntityStub> {

    /**
     * @param loginName
     * @return
     */
    T findByLoginName(String loginName);

}
