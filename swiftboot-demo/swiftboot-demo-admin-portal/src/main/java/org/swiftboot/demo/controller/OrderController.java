package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.swiftboot.common.auth.annotation.Addition;
import org.swiftboot.common.auth.annotation.UserId;
import org.swiftboot.demo.dto.OrderCreateResult;
import org.swiftboot.demo.dto.OrderListResult;
import org.swiftboot.demo.dto.OrderResult;
import org.swiftboot.demo.dto.OrderSaveResult;
import org.swiftboot.demo.request.OrderRequest;
import org.swiftboot.demo.service.OrderService;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.request.IdListRequest;
import org.swiftboot.web.request.IdRequest;
import org.swiftboot.web.response.Response;
import org.swiftboot.web.response.ResponseCode;
import org.swiftboot.web.validate.ConvertValidateResult;

/**
 * 订单
 *
 * @author swiftech 2019-04-07
 **/
@Tag(name = "Order订单")
@Controller
@RequestMapping("/order")
@ResponseBody
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Resource
    private OrderService orderService;

    @Operation(description = "创建订单")
    @PostMapping(value = "create")
    @ConvertValidateResult
    public Response<OrderCreateResult> orderCreate(
            @RequestBody @Validated @Parameter(description = "创建订单参数") OrderRequest request,
            BindingResult bindingResult) {
        log.info("> /order/create");
//        log.info(request.getUserId());
        log.info(request.getHeader("swiftboot_token"));
        log.debug(JsonUtils.object2PrettyJson(request));
        OrderCreateResult ret = orderService.createOrder(request);
        return new Response<>(ret);
    }

    @Operation(description = "保存订单")
    @PutMapping(value = "{id}")
    public Response<OrderSaveResult> orderSave(@PathVariable String id,
            @RequestBody @Validated @Parameter(description = "保存订单参数") OrderRequest request,
            BindingResult bindingResult) {
        log.info("> /order/save");
        if (bindingResult.hasErrors()) {
            log.error("验证错误：" + bindingResult.getErrorCount());
            return new Response<>(ResponseCode.CODE_SYS_ERR, "验证错误");
        }
        log.debug(JsonUtils.object2PrettyJson(request));
        OrderSaveResult ret = orderService.saveOrder(id, request);
        return new Response<>(ret);
    }

    @Operation(description = "查询订单")
    @GetMapping(value = "query")
    public Response<OrderResult> orderQuery(
            @RequestParam("order_id") String orderId) {
        log.info("> /order/query");
        log.debug("  order_id" + orderId);
        OrderResult orderResult = orderService.queryOrder(orderId);
        return new Response<>(orderResult);
    }

    @Operation(description = "查询订单列表")
    @GetMapping(value = "list")
    public Response<OrderListResult> orderList(@UserId String userId, @Addition("some_addition") String some_addition) {
        log.info("> /order/list");
        log.debug(userId);
        log.debug(String.valueOf(some_addition));
        OrderListResult ret = orderService.queryOrderList(userId);
        return new Response<>(ret);
    }

    @Operation(description = "逻辑删除订单")
    @DeleteMapping(value = "delete")
//    @RequiresPermissions("order:delete")
    public Response<Void> orderDelete(
            @RequestBody @Validated @Parameter(description = "订单ID") IdRequest request) {
        log.info("> /order/delete");
        log.debug(JsonUtils.object2PrettyJson(request));
        orderService.deleteOrder(request.getId());
        return new Response<>();
    }

    @Operation(description = "逻辑删除多个订单")
    @DeleteMapping(value = "delete/list")
    public Response<Void> orderDeleteList(
            @RequestBody @Validated @Parameter(description = "订单ID列表") IdListRequest request) {
        log.info("> /order/delete/list");
        log.debug(JsonUtils.object2PrettyJson(request));
        orderService.deleteOrderList(request);
        return new Response<>();
    }


    @Operation(description = "永久删除订单")
    @DeleteMapping(value = "purge")
//    @RequiresPermissions("order:purge")
    public Response<Void> orderPurge(
            @RequestBody @Validated @Parameter(description = "订单ID") IdRequest request) {
        log.info("> /order/purge");
        log.debug(JsonUtils.object2PrettyJson(request));
        orderService.purgeOrder(request.getId());
        return new Response<>();
    }

    @Operation(description = "永久删除多个订单")
    @DeleteMapping(value = "purge/list")
    public Response<Void> orderPurgeList(
            @RequestBody @Validated @Parameter(description = "订单ID列表") IdListRequest request) {
        log.info("> /order/purge/list");
        log.debug(JsonUtils.object2PrettyJson(request));
        orderService.purgeOrderList(request);
        return new Response<>();
    }

}
