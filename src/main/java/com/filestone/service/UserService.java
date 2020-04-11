package com.filestone.service;

import java.util.UUID;

import com.filestone.entity.UserDetail;
import com.filestone.entity.Users;

/**
 * An Interface  for the {@link Users} DAO-Layer service
 * @author Hoffman
 *
 */
public interface UserService {
	
	public boolean createUser(Users user) ;
	
	public Users loginUser(UserDetail details);

	public Users getUserById(UUID id) ;

	public boolean logout(Users user) ;
	
	public Users findByUsernameAndPassword(String username, String password) ;
	
	public Users findByUsername(String username) ;
	 
	

}
