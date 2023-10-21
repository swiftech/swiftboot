package org.swiftboot.demo.service.impl;

import org.swiftboot.demo.model.dao.OrderDetailDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.swiftboot.demo.command.OrderWithDetailCreateCommand;
import org.swiftboot.demo.command.OrderWithDetailSaveCommand;
import org.swiftboot.demo.model.dao.OrderDao;
import org.swiftboot.demo.model.entity.OrderDetailEntity;
import org.swiftboot.demo.model.entity.OrderEntity;
import org.swiftboot.demo.result.OrderCreateResult;
import org.swiftboot.demo.result.OrderSaveResult;
import org.swiftboot.demo.service.OrderDetailRelationService;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.Optional;

/**
 * @author allen
 */
@Service
public class OrderDetailRelationServiceImpl implements OrderDetailRelationService {

    private static final Logger log = LoggerFactory.getLogger(OrderDetailRelationServiceImpl.class);

    // for test edit detail of order
    private final String permanentDetailId = "12345678901234567890123456789012";

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderDetailDao orderDetailDao;

    @Resource
    private EntityManager entityManager;

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
    @Override
    public OrderSaveResult saveOrderWithNewDetail(OrderWithDetailSaveCommand cmd) {
        OrderSaveResult ret = new OrderSaveResult();
        Optional<OrderEntity> optEntity = orderDao.findById(cmd.getId());
        if (optEntity.isPresent()) {
            OrderEntity p = optEntity.get();
            System.out.println(p.getOrderDetails().hashCode());
            p.getOrderDetails().clear(); // remove old details
            p = cmd.populateEntity(p);
            for (OrderDetailEntity orderDetail : p.getOrderDetails()) {
                System.out.printf("'%s' - '%s'%n", orderDetail.getId(), orderDetail.getDescription());
                System.out.println("parent order: " + orderDetail.getOrder());
            }
            System.out.println(p.getOrderDetails().hashCode());
            OrderEntity saved = orderDao.save(p);
            ret.setOrderId(saved.getId());
        }
        else {
            throw new RuntimeException("Order Not found: " + cmd.getId());
        }
        return ret;
    }
}
