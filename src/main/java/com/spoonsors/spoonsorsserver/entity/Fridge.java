package com.spoonsors.spoonsorsserver.entity;

import com.spoonsors.spoonsorsserver.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Table(name="fridge")
@Entity
@Builder
@Getter
@Setter
public class Fridge extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //MySQL의 AUTO_INCREMENT를 사용
    @Column(name = "fridge_id", nullable = false)
    private Long fridgeId;

    @ManyToOne
    @JoinColumn(name = "bMember_id", nullable = false)
    private BMember bMember;

    @Column(name = "fridge_item_name", nullable = false, length = 100)
    private String fridgeItemName;

    @Column(name = "fridge_item_img", columnDefinition = "TEXT")
    private String fridgeItemImg;

    @Column(name = "is_frized", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private boolean fridgeIsFrized;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name="ingredientsId")
    private Ingredients ingredientsId;
}
