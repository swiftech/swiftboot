package org.swiftboot.demo.service.impl;

import org.swiftboot.demo.request.GoodsCreateRequest;
import org.swiftboot.demo.request.GoodsSaveRequest;
import org.swiftboot.demo.request.GoodsWithDetailCreateRequest;
import org.swiftboot.demo.repository.GoodsRepository;
import org.swiftboot.demo.model.GoodsEntity;
import org.swiftboot.demo.dto.GoodsCreateResult;
import org.swiftboot.demo.dto.GoodsListResult;
import org.swiftboot.demo.dto.GoodsResult;
import org.swiftboot.demo.dto.GoodsSaveResult;
import org.swiftboot.demo.service.GoodsService;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.swiftboot.web.dto.PopulatableDto;
import org.swiftboot.web.request.IdListRequest;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.response.ResponseCode;

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
    private GoodsRepository goodsRepository;

    /**
     * 创建商品
     *
     * @param request
     * @return
     */
    @Override
    public GoodsCreateResult createGoods(GoodsCreateRequest request) {
        GoodsEntity p = request.createEntity();
        GoodsEntity saved = goodsRepository.save(p);
        log.debug("创建商品: " + saved.getId());
        return new GoodsCreateResult(saved.getId());
    }

    @Override
    public GoodsCreateResult createWithException(GoodsCreateRequest request) {
        GoodsEntity p1 = request.createEntity();
        goodsRepository.save(p1);
        log.debug("创建商品: " + p1.getId());
        if (true) {
            if (RandomUtils.nextBoolean()) {
                throw new ErrMessageException(ResponseCode.CODE_SYS_ERR);
            }
            else {
                throw new ErrMessageException(ResponseCode.CODE_SYS_ERR, "自定义错误消息");
            }
        }
        return new GoodsCreateResult(p1.getId());
    }

    @Override
    public GoodsCreateResult createGoodsWithDetail(GoodsWithDetailCreateRequest request) {
        GoodsEntity entity = request.createEntity();
        GoodsEntity saved = goodsRepository.save(entity);
        log.debug("创建商品: " + saved.getId());
        return new GoodsCreateResult(saved.getId());
    }

    /**
     * 保存对商品的修改
     *
     * @param request
     * @return
     */
    @Override
    public GoodsSaveResult saveGoods(GoodsSaveRequest request) {
        GoodsSaveResult ret = new GoodsSaveResult();
        Optional<GoodsEntity> optEntity = goodsRepository.findById(request.getId());
        if (optEntity.isPresent()) {
            GoodsEntity p = optEntity.get();
            p = request.populateEntity(p);
            GoodsEntity saved = goodsRepository.save(p);
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
        Optional<GoodsEntity> optEntity = goodsRepository.findById(goodsId);
        if (optEntity.isPresent()) {
            GoodsEntity p = optEntity.get();
            log.trace(String.format("Delete goods %s - %s - %s logically", p.getId(), p.getName(), p.getIsDelete()));
            goodsRepository.deleteLogically(p);
//            p.setDelete(true);
//            goodsDao.save(p);
        }
    }

    /**
     * 批量逻辑删除商品
     *
     * @param request
     */
    @Override
    public void deleteGoodsList(IdListRequest request) {
        List<GoodsEntity> entities = goodsRepository.findAllByIdIn(request.getIds());
        for (GoodsEntity entity : entities) {
            entity.setIsDelete(true);
            goodsRepository.save(entity);
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
            goodsRepository.deleteById(goodsId);
        }
        else {
            throw new RuntimeException("");
        }
    }

    /**
     * 批量永久删除商品
     *
     * @param request
     */
    @Override
    public void purgeGoodsList(IdListRequest request) {
        List<GoodsEntity> entities = goodsRepository.findAllByIdIn(request.getIds());
        for (GoodsEntity entity : entities) {
            goodsRepository.deleteById(entity.getId());
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
        Optional<GoodsEntity> optEntity = goodsRepository.findById(goodsId);
        if (optEntity.isPresent()) {
            log.debug(optEntity.get().getId());
            ret = PopulatableDto.createDto(GoodsResult.class, optEntity.get());
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
        Iterable<GoodsEntity> all = goodsRepository.findAll();
        if (all != null) {
            ret.populateByEntities(all);
            ret.setTotal(goodsRepository.count());
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
        Page<GoodsEntity> allPagination = goodsRepository.findAll(PageRequest.of(page, pageSize));
        if (allPagination != null) {
            ret.populateByEntities(allPagination);
            ret.setTotal(goodsRepository.count());
        }
        else {
            log.debug("没有查到商品");
        }
        return ret;
    }
}
