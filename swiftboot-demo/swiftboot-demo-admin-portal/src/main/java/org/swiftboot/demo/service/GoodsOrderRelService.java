package org.swiftboot.demo.service;

import org.swiftboot.demo.request.GoodsOrderRelCreateCommand;
import org.swiftboot.demo.request.GoodsOrderRelDelPurgeCommand;
import org.swiftboot.demo.request.GoodsOrderRelSaveCommand;
import org.swiftboot.demo.result.GoodsOrderRelCreateResult;
import org.swiftboot.demo.result.GoodsOrderRelListResult;
import org.swiftboot.demo.result.GoodsOrderRelResult;
import org.swiftboot.demo.result.GoodsOrderRelSaveResult;
import org.swiftboot.web.request.IdListCommand;
import org.springframework.transaction.annotation.Transactional;

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
     * @param cmd
     * @return
     */
    GoodsOrderRelCreateResult createGoodsOrderRel(GoodsOrderRelCreateCommand cmd);

    /**
     * 保存对商品订单关系的修改
     *
     * @param cmd
     * @return
     */
    GoodsOrderRelSaveResult saveGoodsOrderRel(GoodsOrderRelSaveCommand cmd);

    /**
     * 逻辑删除商品订单关系
     *
     * @param goodsOrderRelId
     */
    void deleteGoodsOrderRel(String goodsOrderRelId);

    /**
     * 批量逻辑删除商品订单关系
     *
     * @param cmd
     */
    void deleteGoodsOrderRelList(IdListCommand cmd);

    /**
     * 逻辑删除商品订单关系
     *
     * @param cmd
     */
    void deleteGoodsOrderRel(GoodsOrderRelDelPurgeCommand cmd);

    /**
     * 永久删除商品订单关系
     *
     * @param goodsOrderRelId
     */
    void purgeGoodsOrderRel(String goodsOrderRelId);

    /**
     * 批量永久删除商品订单关系
     *
     * @param cmd
     */
    void purgeGoodsOrderRelList(IdListCommand cmd);

    /**
     * 永久删除商品订单关系
     *
     * @param cmd
     */
    void purgeGoodsOrderRel(GoodsOrderRelDelPurgeCommand cmd);

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