package com.spoonsors.spoonsorsserver.service.mealPlanner;

import com.spoonsors.spoonsorsserver.entity.MealPlanner;
import com.spoonsors.spoonsorsserver.repository.MealPlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MealPlannerService {

    private final MealPlannerRepository mealPlannerRepository;

    // 식단 전체 조회
    public List<MealPlanner> findAll(){return mealPlannerRepository.findAll();}
    // 식단 이름으로 검색
    public MealPlanner findByName(String mealplanner_name){return mealPlannerRepository.findByName(mealplanner_name);}

    // 식단 등록
    public Long regist(MealPlanner mealPlanner){
        mealPlannerRepository.regist(mealPlanner);
        return mealPlanner.getMealPlannerId();
    }
    // 식단 수정
    public Long update(MealPlanner mealPlanner){
        MealPlanner updateMealPlanner = mealPlannerRepository.findById(mealPlanner.getMealPlannerId());
        updateMealPlanner.setMenuName1(mealPlanner.getMenuName1());
        updateMealPlanner.setMenuImg1(mealPlanner.getMenuImg1());
        updateMealPlanner.setMenuName2(mealPlanner.getMenuName2());
        updateMealPlanner.setMenuImg2(mealPlanner.getMenuImg2());
        updateMealPlanner.setMenuName3(mealPlanner.getMenuName3());
        updateMealPlanner.setMenuImg3(mealPlanner.getMenuImg3());
        updateMealPlanner.setMenuName4(mealPlanner.getMenuName4());
        updateMealPlanner.setMenuImg4(mealPlanner.getMenuImg4());
        updateMealPlanner.setMealPlannerName(mealPlanner.getMealPlannerName());
        updateMealPlanner.setKcal(mealPlanner.getKcal());
        updateMealPlanner.setCarbohydrate(mealPlanner.getCarbohydrate());
        updateMealPlanner.setProtein(mealPlanner.getProtein());
        updateMealPlanner.setFat(mealPlanner.getFat());
        updateMealPlanner.setNa(mealPlanner.getNa());
        updateMealPlanner.setLevel(mealPlanner.getLevel());

        return updateMealPlanner.getMealPlannerId();
    }

    // 식단 삭제
    public void remove(Long mealPlanner_id){mealPlannerRepository.remove(mealPlanner_id);}

}
