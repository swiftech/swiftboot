package org.swiftboot.demo.service.impl;

import org.swiftboot.demo.request.GoodsCreateCommand;
import org.swiftboot.demo.request.GoodsSaveCommand;
import org.swiftboot.demo.request.GoodsWithDetailCreateCommand;
import org.swiftboot.demo.model.dao.GoodsDao;
import org.swiftboot.demo.model.entity.GoodsEntity;
import org.swiftboot.demo.result.GoodsCreateResult;
import org.swiftboot.demo.result.GoodsListResult;
import org.swiftboot.demo.result.GoodsResult;
import org.swiftboot.demo.result.GoodsSaveResult;
import org.swiftboot.demo.service.GoodsService;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.swiftboot.web.request.IdListCommand;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.exception.ErrorCodeSupport;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 商品服务接口实现
 *
 * @author swiftech 2019-04-07
 **/
@Service
public class GoodsServiceImpl implements GoodsService {

    private static final Logger log = LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Resource
    private GoodsDao goodsDao;

    /**
     * 创建商品
     *
     * @param cmd
     * @return
     */
    @Override
    public GoodsCreateResult createGoods(GoodsCreateCommand cmd) {
        GoodsEntity p = cmd.createEntity();
        GoodsEntity saved = goodsDao.save(p);
        log.debug("创建商品: " + saved.getId());
        return new GoodsCreateResult(saved.getId());
    }

    @Override
    public GoodsCreateResult createWithException(GoodsCreateCommand cmd) {
        GoodsEntity p1 = cmd.createEntity();
        goodsDao.save(p1);
        log.debug("创建商品: " + p1.getId());
        if (true) {
            if (RandomUtils.nextBoolean()) {
                throw new ErrMessageException(ErrorCodeSupport.CODE_SYS_ERR);
            }
            else {
                throw new ErrMessageException(ErrorCodeSupport.CODE_SYS_ERR, "自定义错误消息");
            }
        }
        return new GoodsCreateResult(p1.getId());
    }

    @Override
    public GoodsCreateResult createGoodsWithDetail(GoodsWithDetailCreateCommand cmd) {
        GoodsEntity entity = cmd.createEntity();
        GoodsEntity saved = goodsDao.save(entity);
        log.debug("创建商品: " + saved.getId());
        return new GoodsCreateResult(saved.getId());
    }

    /**
     * 保存对商品的修改
     *
     * @param cmd
     * @return
     */
    @Override
    public GoodsSaveResult saveGoods(GoodsSaveCommand cmd) {
        GoodsSaveResult ret = new GoodsSaveResult();
        Optional<GoodsEntity> optEntity = goodsDao.findById(cmd.getId());
        if (optEntity.isPresent()) {
            GoodsEntity p = optEntity.get();
            p = cmd.populateEntity(p);
            GoodsEntity saved = goodsDao.save(p);
            ret.setGoodsId(saved.getId());
        }
        return ret;
    }

    /**
     * 逻辑删除商品
     *
     * @param goodsId
     */
    @Override
    public void deleteGoods(String goodsId) {
//        goodsDao.deleteLogically(goodsId);
        Optional<GoodsEntity> optEntity = goodsDao.findById(goodsId);
        if (optEntity.isPresent()) {
            GoodsEntity p = optEntity.get();
            log.trace(String.format("Delete goods %s - %s - %s logically", p.getId(), p.getName(), p.getIsDelete()));
            goodsDao.deleteLogically(p);
//            p.setDelete(true);
//            goodsDao.save(p);
        }
    }

    /**
     * 批量逻辑删除商品
     *
     * @param cmd
     */
    @Override
    public void deleteGoodsList(IdListCommand cmd) {
        List<GoodsEntity> entities = goodsDao.findAllByIdIn(cmd.getIds());
        for (GoodsEntity entity : entities) {
            entity.setIsDelete(true);
            goodsDao.save(entity);
            // TODO 处理关联表的数据删除
        }
    }


    /**
     * 永久删除商品
     *
     * @param goodsId
     */
    @Override
    public void purgeGoods(String goodsId) {
        if (StringUtils.isNotBlank(goodsId)) {
            goodsDao.deleteById(goodsId);
        }
        else {
            throw new RuntimeException("");
        }
    }

    /**
     * 批量永久删除商品
     *
     * @param cmd
     */
    @Override
    public void purgeGoodsList(IdListCommand cmd) {
        List<GoodsEntity> entities = goodsDao.findAllByIdIn(cmd.getIds());
        for (GoodsEntity entity : entities) {
            goodsDao.deleteById(entity.getId());
            // TODO 处理关联表的数据删除
        }
    }


    /**
     * 查询商品
     *
     * @param goodsId
     * @return
     */
    @Override
    public GoodsResult queryGoods(String goodsId) {
        GoodsResult ret = null;
        Optional<GoodsEntity> optEntity = goodsDao.findById(goodsId);
        if (optEntity.isPresent()) {
            log.debug(optEntity.get().getId());
            ret = GoodsResult.createResult(GoodsResult.class, optEntity.get());
        }
        else {
            log.debug("没有查询到商品, ID: " + goodsId);
        }
        return ret;
    }

    /**
     * 查询所有商品
     *
     * @return
     */
    @Override
    public GoodsListResult queryGoodsList() {
        GoodsListResult ret = new GoodsListResult();
        Iterable<GoodsEntity> all = goodsDao.findAll();
        if (all != null) {
            ret.populateByEntities(all);
            ret.setTotal(goodsDao.count());
        }
        else {
            log.debug("没有查询到商品");
        }
        return ret;
    }

    /**
     * 分页查询商品
     *
     * @param page     页数，从0开始
     * @param pageSize 页大小
     * @return
     */
    @Override
    public GoodsListResult queryGoodsList(int page, int pageSize) {
        GoodsListResult ret = new GoodsListResult();
        Page<GoodsEntity> allPagination = goodsDao.findAll(PageRequest.of(page, pageSize));
        if (allPagination != null) {
            ret.populateByEntities(allPagination);
            ret.setTotal(goodsDao.count());
        }
        else {
            log.debug("没有查到商品");
        }
        return ret;
    }
}
