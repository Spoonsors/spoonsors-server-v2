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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "mealPlanner_id", nullable = false)//MySQL의 AUTO_INCREMENT를 사용
    private Long mealPlannerId;

    @Column(name = "menu_name1", nullable = false, length = 255)
    private String menuName1;

    @Column(name = "menu_img1", nullable = false, columnDefinition = "TEXT")
    private String menuImg1;

    @Column(name = "menu_name2", length = 255)
    private String menuName2;

    @Column(name = "menu_img2", columnDefinition = "TEXT")
    private String menuImg2;

    @Column(name = "menu_name3", length = 255)
    private String menuName3;

    @Column(name = "menu_img3", columnDefinition = "TEXT")
    private String menuImg3;

    @Column(name = "menu_name4", length = 255)
    private String menuName4;

    @Column(name = "menu_img4", columnDefinition = "TEXT")
    private String menuImg4;

    @Column(name = "mealPlanner_name", nullable = false, length = 100)
    private String mealPlannerName;

    private Float kcal;

    private Float carbohydrate;

    private Float protein;

    private Float fat;
    private Float na;
    @Column(length = 10)
    private String level;

}
