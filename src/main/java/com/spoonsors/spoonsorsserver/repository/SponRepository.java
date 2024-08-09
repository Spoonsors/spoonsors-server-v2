package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.Spon;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SponRepository {

    private final EntityManager em;


    public Optional<Spon> findByIid(Long iid) {
        return em.createQuery("SELECT s FROM Spon s WHERE s.ingredients.ingredients_id = :iid", Spon.class)
                .setParameter("iid", iid)
                .getResultList().stream().findAny();
    }
}
