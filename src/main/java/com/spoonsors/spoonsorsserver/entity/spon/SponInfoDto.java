package com.spoonsors.spoonsorsserver.entity.spon;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SponInfoDto {
    public String itemName;
    public int totalPrice;
    public int quantity;
}
