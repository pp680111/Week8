package com.zst.week8.q6.module.orderitem.entity;

import lombok.Data;

@Data
public class OrderItem {
    /** id*/
    private long id;
    /** 订单id*/
    private long orderId;
    /** 商品id*/
    private long itemId;
    /** 商品库存id*/
    private long itemStockId;
    /** 价格*/
    private String price;
    /** 数量*/
    private int num;
}
