package com.github.entity;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class PriceMonitorMsg {
    private BigDecimal originalPrice;
    private BigDecimal currentPrice;
    private String msg;
    private long updateTime;
    private String title;
    private long itemId;

    public PriceMonitorMsg() {

    }


    public PriceMonitorMsg(BigDecimal originalPrice, BigDecimal currentPrice, String msg, long updateTime, String title, long itemId) {
        this.originalPrice = originalPrice;
        this.currentPrice = currentPrice;
        this.msg = msg;
        this.updateTime = updateTime;
        this.title = title;
        this.itemId = itemId;
    }
}
