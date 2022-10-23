package org.swiftboot.demo.model.dao;

import org.swiftboot.demo.model.entity.GoodsDetailEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * 商品详情数据访问接口
 *
 * @author swiftech 2019-04-07
 **/
public interface GoodsDetailDao extends PagingAndSortingRepository<GoodsDetailEntity, String>, GoodsDetailCustomizeDao {

    /**
     * 按照折扣查询商品详情
     *
     * @param discount 折扣
     * @return
     */
    List<GoodsDetailEntity> findByDiscount(Double discount);

    /**
     * 按照折扣查询未逻辑删除的商品详情
     *
     * @param discount 折扣
     * @return
     */
    List<GoodsDetailEntity> findByIsDeleteFalseAndDiscount(Double discount);

    /**
     * 批量按照ID查询商品详情
     *
     * @param ids ID列表
     * @return
     */
    List<GoodsDetailEntity> findAllByIdIn(List<String> ids);

    /**
     * 查询所有非逻辑删除的商品详情
     *
     * @return
     */
    List<GoodsDetailEntity> findAllByIsDeleteFalse();

    /**
     * 统计非逻辑删除的商品详情总数
     *
     * @return
     */
    long countByIsDeleteFalse();

}
