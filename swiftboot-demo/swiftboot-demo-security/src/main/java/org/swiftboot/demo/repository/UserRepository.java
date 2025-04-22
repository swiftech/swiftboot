package org.swiftboot.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.swiftboot.demo.model.UserEntity;

/**
 *
 */
public interface UserRepository extends CrudRepository<UserEntity, String> {

    UserEntity findByOpenId(String openId);

    UserEntity findByLoginName(String loginName);
}
