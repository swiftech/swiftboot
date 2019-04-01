package org.swiftboot.demo.model.dao;

import org.swiftboot.demo.model.entity.OrderEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单数据访问接口
 *
 * @author swiftech 2019-01-15
 **/
public interface OrderDao extends PagingAndSortingRepository<OrderEntity, String>, OrderCustomizeDao {

    /**
     * 按照发货地址查询订单
     *
     * @param address 发货地址
     * @return
     */
    List<OrderEntity> findByAddress(String address);


    /**
     * 按照商品总数查询订单
     *
     * @param totalCount 商品总数
     * @return
     */
    List<OrderEntity> findByTotalCount(Integer totalCount);


    /**
     * 按照订单编号查询订单
     *
     * @param orderCode 订单编号
     * @return
     */
    List<OrderEntity> findByOrderCode(String orderCode);


    /**
     * 按照订单描述查询订单
     *
     * @param description 订单描述
     * @return
     */
    List<OrderEntity> findByDescription(String description);




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
