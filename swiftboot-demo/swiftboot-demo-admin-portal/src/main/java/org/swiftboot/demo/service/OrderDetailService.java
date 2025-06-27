package org.swiftboot.demo.service;

import org.springframework.transaction.annotation.Transactional;
import org.swiftboot.demo.dto.OrderDetailCreateResult;
import org.swiftboot.demo.dto.OrderDetailListResult;
import org.swiftboot.demo.dto.OrderDetailResult;
import org.swiftboot.demo.dto.OrderDetailSaveResult;
import org.swiftboot.demo.request.OrderDetailRequest;
import org.swiftboot.web.request.IdListRequest;

/**
 * 订单明细服务接口
 *
 * @author swiftech 2019-04-07
 **/
@Transactional
public interface OrderDetailService {

    /**
     * 创建订单明细
     *
     * @param request
     * @return
     */
    OrderDetailCreateResult createOrderDetail(OrderDetailRequest request);

    /**
     * 保存对订单明细的修改
     *
     * @param id
     * @param request
     * @return
     */
    OrderDetailSaveResult saveOrderDetail(String id, OrderDetailRequest request);

    /**
     * 逻辑删除订单明细
     *
     * @param orderDetailId
     */
    void deleteOrderDetail(String orderDetailId);

    /**
     * 批量逻辑删除订单明细
     *
     * @param request
     */
    void deleteOrderDetailList(IdListRequest request);


    /**
     * 永久删除订单明细
     *
     * @param orderDetailId
     */
    void purgeOrderDetail(String orderDetailId);

    /**
     * 批量永久删除订单明细
     *
     * @param request
     */
    void purgeOrderDetailList(IdListRequest request);


    /**
     * 查询订单明细
     *
     * @param orderDetailId
     * @return
     */
    OrderDetailResult queryOrderDetail(String orderDetailId);

    /**
     * 查询所有订单明细
     *
     * @return
     */
    OrderDetailListResult queryOrderDetailList();

    /**
     * 分页查询订单明细
     *
     * @param page 页数，从0开始
     * @param pageSize 页大小
     * @return
     */
    OrderDetailListResult queryOrderDetailList(int page, int pageSize);
}