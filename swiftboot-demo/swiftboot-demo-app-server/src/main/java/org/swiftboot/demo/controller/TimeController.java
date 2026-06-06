package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.swiftboot.demo.request.TimeRequest;
import org.swiftboot.web.response.Response;
import org.swiftboot.web.response.ResponseCode;

import java.time.*;

@Tag(name = "Time")
@Controller
@RequestMapping("/time")
@ResponseBody
@Validated
public class TimeController {


    private static final Logger log = LoggerFactory.getLogger(TimeController.class);

    @Operation(description = "GET request with time params")
    @GetMapping(value = "params")
    public Response<Void> timeParamForGet(@RequestParam LocalDateTime localDateTime,
                                          @RequestParam LocalDate localDate,
                                          @RequestParam LocalTime localTime,
                                          @RequestParam YearMonth yearMonth,
                                          @RequestParam MonthDay monthDay) {
        log.debug(String.valueOf(localDateTime));
        log.debug(String.valueOf(localDate));
        log.debug(String.valueOf(localTime));
        log.debug(String.valueOf(yearMonth));
        log.debug(String.valueOf(monthDay));
        return Response.builder().code(ResponseCode.CODE_OK).build();
    }

    @Operation(description = "GET request with time params")
    @GetMapping(value = "object")
    public Response<Void> timeObjectForGet(TimeRequest timeRequest) {
        log.debug(String.valueOf(timeRequest.getLocalDateTime()));
        log.debug(String.valueOf(timeRequest.getLocalDate()));
        log.debug(String.valueOf(timeRequest.getLocalTime()));
        log.debug(String.valueOf(timeRequest.getYearMonth()));
        log.debug(String.valueOf(timeRequest.getMonthDay()));
        return Response.builder().code(ResponseCode.CODE_OK).build();
    }

    @Operation(description = "POST request with time params")
    @PostMapping(value = "")
    public Response<Void> timeForPost(@RequestBody TimeRequest timeRequest) {
        log.debug(String.valueOf(timeRequest.getLocalDateTime()));
        log.debug(String.valueOf(timeRequest.getLocalDate()));
        log.debug(String.valueOf(timeRequest.getLocalTime()));
        log.debug(String.valueOf(timeRequest.getYearMonth()));
        log.debug(String.valueOf(timeRequest.getMonthDay()));
        return Response.builder().code(ResponseCode.CODE_OK).build();
    }
}
