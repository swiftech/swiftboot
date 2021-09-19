package org.swiftboot.demo.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.BindingResult;
import org.swiftboot.auth.annotation.Addition;
import org.swiftboot.auth.interceptor.UserId;
import org.swiftboot.demo.command.OrderCreateCommand;
import org.swiftboot.demo.command.OrderSaveCommand;
import org.swiftboot.demo.result.OrderCreateResult;
import org.swiftboot.demo.result.OrderListResult;
import org.swiftboot.demo.result.OrderResult;
import org.swiftboot.demo.result.OrderSaveResult;
import org.swiftboot.demo.service.OrderService;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.exception.ErrorCodeSupport;
import org.swiftboot.web.result.HttpResponse;
import org.swiftboot.web.command.IdCommand;
import org.swiftboot.web.command.IdListCommand;
import org.swiftboot.web.validate.ConvertValidateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 订单
 *
 * @author swiftech 2019-04-07
 **/
@Api(tags = {"Order订单"})
@Controller
@RequestMapping("/order")
@ResponseBody
public class OrderController {

    private final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Resource
    private OrderService orderService;

    @ApiOperation(notes = "创建订单", value = "创建订单")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ConvertValidateResult
    public HttpResponse<OrderCreateResult> orderCreate(
            @RequestBody @Validated @ApiParam("创建订单参数") OrderCreateCommand command,
            BindingResult bindingResult) {
        log.info("> /order/create");
        log.info(command.getUserId());
        log.debug(JsonUtils.object2PrettyJson(command));
        OrderCreateResult ret = orderService.createOrder(command);
        return new HttpResponse<>(ret);
    }

    @ApiOperation(notes = "保存订单", value = "保存订单")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public HttpResponse<OrderSaveResult> orderSave(
            @RequestBody @Validated @ApiParam("保存订单参数") OrderSaveCommand command,
            BindingResult bindingResult) {
        log.info("> /order/save");
        if (bindingResult.hasErrors()) {
            log.error("验证错误：" + bindingResult.getErrorCount());
            return new HttpResponse<>(ErrorCodeSupport.CODE_SYS_ERR, "验证错误");
        }
        log.debug(JsonUtils.object2PrettyJson(command));
        OrderSaveResult ret = orderService.saveOrder(command);
        return new HttpResponse<>(ret);
    }

    @ApiOperation(notes = "查询订单", value = "查询订单")
    @RequestMapping(value = "query", method = RequestMethod.GET)
    public HttpResponse<OrderResult> orderQuery(
            @RequestParam("order_id") String orderId) {
        log.info("> /order/query");
        log.debug("  order_id" + orderId);
        OrderResult orderResult = orderService.queryOrder(orderId);
        return new HttpResponse<>(orderResult);
    }

    @ApiOperation(notes = "查询订单列表", value = "查询订单列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public HttpResponse<OrderListResult> orderList(@UserId String userId, @Addition("some_addition") String some_addition) {
        log.info("> /order/list");
        log.debug(userId);
        log.debug(String.valueOf(some_addition));
        OrderListResult ret = orderService.queryOrderList();
        return new HttpResponse<>(ret);
    }

    @ApiOperation(notes = "逻辑删除订单", value = "逻辑删除订单")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    @RequiresPermissions("order:delete")
    public HttpResponse<Void> orderDelete(
            @RequestBody @Validated @ApiParam("订单ID") IdCommand command) {
        log.info("> /order/delete");
        log.debug(JsonUtils.object2PrettyJson(command));
        orderService.deleteOrder(command.getId());
        return new HttpResponse<>();
    }

    @ApiOperation(notes = "逻辑删除多个订单", value = "逻辑删除多个订单")
    @RequestMapping(value = "delete/list", method = RequestMethod.DELETE)
    public HttpResponse<Void> orderDeleteList(
            @RequestBody @Validated @ApiParam("订单ID列表") IdListCommand command) {
        log.info("> /order/delete/list");
        log.debug(JsonUtils.object2PrettyJson(command));
        orderService.deleteOrderList(command);
        return new HttpResponse<>();
    }


    @ApiOperation(notes = "永久删除订单", value = "永久删除订单")
    @RequestMapping(value = "purge", method = RequestMethod.DELETE)
    @RequiresPermissions("order:purge")
    public HttpResponse<Void> orderPurge(
            @RequestBody @Validated @ApiParam("订单ID") IdCommand command) {
        log.info("> /order/purge");
        log.debug(JsonUtils.object2PrettyJson(command));
        orderService.purgeOrder(command.getId());
        return new HttpResponse<>();
    }

    @ApiOperation(notes = "永久删除多个订单", value = "永久删除多个订单")
    @RequestMapping(value = "purge/list", method = RequestMethod.DELETE)
    public HttpResponse<Void> orderPurgeList(
            @RequestBody @Validated @ApiParam("订单ID列表") IdListCommand command) {
        log.info("> /order/purge/list");
        log.debug(JsonUtils.object2PrettyJson(command));
        orderService.purgeOrderList(command);
        return new HttpResponse<>();
    }

}
