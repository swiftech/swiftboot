package org.swiftboot.demo.service;

import org.swiftboot.demo.command.OrderWithDetailCreateCommand;
import org.swiftboot.demo.command.OrderWithDetailSaveCommand;
import org.swiftboot.demo.result.OrderCreateResult;
import org.swiftboot.demo.result.OrderSaveResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author swiftech
 */
@Transactional
public interface OrderDetailRelationService {

    /**
     * 创建带有详情的订单
     *
     * @param cmd
     * @return
     */
    OrderCreateResult createOrderWithDetail(OrderWithDetailCreateCommand cmd);


    OrderSaveResult saveOrderWithDetail(OrderWithDetailSaveCommand cmd);

    OrderSaveResult saveOrderWithNewDetail(OrderWithDetailSaveCommand cmd);
}
