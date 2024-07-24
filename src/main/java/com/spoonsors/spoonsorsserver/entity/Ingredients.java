package com.spoonsors.spoonsorsserver.entity;

import jakarta.persistence.*;
import lombok.*;



@ToString
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Ingredients {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ingredients_id", nullable = false)
    private Long ingredientsId;

    @Column(name = "ingredients_name", nullable = false, length = 100, unique = true)
    private String ingredientsName;

    @Column(name = "product_name", length = 100)
    private String productName;

    @Column(name = "ingredients_image", columnDefinition = "TEXT")
    private String ingredientsImage;

    @Column(name = "price", nullable = false)
    private int price;
}
