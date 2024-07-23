package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.SMember;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SMemberRepository {

    private final EntityManager em;

    public Optional<SMember> findByNickname(String nickname) {
        return em.createQuery("SELECT s FROM SMember s WHERE s.sMember_nickname = :nickname", SMember.class)
                .setParameter("nickname", nickname)
                .getResultList().stream().findAny();
    }

    public Optional<SMember> findId(String name, String phoneNum){
        return em.createQuery("SELECT s FROM SMember s WHERE s.sMember_name = :name and s.sMember_phoneNumber= : phoneNum", SMember.class)
                .setParameter("name",name)
                .setParameter("phoneNum", phoneNum)
                .getResultList().stream().findAny();
    }

    public Optional<SMember> findPwd(String id,String name, String phoneNum){
        return em.createQuery("SELECT s FROM SMember s WHERE s.sMember_id = :id and s.sMember_name = :name and s.sMember_phoneNumber= : phoneNum", SMember.class)
                .setParameter("id",id)
                .setParameter("name",name)
                .setParameter("phoneNum", phoneNum)
                .getResultList().stream().findAny();
    }

    @Modifying
    @Transactional
    public int changePwd(String id, String pwd){
        return em.createQuery("UPDATE SMember s SET s.sMember_pwd = :pwd WHERE s.sMember_id = :id")
                .setParameter("id",id)
                .setParameter("pwd",pwd)
                .executeUpdate();

    }
    public void putToken(Map<String, String> token){
        SMember sMember = em.find(SMember.class, token.get("id"));
        sMember.setToken(token.get("token"));
    }
}