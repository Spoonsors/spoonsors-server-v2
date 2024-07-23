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
        post.setRemain_spon(post.getRemain_spon()-1);
    }

    public String changeState(Long post_id){
        Post post = em.find(Post.class, post_id);
        if(post.getPost_state()==1 && post.getHas_review()==0){
            post.setPost_state(0);
            return "글 마감 취소 완료";
        }else{
            post.setPost_state(1);
            return "글 마감 완료";
        }
    }

    public void canPost(String bMemberId){
        BMember bMember = em.find(BMember.class, bMemberId);
        if(bMember.getCan_post()==0){
            bMember.setCan_post(1);
        }else{
            bMember.setCan_post(0);
        }
    }

    public void changeReviewState(Long post_id){
        Post post = em.find(Post.class, post_id);
        post.setHas_review(1);
    }

    public void delete(Long postId){
        Post post = em.find(Post.class, postId);
        canPost(post.getBMember().getBMember_id());
        em.remove(findById(postId));
    }

    // post id값으로 조회
    public Post findById(Long postId) {
        return em.find(Post.class, postId);
    }
}
