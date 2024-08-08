package com.spoonsors.spoonsorsserver.service.member;

import com.spoonsors.spoonsorsserver.customException.ApiException;
import com.spoonsors.spoonsorsserver.customException.ExceptionEnum;
import com.spoonsors.spoonsorsserver.entity.BMember;
import com.spoonsors.spoonsorsserver.entity.Post;
import com.spoonsors.spoonsorsserver.entity.Review;
import com.spoonsors.spoonsorsserver.entity.Spon;
import com.spoonsors.spoonsorsserver.entity.bMember.ViewPostDto;
import com.spoonsors.spoonsorsserver.entity.bMember.WritePostDto;
import com.spoonsors.spoonsorsserver.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final IPostRepository iPostRepository;
    private final IbMemberRepository ibMemberRepository;
    private final IReviewRepository iReviewRepository;
    private final PostRepository postRepository;

    private final SponRepository sponRepository;
    //글 작성
    public Post writePost(String bMemberId, WritePostDto writePostDto) {
        if(!ibMemberRepository.findById(bMemberId).get().isVerified()){
            throw new ApiException(ExceptionEnum.POST01);
        }
        if(!ibMemberRepository.findById(bMemberId).get().isCanPost()) {
            throw new ApiException(ExceptionEnum.POST02); //후원 등록 가능 상태가 아님
        }

        Optional<BMember> optionalBMember = ibMemberRepository.findById(bMemberId);
        BMember bMember = optionalBMember.get();
        writePostDto.setBMember(bMember);
        writePostDto.setPost_date(LocalDate.now());
        postRepository.canPost(bMemberId);
        //글 저장
        Post post = iPostRepository.save(writePostDto.toEntity());
        return post;


    }

    //전체 글 조회
    public List<Post> viewAllPosts(){
        List<Post> posts=iPostRepository.findAll();
        return posts;
    }

    //단일 글 조회
    public ViewPostDto viewPost(Long postId){
        Optional<Post> optionalPost=iPostRepository.findById(postId);
        Post post=optionalPost.get();
        ViewPostDto viewPostDto = new ViewPostDto();
        viewPostDto.setPost(post);
        if(post.isHasReview()){
            Optional<Review> optionalReview=iReviewRepository.findById(post.getPostId());
            Review review = optionalReview.get();
            viewPostDto.setReview(review);
        }
        return viewPostDto;
    }

    //내가 작성한 글 보기
    public List<Post> viewMyPosts(String bMemberId){
        Optional<BMember> optionalBMember =ibMemberRepository.findById(bMemberId);
        BMember bMember = optionalBMember.get();
        return bMember.getPosts();
    }

    //글 상태 변경
    public String changePostState(Long post_id) {
        boolean check = checkSpon(post_id);
        if(!check){ //후원이 하나 이상 등록
            if(hasReview(post_id)) { //리뷰를 가지고 있으면 글 상태 변경 불가
                throw new ApiException(ExceptionEnum.POST06);
            }
            return postRepository.changeState(post_id);
        }
        //후원 상품이 없는 글은 마감 불가
        throw new ApiException(ExceptionEnum.POST03);
    }

    //후원글 삭제
    public void delete(Long post_id) throws IOException{
        Optional<Post> optionalPost=iPostRepository.findById(post_id);
        Post post=optionalPost.get();
        if(post.isPostState()){
            //후원 완료된 글은 삭제 불가
            throw new ApiException(ExceptionEnum.POST04);
        }
        boolean check = checkSpon(post_id);
        if(check){
            postRepository.delete(post_id);
        }else{
            //후원이 하나라도 있으면 글 삭제 불가
            throw new ApiException(ExceptionEnum.POST05);
        }

    }

    //스폰이 하나라도 등록 되어 있는지 확인하는 함수. 하나 이상 등록돼있으면 false, 모두 등록되지 않았으면 true
    public boolean checkSpon(Long post_id){
        boolean check = true;
        Optional<Post> optionalPost= iPostRepository.findById(post_id);
        Post post = optionalPost.get();
        List<Spon> sponList = sponRepository.checkSpon(post_id);

        if(post.getRemainSpon() != sponList.size()){ //남은 스폰 수와 등록된 수가 같지 않으면 후원이 하나라도 등록된 것임.
            check = false;
        }
        return check;
    }

    public boolean hasReview(Long post_id){
        Optional<Post> optionalPost= iPostRepository.findById(post_id);
        Post post = optionalPost.get();
        return post.isHasReview();
    }

    public void changeRemain(Long postId, int sponAmount){
        Post post = iPostRepository.findById(postId).orElseThrow(() -> new ApiException(ExceptionEnum.POST07));
        postRepository.changeRemain(postId, sponAmount);
        if(post.getRemainSpon()==0){ //더이상 남은 후원이 없으면 글 완료 처리
            postRepository.changeState(post.getPostId());
        }
    }
}
