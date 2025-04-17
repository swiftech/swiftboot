package org.swiftboot.demo.dao;

import org.springframework.data.repository.CrudRepository;
import org.swiftboot.demo.dao.entity.PlatformAuthorization;

/**
 *
 */
public interface PlatformAuthorizationRepository extends CrudRepository<PlatformAuthorization, String> {

    PlatformAuthorization findByOpenId(String openId);

    PlatformAuthorization findByAccessToken(String accessToken);
}
