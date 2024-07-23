package com.spoonsors.spoonsorsserver.controller.mealPlanner;


import com.spoonsors.spoonsorsserver.entity.MealPlanner;
import com.spoonsors.spoonsorsserver.service.mealPlanner.MealPlannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MealPlannerController {

    private final MealPlannerService mealPlannerService;

    // 식단 전체 조회
    @GetMapping("/mealplanner/findAll")
    public List<MealPlanner> findAll(){return mealPlannerService.findAll();}
    // 식단 이름으로 검색
    @GetMapping("/mealplanner/findByName")
    public MealPlanner findByName(@RequestParam String mealPlanner_name){
        return mealPlannerService.findByName(mealPlanner_name);
    }

    // 식단 등록
    @PostMapping("/mealplanner/create")
    public String create(@RequestBody MealPlanner mealPlanner){
        Long mealPlannerId = mealPlannerService.regist(mealPlanner);
        return mealPlannerId + "번 식단 등록 완료";
    }

    // 식단 수정
    @PutMapping("/mealplanner/update")
    public String update(@RequestBody MealPlanner mealPlanner){
        Long mealPlannerId = mealPlannerService.update(mealPlanner);
        return mealPlannerId +"번 식단 수정 완료";
    }
    // 식단 삭제
    @DeleteMapping("/mealplanner/delete/{mealPlanner_id}")
    public String delete(@PathVariable Long mealPlanner_id){
        mealPlannerService.remove(mealPlanner_id);
        return mealPlanner_id +"번 식단 삭제 완료";
    }
}
