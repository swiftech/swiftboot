package org.swiftboot.demo.service.impl;

import org.swiftboot.demo.command.OrderDetailCreateCommand;
import org.swiftboot.demo.command.OrderDetailSaveCommand;
import org.swiftboot.demo.model.dao.OrderDetailDao;
import org.swiftboot.demo.model.entity.OrderDetailEntity;
import org.swiftboot.demo.result.OrderDetailCreateResult;
import org.swiftboot.demo.result.OrderDetailListResult;
import org.swiftboot.demo.result.OrderDetailResult;
import org.swiftboot.demo.result.OrderDetailSaveResult;
import org.swiftboot.demo.service.OrderDetailService;
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
 * 订单明细服务接口实现
 *
 * @author swiftech 2019-04-07
 **/
@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private static final Logger log = LoggerFactory.getLogger(OrderDetailServiceImpl.class);

    @Resource
    private OrderDetailDao orderDetailDao;

    /**
     * 创建订单明细
     *
     * @param cmd
     * @return
     */
    @Override
    public OrderDetailCreateResult createOrderDetail(OrderDetailCreateCommand cmd) {
        OrderDetailEntity p = cmd.createEntity();
        OrderDetailEntity saved = orderDetailDao.save(p);
        log.debug("创建订单明细: " + saved.getId());
        return new OrderDetailCreateResult(saved.getId());
    }

    /**
     * 保存对订单明细的修改
     *
     * @param cmd
     * @return
     */
    @Override
    public OrderDetailSaveResult saveOrderDetail(OrderDetailSaveCommand cmd) {
        OrderDetailSaveResult ret = new OrderDetailSaveResult();
        Optional<OrderDetailEntity> optEntity = orderDetailDao.findById(cmd.getId());
        if (optEntity.isPresent()) {
            OrderDetailEntity p = optEntity.get();
            p = cmd.populateEntity(p);
            OrderDetailEntity saved = orderDetailDao.save(p);
            ret.setOrderDetailId(saved.getId());
        }
        return ret;
    }

    /**
     * 逻辑删除订单明细
     *
     * @param orderDetailId
     */
    @Override
    public void deleteOrderDetail(String orderDetailId) {
        Optional<OrderDetailEntity> optEntity = orderDetailDao.findById(orderDetailId);
        if (optEntity.isPresent()) {
            OrderDetailEntity p = optEntity.get();
            p.setIsDelete(true);
            orderDetailDao.save(p);
        }
    }

    /**
     * 批量逻辑删除订单明细
     *
     * @param cmd
     */
    @Override
    public void deleteOrderDetailList(IdListCommand cmd) {
        List<OrderDetailEntity> entities = orderDetailDao.findAllByIdIn(cmd.getIds());
        for (OrderDetailEntity entity : entities) {
            entity.setIsDelete(true);
            orderDetailDao.save(entity);
            // TODO 处理关联表的数据删除
        }
    }


    /**
     * 永久删除订单明细
     *
     * @param orderDetailId
     */
    @Override
    public void purgeOrderDetail(String orderDetailId) {
        if (StringUtils.isNotBlank(orderDetailId)) {
            orderDetailDao.deleteById(orderDetailId);
        }
        else {
            throw new RuntimeException("");
        }
    }

    /**
     * 批量永久删除订单明细
     *
     * @param cmd
     */
    @Override
    public void purgeOrderDetailList(IdListCommand cmd) {
        List<OrderDetailEntity> entities = orderDetailDao.findAllByIdIn(cmd.getIds());
        for (OrderDetailEntity entity : entities) {
            orderDetailDao.deleteById(entity.getId());
            // TODO 处理关联表的数据删除
        }
    }


    /**
     * 查询订单明细
     *
     * @param orderDetailId
     * @return
     */
    @Override
    public OrderDetailResult queryOrderDetail(String orderDetailId) {
        OrderDetailResult ret = null;
        Optional<OrderDetailEntity> optEntity = orderDetailDao.findById(orderDetailId);
        if (optEntity.isPresent()) {
            log.debug(optEntity.get().getId());
            ret = OrderDetailResult.createResult(OrderDetailResult.class, optEntity.get());
        }
        else {
            log.debug("没有查询到订单明细, ID: " + orderDetailId);
        }
        return ret;
    }

    /**
     * 查询所有订单明细
     *
     * @return
     */
    @Override
    public OrderDetailListResult queryOrderDetailList() {
        OrderDetailListResult ret = new OrderDetailListResult();
        Iterable<OrderDetailEntity> all = orderDetailDao.findAll();
        if (all != null) {
            ret.populateByEntities(all);
            ret.setTotal(orderDetailDao.count());
        }
        else {
            log.debug("没有查询到订单明细");
        }
        return ret;
    }

    /**
     * 分页查询订单明细
     *
     * @param page 页数，从0开始
     * @param pageSize 页大小
     * @return
     */
    @Override
    public OrderDetailListResult queryOrderDetailList(int page, int pageSize) {
        OrderDetailListResult ret = new OrderDetailListResult();
        Page<OrderDetailEntity> allPagination = orderDetailDao.findAll(PageRequest.of(page, pageSize));
        if (allPagination != null) {
            ret.populateByEntities(allPagination);
            ret.setTotal(orderDetailDao.count());
        }
        else {
            log.debug("没有查到订单明细");
        }
        return ret;
    }
}