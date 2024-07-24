package com.spoonsors.spoonsorsserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
