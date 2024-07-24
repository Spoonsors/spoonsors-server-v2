package com.spoonsors.spoonsorsserver.entity.bMember;

import com.spoonsors.spoonsorsserver.entity.Fridge;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class FridgeDto {
    private String name;
    private Boolean isFrized;
    private Date expirationDate;

    public Fridge toEntity(){
        return Fridge.builder()
                .fridgeId(null)
                .fridgeItemName(name)
                .expirationDate(expirationDate)
                .fridgeIsFrized(isFrized)
                .build();
    }
}
