package com.spoonsors.spoonsorsserver.entity.spon;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class SponReadResDto {
    private Long sponId;
    private String sMemberId;
    private Long ingredientId;
    private String ingredientName;
    private String ingredientImg;
    private LocalDate sponDate;
}
