package com.spoonsors.spoonsorsserver.service.review;

import com.spoonsors.spoonsorsserver.customException.ApiException;
import com.spoonsors.spoonsorsserver.customException.ExceptionEnum;
import com.spoonsors.spoonsorsserver.entity.*;
import com.spoonsors.spoonsorsserver.entity.review.GetReviewDto;
import com.spoonsors.spoonsorsserver.entity.review.ReviewDto;
import com.spoonsors.spoonsorsserver.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final IReviewRepository iReviewRepository;
    private final IPostRepository iPostRepository;
    private final IbMemberRepository ibMemberRepository;
    private final ISMemberRepository isMemberRepository;
    private final PostRepository postRepository;

    //리뷰 작성
    public Review writeReview(Long postId, String reviewTxt, String img)throws IOException {


        Optional<Post> optionalPost =iPostRepository.findById(postId);
        Post post = optionalPost.get();
        if(!post.isPostState()) {throw new ApiException(ExceptionEnum.REVIEW01);}// 후원 마감되지 않은 글은 리뷰 작성 불가능 합니다.
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReview_txt(reviewTxt);
        reviewDto.setPost(post);
        reviewDto.setReview_date(LocalDate.now());

        //리뷰 저장
        Review addRreview = iReviewRepository.save(reviewDto.toEntity());
        addRreview.setReviewImg(img);

        postRepository.changeReviewState(postId);
        postRepository.canPost(post.getWriter().getMemberId());
        return addRreview;

    }

    // 내가 작성한 리뷰 확인
    public List<Review> findMyReview(String bMemberId){
        Optional<BMember> optionalBMember = ibMemberRepository.findById(bMemberId);
        List<Post> postList = optionalBMember.get().getPosts();
        List<Review> myReviewList = new ArrayList<>();
        for(Post p : postList){

            if(p.isHasReview()){
                Optional<Review> optionalReview = iReviewRepository.findById(p.getPostId());
                Review myReview = optionalReview.get();

                myReviewList.add(myReview);
            }
        }
        return myReviewList;
    }

    //내가 받은 리뷰 확인(내가 후원한 글의 리뷰)
    public Set<GetReviewDto> getSponReviewList(String sMemberId){
        Optional<SMember> optionalSMember = isMemberRepository.findById(sMemberId);
        SMember sMember=optionalSMember.get();
        List<Spon> spon = sMember.getSpons();
        Set<GetReviewDto> set = new HashSet<>();
        Set<Long> idSet = new HashSet<>();

        for (Spon s : spon) {
            Post post=s.getPost();
            Long postId=post.getPostId();
            if(!idSet.contains(postId)) { //리턴 리스트에 해당 포스트가 존재하지 않는다면 추가
                if(post.isHasReview()){ //해당 포스트가 리뷰를 가지고 있다면
                    Optional<Review> optionalReview =iReviewRepository.findById(postId);
                    Review review = optionalReview.get();
                    GetReviewDto getReviewDto = new GetReviewDto();
                    getReviewDto.setPost_id(postId);
                    getReviewDto.setReview_img(review.getReviewImg());
                    getReviewDto.setReview_txt(review.getReviewText());
                    getReviewDto.setWrite_date(review.getReviewDate());
                    getReviewDto.setWriter_nickname(post.getWriter().getMemberNickname());
                    set.add(getReviewDto);
                    idSet.add(postId);
                }

            }

        }

        return set;
    }
}
