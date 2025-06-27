package org.swiftboot.demo.service;

import org.springframework.transaction.annotation.Transactional;
import org.swiftboot.demo.dto.OrderCreateResult;
import org.swiftboot.demo.dto.OrderListResult;
import org.swiftboot.demo.dto.OrderResult;
import org.swiftboot.demo.dto.OrderSaveResult;
import org.swiftboot.demo.request.OrderRequest;
import org.swiftboot.web.request.IdListRequest;

/**
 * 订单服务接口
 *
 * @author swiftech 2019-04-07
 **/
@Transactional
public interface OrderService {

    /**
     * 创建订单
     *
     * @param request
     * @return
     */
    OrderCreateResult createOrder(OrderRequest request);

    /**
     * 保存对订单的修改
     *
     * @param id
     * @param request
     * @return
     */
    OrderSaveResult saveOrder(String id, OrderRequest request);


    /**
     * 逻辑删除订单
     *
     * @param orderId
     */
    void deleteOrder(String orderId);

    /**
     * 批量逻辑删除订单
     *
     * @param request
     */
    void deleteOrderList(IdListRequest request);


    /**
     * 永久删除订单
     *
     * @param orderId
     */
    void purgeOrder(String orderId);

    /**
     * 批量永久删除订单
     *
     * @param request
     */
    void purgeOrderList(IdListRequest request);


    /**
     * 查询订单
     *
     * @param orderId
     * @return
     */
    OrderResult queryOrder(String orderId);

    /**
     * 查询所有订单
     * @param userId
     * @return
     */
    OrderListResult queryOrderList(String userId);

    /**
     * 分页查询订单
     *
     * @param page 页数，从0开始
     * @param pageSize 页大小
     * @return
     */
    OrderListResult queryOrderList(int page, int pageSize);
}