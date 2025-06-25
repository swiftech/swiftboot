package org.swiftboot.demo.service.impl;

import org.swiftboot.demo.repository.OrderDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.swiftboot.demo.request.OrderWithDetailCreateRequest;
import org.swiftboot.demo.request.OrderWithDetailSaveRequest;
import org.swiftboot.demo.repository.OrderRepository;
import org.swiftboot.demo.model.OrderDetailEntity;
import org.swiftboot.demo.model.OrderEntity;
import org.swiftboot.demo.dto.OrderCreateResult;
import org.swiftboot.demo.dto.OrderSaveResult;
import org.swiftboot.demo.service.OrderDetailRelationService;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import java.util.Optional;

/**
 * @author swiftech
 */
@Service
public class OrderDetailRelationServiceImpl implements OrderDetailRelationService {

    private static final Logger log = LoggerFactory.getLogger(OrderDetailRelationServiceImpl.class);

    // for test edit detail of order
    private final String permanentDetailId = "12345678901234567890123456789012";

    @Resource
    private OrderRepository orderRepository;

    @Resource
    private OrderDetailRepository orderDetailRepository;

    @Resource
    private EntityManager entityManager;

    /**
     * 创建带有详情的订单
     *
     * @param request
     * @return
     */
    @Override
    public OrderCreateResult createOrderWithDetail(OrderWithDetailCreateRequest request) {
        OrderEntity entity = request.createEntity();
        // Add extra permanent detail ( will be merged if already existed)
        OrderDetailEntity od = new OrderDetailEntity();
        od.setId(permanentDetailId);
        od.setDescription("固定订单明细项");
        od.setOrder(entity);
        entity.getOrderDetails().add(od);
        OrderEntity saved = orderRepository.save(entity);
        return new OrderCreateResult(saved.getId());
    }


    /**
     * 编辑订单，在原来明细上再添加新的明细。
     *
     * @param request
     * @return
     */
    @Override
    public OrderSaveResult saveOrderWithDetail(OrderWithDetailSaveRequest request) {
        OrderSaveResult ret = new OrderSaveResult();
        Optional<OrderEntity> optEntity = orderRepository.findById(request.getId());
        if (optEntity.isPresent()) {
            OrderEntity p = optEntity.get();
            p = request.populateEntity(p);
            OrderEntity saved = orderRepository.save(p);
            ret.setOrderId(saved.getId());
        }
        else {
            throw new RuntimeException("Order Not found: " + request.getId());
        }
        return ret;
    }


    /**
     * 编辑订单， 删除原来的明细，添加新的明细
     *
     * @param request
     * @return
     */
    @Override
    public OrderSaveResult saveOrderWithNewDetail(OrderWithDetailSaveRequest request) {
        OrderSaveResult ret = new OrderSaveResult();
        Optional<OrderEntity> optEntity = orderRepository.findById(request.getId());
        if (optEntity.isPresent()) {
            OrderEntity p = optEntity.get();
            System.out.println(p.getOrderDetails().hashCode());
            p.getOrderDetails().clear(); // remove old details
            p = request.populateEntity(p);
            for (OrderDetailEntity orderDetail : p.getOrderDetails()) {
                System.out.printf("'%s' - '%s'%n", orderDetail.getId(), orderDetail.getDescription());
                System.out.println("parent order: " + orderDetail.getOrder());
            }
            System.out.println(p.getOrderDetails().hashCode());
            OrderEntity saved = orderRepository.save(p);
            ret.setOrderId(saved.getId());
        }
        else {
            throw new RuntimeException("Order Not found: " + request.getId());
        }
        return ret;
    }
}
