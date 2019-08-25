package org.swiftboot.demo.model.dao;

import org.swiftboot.demo.model.entity.GoodsEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品数据访问接口
 *
 * @author swiftech 2019-04-07
 **/
public interface GoodsDao extends PagingAndSortingRepository<GoodsEntity, String>, GoodsCustomizeDao {

    /**
     * 按照商品名称查询商品
     *
     * @param name 商品名称
     * @return
     */
    List<GoodsEntity> findByName(String name);

    /**
     * 按照商品名称查询未逻辑删除的商品
     *
     * @param name 商品名称
     * @return
     */
    List<GoodsEntity> findByIsDeleteFalseAndName(String name);

    /**
     * 按照商品描述查询商品
     *
     * @param description 商品描述
     * @return
     */
    List<GoodsEntity> findByDescription(String description);

    /**
     * 按照商品描述查询未逻辑删除的商品
     *
     * @param description 商品描述
     * @return
     */
    List<GoodsEntity> findByIsDeleteFalseAndDescription(String description);

    /**
     * 按照商品价格查询商品
     *
     * @param price 商品价格
     * @return
     */
    List<GoodsEntity> findByPrice(Double price);

    /**
     * 按照商品价格查询未逻辑删除的商品
     *
     * @param price 商品价格
     * @return
     */
    List<GoodsEntity> findByIsDeleteFalseAndPrice(Double price);


    /**
     * 批量按照ID查询商品
     *
     * @param ids ID列表
     * @return
     */
    List<GoodsEntity> findAllByIdIn(List<String> ids);

    /**
     * 查询所有非逻辑删除的商品
     *
     * @return
     */
    List<GoodsEntity> findAllByIsDeleteFalse();


    /**
     * 统计非逻辑删除的商品总数
     *
     * @return
     */
    long countByIsDeleteFalse();

}
