package org.swiftboot.demo.service.impl;

import org.swiftboot.demo.request.OrderDetailCreateRequest;
import org.swiftboot.demo.request.OrderDetailSaveRequest;
import org.swiftboot.demo.repository.OrderDetailRepository;
import org.swiftboot.demo.model.OrderDetailEntity;
import org.swiftboot.demo.dto.OrderDetailCreateResult;
import org.swiftboot.demo.dto.OrderDetailListResult;
import org.swiftboot.demo.dto.OrderDetailResult;
import org.swiftboot.demo.dto.OrderDetailSaveResult;
import org.swiftboot.demo.service.OrderDetailService;
import org.swiftboot.web.dto.PopulatableDto;
import org.swiftboot.web.request.IdListRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
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
    private OrderDetailRepository orderDetailRepository;

    /**
     * 创建订单明细
     *
     * @param request
     * @return
     */
    @Override
    public OrderDetailCreateResult createOrderDetail(OrderDetailCreateRequest request) {
        OrderDetailEntity p = request.createEntity();
        OrderDetailEntity saved = orderDetailRepository.save(p);
        log.debug("创建订单明细: " + saved.getId());
        return new OrderDetailCreateResult(saved.getId());
    }

    /**
     * 保存对订单明细的修改
     *
     * @param request
     * @return
     */
    @Override
    public OrderDetailSaveResult saveOrderDetail(OrderDetailSaveRequest request) {
        OrderDetailSaveResult ret = new OrderDetailSaveResult();
        Optional<OrderDetailEntity> optEntity = orderDetailRepository.findById(request.getId());
        if (optEntity.isPresent()) {
            OrderDetailEntity p = optEntity.get();
            p = request.populateEntity(p);
            OrderDetailEntity saved = orderDetailRepository.save(p);
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
        Optional<OrderDetailEntity> optEntity = orderDetailRepository.findById(orderDetailId);
        if (optEntity.isPresent()) {
            OrderDetailEntity p = optEntity.get();
            p.setIsDelete(true);
            orderDetailRepository.save(p);
        }
    }

    /**
     * 批量逻辑删除订单明细
     *
     * @param request
     */
    @Override
    public void deleteOrderDetailList(IdListRequest request) {
        List<OrderDetailEntity> entities = orderDetailRepository.findAllByIdIn(request.getIds());
        for (OrderDetailEntity entity : entities) {
            entity.setIsDelete(true);
            orderDetailRepository.save(entity);
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
            orderDetailRepository.deleteById(orderDetailId);
        }
        else {
            throw new RuntimeException("");
        }
    }

    /**
     * 批量永久删除订单明细
     *
     * @param request
     */
    @Override
    public void purgeOrderDetailList(IdListRequest request) {
        List<OrderDetailEntity> entities = orderDetailRepository.findAllByIdIn(request.getIds());
        for (OrderDetailEntity entity : entities) {
            orderDetailRepository.deleteById(entity.getId());
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
        Optional<OrderDetailEntity> optEntity = orderDetailRepository.findById(orderDetailId);
        if (optEntity.isPresent()) {
            log.debug(optEntity.get().getId());
            ret = PopulatableDto.createDto(OrderDetailResult.class, optEntity.get());
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
        Iterable<OrderDetailEntity> all = orderDetailRepository.findAll();
        if (all != null) {
            ret.populateByEntities(all);
            ret.setTotal(orderDetailRepository.count());
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
        Page<OrderDetailEntity> allPagination = orderDetailRepository.findAll(PageRequest.of(page, pageSize));
        if (allPagination != null) {
            ret.populateByEntities(allPagination);
            ret.setTotal(orderDetailRepository.count());
        }
        else {
            log.debug("没有查到订单明细");
        }
        return ret;
    }
}