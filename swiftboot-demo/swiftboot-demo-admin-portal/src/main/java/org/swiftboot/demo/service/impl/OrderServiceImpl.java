package org.swiftboot.demo.service.impl;

import org.swiftboot.demo.repository.OrderDetailRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.swiftboot.demo.request.OrderCreateRequest;
import org.swiftboot.demo.request.OrderSaveRequest;
import org.swiftboot.demo.repository.OrderRepository;
import org.swiftboot.demo.model.OrderEntity;
import org.swiftboot.demo.dto.OrderCreateResult;
import org.swiftboot.demo.dto.OrderListResult;
import org.swiftboot.demo.dto.OrderResult;
import org.swiftboot.demo.dto.OrderSaveResult;
import org.swiftboot.demo.service.OrderService;
import org.swiftboot.web.dto.PopulatableDto;
import org.swiftboot.web.request.IdListRequest;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * 订单服务接口实现
 *
 * @author swiftech 2019-04-07
 **/
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource
    private OrderRepository orderRepository;

    @Resource
    private OrderDetailRepository orderDetailRepository;

    @Resource
    private EntityManager entityManager;

    /**
     * 创建订单
     *
     * @param request
     * @return
     */
    @Override
    public OrderCreateResult createOrder(OrderCreateRequest request) {
        OrderEntity p = request.createEntity();
//        p.setUserId(request.getUserId()); // call explicitly for the field COULD be different.
        OrderEntity saved = orderRepository.save(p);
        log.debug("创建订单: " + saved.getId());
        return new OrderCreateResult(saved.getId());
    }

    /**
     * 保存对订单的修改
     *
     * @param request
     * @return
     */
    @Override
    public OrderSaveResult saveOrder(OrderSaveRequest request) {
        OrderSaveResult ret = new OrderSaveResult();
        Optional<OrderEntity> optEntity = orderRepository.findById(request.getId());
        if (optEntity.isPresent()) {
            OrderEntity p = optEntity.get();
            p = request.populateEntity(p);
            OrderEntity saved = orderRepository.save(p);
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
        Optional<OrderEntity> optEntity = orderRepository.findById(orderId);
        if (optEntity.isPresent()) {
            OrderEntity p = optEntity.get();
            p.setIsDelete(true);
            orderRepository.save(p);
        }
    }

    /**
     * 批量逻辑删除订单
     *
     * @param request
     */
    @Override
    public void deleteOrderList(IdListRequest request) {
        List<OrderEntity> entities = orderRepository.findAllByIdIn(request.getIds());
        for (OrderEntity entity : entities) {
            entity.setIsDelete(true);
            orderRepository.save(entity);
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
            orderRepository.deleteById(orderId);
        }
        else {
            throw new RuntimeException("");
        }
    }

    /**
     * 批量永久删除订单
     *
     * @param request
     */
    @Override
    public void purgeOrderList(IdListRequest request) {
        List<OrderEntity> entities = orderRepository.findAllByIdIn(request.getIds());
        for (OrderEntity entity : entities) {
            orderRepository.deleteById(entity.getId());
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
        Optional<OrderEntity> optEntity = orderRepository.findById(orderId);
        if (optEntity.isPresent()) {
            log.debug(optEntity.get().getId());
            ret = PopulatableDto.createDto(OrderResult.class, optEntity.get());
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
    public OrderListResult queryOrderList(String userId) {
        OrderListResult ret = new OrderListResult();
        Iterable<OrderEntity> all = orderRepository.findByIsDeleteFalseAndUserId(userId);
        if (all != null) {
            ret.populateByEntities(all);
            ret.setTotal(orderRepository.count());
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
        Page<OrderEntity> allPagination = orderRepository.findAll(PageRequest.of(page, pageSize));
        if (allPagination != null) {
            ret.populateByEntities(allPagination);
            ret.setTotal(orderRepository.count());
        }
        else {
            log.debug("没有查到订单");
        }
        return ret;
    }
}
