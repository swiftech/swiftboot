package org.swiftboot.demo.service.impl;

import org.swiftboot.demo.controller.command.OrderCreateCommand;
import org.swiftboot.demo.controller.command.OrderSaveCommand;
import org.swiftboot.demo.model.dao.OrderDao;
import org.swiftboot.demo.model.entity.OrderEntity;
import org.swiftboot.demo.result.OrderCreateResult;
import org.swiftboot.demo.result.OrderListResult;
import org.swiftboot.demo.result.OrderResult;
import org.swiftboot.demo.result.OrderSaveResult;
import org.swiftboot.demo.service.OrderService;
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
 * 订单服务接口实现
 *
 * @author swiftech 2019-01-15
 **/
@Service
public class OrderServiceImpl implements OrderService{

    private Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource
    private OrderDao orderDao;

    /**
     * 创建订单
     *
     * @param cmd
     * @return
     */
    @Override
    public OrderCreateResult createOrder(OrderCreateCommand cmd) {
        OrderEntity p = cmd.createEntity();
        OrderEntity saved = orderDao.save(p);
        log.debug("保存订单: " + saved.getId());
        return new OrderCreateResult(saved.getId());
    }

    /**
     * 保存对订单的修改
     *
     * @param cmd
     * @return
     */
    @Override
    public OrderSaveResult saveOrder(OrderSaveCommand cmd) {
        OrderSaveResult ret = new OrderSaveResult();
        Optional<OrderEntity> optEntity = orderDao.findById(cmd.getId());
        if (optEntity != null && optEntity.isPresent()) {
            OrderEntity p = optEntity.get();
            p = cmd.populateEntity(p);
            OrderEntity saved = orderDao.save(p);
            ret.setOrderId(saved.getId());
        }
        return ret;
    }

    /**
     * 逻辑删除订单
     *
     * @param orderId
     */
    @Override
    public void deleteOrder(String orderId) {
        Optional<OrderEntity> optEntity = orderDao.findById(orderId);
        if (optEntity != null && optEntity.isPresent()) {
            OrderEntity p = optEntity.get();
            p.setDelete(true);
            orderDao.save(p);
        }
    }

    /**
     * 批量逻辑删除订单
     *
     * @param cmd
     */
    @Override
    public void deleteOrderList(IdListCommand cmd) {
        List<OrderEntity> entities = orderDao.findAllByIdIn(cmd.getIds());
        for (OrderEntity entity : entities) {
            entity.setDelete(true);
            orderDao.save(entity);
            // TODO 处理关联表的数据删除
        }
    }


    /**
     * 永久删除订单
     *
     * @param orderId
     */
    @Override
    public void purgeOrder(String orderId) {
        if (StringUtils.isNotBlank(orderId)) {
            orderDao.deleteById(orderId);
        }
        else {
            throw new RuntimeException("");
        }
    }

    /**
     * 批量永久删除订单
     *
     * @param cmd
     */
    @Override
    public void purgeOrderList(IdListCommand cmd) {
        List<OrderEntity> entities = orderDao.findAllByIdIn(cmd.getIds());
        for (OrderEntity entity : entities) {
            orderDao.deleteById(entity.getId());
            // TODO 处理关联表的数据删除
        }
    }


    /**
     * 查询订单
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderResult queryOrder(String orderId) {
        OrderResult ret = null;
        Optional<OrderEntity> optEntity = orderDao.findById(orderId);
        if (optEntity != null && optEntity.isPresent()) {
            log.debug(optEntity.get().getId());
            ret = OrderResult.createResult(OrderResult.class, optEntity.get());
        }
        else {
            log.debug("没有查询到" + orderId);
        }
        return ret;
    }

    /**
     * 查询所有订单
     *
     * @return
     */
    @Override
    public OrderListResult queryOrderList() {
        OrderListResult ret = new OrderListResult();
        Iterable<OrderEntity> all = orderDao.findAll();
        if (all != null) {
            ret.populateByEntities(all);
            ret.setTotal(orderDao.count());
        }
        else {
            log.debug("没有查询到");
        }
        return ret;
    }

    /**
     * 分页查询订单
     *
     * @param page 页数，从0开始
     * @param pageSize 页大小
     * @return
     */
    @Override
    public OrderListResult queryOrderList(int page, int pageSize) {
        OrderListResult ret = new OrderListResult();
        Page<OrderEntity> allPagination = orderDao.findAll(PageRequest.of(page, pageSize));
        if (allPagination != null) {
            ret.populateByEntities(allPagination);
            ret.setTotal(orderDao.count());
        }
        else {
            log.debug("没有查到");
        }
        return ret;
    }
}