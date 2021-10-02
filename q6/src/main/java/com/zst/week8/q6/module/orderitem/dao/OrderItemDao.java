package com.zst.week8.q6.module.orderitem.dao;

import com.zst.week8.q6.module.orderitem.entity.OrderItem;

import java.util.List;

public interface OrderItemDao {
    void save(OrderItem entity);

    List<OrderItem> listByOrderId(long orderId);
}
