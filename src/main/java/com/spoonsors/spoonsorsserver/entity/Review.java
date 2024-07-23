package com.spoonsors.spoonsorsserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Review {
    @Id
    @Column(nullable = false)
    private Long review_id;

    @JsonIgnore
    @OneToOne
    @MapsId
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(columnDefinition = "TEXT")
    private String review_img;

    @Column(length = 400)
    private String review_txt;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date review_date;
}
