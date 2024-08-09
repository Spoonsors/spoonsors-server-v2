package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IManagerRepository extends JpaRepository<Ingredients, Long> {
    Optional<Ingredients> findByIngredientsName(String ingredientsName);
}
