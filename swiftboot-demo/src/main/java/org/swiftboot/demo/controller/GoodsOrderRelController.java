package org.swiftboot.demo.controller;

import org.swiftboot.demo.command.GoodsOrderRelCreateCommand;
import org.swiftboot.demo.command.GoodsOrderRelDelPurgeCommand;
import org.swiftboot.demo.command.GoodsOrderRelSaveCommand;
import org.swiftboot.demo.result.GoodsOrderRelCreateResult;
import org.swiftboot.demo.result.GoodsOrderRelListResult;
import org.swiftboot.demo.result.GoodsOrderRelResult;
import org.swiftboot.demo.result.GoodsOrderRelSaveResult;
import org.swiftboot.demo.service.GoodsOrderRelService;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.result.HttpResponse;
import org.swiftboot.web.command.IdCommand;
import org.swiftboot.web.command.IdListCommand;
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
 * 商品订单关系
 *
 * @author swiftech 2019-04-07
 **/
@Api(tags = {"GoodsOrderRel商品订单关系"})
@Controller
@RequestMapping("/goods/order/rel")
@ResponseBody
public class GoodsOrderRelController {

    private Logger log = LoggerFactory.getLogger(GoodsOrderRelController.class);

    @Resource
    private GoodsOrderRelService goodsOrderRelService;

    @ApiOperation(notes = "创建商品订单关系", value = "创建商品订单关系")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public HttpResponse<GoodsOrderRelCreateResult> goodsOrderRelCreate(
            @RequestBody @Validated @ApiParam("创建商品订单关系参数") GoodsOrderRelCreateCommand command) {
        log.info("> /goods/order/rel/create");
        log.debug(JsonUtils.object2PrettyJson(command));
        GoodsOrderRelCreateResult ret = goodsOrderRelService.createGoodsOrderRel(command);
        return new HttpResponse<>(ret);
    }

    @ApiOperation(notes = "保存商品订单关系", value = "保存商品订单关系")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public HttpResponse<GoodsOrderRelSaveResult> goodsOrderRelSave(
            @RequestBody @Validated @ApiParam("保存商品订单关系参数") GoodsOrderRelSaveCommand command) {
        log.info("> /goods/order/rel/save");
        log.debug(JsonUtils.object2PrettyJson(command));
        GoodsOrderRelSaveResult ret = goodsOrderRelService.saveGoodsOrderRel(command);
        return new HttpResponse<>(ret);
    }

    @ApiOperation(notes = "查询商品订单关系", value = "查询商品订单关系")
    @RequestMapping(value = "query", method = RequestMethod.GET)
    public HttpResponse<GoodsOrderRelResult> goodsOrderRelQuery(
            @RequestParam("goods_order_rel_id") String goodsOrderRelId) {
        log.info("> /goods/order/rel/query");
        log.debug("  goods_order_rel_id" + goodsOrderRelId);
        GoodsOrderRelResult goodsOrderRelResult = goodsOrderRelService.queryGoodsOrderRel(goodsOrderRelId);
        return new HttpResponse<>(goodsOrderRelResult);
    }

    @ApiOperation(notes = "查询商品订单关系列表", value = "查询商品订单关系列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public HttpResponse<GoodsOrderRelListResult> goodsOrderRelList() {
        log.info("> /goods/order/rel/list");
        GoodsOrderRelListResult ret = goodsOrderRelService.queryGoodsOrderRelList();
        return new HttpResponse<>(ret);
    }

    @ApiOperation(notes = "逻辑删除商品订单关系", value = "逻辑删除商品订单关系")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public HttpResponse<Void> goodsOrderRelDelete(
            @RequestBody @Validated @ApiParam("商品订单关系ID") IdCommand command) {
        log.info("> /goods/order/rel/delete");
        log.debug(JsonUtils.object2PrettyJson(command));
        goodsOrderRelService.deleteGoodsOrderRel(command.getId());
        return new HttpResponse<>();
    }

    @ApiOperation(notes = "逻辑删除多个商品订单关系", value = "逻辑删除多个商品订单关系")
    @RequestMapping(value = "delete/list", method = RequestMethod.DELETE)
    public HttpResponse<Void> goodsOrderRelDeleteList(
            @RequestBody @Validated @ApiParam("商品订单关系ID列表") IdListCommand command) {
        log.info("> /goods/order/rel/delete/list");
        log.debug(JsonUtils.object2PrettyJson(command));
        goodsOrderRelService.deleteGoodsOrderRelList(command);
        return new HttpResponse<>();
    }

    @ApiOperation(notes = "逻辑删除商品订单关系，按照商品ID和订单ID", value = "逻辑删除商品订单关系")
    @RequestMapping(value = "delete/byfk", method = RequestMethod.DELETE)
    public HttpResponse<Void> goodsOrderRelDeleteByFk(
            @RequestBody @Validated @ApiParam("商品订单关系关系ID") GoodsOrderRelDelPurgeCommand command) {
        log.info("> /goods/order/rel/delete/byfk");
        log.debug(JsonUtils.object2PrettyJson(command));
        goodsOrderRelService.deleteGoodsOrderRel(command);
        return new HttpResponse<>();
    }

    @ApiOperation(notes = "永久删除商品订单关系", value = "永久删除商品订单关系")
    @RequestMapping(value = "purge", method = RequestMethod.DELETE)
    public HttpResponse<Void> goodsOrderRelPurge(
            @RequestBody @Validated @ApiParam("商品订单关系ID") IdCommand command) {
        log.info("> /goods/order/rel/purge");
        log.debug(JsonUtils.object2PrettyJson(command));
        goodsOrderRelService.purgeGoodsOrderRel(command.getId());
        return new HttpResponse<>();
    }

    @ApiOperation(notes = "永久删除多个商品订单关系", value = "永久删除多个商品订单关系")
    @RequestMapping(value = "purge/list", method = RequestMethod.DELETE)
    public HttpResponse<Void> goodsOrderRelPurgeList(
            @RequestBody @Validated @ApiParam("商品订单关系ID列表") IdListCommand command) {
        log.info("> /goods/order/rel/purge/list");
        log.debug(JsonUtils.object2PrettyJson(command));
        goodsOrderRelService.purgeGoodsOrderRelList(command);
        return new HttpResponse<>();
    }

    @ApiOperation(notes = "永久删除商品订单关系，按照商品ID和订单ID", value = "永久删除商品订单关系")
    @RequestMapping(value = "purge/byfk", method = RequestMethod.DELETE)
    public HttpResponse<Void> goodsOrderRelPurgeByFk(
            @RequestBody @Validated @ApiParam("商品订单关系ID") GoodsOrderRelDelPurgeCommand command) {
        log.info("> /goods/order/rel/purge/byfk");
        log.debug(JsonUtils.object2PrettyJson(command));
        goodsOrderRelService.purgeGoodsOrderRel(command);
        return new HttpResponse<>();
    }
}
