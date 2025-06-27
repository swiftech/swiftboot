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
import org.swiftboot.demo.dto.GoodsDetailCreateResult;
import org.swiftboot.demo.dto.GoodsDetailListResult;
import org.swiftboot.demo.dto.GoodsDetailResult;
import org.swiftboot.demo.dto.GoodsDetailSaveResult;
import org.swiftboot.demo.request.GoodsDetailRequest;
import org.swiftboot.demo.service.GoodsDetailService;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.request.IdListRequest;
import org.swiftboot.web.request.IdRequest;
import org.swiftboot.web.response.Response;

/**
 * 商品详情
 *
 * @author swiftech 2019-04-07
 **/
@Tag(name = "GoodsDetail商品详情")
@Controller
@RequestMapping("/goods/detail")
@ResponseBody
public class GoodsDetailController {

    private static final Logger log = LoggerFactory.getLogger(GoodsDetailController.class);

    @Resource
    private GoodsDetailService goodsDetailService;

    @Operation(description = "创建商品详情")
    @PostMapping(value = "create")
    public Response<GoodsDetailCreateResult> goodsDetailCreate(
            @RequestBody @Validated @Parameter(description = "创建商品详情参数") GoodsDetailRequest request) {
        log.info("> /goods/detail/create");
        log.debug(JsonUtils.object2PrettyJson(request));
        GoodsDetailCreateResult ret = goodsDetailService.createGoodsDetail(request);
        return new Response<>(ret);
    }

    @Operation(description = "保存商品详情")
    @PutMapping(value = "{id}")
    public Response<GoodsDetailSaveResult> goodsDetailSave(@PathVariable String id,
            @RequestBody @Validated @Parameter(description = "保存商品详情参数") GoodsDetailRequest request) {
        log.info("> /goods/detail/save");
        log.debug(JsonUtils.object2PrettyJson(request));
        GoodsDetailSaveResult ret = goodsDetailService.saveGoodsDetail(id, request);
        return new Response<>(ret);
    }

    @Operation(description = "查询商品详情")
    @GetMapping(value = "query")
    public Response<GoodsDetailResult> goodsDetailQuery(
            @RequestParam("goods_detail_id") String goodsDetailId) {
        log.info("> /goods/detail/query");
        log.debug("  goods_detail_id" + goodsDetailId);
        GoodsDetailResult goodsDetailResult = goodsDetailService.queryGoodsDetail(goodsDetailId);
        return new Response<>(goodsDetailResult);
    }

    @Operation(description = "查询商品详情列表")
    @GetMapping(value = "list")
    public Response<GoodsDetailListResult> goodsDetailList() {
        log.info("> /goods/detail/list");
        GoodsDetailListResult ret = goodsDetailService.queryGoodsDetailList();
        return new Response<>(ret);
    }

    @Operation(description = "逻辑删除商品详情")
    @DeleteMapping(value = "delete")
    public Response<Void> goodsDetailDelete(
            @RequestBody @Validated @Parameter(description = "商品详情ID") IdRequest request) {
        log.info("> /goods/detail/delete");
        log.debug(JsonUtils.object2PrettyJson(request));
        goodsDetailService.deleteGoodsDetail(request.getId());
        return new Response<>();
    }

    @Operation(description = "逻辑删除多个商品详情")
    @DeleteMapping(value = "delete/list")
    public Response<Void> goodsDetailDeleteList(
            @RequestBody @Validated @Parameter(description = "商品详情ID列表") IdListRequest request) {
        log.info("> /goods/detail/delete/list");
        log.debug(JsonUtils.object2PrettyJson(request));
        goodsDetailService.deleteGoodsDetailList(request);
        return new Response<>();
    }


    @Operation(description = "永久删除商品详情")
    @DeleteMapping(value = "purge")
    public Response<Void> goodsDetailPurge(
            @RequestBody @Validated @Parameter(description = "商品详情ID") IdRequest request) {
        log.info("> /goods/detail/purge");
        log.debug(JsonUtils.object2PrettyJson(request));
        goodsDetailService.purgeGoodsDetail(request.getId());
        return new Response<>();
    }

    @Operation(description = "永久删除多个商品详情")
    @DeleteMapping(value = "purge/list")
    public Response<Void> goodsDetailPurgeList(
            @RequestBody @Validated @Parameter(description = "商品详情ID列表") IdListRequest request) {
        log.info("> /goods/detail/purge/list");
        log.debug(JsonUtils.object2PrettyJson(request));
        goodsDetailService.purgeGoodsDetailList(request);
        return new Response<>();
    }

}
