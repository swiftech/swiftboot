package org.swiftboot.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.swiftboot.demo.model.PlatformAuthorization;

/**
 * Platform is the OAuth2 authorization provider.
 *
 */
public interface PlatformAuthorizationRepository extends CrudRepository<PlatformAuthorization, String> {

    PlatformAuthorization findByOpenId(String openId);

    PlatformAuthorization findByAccessToken(String accessToken);
}
