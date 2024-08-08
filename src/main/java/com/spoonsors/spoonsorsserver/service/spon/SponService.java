package com.spoonsors.spoonsorsserver.service.spon;

import com.spoonsors.spoonsorsserver.customException.ApiException;
import com.spoonsors.spoonsorsserver.customException.ExceptionEnum;
import com.spoonsors.spoonsorsserver.entity.Ingredients;
import com.spoonsors.spoonsorsserver.entity.Post;
import com.spoonsors.spoonsorsserver.entity.SMember;
import com.spoonsors.spoonsorsserver.entity.Spon;
import com.spoonsors.spoonsorsserver.entity.spon.SponDto;
import com.spoonsors.spoonsorsserver.entity.spon.SponInfoDto;
import com.spoonsors.spoonsorsserver.repository.*;
import com.spoonsors.spoonsorsserver.service.member.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SponService {

    private final ManagerRepository managerRepository;
    private final PostService postService;
    private final IPostRepository iPostRepository;
    private final ISponRepository iSponRepository;
    private final ISMemberRepository iSMemberRepository;

    //후원 요청(자립준비청년)
    public void addSpon(List<String> itemlist, Long postId){
        for (String s : itemlist) {
            Ingredients ingredient = managerRepository.findByName(s).get();
            Optional<Post> optionalPost = iPostRepository.findById(postId);
            Post post = optionalPost.get();

            Spon spon = Spon.builder()
                    .sponDate(null)
                    .isSponed(false)
                    .ingredients(ingredient)
                    .post(post)
                    .sMember(null)
                    .build();

            iSponRepository.save(spon);
        }
    }

    //후원 신청(후원자)
   public String applySpon(String tid, List<Long> spons, String sMemberId){

        for (Long sponId : spons) {
            iSponRepository.updateSpon(tid, sponId, LocalDate.now(), sMemberId);
        }

        Spon spon = iSponRepository.findById(spons.get(0)).get();
        postService.changeRemain(spon.getPost().getPostId(), spons.size());


        return "후원 완료";
   }

   // 결제용 후원 정보 생성
    public SponInfoDto getSponInfo(List<Long> sponIds) {
        String itemName = "";
        int totalPrice = 0;

        for (Long sponId : sponIds) {
            Spon spon = iSponRepository.findById(sponId).orElseThrow( () -> new ApiException(ExceptionEnum.SPON02));
            Ingredients ingredients = spon.getIngredients();
            itemName = ingredients.getIngredientsName();
            totalPrice += ingredients.getPrice();
        }

        int quantity = sponIds.size() - 1;
        if (sponIds.size() != 1) {
            itemName = itemName + "외 " + quantity + "건";
        }

        return SponInfoDto.builder().quantity(quantity).itemName(itemName).totalPrice(totalPrice).build();
    }

   //후원 내역
    public List<SponDto> getSponList(String sMemberId){
        Optional<SMember> optionalSMember = iSMemberRepository.findById(sMemberId);
        SMember sMember=optionalSMember.get();
        List<Spon> spon = sMember.getSpons();
        List<SponDto> sponDtos = new ArrayList<>();
        for (Spon s : spon) {

            SponDto sponItem = new SponDto();
            sponItem.setSpon_date(s.getSponDate());
            sponItem.setIngredients_image(s.getIngredients().getIngredientsImage());
            sponItem.setIngredients_name(s.getIngredients().getIngredientsName());
            sponItem.setProduct_name(s.getIngredients().getProductName());
            sponItem.setPost_id(s.getPost().getPostId());
            sponItem.setWriter_nickname(s.getPost().getWriter().getMemberNickname());
            sponItem.setPrice(s.getIngredients().getPrice());

            sponDtos.add(sponItem);
        }
        return sponDtos;
    }


}
