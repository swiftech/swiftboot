package org.swiftboot.demo.model.dao;

import org.swiftboot.demo.model.entity.GoodsOrderRelEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品订单关系数据访问接口
 *
 * @author swiftech 2019-01-15
 **/
public interface GoodsOrderRelDao extends PagingAndSortingRepository<GoodsOrderRelEntity, String>, GoodsOrderRelCustomizeDao {

    /**
     * 按照商品ID查询商品订单关系
     *
     * @param demoGoodsId 商品ID
     * @return
     */
    List<GoodsOrderRelEntity> findByDemoGoodsId(String demoGoodsId);


    /**
     * 按照订单ID查询商品订单关系
     *
     * @param demoOrderId 订单ID
     * @return
     */
    List<GoodsOrderRelEntity> findByDemoOrderId(String demoOrderId);



    /**
     * 按照两个外键查询两个关联表字段存在的关系
     *
     * @param demoGoodsId 商品ID
     * @param demoOrderId 订单ID
     * @return
     */
    List<GoodsOrderRelEntity> findByDemoGoodsIdAndDemoOrderId(String demoGoodsId, String demoOrderId);

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
     * @param demoGoodsId 商品ID
     * @param demoOrderId 订单ID
     */
    void deleteByDemoGoodsIdAndDemoOrderId(String demoGoodsId, String demoOrderId);

    /**
     * 统计非逻辑删除的商品订单关系总数
     *
     * @return
     */
    long countByIsDeleteFalse();

    /**
     * 按照商品ID统计商品订单关系总数
     *
     * @param demoGoodsId 商品ID
     * @return
     */
    long countByDemoGoodsId(String demoGoodsId);

    /**
     * 按照商品ID统计非逻辑删除的商品订单关系总数
     *
     * @param demoGoodsId 商品ID
     * @return
     */
    long countByIsDeleteFalseAndDemoGoodsId(String demoGoodsId);

    /**
     * 按照订单ID统计商品订单关系总数
     *
     * @param demoOrderId 订单ID
     * @return
     */
    long countByDemoOrderId(String demoOrderId);

    /**
     * 按照订单ID统计非逻辑删除的商品订单关系总数
     *
     * @param demoOrderId 订单ID
     * @return
     */
    long countByIsDeleteFalseAndDemoOrderId(String demoOrderId);

}
