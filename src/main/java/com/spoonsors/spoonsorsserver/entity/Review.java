package com.spoonsors.spoonsorsserver.entity;

import com.spoonsors.spoonsorsserver.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@ToString
@Getter
@Setter
@SuperBuilder
@Entity
public class Review  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "review_img", columnDefinition = "TEXT")
    private String reviewImg;

    @Column(name = "review_txt", length = 400)
    private String reviewText;

    @Column(name = "review_date", nullable = false)
    private LocalDate reviewDate;
}
