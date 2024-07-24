package com.spoonsors.spoonsorsserver.entity.review;


import com.spoonsors.spoonsorsserver.entity.Post;
import com.spoonsors.spoonsorsserver.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDto {

    private String review_txt;

    private LocalDate review_date;

    private Post post;

    public Review toEntity(){
        return Review.builder()
                .reviewId(null)
                .reviewText(review_txt)
                .reviewDate(review_date)
                .post(post)
                .build();
    }
}
