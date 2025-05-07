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
import org.swiftboot.demo.dto.GoodsCreateResult;
import org.swiftboot.demo.dto.GoodsListResult;
import org.swiftboot.demo.dto.GoodsResult;
import org.swiftboot.demo.dto.GoodsSaveResult;
import org.swiftboot.demo.request.GoodsCreateRequest;
import org.swiftboot.demo.request.GoodsSaveRequest;
import org.swiftboot.demo.service.GoodsService;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.request.IdListRequest;
import org.swiftboot.web.request.IdRequest;
import org.swiftboot.web.response.Response;
import org.swiftboot.web.validate.ConvertValidateResult;

/**
 * 商品
 *
 * @author swiftech 2019-04-07
 **/
@Tag(name = "Goods商品")
@Controller
@RequestMapping("/goods")
@ResponseBody
@ConvertValidateResult
public class GoodsController {

    private static final Logger log = LoggerFactory.getLogger(GoodsController.class);

    @Resource
    private GoodsService goodsService;

    @Operation(description = "创建商品")
    @RequestMapping(value = "create", method = RequestMethod.POST)
//    @RequiresPermissions("goods:create")
    public Response<GoodsCreateResult> goodsCreate(
            @RequestBody @Validated @Parameter(description = "创建商品参数") GoodsCreateRequest request,
            BindingResult bindingResult) {
        log.info("> /goods/create");
        log.debug(JsonUtils.object2PrettyJson(request));
        GoodsCreateResult ret = goodsService.createGoods(request);
        return new Response<>(ret);
    }

    @Operation(description = "保存商品")
    @RequestMapping(value = "save", method = RequestMethod.POST)
//    @RequiresPermissions("goods:save")
    public Response<GoodsSaveResult> goodsSave(
            @RequestBody @Validated @Parameter(description = "保存商品参数") GoodsSaveRequest request) {
        log.info("> /goods/save");
        log.debug(JsonUtils.object2PrettyJson(request));
        GoodsSaveResult ret = goodsService.saveGoods(request);
        return new Response<>(ret);
    }

    @Operation(description = "查询商品")
    @RequestMapping(value = "query", method = RequestMethod.GET)
//    @RequiresPermissions("goods:query")
    public Response<GoodsResult> goodsQuery(
            @RequestParam("goods_id") String goodsId) {
        log.info("> /goods/query");
        log.debug("  goods_id" + goodsId);
        GoodsResult goodsResult = goodsService.queryGoods(goodsId);
        return new Response<>(goodsResult);
    }

    @Operation(description = "查询商品列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
//    @RequiresPermissions("goods:list")
    public Response<GoodsListResult> goodsList() {
        log.info("> /goods/list");
        GoodsListResult ret = goodsService.queryGoodsList();
        return new Response<>(ret);
    }

    @Operation(description = "逻辑删除商品")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
//    @RequiresPermissions("goods:delete")
    public Response<Void> goodsDelete(
            @RequestBody @Validated @Parameter(description = "商品ID") IdRequest request) {
        log.info("> /goods/delete");
        log.debug(JsonUtils.object2PrettyJson(request));
        goodsService.deleteGoods(request.getId());
        return new Response<>();
    }

    @Operation(description = "逻辑删除多个商品")
    @RequestMapping(value = "delete/list", method = RequestMethod.DELETE)
//    @RequiresPermissions("goods:delete")
    public Response<Void> goodsDeleteList(
            @RequestBody @Validated @Parameter(description = "商品ID列表") IdListRequest request) {
        log.info("> /goods/delete/list");
        log.debug(JsonUtils.object2PrettyJson(request));
        goodsService.deleteGoodsList(request);
        return new Response<>();
    }


    @Operation(description = "永久删除商品")
    @RequestMapping(value = "purge", method = RequestMethod.DELETE)
//    @RequiresPermissions("goods:purge")
    public Response<Void> goodsPurge(
            @RequestBody @Validated @Parameter(description = "商品ID") IdRequest request) {
        log.info("> /goods/purge");
        log.debug(JsonUtils.object2PrettyJson(request));
        goodsService.purgeGoods(request.getId());
        return new Response<>();
    }

    @Operation(description = "永久删除多个商品")
    @RequestMapping(value = "purge/list", method = RequestMethod.DELETE)
//    @RequiresPermissions("goods:purge")
    public Response<Void> goodsPurgeList(
            @RequestBody @Validated @Parameter(description = "商品ID列表") IdListRequest request) {
        log.info("> /goods/purge/list");
        log.debug(JsonUtils.object2PrettyJson(request));
        goodsService.purgeGoodsList(request);
        return new Response<>();
    }

}
