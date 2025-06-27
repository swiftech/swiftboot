package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.swiftboot.demo.dto.OrderDetailCreateResult;
import org.swiftboot.demo.dto.OrderDetailListResult;
import org.swiftboot.demo.dto.OrderDetailResult;
import org.swiftboot.demo.dto.OrderDetailSaveResult;
import org.swiftboot.demo.request.OrderDetailRequest;
import org.swiftboot.demo.service.OrderDetailService;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.request.IdListRequest;
import org.swiftboot.web.request.IdRequest;
import org.swiftboot.web.response.Response;

/**
 * 订单明细
 *
 * @author swiftech 2019-04-07
 **/
@Tag(name = "OrderDetail订单明细")
@Controller
@RequestMapping("/order/detail")
@ResponseBody
public class OrderDetailController {

    private static final Logger log = LoggerFactory.getLogger(OrderDetailController.class);

    @Resource
    private OrderDetailService orderDetailService;

    @Operation(description = "创建订单明细")
    @PostMapping(value = "create")
    public Response<OrderDetailCreateResult> orderDetailCreate(
            @RequestBody @Validated @Parameter(description = "创建订单明细参数") OrderDetailRequest request) {
        log.info("> /order/detail/create");
        log.debug(JsonUtils.object2PrettyJson(request));
        OrderDetailCreateResult ret = orderDetailService.createOrderDetail(request);
        return new Response<>(ret);
    }

    @Operation(description = "保存订单明细")
    @PutMapping(value = "{id}")
    public Response<OrderDetailSaveResult> orderDetailSave(@PathVariable String id,
            @RequestBody @Validated @Parameter(description = "保存订单明细参数") OrderDetailRequest request) {
        log.info("> /order/detail/save");
        log.debug(JsonUtils.object2PrettyJson(request));
        OrderDetailSaveResult ret = orderDetailService.saveOrderDetail(id, request);
        return new Response<>(ret);
    }

    @Operation(description = "查询订单明细")
    @GetMapping(value = "query")
    public Response<OrderDetailResult> orderDetailQuery(
            @RequestParam("order_detail_id") String orderDetailId) {
        log.info("> /order/detail/query");
        log.debug("  order_detail_id" + orderDetailId);
        OrderDetailResult orderDetailResult = orderDetailService.queryOrderDetail(orderDetailId);
        return new Response<>(orderDetailResult);
    }

    @Operation(description = "查询订单明细列表")
    @GetMapping(value = "list")
    public Response<OrderDetailListResult> orderDetailList() {
        log.info("> /order/detail/list");
        OrderDetailListResult ret = orderDetailService.queryOrderDetailList();
        return new Response<>(ret);
    }

    @Operation(description = "逻辑删除订单明细")
    @DeleteMapping(value = "delete")
    public Response<Void> orderDetailDelete(
            @RequestBody @Validated @Parameter(description = "订单明细ID") IdRequest request) {
        log.info("> /order/detail/delete");
        log.debug(JsonUtils.object2PrettyJson(request));
        orderDetailService.deleteOrderDetail(request.getId());
        return new Response<>();
    }

    @Operation(description = "逻辑删除多个订单明细")
    @DeleteMapping(value = "delete/list")
    public Response<Void> orderDetailDeleteList(
            @RequestBody @Validated @Parameter(description = "订单明细ID列表") IdListRequest request) {
        log.info("> /order/detail/delete/list");
        log.debug(JsonUtils.object2PrettyJson(request));
        orderDetailService.deleteOrderDetailList(request);
        return new Response<>();
    }


    @Operation(description = "永久删除订单明细")
    @DeleteMapping(value = "purge")
    public Response<Void> orderDetailPurge(
            @RequestBody @Validated @Parameter(description = "订单明细ID") IdRequest request) {
        log.info("> /order/detail/purge");
        log.debug(JsonUtils.object2PrettyJson(request));
        orderDetailService.purgeOrderDetail(request.getId());
        return new Response<>();
    }

    @Operation(description = "永久删除多个订单明细")
    @DeleteMapping(value = "purge/list")
    public Response<Void> orderDetailPurgeList(
            @RequestBody @Validated @Parameter(description = "订单明细ID列表") IdListRequest request) {
        log.info("> /order/detail/purge/list");
        log.debug(JsonUtils.object2PrettyJson(request));
        orderDetailService.purgeOrderDetailList(request);
        return new Response<>();
    }

}
