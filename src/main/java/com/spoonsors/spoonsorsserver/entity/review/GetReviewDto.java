package com.spoonsors.spoonsorsserver.entity.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class GetReviewDto {
    private String review_txt;
    private String review_img;
    private String writer_nickname;
    private Date write_date;
    private Long post_id;
}
