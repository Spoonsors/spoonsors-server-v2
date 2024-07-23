package com.spoonsors.spoonsorsserver.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

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
    private Long spon_id;

    @ManyToOne
    @JoinColumn(name="sMember_id",nullable = true)
    private SMember sMember;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @OneToOne // 스폰 하나에 식재료 하나
    @JoinColumn(name = "ingredients_id",nullable = false)
    private Ingredients ingredients;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date spon_date;

    @ColumnDefault("0")
    private Integer spon_state; // 디폴트 설정

    @JsonIgnore
    private String tid; //결제 고유 번호
}
