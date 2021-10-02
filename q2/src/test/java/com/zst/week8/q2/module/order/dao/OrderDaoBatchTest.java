package com.zst.week8.q2.module.order.dao;

import com.zst.week8.q2.module.order.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class OrderDaoBatchTest {
    @Autowired
    private OrderDao orderDao;


    // batchSize=30K,time=1213578，关闭shardingSphere的输出sql功能之后缩短为776218
    // 单线程生成对象速度过慢
    @Test
    public void testInsertBatch10M() {
        int batchSize = 30_000;
        List<Order> batchList = new ArrayList<>(batchSize);
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < 10_000_000; i++) {
            Order entity = Order.def();
            entity.setPrice("103.52");
            entity.setShippingAddress("广东省广州市天河区");
            entity.setUserId(System.currentTimeMillis());
            long time = System.currentTimeMillis();
            entity.setCreateTime(time);
            entity.setUpdateTime(time);
            batchList.add(entity);

            if (batchList.size() == batchSize) {
                orderDao.saveBatch(batchList);
                batchList.clear();
            }
        }
        System.out.println(System.currentTimeMillis() - currentTime);
    }

    // batchSize=30000, time = 1633964, 怀疑是shardingSphere瓶颈
    // 禁用打印sql之后，time=540986
    @Test
    public void testParallelInsertBatch10M() throws InterruptedException {
        int batchSize = 30_000;
        int totalSize = 10_000_000;
        int threadNum = Runtime.getRuntime().availableProcessors() / 2;
        int rowPerThread = 10_000_000 / threadNum;
        ExecutorService fixedExecutor = Executors.newFixedThreadPool(threadNum);
        List<CompletableFuture> futures = new ArrayList<>();

        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < threadNum; i++) {
            int finalI = i;
            futures.add(CompletableFuture.runAsync(() -> {
                List<Order> batchList = new ArrayList<>(batchSize);
                for (int j = finalI * rowPerThread; j <= Math.min((finalI + 1) * rowPerThread, totalSize); j++) {
                    Order entity = Order.def();
                    entity.setPrice("103.52");
                    entity.setShippingAddress("广东省广州市天河区");
                    entity.setUserId(System.currentTimeMillis());
                    long time = System.currentTimeMillis();
                    entity.setCreateTime(time);
                    entity.setUpdateTime(time);
                    batchList.add(entity);

                    if (batchList.size() == batchSize) {
                        orderDao.saveBatch(batchList);
                        batchList.clear();
                    }
                }

                if (!batchList.isEmpty()) {
                    orderDao.saveBatch(batchList);
                }
            }, fixedExecutor));
        }
        futures.forEach(CompletableFuture::join);
        System.out.println(System.currentTimeMillis() - currentTime);
    }
}
