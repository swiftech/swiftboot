package org.swiftboot.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.swiftboot.demo.model.OrderEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * 订单数据访问接口
 *
 * @author swiftech 2019-04-07
 **/
public interface OrderRepository extends PagingAndSortingRepository<OrderEntity, String>, CrudRepository<OrderEntity, String> {

    /**
     * 按照订单编号查询订单
     *
     * @param orderCode 订单编号
     * @return
     */
    List<OrderEntity> findByOrderCode(String orderCode);

    /**
     * 按照订单编号查询未逻辑删除的订单
     *
     * @param orderCode 订单编号
     * @return
     */
    List<OrderEntity> findByIsDeleteFalseAndOrderCode(String orderCode);

    /**
     * 按照订单描述查询订单
     *
     * @param description 订单描述
     * @return
     */
    List<OrderEntity> findByDescription(String description);

    /**
     * 按照订单描述查询未逻辑删除的订单
     *
     * @param description 订单描述
     * @return
     */
    List<OrderEntity> findByIsDeleteFalseAndDescription(String description);

    /**
     * 按照商品总数查询订单
     *
     * @param totalCount 商品总数
     * @return
     */
    List<OrderEntity> findByTotalCount(Integer totalCount);

    /**
     * 按照商品总数查询未逻辑删除的订单
     *
     * @param totalCount 商品总数
     * @return
     */
    List<OrderEntity> findByIsDeleteFalseAndTotalCount(Integer totalCount);

    /**
     * 按照发货地址查询订单
     *
     * @param address 发货地址
     * @return
     */
    List<OrderEntity> findByAddress(String address);

    /**
     * 按照发货地址查询未逻辑删除的订单
     *
     * @param address 发货地址
     * @return
     */
    List<OrderEntity> findByIsDeleteFalseAndAddress(String address);

    /**
     * 按照APP_USER ID查询订单
     *
     * @param userId APP_USER ID
     * @return
     */
    List<OrderEntity> findByUserId(String userId);

    /**
     * 按照APP_USER ID查询未逻辑删除的订单
     *
     * @param userId APP_USER ID
     * @return
     */
    List<OrderEntity> findByIsDeleteFalseAndUserId(String userId);


    /**
     * 批量按照ID查询订单
     *
     * @param ids ID列表
     * @return
     */
    List<OrderEntity> findAllByIdIn(List<String> ids);

    /**
     * 查询所有非逻辑删除的订单
     *
     * @return
     */
    List<OrderEntity> findAllByIsDeleteFalse();


    /**
     * 统计非逻辑删除的订单总数
     *
     * @return
     */
    long countByIsDeleteFalse();

}
