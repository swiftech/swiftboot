package org.swiftboot.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.swiftboot.demo.model.TimezoneBasedEntity;

import java.time.Instant;
import java.util.List;

@Repository
public interface TimezoneBasedRepository extends CrudRepository<TimezoneBasedEntity, String> {

    List<TimezoneBasedEntity> findByCreateTimeGreaterThanAndCreateTimeLessThanOrderByCreateTimeDesc(Instant startTime, Instant endTime);
}
