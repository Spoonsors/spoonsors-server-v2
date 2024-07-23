package com.spoonsors.spoonsorsserver.entity.bMember;

import com.spoonsors.spoonsorsserver.entity.BMember;
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
    private Integer isFrized;
    private Date expirationDate;

    public Fridge toEntity(){
        return Fridge.builder()
                .fridge_id(null)
                .fridge_item_name(name)
                .expiration_date(expirationDate)
                .is_frized(isFrized)
                .build();
    }
}
