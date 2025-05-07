package org.swiftboot.demo.controller;

import org.swiftboot.demo.dto.OrderCreateResult;
import org.swiftboot.demo.dto.OrderSaveResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.demo.request.OrderWithDetailCreateRequest;
import org.swiftboot.demo.request.OrderWithDetailSaveRequest;
import org.swiftboot.demo.service.OrderDetailRelationService;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.response.ResponseCode;
import org.swiftboot.web.response.Response;
import org.swiftboot.web.validate.ConvertValidateResult;

import jakarta.annotation.Resource;

/**
 * 测试实体类关联
 *
 * @author swiftech
 */
@Controller
@RequestMapping("/order")
@ResponseBody
@ConvertValidateResult
public class RelationController {

    private static final Logger log = LoggerFactory.getLogger(RelationController.class);

    @Resource
    private OrderDetailRelationService orderService;

    @Operation(description = "创建订单（带详情）")
    @RequestMapping(value = "/with_detail/create", method = RequestMethod.POST)
    public Response<OrderCreateResult> orderWithDetailCreate(
            @RequestBody @Validated @Parameter(description = "创建订单（带详情）参数") OrderWithDetailCreateRequest request) {
        log.info("> /order/with_detail/create");
        log.debug(JsonUtils.object2PrettyJson(request));
        OrderCreateResult ret = orderService.createOrderWithDetail(request);
        return new Response<>(ret);
    }

    @Operation(description = "编辑保存订单（带详情）")
    @RequestMapping(value = "/with_detail/save", method = RequestMethod.POST)
    public Response<OrderSaveResult> orderWithDetailSave(
            @RequestBody @Validated @Parameter(description = "编辑保存订单参数（带详情）") OrderWithDetailSaveRequest request,
            BindingResult bindingResult) {
        log.info("> /order/with_detail/save");
        if (bindingResult.hasErrors()) {
            log.error("验证错误：" + bindingResult.getErrorCount());
            return new Response<>(ResponseCode.CODE_SYS_ERR, "验证错误");
        }
        log.debug(JsonUtils.object2PrettyJson(request));
        OrderSaveResult ret = orderService.saveOrderWithNewDetail(request);
        return new Response<>(ret);
    }
}
