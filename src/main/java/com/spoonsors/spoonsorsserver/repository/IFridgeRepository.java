package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.Fridge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFridgeRepository extends JpaRepository<Fridge, Long> {
}
