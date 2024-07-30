package com.spoonsors.spoonsorsserver.controller.mealPlanner;


import com.spoonsors.spoonsorsserver.customException.ApiException;
import com.spoonsors.spoonsorsserver.customException.ExceptionEnum;
import com.spoonsors.spoonsorsserver.entity.MealPlanner;
import com.spoonsors.spoonsorsserver.entity.mealPlanner.CreateUpdateReqDto;
import com.spoonsors.spoonsorsserver.service.mealPlanner.MealPlannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MealPlannerController {

    private final MealPlannerService mealPlannerService;

    @Operation(summary = "식단 전체 조회", description = "모든 식단을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "식단 조회 성공")
    })
    @GetMapping("/mealplanners")
    public List<MealPlanner> findAll(){return mealPlannerService.findAll();}


    @Operation(summary = "이름으로 식단 검색", description = "주어진 이름으로 식단을 검색합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "식단 조회 성공"),
            @ApiResponse(responseCode = "404", description = "식단을 찾을 수 없음")
    })
    @GetMapping("/mealplanners/{mealPlannerName}")
    public MealPlanner findByName(@PathVariable String mealPlannerName) {
        return mealPlannerService.findByName(mealPlannerName)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEALPLANNERNOTFOUND));
    }

    //TODO: 멀티파트로 변경
    @Operation(summary = "식단 등록", description = "새로운 식단을 등록합니다. 관리자만 접근 가능합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "식단 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/nutritionist/mealplanners")
    public String create(@RequestBody CreateUpdateReqDto mealPlanner) {
        Long mealPlannerId = mealPlannerService.register(mealPlanner);
        return mealPlannerId + "번 식단 등록 완료";
    }
    //TODO: 멀티파트로 변경
    @Operation(summary = "식단 수정", description = "기존 식단 정보를 수정합니다. 관리자만 접근 가능합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "식단 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "식단을 찾을 수 없음")
    })
    @PutMapping("/nutritionist/mealplanners/{id}")
    public String update(@Parameter(description = "식단 ID", example = "1") @PathVariable Long id,@RequestBody CreateUpdateReqDto mealPlanner) {
        Long mealPlannerId = mealPlannerService.update(id, mealPlanner);
        return mealPlannerId + "번 식단 수정 완료";
    }

    @Operation(summary = "식단 삭제", description = "지정된 ID의 식단을 삭제합니다. 관리자만 접근 가능합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "식단 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "식단을 찾을 수 없음")
    })
    @DeleteMapping("/nutritionist/mealplanners/{id}")
    public String delete(@Parameter(description = "식단 ID", example = "1") @PathVariable Long id){
        mealPlannerService.remove(id);
        return id +"번 식단 삭제 완료";
    }
}
