package com.spoonsors.spoonsorsserver.service.manager;

import com.spoonsors.spoonsorsserver.customException.ApiException;
import com.spoonsors.spoonsorsserver.customException.ExceptionEnum;
import com.spoonsors.spoonsorsserver.entity.BMember;
import com.spoonsors.spoonsorsserver.entity.Ingredients;
import com.spoonsors.spoonsorsserver.entity.manager.CertificateDto;
import com.spoonsors.spoonsorsserver.entity.manager.IngredientsDto;
import com.spoonsors.spoonsorsserver.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ManagerService {

    private final ManagerRepository managerRepository;

    private final BMemberRepository bMemberRepository;
    private final IbMemberRepository ibMemberRepository;
    private final IManagerRepository iManagerRepository;
    private final IIngredientsRepository iIngredientsRepository;
    private final SponRepository SponRepository;

    //식재료 등록
    public Ingredients regist(IngredientsDto ingredientsDto, String img) throws IOException {
        if(managerRepository.findByName(ingredientsDto.getIngredientsName()).isPresent()){
            //등록된 식재료일 시 에러
            throw new ApiException(ExceptionEnum.MANAGER01);
        }
        Ingredients addIngredientItem = iManagerRepository.save(ingredientsDto.toEntity());
        addIngredientItem.setIngredientsImage(img);
        return addIngredientItem;
    }

    // 식재료 전체 조회
    public List<Ingredients> findAll(){
        return managerRepository.findAll();
    }

    //name으로 식재료 검색
    public Ingredients findByName(String name){
        if(managerRepository.findByName(name).isPresent()) {
            return managerRepository.findByName(name).get();
        }else{
            //검색한 이름의 식재료가 없을 시 에러
            throw new ApiException(ExceptionEnum.MANAGER03);
        }
    }

    //식재료 수정
    public Ingredients update( Long ingredients_id,  IngredientsDto ingredientsDto, String img){

        Ingredients updateIngredient  = Ingredients.builder()
                .ingredientsId(ingredients_id)
                .ingredientsImage(img)
                .ingredientsName(ingredientsDto.getIngredientsName())
                .productName(ingredientsDto.getProductName())
                .price(ingredientsDto.getPrice())
                .build();

        return iIngredientsRepository.save(updateIngredient);
    }

    // 식재료 삭제
    public void remove(Long ingredients_id){
        if(SponRepository.findByIid(ingredients_id).isPresent()){
            //후원 등록되어 있는 식재료 일시 에러
            throw new ApiException(ExceptionEnum.MANAGER02);
        }
        managerRepository.remove(ingredients_id);
    }

    // 수혜자 증명서 등록 상태 확인
    public List<CertificateDto> certificate(){
        List<BMember> bMember = ibMemberRepository.findAll();
        List<CertificateDto> certificateDtos = new ArrayList<>();
        for(BMember b : bMember){
            CertificateDto certificateDto = new CertificateDto();
            certificateDto.setBMember_id(b.getBMemberId());
            certificateDto.setBMember_name(b.getBMemberName());
            certificateDto.setBMember_birth(b.getBMemberBirth().toString());
            certificateDto.setBMember_phoneNumber(b.getBMemberPhoneNumber());
            certificateDto.setBMember_address(b.getBMemberAddress());
            certificateDto.setBMember_certificate(b.getBMemberCertificate());
            certificateDto.set_verified(b.isVerified());
            certificateDtos.add(certificateDto);
        }
        return certificateDtos;
    }

    // 수혜자 증명서 등록 상태 변경
    public String isVerified(String bMember_id, int state){

        if(state==1) {
            bMemberRepository.updateVerified(bMember_id, state, 1);
            return bMember_id+ "수혜자 증명서 승낙 완료";
        }
        bMemberRepository.updateVerified(bMember_id, state, 0);
        return bMember_id+ "수혜자 증명서 거절 완료";


    }
}
