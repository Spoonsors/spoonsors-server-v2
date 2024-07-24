package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IIngredientsRepository extends JpaRepository<Ingredients, Long> {
    @Query("SELECT i FROM Ingredients i WHERE i.ingredientsName =:name")
    Optional<Ingredients> findByIngredientsName(String name);
}
