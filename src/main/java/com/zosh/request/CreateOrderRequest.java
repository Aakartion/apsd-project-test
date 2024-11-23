package com.zosh.request;

import com.zosh.domain.ORDER_TYPE;
import lombok.Data;

@Data
public class CreateOrderRequest {
    private String coinId;
    private double quantity;
    private ORDER_TYPE orderType;
}
