package com.spoonsors.spoonsorsserver.entity.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PaymentDto {
    String member_id;
    List<Long> spon_list;
}
