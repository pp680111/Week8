package com.zst.week8.q6;

import com.zst.week8.q6.module.order.dao.OrderDao;
import com.zst.week8.q6.module.order.entity.Order;
import com.zst.week8.q6.module.orderitem.dao.OrderItemDao;
import com.zst.week8.q6.module.orderitem.entity.OrderItem;
import com.zst.week8.q6.utils.IDGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class XATransactionTest {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;

    /**
     * 测试在ShardingSphere-Proxy开启XA事务的情况下同时操作两张表的事务
     */
    @Test
    @Transactional
    @Rollback(false)
    public void testXATransactionalSaveOrder() {
        Order order = new Order();
        order.setId(IDGenerator.get());
        order.setPrice("100.00");
        order.setShippingAddress("Guangdong Province, Guangzhou city, Tianhe district");
        order.setStatus(1);
        order.setCreateTime(System.currentTimeMillis());
        order.setUpdateTime(System.currentTimeMillis());
        /*
            orderItem表在db0，按照userId%2的分库规则，为了让事务涉及到两个数据库，手动设定userId=1，
            让ShardingSphere把这条order数据分到db1中，实现两个数据库的XA分布式事务
         */
        order.setUserId(1);
        orderDao.save(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(IDGenerator.get());
        orderItem.setItemId(IDGenerator.get());
        orderItem.setItemStockId(IDGenerator.get());
        orderItem.setOrderId(order.getId());
        orderItem.setNum(1);
        orderItem.setPrice("100.00");

        orderItemDao.save(orderItem);
    }
}
