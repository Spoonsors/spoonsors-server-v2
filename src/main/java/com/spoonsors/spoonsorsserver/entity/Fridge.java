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
public class Fridge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //MySQL의 AUTO_INCREMENT를 사용
    @Column(nullable = false)
    private Long fridge_id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private BMember bMember;

    @Column( length = 100, nullable = false)
    private String fridge_item_name;

    @Column(columnDefinition = "TEXT")
    private String fridge_item_img;

    @Column(nullable = false)
    private Integer is_frized;

    @Temporal(value = TemporalType.DATE)
    private Date expiration_date;
}
