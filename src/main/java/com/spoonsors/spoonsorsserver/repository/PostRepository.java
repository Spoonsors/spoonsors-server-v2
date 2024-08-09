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


    // post id값으로 조회
    public Post findById(Long postId) {
        return em.find(Post.class, postId);
    }
}
