package org.swiftboot.demo.service;

import org.swiftboot.demo.request.OrderWithDetailRequest;
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
     * @param request
     * @return
     */
    OrderCreateResult createOrderWithDetail(OrderWithDetailRequest request);


    OrderSaveResult saveOrderWithDetail(String id, OrderWithDetailSaveRequest request);

    OrderSaveResult saveOrderWithNewDetail(String id, OrderWithDetailSaveRequest request);
}
