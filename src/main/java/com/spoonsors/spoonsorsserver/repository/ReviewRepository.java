package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.Review;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {
    private final EntityManager em;

    public Optional<Review> findById(Long postId){
        List<Review> reviews = em.createQuery("SELECT r FROM Review r where r.post.post_id= :postId", Review.class)
                .setParameter("postId", postId)
                .getResultList();
        return reviews.stream().findAny();
    }
}
