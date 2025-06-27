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
import org.swiftboot.demo.dto.GoodsOrderRelCreateResult;
import org.swiftboot.demo.dto.GoodsOrderRelListResult;
import org.swiftboot.demo.dto.GoodsOrderRelResult;
import org.swiftboot.demo.dto.GoodsOrderRelSaveResult;
import org.swiftboot.demo.request.GoodsOrderRelDelPurgeRequest;
import org.swiftboot.demo.request.GoodsOrderRelRequest;
import org.swiftboot.demo.service.GoodsOrderRelService;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.request.IdListRequest;
import org.swiftboot.web.request.IdRequest;
import org.swiftboot.web.response.Response;

/**
 * 商品订单关系
 *
 * @author swiftech 2019-04-07
 **/
@Tag(name = "GoodsOrderRel商品订单关系")
@Controller
@RequestMapping("/goods/order/rel")
@ResponseBody
public class GoodsOrderRelController {

    private static final Logger log = LoggerFactory.getLogger(GoodsOrderRelController.class);

    @Resource
    private GoodsOrderRelService goodsOrderRelService;

    @Operation(description = "创建商品订单关系")
    @PostMapping(value = "create")
    public Response<GoodsOrderRelCreateResult> goodsOrderRelCreate(
            @RequestBody @Validated @Parameter(description = "创建商品订单关系参数") GoodsOrderRelRequest request) {
        log.info("> /goods/order/rel/create");
        log.debug(JsonUtils.object2PrettyJson(request));
        GoodsOrderRelCreateResult ret = goodsOrderRelService.createGoodsOrderRel(request);
        return new Response<>(ret);
    }

    @Operation(description = "保存商品订单关系")
    @PutMapping(value = "{id}")
    public Response<GoodsOrderRelSaveResult> goodsOrderRelSave(@PathVariable String id,
            @RequestBody @Validated @Parameter(description = "保存商品订单关系参数") GoodsOrderRelRequest request) {
        log.info("> /goods/order/rel/save");
        log.debug(JsonUtils.object2PrettyJson(request));
        GoodsOrderRelSaveResult ret = goodsOrderRelService.saveGoodsOrderRel(id, request);
        return new Response<>(ret);
    }

    @Operation(description = "查询商品订单关系")
    @GetMapping(value = "query")
    public Response<GoodsOrderRelResult> goodsOrderRelQuery(
            @RequestParam("goods_order_rel_id") String goodsOrderRelId) {
        log.info("> /goods/order/rel/query");
        log.debug("  goods_order_rel_id" + goodsOrderRelId);
        GoodsOrderRelResult goodsOrderRelResult = goodsOrderRelService.queryGoodsOrderRel(goodsOrderRelId);
        return new Response<>(goodsOrderRelResult);
    }

    @Operation(description = "查询商品订单关系列表")
    @GetMapping(value = "list")
    public Response<GoodsOrderRelListResult> goodsOrderRelList() {
        log.info("> /goods/order/rel/list");
        GoodsOrderRelListResult ret = goodsOrderRelService.queryGoodsOrderRelList();
        return new Response<>(ret);
    }

    @Operation(description = "逻辑删除商品订单关系")
    @DeleteMapping(value = "delete")
    public Response<Void> goodsOrderRelDelete(
            @RequestBody @Validated @Parameter(description = "商品订单关系ID") IdRequest request) {
        log.info("> /goods/order/rel/delete");
        log.debug(JsonUtils.object2PrettyJson(request));
        goodsOrderRelService.deleteGoodsOrderRel(request.getId());
        return new Response<>();
    }

    @Operation(description = "逻辑删除多个商品订单关系")
    @DeleteMapping(value = "delete/list")
    public Response<Void> goodsOrderRelDeleteList(
            @RequestBody @Validated @Parameter(description = "商品订单关系ID列表") IdListRequest request) {
        log.info("> /goods/order/rel/delete/list");
        log.debug(JsonUtils.object2PrettyJson(request));
        goodsOrderRelService.deleteGoodsOrderRelList(request);
        return new Response<>();
    }

    @Operation(description = "逻辑删除商品订单关系，按照商品ID和订单ID")
    @DeleteMapping(value = "delete/byfk")
    public Response<Void> goodsOrderRelDeleteByFk(
            @RequestBody @Validated @Parameter(description = "商品订单关系关系ID") GoodsOrderRelDelPurgeRequest request) {
        log.info("> /goods/order/rel/delete/byfk");
        log.debug(JsonUtils.object2PrettyJson(request));
        goodsOrderRelService.deleteGoodsOrderRel(request);
        return new Response<>();
    }

    @Operation(description = "永久删除商品订单关系")
    @DeleteMapping(value = "purge")
    public Response<Void> goodsOrderRelPurge(
            @RequestBody @Validated @Parameter(description = "商品订单关系ID") IdRequest request) {
        log.info("> /goods/order/rel/purge");
        log.debug(JsonUtils.object2PrettyJson(request));
        goodsOrderRelService.purgeGoodsOrderRel(request.getId());
        return new Response<>();
    }

    @Operation(description = "永久删除多个商品订单关系")
    @DeleteMapping(value = "purge/list")
    public Response<Void> goodsOrderRelPurgeList(
            @RequestBody @Validated @Parameter(description = "商品订单关系ID列表") IdListRequest request) {
        log.info("> /goods/order/rel/purge/list");
        log.debug(JsonUtils.object2PrettyJson(request));
        goodsOrderRelService.purgeGoodsOrderRelList(request);
        return new Response<>();
    }

    @Operation(description = "永久删除商品订单关系，按照商品ID和订单ID")
    @DeleteMapping(value = "purge/byfk")
    public Response<Void> goodsOrderRelPurgeByFk(
            @RequestBody @Validated @Parameter(description = "商品订单关系ID") GoodsOrderRelDelPurgeRequest request) {
        log.info("> /goods/order/rel/purge/byfk");
        log.debug(JsonUtils.object2PrettyJson(request));
        goodsOrderRelService.purgeGoodsOrderRel(request);
        return new Response<>();
    }
}
