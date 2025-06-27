package org.swiftboot.demo.service;

import org.springframework.transaction.annotation.Transactional;
import org.swiftboot.demo.dto.GoodsCreateResult;
import org.swiftboot.demo.dto.GoodsListResult;
import org.swiftboot.demo.dto.GoodsResult;
import org.swiftboot.demo.dto.GoodsSaveResult;
import org.swiftboot.demo.request.GoodsRequest;
import org.swiftboot.demo.request.GoodsWithDetailRequest;
import org.swiftboot.web.request.IdListRequest;

/**
 * 商品服务接口
 *
 * @author swiftech 2019-04-07
 **/
@Transactional
public interface GoodsService {

    /**
     * 创建商品
     *
     * @param request
     * @return
     */
    GoodsCreateResult createGoods(GoodsRequest request);

    /**
     * 测试事务
     * @param request
     * @return
     */
    GoodsCreateResult createWithException(GoodsRequest request);

    /**
     * 测试 JPA 关联
     * @param request
     * @return
     */
    GoodsCreateResult createGoodsWithDetail(GoodsWithDetailRequest request);

    /**
     * 保存对商品的修改
     *
     * @param request
     * @return
     */
    GoodsSaveResult saveGoods(String id, GoodsRequest request);

    /**
     * 逻辑删除商品
     *
     * @param goodsId
     */
    void deleteGoods(String goodsId);

    /**
     * 批量逻辑删除商品
     *
     * @param request
     */
    void deleteGoodsList(IdListRequest request);


    /**
     * 永久删除商品
     *
     * @param goodsId
     */
    void purgeGoods(String goodsId);

    /**
     * 批量永久删除商品
     *
     * @param request
     */
    void purgeGoodsList(IdListRequest request);


    /**
     * 查询商品
     *
     * @param goodsId
     * @return
     */
    GoodsResult queryGoods(String goodsId);

    /**
     * 查询所有商品
     *
     * @return
     */
    GoodsListResult queryGoodsList();

    /**
     * 分页查询商品
     *
     * @param page 页数，从0开始
     * @param pageSize 页大小
     * @return
     */
    GoodsListResult queryGoodsList(int page, int pageSize);
}