package org.swiftboot.demo.model.dao;

import org.swiftboot.demo.model.entity.GoodsEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品数据访问接口
 *
 * @author swiftech 2019-01-15
 **/
public interface GoodsDao extends PagingAndSortingRepository<GoodsEntity, String>, GoodsCustomizeDao {

    /**
     * 按照商品价格查询商品
     *
     * @param price 商品价格
     * @return
     */
    List<GoodsEntity> findByPrice(Double price);


    /**
     * 按照商品描述查询商品
     *
     * @param description 商品描述
     * @return
     */
    List<GoodsEntity> findByDescription(String description);


    /**
     * 按照商品名称查询商品
     *
     * @param name 商品名称
     * @return
     */
    List<GoodsEntity> findByName(String name);




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
