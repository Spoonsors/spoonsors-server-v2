package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.Spon;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ISponRepository extends JpaRepository<Spon, Long> {

    @Modifying
    @Query("UPDATE Spon s SET s.tid = :tid WHERE s.sponId = :sponId")
    void updateTid(@Param("tid") String tid, @Param("sponId") Long sponId);
}