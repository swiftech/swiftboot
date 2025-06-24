package org.swiftboot.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.swiftboot.demo.model.UserEntity;

import java.util.Optional;

/**
 *
 */
public interface UserRepository extends CrudRepository<UserEntity, String> {

    Optional<UserEntity> findByOpenId(String openId);

    Optional<UserEntity> findByLoginName(String loginName);
}
