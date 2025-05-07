package org.swiftboot.demo.service;

import org.swiftboot.demo.request.GoodsDetailCreateRequest;
import org.swiftboot.demo.request.GoodsDetailSaveRequest;
import org.swiftboot.demo.dto.GoodsDetailCreateResult;
import org.swiftboot.demo.dto.GoodsDetailListResult;
import org.swiftboot.demo.dto.GoodsDetailResult;
import org.swiftboot.demo.dto.GoodsDetailSaveResult;
import org.swiftboot.web.request.IdListRequest;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品详情服务接口
 *
 * @author swiftech 2019-04-07
 **/
@Transactional
public interface GoodsDetailService {

    /**
     * 创建商品详情
     *
     * @param cmd
     * @return
     */
    GoodsDetailCreateResult createGoodsDetail(GoodsDetailCreateRequest cmd);

    /**
     * 保存对商品详情的修改
     *
     * @param cmd
     * @return
     */
    GoodsDetailSaveResult saveGoodsDetail(GoodsDetailSaveRequest cmd);

    /**
     * 逻辑删除商品详情
     *
     * @param goodsDetailId
     */
    void deleteGoodsDetail(String goodsDetailId);

    /**
     * 批量逻辑删除商品详情
     *
     * @param cmd
     */
    void deleteGoodsDetailList(IdListRequest request);


    /**
     * 永久删除商品详情
     *
     * @param goodsDetailId
     */
    void purgeGoodsDetail(String goodsDetailId);

    /**
     * 批量永久删除商品详情
     *
     * @param cmd
     */
    void purgeGoodsDetailList(IdListRequest request);


    /**
     * 查询商品详情
     *
     * @param goodsDetailId
     * @return
     */
    GoodsDetailResult queryGoodsDetail(String goodsDetailId);

    /**
     * 查询所有商品详情
     *
     * @return
     */
    GoodsDetailListResult queryGoodsDetailList();

    /**
     * 分页查询商品详情
     *
     * @param page 页数，从0开始
     * @param pageSize 页大小
     * @return
     */
    GoodsDetailListResult queryGoodsDetailList(int page, int pageSize);
}