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
                .ingredientsId(null)
                .ingredientsName(ingredientsName)
                .productName(productName)
                .price(price)
                .build();
    }
}
