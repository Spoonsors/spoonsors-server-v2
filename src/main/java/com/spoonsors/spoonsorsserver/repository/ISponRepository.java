package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.Spon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISponRepository extends JpaRepository<Spon, Long> {
}
