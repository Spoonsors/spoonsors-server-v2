package com.spoonsors.spoonsorsserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spoonsors.spoonsorsserver.entity.common.BaseEntity;
import com.spoonsors.spoonsorsserver.entity.post.PostsReadResDto;
import com.spoonsors.spoonsorsserver.entity.post.ViewPostDto;
import com.spoonsors.spoonsorsserver.entity.review.ReviewReadResDto;
import com.spoonsors.spoonsorsserver.entity.spon.SponReadResDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@SuperBuilder
@Entity
@NoArgsConstructor(force = true)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id", nullable = false)
    private Long postId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "writer_id", nullable = false)
    private BMember writer;

    @Column( name="post_title", length = 100)
    private String postTitle;

    @Column(name="post_txt", length = 400)
    private String postTxt;

    @Column(name = "is_finished", columnDefinition = "TINYINT DEFAULT 0")
    private boolean isFinished;

    @Column(name = "has_review", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private boolean hasReview;


    @Column(name = "remain_spon", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int remainSpon;

    @Column(name = "menu_img", columnDefinition = "TEXT")
    private String menuImg;

    @Column(name = "menu_name", length = 255)
    private String menuName;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.REMOVE})
    private List<Spon> spons;

    public void delete() {
        this.deletedYn = true;
    }

    public void toggleFinish(){
        this.isFinished = !isFinished;
    }
    public void updateRemainSpon(int n){
        this.remainSpon -=n;
    }
    public void updateRemainSponAndFinish(int n){
        this.remainSpon -=n;
        this.isFinished = true;
    }

    public ViewPostDto toViewPostDto(Review review) {
        return ViewPostDto.builder()
                .postId(this.postId)
                .writerId(this.writer != null ? this.writer.getMemberId() : null) // assuming `getId()` method exists
                .postTitle(this.postTitle)
                .postTxt(this.postTxt)
                .isFinished(this.isFinished)
                .hasReview(this.hasReview)
                .remainSpon(this.remainSpon)
                .menuImg(this.menuImg)
                .menuName(this.menuName)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .spons(this.spons != null ? this.spons.stream()
                        .map(spon -> SponReadResDto.builder()
                                .sponId(spon.getSponId())
                                .sMemberId(spon.getSMember() != null ? spon.getSMember().getMemberId() : null)
                                .sponDate(spon.getSponDate())
                                .ingredientName(spon.getIngredients().getIngredientsName())
                                .ingredientImg(spon.getIngredients().getIngredientsImage())
                                .ingredientId(spon.getIngredients().getIngredientsId())
                                .build())
                        .collect(Collectors.toList()) : null)
                .review(review != null ? ReviewReadResDto.builder()
                        .reviewId(review.getReviewId())
                        .reviewImg(review.getReviewImg())
                        .reviewText(review.getReviewText())
                        .createdAt(review.getCreatedAt())
                        .updatedAt(review.getUpdatedAt())
                        .build() : null)
                .build();
    }

    public PostsReadResDto toPostsReadResDto(){
        return PostsReadResDto.builder()
                .postId(this.postId)
                .writerId(this.writer.getMemberId())
                .postTitle(this.postTitle)
                .postTxt(this.postTxt)
                .isFinished(this.isFinished)
                .remainSpon(this.remainSpon)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt()).build();
    }
}
