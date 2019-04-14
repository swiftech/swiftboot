package org.swiftboot.demo.service.impl;

import org.swiftboot.demo.controller.command.GoodsOrderRelCreateCommand;
import org.swiftboot.demo.controller.command.GoodsOrderRelDelPurgeCommand;
import org.swiftboot.demo.controller.command.GoodsOrderRelSaveCommand;
import org.swiftboot.demo.model.dao.GoodsOrderRelDao;
import org.swiftboot.demo.model.entity.GoodsOrderRelEntity;
import org.swiftboot.demo.result.GoodsOrderRelCreateResult;
import org.swiftboot.demo.result.GoodsOrderRelListResult;
import org.swiftboot.demo.result.GoodsOrderRelResult;
import org.swiftboot.demo.result.GoodsOrderRelSaveResult;
import org.swiftboot.demo.service.GoodsOrderRelService;
import org.swiftboot.web.command.IdListCommand;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 商品订单关系服务接口实现
 *
 * @author swiftech 2019-04-07
 **/
@Service
public class GoodsOrderRelServiceImpl implements GoodsOrderRelService{

    private Logger log = LoggerFactory.getLogger(GoodsOrderRelServiceImpl.class);

    @Resource
    private GoodsOrderRelDao goodsOrderRelDao;

    /**
     * 创建商品订单关系
     *
     * @param cmd
     * @return
     */
    @Override
    public GoodsOrderRelCreateResult createGoodsOrderRel(GoodsOrderRelCreateCommand cmd) {
        GoodsOrderRelEntity p = cmd.createEntity();
        GoodsOrderRelEntity saved = goodsOrderRelDao.save(p);
        log.debug("保存商品订单关系: " + saved.getId());
        return new GoodsOrderRelCreateResult(saved.getId());
    }

    /**
     * 保存对商品订单关系的修改
     *
     * @param cmd
     * @return
     */
    @Override
    public GoodsOrderRelSaveResult saveGoodsOrderRel(GoodsOrderRelSaveCommand cmd) {
        GoodsOrderRelSaveResult ret = new GoodsOrderRelSaveResult();
        Optional<GoodsOrderRelEntity> optEntity = goodsOrderRelDao.findById(cmd.getId());
        if (optEntity.isPresent()) {
            GoodsOrderRelEntity p = optEntity.get();
            p = cmd.populateEntity(p);
            GoodsOrderRelEntity saved = goodsOrderRelDao.save(p);
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
        Optional<GoodsOrderRelEntity> optEntity = goodsOrderRelDao.findById(goodsOrderRelId);
        if (optEntity.isPresent()) {
            GoodsOrderRelEntity p = optEntity.get();
            p.setDelete(true);
            goodsOrderRelDao.save(p);
        }
    }

    /**
     * 批量逻辑删除商品订单关系
     *
     * @param cmd
     */
    @Override
    public void deleteGoodsOrderRelList(IdListCommand cmd) {
        List<GoodsOrderRelEntity> entities = goodsOrderRelDao.findAllByIdIn(cmd.getIds());
        for (GoodsOrderRelEntity entity : entities) {
            entity.setDelete(true);
            goodsOrderRelDao.save(entity);
            // TODO 处理关联表的数据删除
        }
    }

    /**
     * 逻辑删除商品订单关系
     *
     * @param cmd
     */
    @Override
    public void deleteGoodsOrderRel(GoodsOrderRelDelPurgeCommand cmd) {
        List<GoodsOrderRelEntity> entities =
        goodsOrderRelDao.findByGoodsIdAndOrderId(
            cmd.getGoodsId(), cmd.getOrderId());
        for (GoodsOrderRelEntity p : entities) {
            p.setDelete(true);
            goodsOrderRelDao.save(p);
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
            goodsOrderRelDao.deleteById(goodsOrderRelId);
        }
        else {
            throw new RuntimeException("");
        }
    }

    /**
     * 批量永久删除商品订单关系
     *
     * @param cmd
     */
    @Override
    public void purgeGoodsOrderRelList(IdListCommand cmd) {
        List<GoodsOrderRelEntity> entities = goodsOrderRelDao.findAllByIdIn(cmd.getIds());
        for (GoodsOrderRelEntity entity : entities) {
            goodsOrderRelDao.deleteById(entity.getId());
            // TODO 处理关联表的数据删除
        }
    }

    /**
     * 永久删除商品订单关系
     *
     * @param cmd
     */
    @Override
    public void purgeGoodsOrderRel(GoodsOrderRelDelPurgeCommand cmd) {
        goodsOrderRelDao.deleteByGoodsIdAndOrderId (
            cmd.getGoodsId(), cmd.getOrderId());
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
        Optional<GoodsOrderRelEntity> optEntity = goodsOrderRelDao.findById(goodsOrderRelId);
        if (optEntity.isPresent()) {
            log.debug(optEntity.get().getId());
            ret = GoodsOrderRelResult.createResult(GoodsOrderRelResult.class, optEntity.get());
        }
        else {
            log.debug("没有查询到" + goodsOrderRelId);
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
        Iterable<GoodsOrderRelEntity> all = goodsOrderRelDao.findAll();
        if (all != null) {
            ret.populateByEntities(all);
            ret.setTotal(goodsOrderRelDao.count());
        }
        else {
            log.debug("没有查询到");
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
        Page<GoodsOrderRelEntity> allPagination = goodsOrderRelDao.findAll(PageRequest.of(page, pageSize));
        if (allPagination != null) {
            ret.populateByEntities(allPagination);
            ret.setTotal(goodsOrderRelDao.count());
        }
        else {
            log.debug("没有查到");
        }
        return ret;
    }
}