package org.swiftboot.demo.service.impl;

import org.swiftboot.demo.repository.GoodsRepository;
import org.swiftboot.demo.request.GoodsDetailCreateRequest;
import org.swiftboot.demo.request.GoodsDetailSaveRequest;
import org.swiftboot.demo.repository.GoodsDetailRepository;
import org.swiftboot.demo.model.GoodsDetailEntity;
import org.swiftboot.demo.model.GoodsEntity;
import org.swiftboot.demo.dto.GoodsDetailCreateResult;
import org.swiftboot.demo.dto.GoodsDetailListResult;
import org.swiftboot.demo.dto.GoodsDetailResult;
import org.swiftboot.demo.dto.GoodsDetailSaveResult;
import org.swiftboot.demo.service.GoodsDetailService;
import org.swiftboot.web.dto.PopulatableDto;
import org.swiftboot.web.request.IdListRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 商品详情服务接口实现
 *
 * @author swiftech 2019-04-07
 **/
@Service
public class GoodsDetailServiceImpl implements GoodsDetailService {

    private static final Logger log = LoggerFactory.getLogger(GoodsDetailServiceImpl.class);

    @Resource
    private GoodsRepository goodsRepository;

    @Resource
    private GoodsDetailRepository goodsDetailRepository;


    /**
     * 创建商品详情
     *
     * @param request
     * @return
     */
    @Override
    public GoodsDetailCreateResult createGoodsDetail(GoodsDetailCreateRequest request) {
        GoodsDetailEntity p = request.createEntity();
        GoodsDetailEntity saved = goodsDetailRepository.save(p);
        Optional<GoodsEntity> optGoods = goodsRepository.findById(request.getGoodsId());
        if (optGoods.isPresent()){
            optGoods.get().setGoodsDetail(p);
            goodsRepository.save(optGoods.get());
        }
        log.debug("创建商品详情: " + saved.getId());
        return new GoodsDetailCreateResult(saved.getId());
    }

    /**
     * 保存对商品详情的修改
     *
     * @param request
     * @return
     */
    @Override
    public GoodsDetailSaveResult saveGoodsDetail(GoodsDetailSaveRequest request) {
        GoodsDetailSaveResult ret = new GoodsDetailSaveResult();
        Optional<GoodsDetailEntity> optEntity = goodsDetailRepository.findById(request.getId());
        if (optEntity.isPresent()) {
            GoodsDetailEntity p = optEntity.get();
            p = request.populateEntity(p);
            GoodsDetailEntity saved = goodsDetailRepository.save(p);
            ret.setGoodsDetailId(saved.getId());
        }
        return ret;
    }

    /**
     * 逻辑删除商品详情
     *
     * @param goodsDetailId
     */
    @Override
    public void deleteGoodsDetail(String goodsDetailId) {
        Optional<GoodsDetailEntity> optEntity = goodsDetailRepository.findById(goodsDetailId);
        if (optEntity.isPresent()) {
            GoodsDetailEntity p = optEntity.get();
            p.setIsDelete(true);
            goodsDetailRepository.save(p);
        }
    }

    /**
     * 批量逻辑删除商品详情
     *
     * @param request
     */
    @Override
    public void deleteGoodsDetailList(IdListRequest request) {
        List<GoodsDetailEntity> entities = goodsDetailRepository.findAllByIdIn(request.getIds());
        for (GoodsDetailEntity entity : entities) {
            entity.setIsDelete(true);
            goodsDetailRepository.save(entity);
            // TODO 处理关联表的数据删除
        }
    }


    /**
     * 永久删除商品详情
     *
     * @param goodsDetailId
     */
    @Override
    public void purgeGoodsDetail(String goodsDetailId) {
        if (StringUtils.isNotBlank(goodsDetailId)) {
            goodsDetailRepository.deleteById(goodsDetailId);
        }
        else {
            throw new RuntimeException("");
        }
    }

    /**
     * 批量永久删除商品详情
     *
     * @param request
     */
    @Override
    public void purgeGoodsDetailList(IdListRequest request) {
        List<GoodsDetailEntity> entities = goodsDetailRepository.findAllByIdIn(request.getIds());
        for (GoodsDetailEntity entity : entities) {
            goodsDetailRepository.deleteById(entity.getId());
            // TODO 处理关联表的数据删除
        }
    }


    /**
     * 查询商品详情
     *
     * @param goodsDetailId
     * @return
     */
    @Override
    public GoodsDetailResult queryGoodsDetail(String goodsDetailId) {
        GoodsDetailResult ret = null;
        Optional<GoodsDetailEntity> optEntity = goodsDetailRepository.findById(goodsDetailId);
        if (optEntity.isPresent()) {
            log.debug(optEntity.get().getId());
            ret = PopulatableDto.createDto(GoodsDetailResult.class, optEntity.get());
        }
        else {
            log.debug("没有查询到商品详情, ID: " + goodsDetailId);
        }
        return ret;
    }

    /**
     * 查询所有商品详情
     *
     * @return
     */
    @Override
    public GoodsDetailListResult queryGoodsDetailList() {
        GoodsDetailListResult ret = new GoodsDetailListResult();
        Iterable<GoodsDetailEntity> all = goodsDetailRepository.findAll();
        if (all != null) {
            ret.populateByEntities(all);
            ret.setTotal(goodsDetailRepository.count());
        }
        else {
            log.debug("没有查询到商品详情");
        }
        return ret;
    }

    /**
     * 分页查询商品详情
     *
     * @param page 页数，从0开始
     * @param pageSize 页大小
     * @return
     */
    @Override
    public GoodsDetailListResult queryGoodsDetailList(int page, int pageSize) {
        GoodsDetailListResult ret = new GoodsDetailListResult();
        Page<GoodsDetailEntity> allPagination = goodsDetailRepository.findAll(PageRequest.of(page, pageSize));
        if (allPagination != null) {
            ret.populateByEntities(allPagination);
            ret.setTotal(goodsDetailRepository.count());
        }
        else {
            log.debug("没有查到商品详情");
        }
        return ret;
    }
}