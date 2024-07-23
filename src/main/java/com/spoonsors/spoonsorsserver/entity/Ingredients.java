package com.spoonsors.spoonsorsserver.entity;

import jakarta.persistence.*;
import lombok.*;



@ToString
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ingredients {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //MySQL의 AUTO_INCREMENT를 사용
    @Column(nullable = false)
    private Long ingredients_id;

    @Column(length = 100, nullable = false, unique = true)
    private String ingredients_name;

    @Column(length = 100)
    private String product_name;

    @Column(columnDefinition = "TEXT")
    private String ingredients_image;

    @Column(nullable = false)
    private Integer price;
}
