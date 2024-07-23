package com.spoonsors.spoonsorsserver.controller.review;

import com.spoonsors.spoonsorsserver.entity.Review;
import com.spoonsors.spoonsorsserver.entity.review.GetReviewDto;
import com.spoonsors.spoonsorsserver.service.Image.S3Uploader;
import com.spoonsors.spoonsorsserver.service.review.ReviewService;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final S3Uploader s3Uploader;

    //리뷰 둥록
    @PostMapping(value = "/review/create/{post_id}", consumes = {MediaType.APPLICATION_JSON_VALUE, "multipart/form-data"})
    public Review wirteReview(@PathVariable Long post_id, @RequestPart(value = "reviewTxt") String reviewTxt, @RequestPart(value = "img") MultipartFile img){
        Review review = null;
        try {
            String url = s3Uploader.upload(img,"review");
            review = reviewService.writeReview(post_id, reviewTxt, url);
        }catch (IOException e){
            e.printStackTrace();
        }

        return review;
    }

    // 내가 작성한 리뷰 확인
    @GetMapping("/review/findMyReview/{bMemberId}")
    public List<Review> findMyReview(@PathVariable String bMemberId){
        List<Review> myReview = null;
        try{
            myReview = reviewService.findMyReview(bMemberId);
        }catch (PersistenceException e){
            e.getMessage();
        }
        return myReview;
    }

    // 내가 받은 리뷰 확인(후원자)
    @GetMapping("/sMember/receivedReview/{sMemberId}")
    public Set<GetReviewDto> getReview(@PathVariable String sMemberId){

        return reviewService.getSponReviewList(sMemberId);
    }
}
