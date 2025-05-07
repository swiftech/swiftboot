package org.swiftboot.demo.service;

import org.swiftboot.demo.request.OrderWithDetailCreateRequest;
import org.swiftboot.demo.request.OrderWithDetailSaveRequest;
import org.swiftboot.demo.dto.OrderCreateResult;
import org.swiftboot.demo.dto.OrderSaveResult;
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
    OrderCreateResult createOrderWithDetail(OrderWithDetailCreateRequest cmd);


    OrderSaveResult saveOrderWithDetail(OrderWithDetailSaveRequest cmd);

    OrderSaveResult saveOrderWithNewDetail(OrderWithDetailSaveRequest cmd);
}
