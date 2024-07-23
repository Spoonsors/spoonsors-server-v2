package com.spoonsors.spoonsorsserver.entity.manager;

import com.spoonsors.spoonsorsserver.entity.Ingredients;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class IngredientsDto {

    private String ingredientsName;

    private String productName;

    private Integer price;

    public Ingredients toEntity() {
        return Ingredients.builder()
                .ingredients_id(null)
                .ingredients_name(ingredientsName)
                .product_name(productName)
                .price(price)
                .build();
    }
}
