package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.MealPlanner;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MealPlannerRepository {

    private final EntityManager em;

    // 식단 전체 조회
    public List<MealPlanner> findAll(){
        return em.createQuery("SELECT u FROM MealPlanner u", MealPlanner.class)
                .getResultList();
    }
    // 식단 이름으로 검색
    public MealPlanner findByName(String mealplanner_name){
        return em.createQuery("SELECT u FROM MealPlanner u WHERE u.mealPlanner_name = :mealplanner_name", MealPlanner.class)
                .setParameter("mealplanner_name", mealplanner_name)
                .getSingleResult();
    }

    // 식단 등록
    public void regist(MealPlanner mealPlanner){em.persist(mealPlanner);}
    // 식단 id로 검색
    public MealPlanner findById(Long mealPlanner_id){return em.find(MealPlanner.class , mealPlanner_id);}

    // 식단 삭제
    public void remove(Long mealPlanner_id){em.remove(findById(mealPlanner_id));}
}
