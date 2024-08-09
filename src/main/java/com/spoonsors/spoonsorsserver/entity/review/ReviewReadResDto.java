package com.spoonsors.spoonsorsserver.entity.review;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ReviewReadResDto {
    private Long reviewId;
    private String reviewText;
    private String reviewImg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
