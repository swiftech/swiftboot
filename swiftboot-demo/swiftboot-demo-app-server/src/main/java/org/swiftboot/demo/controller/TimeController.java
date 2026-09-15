package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.swiftboot.demo.dto.TimezoneBasedDto;
import org.swiftboot.demo.request.TimeRequest;
import org.swiftboot.demo.service.TimezoneBasedService;
import org.swiftboot.web.response.Response;
import org.swiftboot.web.response.ResponseCode;

import java.time.*;
import java.util.List;
import java.util.TimeZone;

@Tag(name = "Time")
@Controller
@RequestMapping("/time")
@ResponseBody
@Validated
public class TimeController {

    private static final Logger log = LoggerFactory.getLogger(TimeController.class);

    @Resource
    private TimezoneBasedService timezoneBasedService;

    @Operation(description = "GET request with time params")
    @GetMapping(value = "params")
    public Response<TimeRequest> timeParamForGet(@RequestParam LocalDateTime localDateTime,
                                          @RequestParam LocalDate localDate,
                                          @RequestParam LocalTime localTime,
                                          @RequestParam Instant instant,
                                          @RequestParam OffsetDateTime offsetDateTime,
                                          @RequestParam YearMonth yearMonth,
                                          @RequestParam MonthDay monthDay) {
        log.debug("Timezone: %s".formatted(TimeZone.getDefault().getDisplayName()));
        log.debug("Locale: %s".formatted(LocaleContextHolder.getLocale()));
        log.debug(String.valueOf(localDateTime));
        log.debug(String.valueOf(localDate));
        log.debug(String.valueOf(localTime));
        log.debug(String.valueOf(instant));
        log.debug(String.valueOf(offsetDateTime));
        log.debug(String.valueOf(yearMonth));
        log.debug(String.valueOf(monthDay));
        TimeRequest ret = new TimeRequest();
        ret.setLocalDateTime(localDateTime);
        ret.setLocalDate(localDate);
        ret.setLocalTime(localTime);
        ret.setInstant(instant);
        ret.setOffsetDateTime(offsetDateTime);
        ret.setYearMonth(yearMonth);
        ret.setMonthDay(monthDay);
        return new Response<>(ret);
    }

    @Operation(description = "GET request with time params")
    @GetMapping(value = "object")
    public Response<TimeRequest> timeObjectForGet(TimeRequest timeRequest) {
        log.debug("Timezone: %s".formatted(TimeZone.getDefault().getDisplayName()));
        log.debug("Locale: %s".formatted(LocaleContextHolder.getLocale()));
        log.debug(String.valueOf(timeRequest.getLocalDateTime()));
        log.debug(String.valueOf(timeRequest.getLocalDate()));
        log.debug(String.valueOf(timeRequest.getLocalTime()));
        log.debug(String.valueOf(timeRequest.getInstant()));
        log.debug(String.valueOf(timeRequest.getOffsetDateTime()));
        log.debug(String.valueOf(timeRequest.getYearMonth()));
        log.debug(String.valueOf(timeRequest.getMonthDay()));
        return new Response<>(timeRequest);
    }

    @Operation(description = "POST request with time params")
    @PostMapping(value = "")
    public Response<TimeRequest> timeForPost(@RequestBody TimeRequest timeRequest) {
        log.debug("Timezone: %s".formatted(TimeZone.getDefault()));
        log.debug("Locale: %s".formatted(LocaleContextHolder.getLocale().getDisplayName()));
        log.debug(String.valueOf(timeRequest.getLocalDateTime()));
        log.debug(String.valueOf(timeRequest.getLocalDate()));
        log.debug(String.valueOf(timeRequest.getLocalTime()));
        log.debug(String.valueOf(timeRequest.getInstant()));
        log.debug(String.valueOf(timeRequest.getOffsetDateTime()));
        log.debug(String.valueOf(timeRequest.getYearMonth()));
        log.debug(String.valueOf(timeRequest.getMonthDay()));
        return new Response<>(timeRequest);
    }

    @GetMapping("timezone")
    public Response<List<TimezoneBasedDto>> timezone(@RequestParam Instant startTime, @RequestParam Instant endTime) {
//        LocalDateTime start = LocalDateTime.now().minusDays(1);
//        LocalDateTime end = LocalDateTime.now().plusDays(1);
        log.debug("from %s to %s".formatted(startTime, endTime));
        List<TimezoneBasedDto> ret = timezoneBasedService.query(startTime, endTime);
        return new Response<>(ret);
    }
}
