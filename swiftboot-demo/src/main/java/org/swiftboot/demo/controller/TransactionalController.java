package org.swiftboot.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.demo.command.GoodsCreateCommand;
import org.swiftboot.demo.command.GoodsWithDetailCreateCommand;
import org.swiftboot.demo.result.GoodsCreateResult;
import org.swiftboot.demo.service.GoodsService;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.result.HttpResponse;
import org.swiftboot.web.validate.ConvertValidateResult;

import javax.annotation.Resource;

/**
 * 测试事务的接口
 *
 * @author switech
 */
@Api(tags = {"Transactional"})
@Controller
@RequestMapping("/trans/")
@ResponseBody
@ConvertValidateResult
public class TransactionalController {

    private final Logger log = LoggerFactory.getLogger(GoodsController.class);

    @Resource
    private GoodsService goodsService;

    @ApiOperation(notes = "创建商品（带详情）", value = "创建商品（带详情）")
    @RequestMapping(value = "/goods_with_detail/create", method = RequestMethod.POST)
    public HttpResponse<GoodsCreateResult> goodsWithDetailCreate(
            @RequestBody @Validated @ApiParam("创建商品（带详情）参数") GoodsWithDetailCreateCommand command) {
        log.info("> /trans/goods_with_detail/create");
        log.debug(JsonUtils.object2PrettyJson(command));
        GoodsCreateResult ret = goodsService.createGoodsWithDetail(command);
        return new HttpResponse<>(ret);
    }

    @ApiOperation(notes = "创建商品（异常）", value = "创建商品（异常）")
    @RequestMapping(value = "/goods/create2", method = RequestMethod.POST)
    public HttpResponse<GoodsCreateResult> goods2Create(
            @RequestBody @Validated @ApiParam("创建2个商品") GoodsCreateCommand command) {
        log.info("> /trans/goods/create2");
        log.debug(JsonUtils.object2PrettyJson(command));
        GoodsCreateResult ret = goodsService.createWithException(command);
        return new HttpResponse<>(ret);
    }
}
