package com.spoonsors.spoonsorsserver.service.authorize;

import com.spoonsors.spoonsorsserver.entity.authorize.Auth;
import com.spoonsors.spoonsorsserver.repository.IAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final IAuthRepository authRepository;

    @Transactional
    public Auth addAuth(Auth auth){
        //save
        Auth save = authRepository.save(auth);
        log.info("auth={}",auth);
        Optional<Auth> result = authRepository.findById(save.getPhoneNum());
        log.info("authRepository.findById(save.getPhoneNum())={}",authRepository.findById(save.getPhoneNum()));
        // Handling
        // 해당 data 존재시 return.
        if(result.isPresent()) {
            return result.get();
        }else {throw new RuntimeException("Database has no Data");}
    }

    @Transactional(readOnly = true)
    public Auth getAuthById(String authId){
        Optional<Auth> result = authRepository.findById(authId);
        Auth auth = result.get();
        log.info("----------------log----auth---------{}", auth);
        log.info("----------------log----auth.phn---------{}", auth.getPhoneNum());
        log.info("----------------log----auth.code---------{}", auth.getCode());
        if(result.isPresent()) {
            return result.get();
        }else {throw new RuntimeException("Database has no Data");}
    }
}
