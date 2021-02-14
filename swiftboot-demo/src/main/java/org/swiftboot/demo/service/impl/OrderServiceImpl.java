package org.swiftboot.demo.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.swiftboot.demo.command.OrderCreateCommand;
import org.swiftboot.demo.command.OrderSaveCommand;
import org.swiftboot.demo.command.OrderWithDetailCreateCommand;
import org.swiftboot.demo.command.OrderWithDetailSaveCommand;
import org.swiftboot.demo.model.dao.OrderDao;
import org.swiftboot.demo.model.dao.OrderDetailDao;
import org.swiftboot.demo.model.entity.OrderDetailEntity;
import org.swiftboot.demo.model.entity.OrderEntity;
import org.swiftboot.demo.result.OrderCreateResult;
import org.swiftboot.demo.result.OrderListResult;
import org.swiftboot.demo.result.OrderResult;
import org.swiftboot.demo.result.OrderSaveResult;
import org.swiftboot.demo.service.OrderService;
import org.swiftboot.web.command.IdListCommand;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 订单服务接口实现
 *
 * @author swiftech 2019-04-07
 **/
@Service
public class OrderServiceImpl implements OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderDetailDao orderDetailDao;

    // for test edit detail of order
    private String permanentDetailId = "12345678901234567890123456789012";

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
        log.debug("创建订单: " + saved.getId());
        return new OrderCreateResult(saved.getId());
    }

    /**
     * 创建带有详情的订单
     *
     * @param cmd
     * @return
     */
    @Override
    public OrderCreateResult createOrderWithDetail(OrderWithDetailCreateCommand cmd) {
        OrderEntity entity = cmd.createEntity();
        // Add extra permanent detail ( will be merged if already existed)
        OrderDetailEntity od = new OrderDetailEntity();
        od.setId(permanentDetailId);
        od.setDescription("固定订单明细项");
        od.setOrder(entity);
        entity.getOrderDetails().add(od);
        OrderEntity saved = orderDao.save(entity);
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
        if (optEntity.isPresent()) {
            OrderEntity p = optEntity.get();
            p = cmd.populateEntity(p);
            OrderEntity saved = orderDao.save(p);
            ret.setOrderId(saved.getId());
        }
        return ret;
    }

    /**
     * 编辑订单，在原来明细上再添加新的明细。
     *
     * @param cmd
     * @return
     */
    @Override
    public OrderSaveResult saveOrderWithDetail(OrderWithDetailSaveCommand cmd) {
        OrderSaveResult ret = new OrderSaveResult();
        Optional<OrderEntity> optEntity = orderDao.findById(cmd.getId());
        if (optEntity.isPresent()) {
            OrderEntity p = optEntity.get();
            p = cmd.populateEntity(p);
            for (OrderDetailEntity orderDetail : p.getOrderDetails()) {
                System.out.printf("'%s' - '%s'%n", orderDetail.getId(), orderDetail.getDescription());
            }
            OrderEntity saved = orderDao.save(p);
            ret.setOrderId(saved.getId());
        }
        else {
            throw new RuntimeException("Order Not found: " + cmd.getId());
        }
        return ret;
    }

    /**
     * 编辑订单， 删除原来的明细，添加新的明细
     *
     * @param cmd
     * @return
     */
    public OrderSaveResult saveOrderWithNewDetail(OrderWithDetailSaveCommand cmd) {
        OrderSaveResult ret = new OrderSaveResult();
        Optional<OrderEntity> optEntity = orderDao.findById(cmd.getId());
        if (optEntity.isPresent()) {
            OrderEntity p = optEntity.get();
            p.getOrderDetails().clear(); // remove old details
            p = cmd.populateEntity(p);
            OrderEntity saved = orderDao.save(p);
            ret.setOrderId(saved.getId());
        }
        else {
            throw new RuntimeException("Order Not found: " + cmd.getId());
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
        if (optEntity.isPresent()) {
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
        if (optEntity.isPresent()) {
            log.debug(optEntity.get().getId());
            ret = OrderResult.createResult(OrderResult.class, optEntity.get());
        }
        else {
            log.debug("没有查询到订单, ID: " + orderId);
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
            log.debug("没有查询到订单");
        }
        return ret;
    }

    /**
     * 分页查询订单
     *
     * @param page     页数，从0开始
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
            log.debug("没有查到订单");
        }
        return ret;
    }
}
