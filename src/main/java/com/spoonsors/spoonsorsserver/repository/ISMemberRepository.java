package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.SMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ISMemberRepository extends JpaRepository<SMember, String> {
    //Optional<SMember> findByEmail(String email);
}
