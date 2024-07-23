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
public class MealPlanner {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //MySQL의 AUTO_INCREMENT를 사용
    private Long mealPlanner_id;

    @Column( nullable = false)
    private String menu_name1;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String menu_img1;

    private String menu_name2;
    @Column(columnDefinition = "TEXT")
    private String menu_img2;

    private String menu_name3;
    @Column(columnDefinition = "TEXT")
    private String menu_img3;


    private String menu_name4;
    @Column(columnDefinition = "TEXT")
    private String menu_img4;

    @Column(length = 100, nullable = false)
    private String mealPlanner_name;

    private Float kcal;

    private Float carbohydrate;

    private Float protein;

    private Float fat;
    private Float na;
    @Column(length = 10)
    private String level;

}
