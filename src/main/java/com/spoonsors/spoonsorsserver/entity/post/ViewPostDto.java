package com.spoonsors.spoonsorsserver.entity.post;

import com.spoonsors.spoonsorsserver.entity.review.ReviewReadResDto;
import com.spoonsors.spoonsorsserver.entity.spon.SponReadResDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@Getter
public class ViewPostDto {
    private Long postId;
    private String writerId;
    private String postTitle;
    private String postTxt;
    private Boolean isFinished;
    private Boolean hasReview;
    private int remainSpon;
    private String menuImg;
    private String menuName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    List<SponReadResDto> spons;
    ReviewReadResDto review;


}
