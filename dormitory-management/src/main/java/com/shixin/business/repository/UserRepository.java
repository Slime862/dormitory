package com.shixin.business.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shixin.business.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
	User findByUsernameAndPasswordAndPermission(String username, String password, Integer permission);
	
	User findByUsername(String username);
}
