package com.spoonsors.spoonsorsserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@ToString
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Spon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //MySQL의 AUTO_INCREMENT를 사용
    @Column(name = "spon_id", nullable = false)
    private Long sponId;

    @ManyToOne
    @JoinColumn(name="sMember_id",nullable = true)
    private SMember sMember;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "ingredients_id", nullable = false)
    private Ingredients ingredients;

    @Column(name = "spon_date")
    private LocalDate sponDate;

    @Column(name = "is_sponed", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private boolean isSponed;

    @Column(name = "tid", length = 255)
    private String tid;
}
