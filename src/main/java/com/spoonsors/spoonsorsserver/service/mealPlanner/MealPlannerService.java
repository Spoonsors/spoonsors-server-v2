package com.spoonsors.spoonsorsserver.service.mealPlanner;

import com.spoonsors.spoonsorsserver.customException.ApiException;
import com.spoonsors.spoonsorsserver.customException.ExceptionEnum;
import com.spoonsors.spoonsorsserver.entity.MealPlanner;
import com.spoonsors.spoonsorsserver.entity.mealPlanner.CreateUpdateReqDto;
import com.spoonsors.spoonsorsserver.repository.IMealPlannerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MealPlannerService {

    private final IMealPlannerRepository iMealPlannerRepository;

    // 식단 전체 조회
    public List<MealPlanner> findAll(){return iMealPlannerRepository.findAllByDeletedYnFalse();}
    // 식단 이름으로 검색
    public Optional<MealPlanner> findByName(String mealPlannerName) {
        return iMealPlannerRepository.findMealPlannerByMealPlannerNameAndDeletedYnFalse(mealPlannerName);
    }
    // 식단 등록
    public Long register(CreateUpdateReqDto mealPlannerDTO) {
        MealPlanner mealPlanner = MealPlanner.builder()
                .menuName1(mealPlannerDTO.getMenuName1())
                .menuImg1(mealPlannerDTO.getMenuImg1())
                .menuName2(mealPlannerDTO.getMenuName2())
                .menuImg2(mealPlannerDTO.getMenuImg2())
                .menuName3(mealPlannerDTO.getMenuName3())
                .menuImg3(mealPlannerDTO.getMenuImg3())
                .menuName4(mealPlannerDTO.getMenuName4())
                .menuImg4(mealPlannerDTO.getMenuImg4())
                .mealPlannerName(mealPlannerDTO.getMealPlannerName())
                .kcal(mealPlannerDTO.getKcal())
                .carbohydrate(mealPlannerDTO.getCarbohydrate())
                .protein(mealPlannerDTO.getProtein())
                .fat(mealPlannerDTO.getFat())
                .na(mealPlannerDTO.getNa())
                .level(mealPlannerDTO.getLevel())
                .build();

        iMealPlannerRepository.save(mealPlanner);
        return mealPlanner.getMealPlannerId();
    }

    // 식단 수정
    public Long update(Long id, CreateUpdateReqDto mealPlannerDTO) {
        MealPlanner mealPlanner = iMealPlannerRepository.findById(id)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEALPLANNERNOTFOUND));

        MealPlanner updatedMealPlanner = MealPlanner.builder()
                .mealPlannerId(id)
                .menuName1(mealPlannerDTO.getMenuName1())
                .menuImg1(mealPlannerDTO.getMenuImg1())
                .menuName2(mealPlannerDTO.getMenuName2())
                .menuImg2(mealPlannerDTO.getMenuImg2())
                .menuName3(mealPlannerDTO.getMenuName3())
                .menuImg3(mealPlannerDTO.getMenuImg3())
                .menuName4(mealPlannerDTO.getMenuName4())
                .menuImg4(mealPlannerDTO.getMenuImg4())
                .mealPlannerName(mealPlannerDTO.getMealPlannerName())
                .kcal(mealPlannerDTO.getKcal())
                .carbohydrate(mealPlannerDTO.getCarbohydrate())
                .protein(mealPlannerDTO.getProtein())
                .fat(mealPlannerDTO.getFat())
                .na(mealPlannerDTO.getNa())
                .level(mealPlannerDTO.getLevel())
                .build();

        iMealPlannerRepository.save(updatedMealPlanner);
        return updatedMealPlanner.getMealPlannerId();
    }

    // 식단 삭제
    @Transactional
    public void remove(Long id){
        MealPlanner mealPlanner = iMealPlannerRepository.findById(id)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEALPLANNERNOTFOUND));
        mealPlanner.delete();
    }

}
