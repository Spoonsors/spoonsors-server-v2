package com.spoonsors.spoonsorsserver.entity.spon;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class SponDto {
    private Long post_id;
    private String writer_nickname;
    private String ingredients_name;
    private String product_name;
    private String ingredients_image;
    private Integer price;
    private Date spon_date;
}
