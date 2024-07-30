package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.MealPlanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IMealPlannerRepository extends JpaRepository<MealPlanner, Long> {
    List<MealPlanner> findAllByDeletedYnFalse();

    Optional<MealPlanner> findMealPlannerByMealPlannerNameAndDeletedYnFalse(String mealPlannerName);
}
