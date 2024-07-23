package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.authorize.Auth;
import org.springframework.data.repository.CrudRepository;

public interface IAuthRepository extends CrudRepository<Auth,String> {
}
