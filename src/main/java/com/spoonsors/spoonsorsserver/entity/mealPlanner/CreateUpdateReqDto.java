package com.spoonsors.spoonsorsserver.entity.mealPlanner;

import lombok.Getter;

@Getter
public class CreateUpdateReqDto {
    private String menuName1;

    private String menuImg1;

    private String menuName2;

    private String menuImg2;

    private String menuName3;

    private String menuImg3;

    private String menuName4;

    private String menuImg4;

    private String mealPlannerName;

    private Float kcal;

    private Float carbohydrate;

    private Float protein;

    private Float fat;

    private Float na;

    private String level;
}
