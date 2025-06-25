package org.swiftboot.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.swiftboot.demo.model.OrderDetailEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * 订单明细数据访问接口
 *
 * @author swiftech 2019-04-07
 **/
public interface OrderDetailRepository extends PagingAndSortingRepository<OrderDetailEntity, String>, CrudRepository<OrderDetailEntity, String> {

    /**
     * 按照订单ID查询订单明细
     *
     * @param orderId 订单ID
     * @return
     */
    List<OrderDetailEntity> findByOrderId(String orderId);

    /**
     * 按照订单ID查询未逻辑删除的订单明细
     *
     * @param orderId 订单ID
     * @return
     */
    List<OrderDetailEntity> findByIsDeleteFalseAndOrderId(String orderId);


    /**
     * 批量按照ID查询订单明细
     *
     * @param ids ID列表
     * @return
     */
    List<OrderDetailEntity> findAllByIdIn(List<String> ids);

    /**
     * 查询所有非逻辑删除的订单明细
     *
     * @return
     */
    List<OrderDetailEntity> findAllByIsDeleteFalse();


    /**
     * 统计非逻辑删除的订单明细总数
     *
     * @return
     */
    long countByIsDeleteFalse();

    /**
     * 按照订单ID统计订单明细总数
     *
     * @param orderId 订单ID
     * @return
     */
    long countByOrderId(String orderId);

    /**
     * 按照订单ID统计非逻辑删除的订单明细总数
     *
     * @param orderId 订单ID
     * @return
     */
    long countByIsDeleteFalseAndOrderId(String orderId);

}
