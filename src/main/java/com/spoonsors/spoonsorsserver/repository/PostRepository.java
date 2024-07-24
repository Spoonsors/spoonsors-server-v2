package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.BMember;
import com.spoonsors.spoonsorsserver.entity.Post;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;



@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    public void changeRemain(Long post_id){
        Post post = em.find(Post.class, post_id);
        post.setRemainSpon(post.getRemainSpon()-1);
    }

    public String changeState(Long post_id){
        Post post = em.find(Post.class, post_id);
        if(post.isPostState() && !post.isHasReview()){
            post.setPostState(false);
            return "글 마감 취소 완료";
        }else{
            post.setPostState(true);
            return "글 마감 완료";
        }
    }

    public void canPost(String bMemberId){
        BMember bMember = em.find(BMember.class, bMemberId);
        if(bMember.isCanPost()){
            bMember.setCanPost(false);
        }else{
            bMember.setCanPost(true);
        }
    }

    public void changeReviewState(Long post_id){
        Post post = em.find(Post.class, post_id);
        post.setHasReview(true);
    }

    public void delete(Long postId){
        Post post = em.find(Post.class, postId);
        canPost(post.getWriter().getBMemberId());
        em.remove(findById(postId));
    }

    // post id값으로 조회
    public Post findById(Long postId) {
        return em.find(Post.class, postId);
    }
}
