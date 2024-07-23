package com.spoonsors.spoonsorsserver.controller.manager;

import com.spoonsors.spoonsorsserver.customException.ApiException;
import com.spoonsors.spoonsorsserver.customException.ExceptionEnum;
import com.spoonsors.spoonsorsserver.entity.Ingredients;
import com.spoonsors.spoonsorsserver.entity.manager.CertificateDto;
import com.spoonsors.spoonsorsserver.entity.manager.IngredientsDto;
import com.spoonsors.spoonsorsserver.service.Image.S3Uploader;
import com.spoonsors.spoonsorsserver.service.manager.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final S3Uploader s3Uploader;
    // 식재료 목록 조회
    @GetMapping("/manager/findAll")
    public List<Ingredients> findAll(){
        return managerService.findAll();
    }

    // 이름으로 식재료 조회
    @GetMapping("/manager/findByName")
    public Ingredients findByName(@RequestParam String ingredients_name){
        return managerService.findByName(ingredients_name);
    }

    // 식재료 등록

    @PostMapping(value ="/manager/create", consumes = {MediaType.APPLICATION_JSON_VALUE, "multipart/form-data"})
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

    //식재로 수정
    @PutMapping("/manager/update/{ingredients_id}")
    public Ingredients update(@PathVariable Long ingredients_id, @RequestPart IngredientsDto ingredientsDto, @RequestPart(value = "img", required = false) MultipartFile img) throws IOException {
        Ingredients ingredient = null;
        String url = s3Uploader.upload(img,"ingredients");
        ingredient = managerService.update(ingredients_id, ingredientsDto, url);
        return ingredient;
    }

    //식재료 삭제
    @DeleteMapping("/manager/delete/{ingredients_id}")
    public String delete(@PathVariable Long ingredients_id){
        managerService.remove(ingredients_id);
        return ingredients_id + "번 식재료 삭제 완료";
    }

    // 수혜자 증명서 등록 상태 확인
    @GetMapping("/manager/certificate")
    public List<CertificateDto> certificate(){return managerService.certificate();}

    // 수혜자 증명서 등록 상태 변경
    @PutMapping("/manager/isVerified/{bMember_id}/{state}")
    public String isVeified(@PathVariable String bMember_id, @PathVariable int state){
        String result;
        result = managerService.isVerified(bMember_id, state);
        if(result == null){
            throw new ApiException(ExceptionEnum.MANAGER04);
        }else {
            return result;
        }
    }
}
