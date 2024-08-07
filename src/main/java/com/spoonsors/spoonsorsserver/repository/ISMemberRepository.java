package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.SMember;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISMemberRepository extends JpaRepository<SMember, String> {
    Optional<SMember> findByMemberNickname(String nickname);

    @Modifying
    @Query("update SMember s set s.token = :token where s.memberId = :memberId")
    void updateToken(@Param("memberId") String memberId, @Param("token") String token);
}
