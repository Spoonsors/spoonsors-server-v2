package com.spoonsors.spoonsorsserver.entity.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PayHistoryDto {
    private int total;
    private int tax_free;
    private int vat;
    private int point;
    private int discount;
}
