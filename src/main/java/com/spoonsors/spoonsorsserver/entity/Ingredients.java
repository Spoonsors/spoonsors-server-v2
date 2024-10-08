package com.spoonsors.spoonsorsserver.entity;

import com.spoonsors.spoonsorsserver.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@ToString
@Setter
@Getter
@SuperBuilder
@Entity
@NoArgsConstructor(force = true)
public class Ingredients extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
