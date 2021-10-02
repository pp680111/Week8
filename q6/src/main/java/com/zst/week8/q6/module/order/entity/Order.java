package com.zst.week8.q6.module.order.entity;

import lombok.Data;

@Data
public class Order {
    private long id;
    private long userId;
    private String shippingAddress;
    private int status;
    private String price;
    private long createTime;
    private long updateTime;

    public static Order def() {
        Order entity = new Order();
        entity.setStatus(1);
        return entity;
    }
}
