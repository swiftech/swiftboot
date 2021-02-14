package org.swiftboot.demo.service;

import org.swiftboot.demo.command.OrderCreateCommand;
import org.swiftboot.demo.command.OrderSaveCommand;
import org.swiftboot.demo.command.OrderWithDetailCreateCommand;
import org.swiftboot.demo.command.OrderWithDetailSaveCommand;
import org.swiftboot.demo.result.OrderCreateResult;
import org.swiftboot.demo.result.OrderListResult;
import org.swiftboot.demo.result.OrderResult;
import org.swiftboot.demo.result.OrderSaveResult;
import org.swiftboot.web.command.IdListCommand;
import org.springframework.transaction.annotation.Transactional;

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
     * @param cmd
     * @return
     */
    OrderCreateResult createOrder(OrderCreateCommand cmd);

    /**
     * 创建带有详情的订单
     *
     * @param cmd
     * @return
     */
    OrderCreateResult createOrderWithDetail(OrderWithDetailCreateCommand cmd);

    /**
     * 保存对订单的修改
     *
     * @param cmd
     * @return
     */
    OrderSaveResult saveOrder(OrderSaveCommand cmd);

    OrderSaveResult saveOrderWithDetail(OrderWithDetailSaveCommand cmd);

    OrderSaveResult saveOrderWithNewDetail(OrderWithDetailSaveCommand cmd);

    /**
     * 逻辑删除订单
     *
     * @param orderId
     */
    void deleteOrder(String orderId);

    /**
     * 批量逻辑删除订单
     *
     * @param cmd
     */
    void deleteOrderList(IdListCommand cmd);


    /**
     * 永久删除订单
     *
     * @param orderId
     */
    void purgeOrder(String orderId);

    /**
     * 批量永久删除订单
     *
     * @param cmd
     */
    void purgeOrderList(IdListCommand cmd);


    /**
     * 查询订单
     *
     * @param orderId
     * @return
     */
    OrderResult queryOrder(String orderId);

    /**
     * 查询所有订单
     *
     * @return
     */
    OrderListResult queryOrderList();

    /**
     * 分页查询订单
     *
     * @param page 页数，从0开始
     * @param pageSize 页大小
     * @return
     */
    OrderListResult queryOrderList(int page, int pageSize);
}