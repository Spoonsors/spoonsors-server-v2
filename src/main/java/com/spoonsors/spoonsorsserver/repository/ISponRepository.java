package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.Spon;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ISponRepository extends JpaRepository<Spon, Long> {

    @Modifying
    @Query("UPDATE Spon s SET s.tid = :tid, s.sponDate = :sponDate, s.sMember.id = :sMemberId, s.isSponed = true WHERE s.sponId = :sponId")
    void updateSpon(@Param("tid") String tid, @Param("sponId") Long sponId, @Param("sponDate") LocalDate sponDate, @Param("sMemberId") String sMemberId);
}