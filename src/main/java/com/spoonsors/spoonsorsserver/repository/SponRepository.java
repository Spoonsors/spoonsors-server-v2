package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.Spon;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SponRepository {

    private final EntityManager em;

    public void putTid(Long spon_id, String tid){
        Spon spon = em.find(Spon.class, spon_id);
        spon.setTid(tid);
    }

    public List<Spon> findByTid(String tid) {
        return em.createQuery("SELECT s FROM Spon s WHERE s.tid = :tid", Spon.class)
                .setParameter("tid", tid)
                .getResultList();
    }

    public List<Spon> checkSpon(Long postId){
        return em.createQuery("SELECT s FROM Spon s WHERE s.post.post_id = :postId", Spon.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public Optional<Spon> findByIid(Long iid) {
        return em.createQuery("SELECT s FROM Spon s WHERE s.ingredients.ingredients_id = :iid", Spon.class)
                .setParameter("iid", iid)
                .getResultList().stream().findAny();
    }
}
