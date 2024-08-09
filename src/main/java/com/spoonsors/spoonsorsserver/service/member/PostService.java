package com.spoonsors.spoonsorsserver.service.member;

import com.spoonsors.spoonsorsserver.customException.ApiException;
import com.spoonsors.spoonsorsserver.customException.ExceptionEnum;
import com.spoonsors.spoonsorsserver.entity.BMember;
import com.spoonsors.spoonsorsserver.entity.Post;
import com.spoonsors.spoonsorsserver.entity.Review;
import com.spoonsors.spoonsorsserver.entity.post.PostsReadResDto;
import com.spoonsors.spoonsorsserver.entity.post.ViewPostDto;
import com.spoonsors.spoonsorsserver.entity.post.WritePostDto;
import com.spoonsors.spoonsorsserver.repository.IPostRepository;
import com.spoonsors.spoonsorsserver.repository.IReviewRepository;
import com.spoonsors.spoonsorsserver.repository.IbMemberRepository;
import com.spoonsors.spoonsorsserver.repository.SponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final IPostRepository iPostRepository;
    private final IReviewRepository iReviewRepository;
    private final IbMemberRepository ibMemberRepository; //TODO: security후 삭제
    private final SponRepository sponRepository;
    //글 작성
    @Transactional
    public Post writePost(BMember member, WritePostDto writePostDto) {
        if(!member.isVerified()){
            throw new ApiException(ExceptionEnum.POST01); //인증이 되지 않은 사용자
        }
        if(!member.isCanPost()) {
            throw new ApiException(ExceptionEnum.POST02); //후원 등록 가능 상태가 아님
        }

        member.setCanPost(false);

        return iPostRepository.save(writePostDto.toEntity(member,writePostDto.getItem_list().size()));

    }

    //전체 글 조회
    public List<PostsReadResDto> viewAllPosts(){
        List<PostsReadResDto> dto = new ArrayList<>();
        iPostRepository.findByDeletedYnFalseOrderByCreatedAtDesc().forEach(post -> dto.add(post.toPostsReadResDto()));
        return dto;
    }

    //단일 글 조회
    @Transactional
    public ViewPostDto viewPost(Long postId){
        Post post=iPostRepository.findById(postId).orElseThrow(() -> new ApiException(ExceptionEnum.POST07));
        Review review = iReviewRepository.findById(postId).orElse(null);
        return post.toViewPostDto(review);
    }

    //내가 작성한 글 보기
    public List<PostsReadResDto> viewMyPosts(BMember member){
        member = ibMemberRepository.findById("user123").get();
        List<PostsReadResDto> dto = new ArrayList<>();
        iPostRepository.findByWriterAndDeletedYnFalseOrderByCreatedAtDesc(member).forEach(post -> dto.add(post.toPostsReadResDto()));
        return dto;
    }

    //글 상태 변경
    @Transactional
    public String changePostState(Long postId) {
        Post post=iPostRepository.findById(postId).orElseThrow(() -> new ApiException(ExceptionEnum.POST07));

        if(post.getSpons().size() == post.getRemainSpon()) throw new ApiException(ExceptionEnum.POST03); //후원 상품이 없는 글은 마감 불가
        if(post.isHasReview()) throw new ApiException(ExceptionEnum.POST06); //리뷰를 가지고 있으면 글 상태 변경 불가

        String s = post.isFinished() ? "마감 취소":"마감";
        post.toggleFinish();
        return s;
    }

    //후원글 삭제
    @Transactional
    public void delete(Long postId) throws IOException{
        Post post=iPostRepository.findById(postId).orElseThrow(() -> new ApiException(ExceptionEnum.POST07));

        if(post.isFinished()) throw new ApiException(ExceptionEnum.POST04); //후원 완료된 글은 삭제 불가
        if(post.getSpons().size() != post.getRemainSpon()) throw new ApiException(ExceptionEnum.POST05);

        post.delete();
    }

    @Transactional
    public void changeRemain(Long postId, int sponAmount) {
        Post post = iPostRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.POST07));

        if (post.getRemainSpon() - sponAmount <= 0) {
            post.updateRemainSponAndFinish(sponAmount);
        } else {
            post.updateRemainSpon(sponAmount);
        }
    }
}
