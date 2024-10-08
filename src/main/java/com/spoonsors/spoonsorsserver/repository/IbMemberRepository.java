package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.BMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IbMemberRepository extends JpaRepository<BMember, String> {
    //Optional<BMember> findByEmail(String email);
    Optional<BMember> findByMemberNickname(String nickname);
}
