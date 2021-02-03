package org.swiftboot.demo.controller;

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
import org.swiftboot.demo.command.OrderWithDetailCreateCommand;
import org.swiftboot.demo.result.OrderCreateResult;
import org.swiftboot.demo.service.OrderService;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.result.HttpResponse;
import org.swiftboot.web.validate.ConvertValidateResult;

import javax.annotation.Resource;

/**
 * 测试实体类关联
 *
 * @author allen
 */
@Controller
@RequestMapping("/order")
@ResponseBody
@ConvertValidateResult
public class RelationController {

    private Logger log = LoggerFactory.getLogger(RelationController.class);

    @Resource
    private OrderService orderService;

    @ApiOperation(notes = "创建订单（带详情）", value = "创建订单（带详情）")
    @RequestMapping(value = "/with_detail/create", method = RequestMethod.POST)
    public HttpResponse<OrderCreateResult> orderWithDetailCreate(
            @RequestBody @Validated @ApiParam("创建订单（带详情）参数") OrderWithDetailCreateCommand command) {
        log.info("> /order_with_detail/create");
        log.debug(JsonUtils.object2PrettyJson(command));
        OrderCreateResult ret = orderService.createOrderWithDetail(command);
        return new HttpResponse<>(ret);
    }
}
