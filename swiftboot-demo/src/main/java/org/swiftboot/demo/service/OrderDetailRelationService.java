package org.swiftboot.demo.service;

import org.springframework.transaction.annotation.Transactional;
import org.swiftboot.demo.command.OrderWithDetailCreateCommand;
import org.swiftboot.demo.command.OrderWithDetailSaveCommand;
import org.swiftboot.demo.result.OrderCreateResult;
import org.swiftboot.demo.result.OrderSaveResult;

/**
 * @author allen
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
