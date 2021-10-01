package com.zst.week8.q2.module.order.dao;

import com.zst.week8.q2.module.order.entity.Order;

import java.util.List;

public interface OrderDao {
    Order get(long id);

    Order getFirst();

    long count();

    List<Order> page(int start, int end);

    void save(Order entity);

    void update(Order entity);

    void delete(long id);


}
