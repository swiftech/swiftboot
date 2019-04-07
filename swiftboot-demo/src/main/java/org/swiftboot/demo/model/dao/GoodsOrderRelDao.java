package org.swiftboot.demo.model.dao;

import org.swiftboot.demo.model.entity.GoodsOrderRelEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品订单关系数据访问接口
 *
 * @author swiftech 2019-04-07
 **/
public interface GoodsOrderRelDao extends PagingAndSortingRepository<GoodsOrderRelEntity, String>, GoodsOrderRelCustomizeDao {

    /**
     * 按照商品ID查询商品订单关系
     *
     * @param goodsId 商品ID
     * @return
     */
    List<GoodsOrderRelEntity> findByGoodsId(String goodsId);

    /**
     * 按照商品ID查询未逻辑删除的商品订单关系
     *
     * @param goodsId 商品ID
     * @return
     */
    List<GoodsOrderRelEntity> findByIsDeleteFalseAndGoodsId(String goodsId);

    /**
     * 按照订单ID查询商品订单关系
     *
     * @param orderId 订单ID
     * @return
     */
    List<GoodsOrderRelEntity> findByOrderId(String orderId);

    /**
     * 按照订单ID查询未逻辑删除的商品订单关系
     *
     * @param orderId 订单ID
     * @return
     */
    List<GoodsOrderRelEntity> findByIsDeleteFalseAndOrderId(String orderId);


    /**
     * 按照两个外键查询两个关联表字段存在的关系
     *
     * @param goodsId 商品ID
     * @param orderId 订单ID
     * @return
     */
    List<GoodsOrderRelEntity> findByGoodsIdAndOrderId(String goodsId, String orderId);

    /**
     * 批量按照ID查询商品订单关系
     *
     * @param ids ID列表
     * @return
     */
    List<GoodsOrderRelEntity> findAllByIdIn(List<String> ids);

    /**
     * 查询所有非逻辑删除的商品订单关系
     *
     * @return
     */
    List<GoodsOrderRelEntity> findAllByIsDeleteFalse();

    /**
     * 删除两个关联表字段存在的关系
     *
     * @param goodsId 商品ID
     * @param orderId 订单ID
     */
    void deleteByGoodsIdAndOrderId(String goodsId, String orderId);

    /**
     * 统计非逻辑删除的商品订单关系总数
     *
     * @return
     */
    long countByIsDeleteFalse();

    /**
     * 按照商品ID统计商品订单关系总数
     *
     * @param goodsId 商品ID
     * @return
     */
    long countByGoodsId(String goodsId);

    /**
     * 按照商品ID统计非逻辑删除的商品订单关系总数
     *
     * @param goodsId 商品ID
     * @return
     */
    long countByIsDeleteFalseAndGoodsId(String goodsId);

    /**
     * 按照订单ID统计商品订单关系总数
     *
     * @param orderId 订单ID
     * @return
     */
    long countByOrderId(String orderId);

    /**
     * 按照订单ID统计非逻辑删除的商品订单关系总数
     *
     * @param orderId 订单ID
     * @return
     */
    long countByIsDeleteFalseAndOrderId(String orderId);

}
