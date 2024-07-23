package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.Ingredients;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ManagerRepository {

    private final EntityManager em;

    //식재료 등록, 수정
    public Ingredients regist(Ingredients ingredientItem){
        em.persist(ingredientItem);
        return ingredientItem;
    }

    //모든 식재료 조회
    public List<Ingredients> findAll() {
        return em.createQuery("SELECT u FROM Ingredients u", Ingredients.class)
                .getResultList();
    }

    // 식재료 삭제
    public void remove(Long ingredients_id) {
        em.remove(findById(ingredients_id));
    }

    // 식재료 id값으로 조회
    public Ingredients findById(Long ingredients_id) {
        return em.find(Ingredients.class, ingredients_id);
    }

    // 식재료 이름으로 조회
    public Optional<Ingredients> findByName(String findName){
        return em.createQuery("SELECT u FROM Ingredients u WHERE u.ingredients_name = :findName", Ingredients.class)
                .setParameter("findName", findName)
                .getResultList().stream().findAny();
    }

}
