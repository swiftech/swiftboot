package org.swiftboot.demo.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.swiftboot.demo.dto.TimezoneBasedDto;
import org.swiftboot.demo.model.TimezoneBasedEntity;
import org.swiftboot.demo.repository.TimezoneBasedRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class TimezoneBasedService {

    @Resource
    private TimezoneBasedRepository timezoneBasedRepository;

    @PostConstruct
    public void init() {
        // 每次都增加一条数据
        TimezoneBasedEntity entity = new TimezoneBasedEntity();
        entity.setName("name " + Instant.now());
        timezoneBasedRepository.save(entity);
    }

    public List<TimezoneBasedDto> query(Instant startTime, Instant endTime) {
        List<TimezoneBasedEntity> entities = timezoneBasedRepository.findByCreateTimeGreaterThanAndCreateTimeLessThanOrderByCreateTimeDesc(
                startTime, endTime);
        return entities.stream().map(e -> {
            TimezoneBasedDto dto = new TimezoneBasedDto();
            dto.populateByEntity(e, false);
            return dto;
        }).toList();
    }
}
