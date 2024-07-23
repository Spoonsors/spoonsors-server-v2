package com.spoonsors.spoonsorsserver.entity.review;


import com.spoonsors.spoonsorsserver.entity.Post;
import com.spoonsors.spoonsorsserver.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.text.html.parser.Entity;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDto {

    private String review_txt;

    private Date review_date;

    private Post post;

    public Review toEntity(){
        return Review.builder()
                .review_id(null)
                .review_txt(review_txt)
                .review_date(review_date)
                .post(post)
                .build();
    }
}
