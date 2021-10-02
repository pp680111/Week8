package com.zst.week8.q6.module.item.entitiy;

import lombok.Data;

@Data
public class Item {
    /** id*/
    private long id;
    /** 名称*/
    private String name;
    /** 状态*/
    private int status;
    /** 商品描述*/
    private String description;
}
