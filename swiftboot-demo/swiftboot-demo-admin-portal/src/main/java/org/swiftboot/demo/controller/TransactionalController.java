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
import org.swiftboot.demo.dto.GoodsCreateResult;
import org.swiftboot.demo.request.GoodsRequest;
import org.swiftboot.demo.request.GoodsWithDetailRequest;
import org.swiftboot.demo.service.GoodsService;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.response.Response;
import org.swiftboot.web.validate.ConvertValidateResult;

/**
 * 测试事务的接口
 *
 * @author switech
 */
@Tag(name = "Transactional")
@Controller
@RequestMapping("/trans/")
@ResponseBody
@ConvertValidateResult
public class TransactionalController {

    private static final Logger log = LoggerFactory.getLogger(TransactionalController.class);

    @Resource
    private GoodsService goodsService;

    @Operation(description = "创建商品（带详情）")
    @PostMapping(value = "/goods_with_detail/create")
    public Response<GoodsCreateResult> goodsWithDetailCreate(
            @RequestBody @Validated @Parameter(description = "创建商品（带详情）参数") GoodsWithDetailRequest request) {
        log.info("> /trans/goods_with_detail/create");
        log.debug(JsonUtils.object2PrettyJson(request));
        GoodsCreateResult ret = goodsService.createGoodsWithDetail(request);
        return new Response<>(ret);
    }

    @Operation(description = "创建商品（异常）")
    @PostMapping(value = "/goods/create2")
    public Response<GoodsCreateResult> goods2Create(
            @RequestBody @Validated @Parameter(description = "创建2个商品") GoodsRequest request) {
        log.info("> /trans/goods/create2");
        log.debug(JsonUtils.object2PrettyJson(request));
        GoodsCreateResult ret = goodsService.createWithException(request);
        return new Response<>(ret);
    }
}
