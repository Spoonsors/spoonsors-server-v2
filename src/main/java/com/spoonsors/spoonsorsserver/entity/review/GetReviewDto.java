package com.spoonsors.spoonsorsserver.entity.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class GetReviewDto {
    private String review_txt;
    private String review_img;
    private String writer_nickname;
    private LocalDate write_date;
    private Long post_id;
}
