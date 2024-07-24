package com.spoonsors.spoonsorsserver.service.spon;

import com.spoonsors.spoonsorsserver.customException.ApiException;
import com.spoonsors.spoonsorsserver.customException.ExceptionEnum;
import com.spoonsors.spoonsorsserver.entity.Ingredients;
import com.spoonsors.spoonsorsserver.entity.Post;
import com.spoonsors.spoonsorsserver.entity.SMember;
import com.spoonsors.spoonsorsserver.entity.Spon;
import com.spoonsors.spoonsorsserver.entity.spon.SponDto;
import com.spoonsors.spoonsorsserver.repository.*;
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
    private final SponRepository sponRepository;
    private final PostRepository postRepository;
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
   public String applySpon(String tid, String sMemberId){

       List <Spon> sponList = sponRepository.findByTid(tid);

        Optional<SMember> optionalSMember = iSMemberRepository.findById(sMemberId);
        SMember sMember = optionalSMember.get();

        for(int i=0;i<sponList.size();i++){
            Post post = sponList.get(i).getPost();
            Spon spon2=Spon.builder()
                    .sponId(sponList.get(i).getSponId())
                    .sponDate(LocalDate.now())
                    .ingredients(sponList.get(i).getIngredients())
                    .sMember(sMember)
                    .post(post)
                    .isSponed(true)
                    .tid(tid)
                    .build();

            iSponRepository.save(spon2);

            postRepository.changeRemain(post.getPostId());
            //자동으로 post상태 바꿔주는 코드
            if(post.getRemainSpon()==0){ //더이상 남은 후원이 없으면 글 완료 처리
                postRepository.changeState(post.getPostId());
            }
        }

       return "후원 완료";
   }

    //후원 상태 확인
    public String checkSpon(Long spon_id){
        Optional<Spon> optionalSpon= iSponRepository.findById(spon_id);
        Spon spon = optionalSpon.get();

        //후원이 완료된 물품일 경우 오류
        if(spon.isSponed()){
            throw new ApiException(ExceptionEnum.SPON01);
        }

        return "후원 가능";
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
