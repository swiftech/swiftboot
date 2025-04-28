package org.swiftboot.demo.controller;

import org.swiftboot.demo.result.OrderDetailCreateResult;
import org.swiftboot.demo.result.OrderDetailListResult;
import org.swiftboot.demo.result.OrderDetailResult;
import org.swiftboot.demo.result.OrderDetailSaveResult;
import org.swiftboot.demo.request.OrderDetailCreateCommand;
import org.swiftboot.demo.request.OrderDetailSaveCommand;
import org.swiftboot.demo.service.OrderDetailService;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.dto.HttpResponse;
import org.swiftboot.web.request.IdCommand;
import org.swiftboot.web.request.IdListCommand;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
 * 订单明细
 *
 * @author swiftech 2019-04-07
 **/
@Tag(name = "OrderDetail订单明细"})
@Controller
@RequestMapping("/order/detail")
@ResponseBody
public class OrderDetailController {

    private static final Logger log = LoggerFactory.getLogger(OrderDetailController.class);

    @Resource
    private OrderDetailService orderDetailService;

    @Operation(description = "创建订单明细", value = "创建订单明细")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public HttpResponse<OrderDetailCreateResult> orderDetailCreate(
            @RequestBody @Validated @Parameter(description = "创建订单明细参数") OrderDetailCreateCommand command) {
        log.info("> /order/detail/create");
        log.debug(JsonUtils.object2PrettyJson(command));
        OrderDetailCreateResult ret = orderDetailService.createOrderDetail(command);
        return new HttpResponse<>(ret);
    }

    @Operation(description = "保存订单明细", value = "保存订单明细")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public HttpResponse<OrderDetailSaveResult> orderDetailSave(
            @RequestBody @Validated @Parameter(description = "保存订单明细参数") OrderDetailSaveCommand command) {
        log.info("> /order/detail/save");
        log.debug(JsonUtils.object2PrettyJson(command));
        OrderDetailSaveResult ret = orderDetailService.saveOrderDetail(command);
        return new HttpResponse<>(ret);
    }

    @Operation(description = "查询订单明细", value = "查询订单明细")
    @RequestMapping(value = "query", method = RequestMethod.GET)
    public HttpResponse<OrderDetailResult> orderDetailQuery(
            @RequestParam("order_detail_id") String orderDetailId) {
        log.info("> /order/detail/query");
        log.debug("  order_detail_id" + orderDetailId);
        OrderDetailResult orderDetailResult = orderDetailService.queryOrderDetail(orderDetailId);
        return new HttpResponse<>(orderDetailResult);
    }

    @Operation(description = "查询订单明细列表", value = "查询订单明细列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public HttpResponse<OrderDetailListResult> orderDetailList() {
        log.info("> /order/detail/list");
        OrderDetailListResult ret = orderDetailService.queryOrderDetailList();
        return new HttpResponse<>(ret);
    }

    @Operation(description = "逻辑删除订单明细", value = "逻辑删除订单明细")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public HttpResponse<Void> orderDetailDelete(
            @RequestBody @Validated @Parameter(description = "订单明细ID") IdCommand command) {
        log.info("> /order/detail/delete");
        log.debug(JsonUtils.object2PrettyJson(command));
        orderDetailService.deleteOrderDetail(command.getId());
        return new HttpResponse<>();
    }

    @Operation(description = "逻辑删除多个订单明细", value = "逻辑删除多个订单明细")
    @RequestMapping(value = "delete/list", method = RequestMethod.DELETE)
    public HttpResponse<Void> orderDetailDeleteList(
            @RequestBody @Validated @Parameter(description = "订单明细ID列表") IdListCommand command) {
        log.info("> /order/detail/delete/list");
        log.debug(JsonUtils.object2PrettyJson(command));
        orderDetailService.deleteOrderDetailList(command);
        return new HttpResponse<>();
    }


    @Operation(description = "永久删除订单明细", value = "永久删除订单明细")
    @RequestMapping(value = "purge", method = RequestMethod.DELETE)
    public HttpResponse<Void> orderDetailPurge(
            @RequestBody @Validated @Parameter(description = "订单明细ID") IdCommand command) {
        log.info("> /order/detail/purge");
        log.debug(JsonUtils.object2PrettyJson(command));
        orderDetailService.purgeOrderDetail(command.getId());
        return new HttpResponse<>();
    }

    @Operation(description = "永久删除多个订单明细", value = "永久删除多个订单明细")
    @RequestMapping(value = "purge/list", method = RequestMethod.DELETE)
    public HttpResponse<Void> orderDetailPurgeList(
            @RequestBody @Validated @Parameter(description = "订单明细ID列表") IdListCommand command) {
        log.info("> /order/detail/purge/list");
        log.debug(JsonUtils.object2PrettyJson(command));
        orderDetailService.purgeOrderDetailList(command);
        return new HttpResponse<>();
    }

}
