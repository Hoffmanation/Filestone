package com.filestone.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.filestone.entity.Role;
import com.filestone.entity.UserDetail;
import com.filestone.entity.Users;
import com.filestone.service.UserService;
import com.filestone.service.repository.RoleRepository;
import com.filestone.service.repository.UserRepository;


/**
 * A Service-Implementation class  for the {@link User} DAO-Layer service
 * @author Hoffman
 *
 */
@Service
public class UserServiceImpl implements UserService {
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getSimpleName());
	
	/*
	 * Spring Dependency Injection
	 */
	@Autowired
	private RoleRepository roleDao;
	
	@Autowired
	private UserRepository userDao;


	/**
	 * CRUD methods for the {@link Users} entity
	 */	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)	
	public boolean createUser(Users user) {
        Role newRole = new Role("ROLE_USER");
        roleDao.save(newRole);
        Set<Role> roles = new HashSet<>();
        roles.add(newRole);
		user.setRoles(roles);
		userDao.save(user);
		return true;
	}
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)	
	public Users loginUser(UserDetail userDetail) {
		Users user = findByUsername(userDetail.getUsername()) ;
		List<UserDetail> details = user.getDetails();
		details.add(new UserDetail(user, user.getUsername(), new Date(System.currentTimeMillis()),null));
		user.setDetails(details);
		Users updatedUser =  userDao.save(user);
		return updatedUser;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Users findByUsernameAndPassword(String username, String password) {
		return userDao.findByUsernameAndPassword(username, password);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public  Users getUserById(UUID id) {
				return userDao.getUserById(id);

	}


	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean logout(Users user) {
		Users tempUser = getUserById(user.getId()) ;
		List<UserDetail> detailsList = tempUser .getDetails();
		detailsList.get(detailsList.size()-1).setLogoutTime(new Date(System.currentTimeMillis()));
		tempUser .setDetails(detailsList);
		 userDao.saveAndFlush(tempUser) ;
		Users updatedUser = userDao.findOne(tempUser .getId()) ;
		return updatedUser!=null; 
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Users findByUsername(String username) {
		return userDao.findByUsername(username);
	}

}