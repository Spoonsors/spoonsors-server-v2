package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.BMember;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BMemberRepository {
    private final EntityManager em;

    public Optional<BMember> findByNickname(String nickname) {
        return em.createQuery("SELECT b FROM BMember b WHERE b.bMember_nickname = :nickname", BMember.class)
                .setParameter("nickname", nickname)
                .getResultList().stream().findAny();
    }

    public Optional<BMember> findId(String name, String phoneNum){
        return em.createQuery("SELECT b FROM BMember b WHERE b.bMember_name = :name and b.bMember_phoneNumber= : phoneNum", BMember.class)
                .setParameter("name",name)
                .setParameter("phoneNum", phoneNum)
                .getResultList().stream().findAny();
    }

    public Optional<BMember> findPwd(String id, String name, String phoneNum){
        return em.createQuery("SELECT b FROM BMember b WHERE b.bMember_id = : id and b.bMember_name = :name and b.bMember_phoneNumber= : phoneNum", BMember.class)
                .setParameter("id",id)
                .setParameter("name",name)
                .setParameter("phoneNum", phoneNum)
                .getResultList().stream().findAny();
    }
    @Modifying
    @Transactional
    public int changePwd(String id, String pwd){
        return em.createQuery("UPDATE BMember b SET b.bMember_pwd = :pwd WHERE b.bMember_id = :id")
                .setParameter("id",id)
                .setParameter("pwd",pwd)
                .executeUpdate();

    }
    public void putToken(Map<String, String> token){
        BMember bMember = em.find(BMember.class, token.get("id"));
        bMember.setToken(token.get("token"));
    }
    @Modifying
    @Transactional
    public void updateVerified(String bMember_id, int verifiedState, int postState){
        em.createQuery("UPDATE BMember b SET b.is_verified = :verifiedState,b.can_post = :postState WHERE b.bMember_id = :bMember_id")
                .setParameter("bMember_id",bMember_id)
                .setParameter("verifiedState",verifiedState)
                .setParameter("postState",postState)
                .executeUpdate();
    }

}
