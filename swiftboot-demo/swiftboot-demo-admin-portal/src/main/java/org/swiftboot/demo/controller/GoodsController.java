package org.swiftboot.demo.controller;

import org.swiftboot.demo.result.GoodsCreateResult;
import org.swiftboot.demo.result.GoodsListResult;
import org.swiftboot.demo.result.GoodsResult;
import org.swiftboot.demo.result.GoodsSaveResult;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.BindingResult;
import org.swiftboot.demo.command.GoodsCreateCommand;
import org.swiftboot.demo.command.GoodsSaveCommand;
import org.swiftboot.demo.service.GoodsService;
import org.swiftboot.util.JsonUtils;
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

import jakarta.annotation.Resource;

/**
 * 商品
 *
 * @author swiftech 2019-04-07
 **/
@Api(tags = {"Goods商品"})
@Controller
@RequestMapping("/goods")
@ResponseBody
@ConvertValidateResult
public class GoodsController {

    private static final Logger log = LoggerFactory.getLogger(GoodsController.class);

    @Resource
    private GoodsService goodsService;

    @ApiOperation(notes = "创建商品", value = "创建商品")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @RequiresPermissions("goods:create")
    public HttpResponse<GoodsCreateResult> goodsCreate(
            @RequestBody @Validated @ApiParam("创建商品参数") GoodsCreateCommand command,
            BindingResult bindingResult) {
        log.info("> /goods/create");
        log.debug(JsonUtils.object2PrettyJson(command));
        GoodsCreateResult ret = goodsService.createGoods(command);
        return new HttpResponse<>(ret);
    }

    @ApiOperation(notes = "保存商品", value = "保存商品")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @RequiresPermissions("goods:save")
    public HttpResponse<GoodsSaveResult> goodsSave(
            @RequestBody @Validated @ApiParam("保存商品参数") GoodsSaveCommand command) {
        log.info("> /goods/save");
        log.debug(JsonUtils.object2PrettyJson(command));
        GoodsSaveResult ret = goodsService.saveGoods(command);
        return new HttpResponse<>(ret);
    }

    @ApiOperation(notes = "查询商品", value = "查询商品")
    @RequestMapping(value = "query", method = RequestMethod.GET)
    @RequiresPermissions("goods:query")
    public HttpResponse<GoodsResult> goodsQuery(
            @RequestParam("goods_id") String goodsId) {
        log.info("> /goods/query");
        log.debug("  goods_id" + goodsId);
        GoodsResult goodsResult = goodsService.queryGoods(goodsId);
        return new HttpResponse<>(goodsResult);
    }

    @ApiOperation(notes = "查询商品列表", value = "查询商品列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @RequiresPermissions("goods:list")
    public HttpResponse<GoodsListResult> goodsList() {
        log.info("> /goods/list");
        GoodsListResult ret = goodsService.queryGoodsList();
        return new HttpResponse<>(ret);
    }

    @ApiOperation(notes = "逻辑删除商品", value = "逻辑删除商品")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    @RequiresPermissions("goods:delete")
    public HttpResponse<Void> goodsDelete(
            @RequestBody @Validated @ApiParam("商品ID") IdCommand command) {
        log.info("> /goods/delete");
        log.debug(JsonUtils.object2PrettyJson(command));
        goodsService.deleteGoods(command.getId());
        return new HttpResponse<>();
    }

    @ApiOperation(notes = "逻辑删除多个商品", value = "逻辑删除多个商品")
    @RequestMapping(value = "delete/list", method = RequestMethod.DELETE)
    @RequiresPermissions("goods:delete")
    public HttpResponse<Void> goodsDeleteList(
            @RequestBody @Validated @ApiParam("商品ID列表") IdListCommand command) {
        log.info("> /goods/delete/list");
        log.debug(JsonUtils.object2PrettyJson(command));
        goodsService.deleteGoodsList(command);
        return new HttpResponse<>();
    }


    @ApiOperation(notes = "永久删除商品", value = "永久删除商品")
    @RequestMapping(value = "purge", method = RequestMethod.DELETE)
    @RequiresPermissions("goods:purge")
    public HttpResponse<Void> goodsPurge(
            @RequestBody @Validated @ApiParam("商品ID") IdCommand command) {
        log.info("> /goods/purge");
        log.debug(JsonUtils.object2PrettyJson(command));
        goodsService.purgeGoods(command.getId());
        return new HttpResponse<>();
    }

    @ApiOperation(notes = "永久删除多个商品", value = "永久删除多个商品")
    @RequestMapping(value = "purge/list", method = RequestMethod.DELETE)
    @RequiresPermissions("goods:purge")
    public HttpResponse<Void> goodsPurgeList(
            @RequestBody @Validated @ApiParam("商品ID列表") IdListCommand command) {
        log.info("> /goods/purge/list");
        log.debug(JsonUtils.object2PrettyJson(command));
        goodsService.purgeGoodsList(command);
        return new HttpResponse<>();
    }

}
