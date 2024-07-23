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
        return mealPlanner.getMealPlanner_id();
    }
    // 식단 수정
    public Long update(MealPlanner mealPlanner){
        MealPlanner updateMealPlanner = mealPlannerRepository.findById(mealPlanner.getMealPlanner_id());
        updateMealPlanner.setMenu_name1(mealPlanner.getMenu_name1());
        updateMealPlanner.setMenu_img1(mealPlanner.getMenu_img1());
        updateMealPlanner.setMenu_name2(mealPlanner.getMenu_name2());
        updateMealPlanner.setMenu_img2(mealPlanner.getMenu_img2());
        updateMealPlanner.setMenu_name3(mealPlanner.getMenu_name3());
        updateMealPlanner.setMenu_img3(mealPlanner.getMenu_img3());
        updateMealPlanner.setMenu_name4(mealPlanner.getMenu_name4());
        updateMealPlanner.setMenu_img4(mealPlanner.getMenu_img4());
        updateMealPlanner.setMealPlanner_name(mealPlanner.getMealPlanner_name());
        updateMealPlanner.setKcal(mealPlanner.getKcal());
        updateMealPlanner.setCarbohydrate(mealPlanner.getCarbohydrate());
        updateMealPlanner.setProtein(mealPlanner.getProtein());
        updateMealPlanner.setFat(mealPlanner.getFat());
        updateMealPlanner.setNa(mealPlanner.getNa());
        updateMealPlanner.setLevel(mealPlanner.getLevel());

        return updateMealPlanner.getMealPlanner_id();
    }

    // 식단 삭제
    public void remove(Long mealPlanner_id){mealPlannerRepository.remove(mealPlanner_id);}

}
