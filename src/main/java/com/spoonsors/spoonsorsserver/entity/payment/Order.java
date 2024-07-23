package com.spoonsors.spoonsorsserver.entity.payment;

import lombok.Getter;

@Getter
public class Order {
    private String product_name; //상품명
    private Integer price; //가격
}
