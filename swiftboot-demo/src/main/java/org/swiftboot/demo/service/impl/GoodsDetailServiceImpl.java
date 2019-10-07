package org.swiftboot.demo.service.impl;

import org.swiftboot.demo.command.GoodsDetailCreateCommand;
import org.swiftboot.demo.command.GoodsDetailSaveCommand;
import org.swiftboot.demo.model.dao.GoodsDao;
import org.swiftboot.demo.model.dao.GoodsDetailDao;
import org.swiftboot.demo.model.entity.GoodsDetailEntity;
import org.swiftboot.demo.model.entity.GoodsEntity;
import org.swiftboot.demo.result.GoodsDetailCreateResult;
import org.swiftboot.demo.result.GoodsDetailListResult;
import org.swiftboot.demo.result.GoodsDetailResult;
import org.swiftboot.demo.result.GoodsDetailSaveResult;
import org.swiftboot.demo.service.GoodsDetailService;
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
 * 商品详情服务接口实现
 *
 * @author swiftech 2019-04-07
 **/
@Service
public class GoodsDetailServiceImpl implements GoodsDetailService {

    private Logger log = LoggerFactory.getLogger(GoodsDetailServiceImpl.class);

    @Resource
    private GoodsDao goodsDao;

    @Resource
    private GoodsDetailDao goodsDetailDao;


    /**
     * 创建商品详情
     *
     * @param cmd
     * @return
     */
    @Override
    public GoodsDetailCreateResult createGoodsDetail(GoodsDetailCreateCommand cmd) {
        GoodsDetailEntity p = cmd.createEntity();
        GoodsDetailEntity saved = goodsDetailDao.save(p);
        Optional<GoodsEntity> optGoods = goodsDao.findById(cmd.getGoodsId());
        if (optGoods.isPresent()){
            optGoods.get().setGoodsDetail(p);
            goodsDao.save(optGoods.get());
        }
        log.debug("创建商品详情: " + saved.getId());
        return new GoodsDetailCreateResult(saved.getId());
    }

    /**
     * 保存对商品详情的修改
     *
     * @param cmd
     * @return
     */
    @Override
    public GoodsDetailSaveResult saveGoodsDetail(GoodsDetailSaveCommand cmd) {
        GoodsDetailSaveResult ret = new GoodsDetailSaveResult();
        Optional<GoodsDetailEntity> optEntity = goodsDetailDao.findById(cmd.getId());
        if (optEntity.isPresent()) {
            GoodsDetailEntity p = optEntity.get();
            p = cmd.populateEntity(p);
            GoodsDetailEntity saved = goodsDetailDao.save(p);
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
        Optional<GoodsDetailEntity> optEntity = goodsDetailDao.findById(goodsDetailId);
        if (optEntity.isPresent()) {
            GoodsDetailEntity p = optEntity.get();
            p.setDelete(true);
            goodsDetailDao.save(p);
        }
    }

    /**
     * 批量逻辑删除商品详情
     *
     * @param cmd
     */
    @Override
    public void deleteGoodsDetailList(IdListCommand cmd) {
        List<GoodsDetailEntity> entities = goodsDetailDao.findAllByIdIn(cmd.getIds());
        for (GoodsDetailEntity entity : entities) {
            entity.setDelete(true);
            goodsDetailDao.save(entity);
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
            goodsDetailDao.deleteById(goodsDetailId);
        }
        else {
            throw new RuntimeException("");
        }
    }

    /**
     * 批量永久删除商品详情
     *
     * @param cmd
     */
    @Override
    public void purgeGoodsDetailList(IdListCommand cmd) {
        List<GoodsDetailEntity> entities = goodsDetailDao.findAllByIdIn(cmd.getIds());
        for (GoodsDetailEntity entity : entities) {
            goodsDetailDao.deleteById(entity.getId());
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
        Optional<GoodsDetailEntity> optEntity = goodsDetailDao.findById(goodsDetailId);
        if (optEntity.isPresent()) {
            log.debug(optEntity.get().getId());
            ret = GoodsDetailResult.createResult(GoodsDetailResult.class, optEntity.get());
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
        Iterable<GoodsDetailEntity> all = goodsDetailDao.findAll();
        if (all != null) {
            ret.populateByEntities(all);
            ret.setTotal(goodsDetailDao.count());
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
        Page<GoodsDetailEntity> allPagination = goodsDetailDao.findAll(PageRequest.of(page, pageSize));
        if (allPagination != null) {
            ret.populateByEntities(allPagination);
            ret.setTotal(goodsDetailDao.count());
        }
        else {
            log.debug("没有查到商品详情");
        }
        return ret;
    }
}