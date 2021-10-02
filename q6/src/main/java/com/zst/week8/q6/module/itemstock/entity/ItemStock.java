package com.zst.week8.q6.module.itemstock.entity;

import lombok.Data;

@Data
public class ItemStock {
    /** id*/
    private long id;
    /** 商品id*/
    private long itemId;
    /** 商品库存标识*/
    private String mark;
    /** 价格*/
    private String price;
    /** 库存量*/
    private int stockNum;
    /** 状态*/
    private int status;
}
