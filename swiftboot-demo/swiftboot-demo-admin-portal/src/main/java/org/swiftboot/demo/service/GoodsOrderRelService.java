package org.swiftboot.demo.service;

import org.springframework.transaction.annotation.Transactional;
import org.swiftboot.demo.dto.GoodsOrderRelCreateResult;
import org.swiftboot.demo.dto.GoodsOrderRelListResult;
import org.swiftboot.demo.dto.GoodsOrderRelResult;
import org.swiftboot.demo.dto.GoodsOrderRelSaveResult;
import org.swiftboot.demo.request.GoodsOrderRelDelPurgeRequest;
import org.swiftboot.demo.request.GoodsOrderRelRequest;
import org.swiftboot.web.request.IdListRequest;

/**
 * 商品订单关系服务接口
 *
 * @author swiftech 2019-04-07
 **/
@Transactional
public interface GoodsOrderRelService {

    /**
     * 创建商品订单关系
     *
     * @param request
     * @return
     */
    GoodsOrderRelCreateResult createGoodsOrderRel(GoodsOrderRelRequest request);

    /**
     * 保存对商品订单关系的修改
     *
     * @param id
     * @param request
     * @return
     */
    GoodsOrderRelSaveResult saveGoodsOrderRel(String id, GoodsOrderRelRequest request);

    /**
     * 逻辑删除商品订单关系
     *
     * @param goodsOrderRelId
     */
    void deleteGoodsOrderRel(String goodsOrderRelId);

    /**
     * 批量逻辑删除商品订单关系
     *
     * @param request
     */
    void deleteGoodsOrderRelList(IdListRequest request);

    /**
     * 逻辑删除商品订单关系
     *
     * @param request
     */
    void deleteGoodsOrderRel(GoodsOrderRelDelPurgeRequest request);

    /**
     * 永久删除商品订单关系
     *
     * @param goodsOrderRelId
     */
    void purgeGoodsOrderRel(String goodsOrderRelId);

    /**
     * 批量永久删除商品订单关系
     *
     * @param request
     */
    void purgeGoodsOrderRelList(IdListRequest request);

    /**
     * 永久删除商品订单关系
     *
     * @param request
     */
    void purgeGoodsOrderRel(GoodsOrderRelDelPurgeRequest request);

    /**
     * 查询商品订单关系
     *
     * @param goodsOrderRelId
     * @return
     */
    GoodsOrderRelResult queryGoodsOrderRel(String goodsOrderRelId);

    /**
     * 查询所有商品订单关系
     *
     * @return
     */
    GoodsOrderRelListResult queryGoodsOrderRelList();

    /**
     * 分页查询商品订单关系
     *
     * @param page 页数，从0开始
     * @param pageSize 页大小
     * @return
     */
    GoodsOrderRelListResult queryGoodsOrderRelList(int page, int pageSize);
}