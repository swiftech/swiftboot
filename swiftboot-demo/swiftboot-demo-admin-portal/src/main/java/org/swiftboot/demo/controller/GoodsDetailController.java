package org.swiftboot.demo.controller;

import org.swiftboot.demo.result.GoodsDetailCreateResult;
import org.swiftboot.demo.result.GoodsDetailListResult;
import org.swiftboot.demo.result.GoodsDetailResult;
import org.swiftboot.demo.result.GoodsDetailSaveResult;
import org.swiftboot.demo.command.GoodsDetailCreateCommand;
import org.swiftboot.demo.command.GoodsDetailSaveCommand;
import org.swiftboot.demo.service.GoodsDetailService;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.dto.HttpResponse;
import org.swiftboot.web.command.IdCommand;
import org.swiftboot.web.command.IdListCommand;
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
 * 商品详情
 *
 * @author swiftech 2019-04-07
 **/
@Tag(name = "GoodsDetail商品详情"})
@Controller
@RequestMapping("/goods/detail")
@ResponseBody
public class GoodsDetailController {

    private static final Logger log = LoggerFactory.getLogger(GoodsDetailController.class);

    @Resource
    private GoodsDetailService goodsDetailService;

    @Operation(description = "创建商品详情", value = "创建商品详情")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public HttpResponse<GoodsDetailCreateResult> goodsDetailCreate(
            @RequestBody @Validated @Parameter(description = "创建商品详情参数") GoodsDetailCreateCommand command) {
        log.info("> /goods/detail/create");
        log.debug(JsonUtils.object2PrettyJson(command));
        GoodsDetailCreateResult ret = goodsDetailService.createGoodsDetail(command);
        return new HttpResponse<>(ret);
    }

    @Operation(description = "保存商品详情", value = "保存商品详情")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public HttpResponse<GoodsDetailSaveResult> goodsDetailSave(
            @RequestBody @Validated @Parameter(description = "保存商品详情参数") GoodsDetailSaveCommand command) {
        log.info("> /goods/detail/save");
        log.debug(JsonUtils.object2PrettyJson(command));
        GoodsDetailSaveResult ret = goodsDetailService.saveGoodsDetail(command);
        return new HttpResponse<>(ret);
    }

    @Operation(description = "查询商品详情", value = "查询商品详情")
    @RequestMapping(value = "query", method = RequestMethod.GET)
    public HttpResponse<GoodsDetailResult> goodsDetailQuery(
            @RequestParam("goods_detail_id") String goodsDetailId) {
        log.info("> /goods/detail/query");
        log.debug("  goods_detail_id" + goodsDetailId);
        GoodsDetailResult goodsDetailResult = goodsDetailService.queryGoodsDetail(goodsDetailId);
        return new HttpResponse<>(goodsDetailResult);
    }

    @Operation(description = "查询商品详情列表", value = "查询商品详情列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public HttpResponse<GoodsDetailListResult> goodsDetailList() {
        log.info("> /goods/detail/list");
        GoodsDetailListResult ret = goodsDetailService.queryGoodsDetailList();
        return new HttpResponse<>(ret);
    }

    @Operation(description = "逻辑删除商品详情", value = "逻辑删除商品详情")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public HttpResponse<Void> goodsDetailDelete(
            @RequestBody @Validated @Parameter(description = "商品详情ID") IdCommand command) {
        log.info("> /goods/detail/delete");
        log.debug(JsonUtils.object2PrettyJson(command));
        goodsDetailService.deleteGoodsDetail(command.getId());
        return new HttpResponse<>();
    }

    @Operation(description = "逻辑删除多个商品详情", value = "逻辑删除多个商品详情")
    @RequestMapping(value = "delete/list", method = RequestMethod.DELETE)
    public HttpResponse<Void> goodsDetailDeleteList(
            @RequestBody @Validated @Parameter(description = "商品详情ID列表") IdListCommand command) {
        log.info("> /goods/detail/delete/list");
        log.debug(JsonUtils.object2PrettyJson(command));
        goodsDetailService.deleteGoodsDetailList(command);
        return new HttpResponse<>();
    }


    @Operation(description = "永久删除商品详情", value = "永久删除商品详情")
    @RequestMapping(value = "purge", method = RequestMethod.DELETE)
    public HttpResponse<Void> goodsDetailPurge(
            @RequestBody @Validated @Parameter(description = "商品详情ID") IdCommand command) {
        log.info("> /goods/detail/purge");
        log.debug(JsonUtils.object2PrettyJson(command));
        goodsDetailService.purgeGoodsDetail(command.getId());
        return new HttpResponse<>();
    }

    @Operation(description = "永久删除多个商品详情", value = "永久删除多个商品详情")
    @RequestMapping(value = "purge/list", method = RequestMethod.DELETE)
    public HttpResponse<Void> goodsDetailPurgeList(
            @RequestBody @Validated @Parameter(description = "商品详情ID列表") IdListCommand command) {
        log.info("> /goods/detail/purge/list");
        log.debug(JsonUtils.object2PrettyJson(command));
        goodsDetailService.purgeGoodsDetailList(command);
        return new HttpResponse<>();
    }

}
