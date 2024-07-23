package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.Fridge;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FridgeRepository {
    private final EntityManager em;

    public Fridge addFridgeItem(Fridge fridgeItem){
        em.persist(fridgeItem);
        return fridgeItem;
    }

    public List<Fridge> getFridgeItems(String bMemberId){
        return em.createQuery("SELECT f FROM Fridge f where f.bMember.bMember_id= :bMemberId", Fridge.class)
                .setParameter("bMemberId", bMemberId)
                .getResultList();
    }

    public void deleteFridgeItem( Long fridge_id){
        em.remove(findById(fridge_id));

    }

    public Fridge findById(Long fridge_id) {
        return em.find(Fridge.class, fridge_id);
    }
}
