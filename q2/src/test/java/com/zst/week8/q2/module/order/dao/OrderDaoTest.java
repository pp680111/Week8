package com.zst.week8.q2.module.order.dao;

import com.zst.week8.q2.module.order.entity.Order;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class OrderDaoTest {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Test
    public void testInsert() {
        Order entity = Order.def();
        entity.setPrice("103.52");
        entity.setShippingAddress("广东省广州市天河区");
        entity.setUserId(System.currentTimeMillis());
        long time = System.currentTimeMillis();
        entity.setCreateTime(time);
        entity.setUpdateTime(time);
        orderDao.save(entity);
    }

    @Test
    public void testBatchInsert() {
        // 批量插入1k条数据，手动看看在各个子库子表的分布情况
        IntStream.range(0, 1000).forEach(i -> testInsert());
    }

    @Test
    public void testGetPage() {
        long count = orderDao.count();
        List<Order> list = orderDao.page(0, (int) count);
        Assertions.assertTrue(!list.isEmpty());
        Assertions.assertEquals(count, list.size());

        list.forEach(System.out::println);
    }

    @Test
    @Transactional
    public void update() {
        Order order = orderDao.getFirst();
        Order orderOld = new Order();
        BeanUtils.copyProperties(order, orderOld);

        order.setShippingAddress(Long.toString(System.currentTimeMillis()));
        // 在分库规则中被引用的列，是不可以更新的，因为分库分表中间件不太可能在update的时候把一条数据从一个库中迁移到另一个库
//        order.setUserId(System.currentTimeMillis());
        order.setPrice(Long.toString(System.currentTimeMillis()));
        order.setStatus(order.getStatus() == 1 ? 2 : 1);
        order.setUpdateTime(System.currentTimeMillis());
        orderDao.update(order);

        order = orderDao.get(order.getId());
        Assertions.assertFalse(order.getPrice().equals(orderOld.getPrice()));
        Assertions.assertFalse(order.getShippingAddress().equals(orderOld.getShippingAddress()));
        Assertions.assertFalse(order.getStatus() == orderOld.getStatus());
        Assertions.assertFalse(order.getUpdateTime() == orderOld.getUpdateTime());
    }

    @Test
    @Transactional
    public void delete() {
        Order order = orderDao.getFirst();
        orderDao.delete(order.getId());
        Assertions.assertNull(orderDao.get(order.getId()));
    }

}
