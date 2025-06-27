package org.swiftboot.demo.service.impl;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.swiftboot.demo.dto.GoodsOrderRelCreateResult;
import org.swiftboot.demo.dto.GoodsOrderRelListResult;
import org.swiftboot.demo.dto.GoodsOrderRelResult;
import org.swiftboot.demo.dto.GoodsOrderRelSaveResult;
import org.swiftboot.demo.model.GoodsOrderRelEntity;
import org.swiftboot.demo.repository.GoodsOrderRelRepository;
import org.swiftboot.demo.request.GoodsOrderRelDelPurgeRequest;
import org.swiftboot.demo.request.GoodsOrderRelRequest;
import org.swiftboot.demo.service.GoodsOrderRelService;
import org.swiftboot.web.dto.PopulatableDto;
import org.swiftboot.web.request.IdListRequest;

import java.util.List;
import java.util.Optional;

/**
 * 商品订单关系服务接口实现
 *
 * @author swiftech 2019-04-07
 **/
@Service
public class GoodsOrderRelServiceImpl implements GoodsOrderRelService {

    private static final Logger log = LoggerFactory.getLogger(GoodsOrderRelServiceImpl.class);

    @Resource
    private GoodsOrderRelRepository goodsOrderRelRepository;

    /**
     * 创建商品订单关系
     *
     * @param request
     * @return
     */
    @Override
    public GoodsOrderRelCreateResult createGoodsOrderRel(GoodsOrderRelRequest request) {
        GoodsOrderRelEntity p = request.createEntity();
        GoodsOrderRelEntity saved = goodsOrderRelRepository.save(p);
        log.debug("创建商品订单关系: " + saved.getId());
        return new GoodsOrderRelCreateResult(saved.getId());
    }

    /**
     * 保存对商品订单关系的修改
     *
     * @param id
     * @param request
     * @return
     */
    @Override
    public GoodsOrderRelSaveResult saveGoodsOrderRel(String id, GoodsOrderRelRequest request) {
        GoodsOrderRelSaveResult ret = new GoodsOrderRelSaveResult();
        Optional<GoodsOrderRelEntity> optEntity = goodsOrderRelRepository.findById(id);
        if (optEntity.isPresent()) {
            GoodsOrderRelEntity p = optEntity.get();
            p = request.populateEntity(p);
            GoodsOrderRelEntity saved = goodsOrderRelRepository.save(p);
            ret.setGoodsOrderRelId(saved.getId());
        }
        return ret;
    }

    /**
     * 逻辑删除商品订单关系
     *
     * @param goodsOrderRelId
     */
    @Override
    public void deleteGoodsOrderRel(String goodsOrderRelId) {
        Optional<GoodsOrderRelEntity> optEntity = goodsOrderRelRepository.findById(goodsOrderRelId);
        if (optEntity.isPresent()) {
            GoodsOrderRelEntity p = optEntity.get();
            p.setIsDelete(true);
            goodsOrderRelRepository.save(p);
        }
    }

    /**
     * 批量逻辑删除商品订单关系
     *
     * @param request
     */
    @Override
    public void deleteGoodsOrderRelList(IdListRequest request) {
        List<GoodsOrderRelEntity> entities = goodsOrderRelRepository.findAllByIdIn(request.getIds());
        for (GoodsOrderRelEntity entity : entities) {
            entity.setIsDelete(true);
            goodsOrderRelRepository.save(entity);
            // TODO 处理关联表的数据删除
        }
    }

    /**
     * 逻辑删除商品订单关系
     *
     * @param request
     */
    @Override
    public void deleteGoodsOrderRel(GoodsOrderRelDelPurgeRequest request) {
        List<GoodsOrderRelEntity> entities =
        goodsOrderRelRepository.findByGoodsIdAndOrderId(
            request.getGoodsId(), request.getOrderId());
        for (GoodsOrderRelEntity p : entities) {
            p.setIsDelete(true);
            goodsOrderRelRepository.save(p);
        }
    }

    /**
     * 永久删除商品订单关系
     *
     * @param goodsOrderRelId
     */
    @Override
    public void purgeGoodsOrderRel(String goodsOrderRelId) {
        if (StringUtils.isNotBlank(goodsOrderRelId)) {
            goodsOrderRelRepository.deleteById(goodsOrderRelId);
        }
        else {
            throw new RuntimeException("");
        }
    }

    /**
     * 批量永久删除商品订单关系
     *
     * @param request
     */
    @Override
    public void purgeGoodsOrderRelList(IdListRequest request) {
        List<GoodsOrderRelEntity> entities = goodsOrderRelRepository.findAllByIdIn(request.getIds());
        for (GoodsOrderRelEntity entity : entities) {
            goodsOrderRelRepository.deleteById(entity.getId());
            // TODO 处理关联表的数据删除
        }
    }

    /**
     * 永久删除商品订单关系
     *
     * @param request
     */
    @Override
    public void purgeGoodsOrderRel(GoodsOrderRelDelPurgeRequest request) {
        goodsOrderRelRepository.deleteByGoodsIdAndOrderId (
            request.getGoodsId(), request.getOrderId());
    }

    /**
     * 查询商品订单关系
     *
     * @param goodsOrderRelId
     * @return
     */
    @Override
    public GoodsOrderRelResult queryGoodsOrderRel(String goodsOrderRelId) {
        GoodsOrderRelResult ret = null;
        Optional<GoodsOrderRelEntity> optEntity = goodsOrderRelRepository.findById(goodsOrderRelId);
        if (optEntity.isPresent()) {
            log.debug(optEntity.get().getId());
            ret = PopulatableDto.createDto(GoodsOrderRelResult.class, optEntity.get());
        }
        else {
            log.debug("没有查询到商品订单关系, ID: " + goodsOrderRelId);
        }
        return ret;
    }

    /**
     * 查询所有商品订单关系
     *
     * @return
     */
    @Override
    public GoodsOrderRelListResult queryGoodsOrderRelList() {
        GoodsOrderRelListResult ret = new GoodsOrderRelListResult();
        Iterable<GoodsOrderRelEntity> all = goodsOrderRelRepository.findAll();
        if (all != null) {
            ret.populateByEntities(all);
            ret.setTotal(goodsOrderRelRepository.count());
        }
        else {
            log.debug("没有查询到商品订单关系");
        }
        return ret;
    }

    /**
     * 分页查询商品订单关系
     *
     * @param page 页数，从0开始
     * @param pageSize 页大小
     * @return
     */
    @Override
    public GoodsOrderRelListResult queryGoodsOrderRelList(int page, int pageSize) {
        GoodsOrderRelListResult ret = new GoodsOrderRelListResult();
        Page<GoodsOrderRelEntity> allPagination = goodsOrderRelRepository.findAll(PageRequest.of(page, pageSize));
        if (allPagination != null) {
            ret.populateByEntities(allPagination);
            ret.setTotal(goodsOrderRelRepository.count());
        }
        else {
            log.debug("没有查到商品订单关系");
        }
        return ret;
    }
}
