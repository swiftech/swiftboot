package org.swiftboot.demo.service;

import org.swiftboot.demo.controller.command.GoodsCreateCommand;
import org.swiftboot.demo.controller.command.GoodsSaveCommand;
import org.swiftboot.demo.result.GoodsCreateResult;
import org.swiftboot.demo.result.GoodsListResult;
import org.swiftboot.demo.result.GoodsResult;
import org.swiftboot.demo.result.GoodsSaveResult;
import org.swiftboot.web.command.IdListCommand;
import org.springframework.transaction.annotation.Transactional;

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
     * @param cmd
     * @return
     */
    GoodsCreateResult createGoods(GoodsCreateCommand cmd);

    /**
     * 保存对商品的修改
     *
     * @param cmd
     * @return
     */
    GoodsSaveResult saveGoods(GoodsSaveCommand cmd);

    /**
     * 逻辑删除商品
     *
     * @param goodsId
     */
    void deleteGoods(String goodsId);

    /**
     * 批量逻辑删除商品
     *
     * @param cmd
     */
    void deleteGoodsList(IdListCommand cmd);


    /**
     * 永久删除商品
     *
     * @param goodsId
     */
    void purgeGoods(String goodsId);

    /**
     * 批量永久删除商品
     *
     * @param cmd
     */
    void purgeGoodsList(IdListCommand cmd);


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