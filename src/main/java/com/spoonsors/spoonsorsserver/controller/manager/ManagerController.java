package com.spoonsors.spoonsorsserver.controller.manager;

import com.spoonsors.spoonsorsserver.customException.ApiException;
import com.spoonsors.spoonsorsserver.customException.ExceptionEnum;
import com.spoonsors.spoonsorsserver.entity.Ingredients;
import com.spoonsors.spoonsorsserver.entity.manager.CertificateDto;
import com.spoonsors.spoonsorsserver.entity.manager.IngredientsDto;
import com.spoonsors.spoonsorsserver.service.Image.S3Uploader;
import com.spoonsors.spoonsorsserver.service.manager.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final S3Uploader s3Uploader;
    // 식재료 목록 조회
    @Operation(summary = "식재료 목록 조회", description = "모든 식재료를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "식재료 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Ingredients.class))))
    })
    @GetMapping("/ingredients")
    public List<Ingredients> findAll(){
        return managerService.findAll();
    }

    @Operation(summary = "이름으로 식재료 조회", description = "주어진 이름으로 식단을 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "식재료 검색 성공", content = @Content(schema = @Schema(implementation = Ingredients.class))),
            @ApiResponse(responseCode = "404", description = "식재료를 찾을 수 없음")
    })
    @GetMapping("/ingredients/{ingredients_name}")
    public Ingredients findByName(@PathVariable String ingredients_name){
        return managerService.findByName(ingredients_name);
    }

    @Operation(summary = "식재료 등록", description = "식재료를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "식재료 등록 성공", content = @Content(schema = @Schema(implementation = Ingredients.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 입력값")
    })
    @PostMapping(value = "/ingredients", consumes = {MediaType.APPLICATION_JSON_VALUE, "multipart/form-data"})
    public Ingredients create(@RequestPart IngredientsDto ingredientsDto, @RequestPart(value = "img", required = false) MultipartFile img){
        Ingredients ingredient = null;
        try{
            String url = s3Uploader.upload(img,"ingredients");
            ingredient = managerService.regist(ingredientsDto, url);
        }catch (IOException e){
            e.printStackTrace();
        }
        return ingredient;
    }

    @Operation(summary = "식재료 수정", description = "식재료를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "식재료 수정 성공", content = @Content(schema = @Schema(implementation = Ingredients.class))),
            @ApiResponse(responseCode = "404", description = "식재료를 찾을 수 없음")
    })
    @PutMapping(value = "/ingredients/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, "multipart/form-data"})
    public Ingredients update(@PathVariable("id") Long ingredients_id, @RequestPart IngredientsDto ingredientsDto, @RequestPart(value = "img", required = false) MultipartFile img) throws IOException {
        String url = s3Uploader.upload(img,"ingredients");
        return managerService.update(ingredients_id, ingredientsDto, url);
    }


    @Operation(summary = "식재료 사제", description = "식재료를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "식재료 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "식재료를 찾을 수 없음")
    })
    @DeleteMapping("/ingredients/{id}")
    public String delete(@PathVariable("id") Long ingredients_id){
        managerService.remove(ingredients_id);
        return ingredients_id + "번 식재료 삭제 완료";
    }

    @Operation(summary = "등록 상태 확인", description = "수혜자의 증명서가 등록되었는지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 상태 확인 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CertificateDto.class))))
    })
    @GetMapping("/certificates")
    public List<CertificateDto> certificate(){
        return managerService.certificate();
    }

    @Operation(summary = "수혜자 증명서 등록상태 변경", description = "수혜자 증명서의 등록상태를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "증명서 상태 변경 성공"),
            @ApiResponse(responseCode = "404", description = "수혜자 찾을 수 없음")
    })
    @PutMapping("/certificates/{bMember_id}/status/{state}")
    public String isVerified(@PathVariable String bMember_id, @PathVariable int state){
        String result = managerService.isVerified(bMember_id, state);
        if(result == null){
            throw new ApiException(ExceptionEnum.MANAGER04);
        }
        return result;
    }
}
