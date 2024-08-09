package com.spoonsors.spoonsorsserver.entity.post;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostsReadResDto {

    private Long postId;

    private String writerId;

    private String postTitle;

    private String postTxt;

    private Boolean isFinished;

    private int remainSpon;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
