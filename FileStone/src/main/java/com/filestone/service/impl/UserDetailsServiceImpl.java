package com.filestone.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.filestone.entity.Role;
import com.filestone.entity.Users;
import com.filestone.service.repository.UserRepository;

/**
 * A Service-Implementation class for the {@link UserDetails} DAO-Layer service
 * 
 * @author Hoffman
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	/*
	 * Spring Dependency Injection
	 */
	@Autowired
	private UserRepository userRepository;

	/*
	 * Get User By user name from DB
	 * 
	 * @param username
	 * @param password
	 * @return {@link UserDetails}
	 */
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userRepository.findByUsername(username);
		if (user == null) {
			return null;
		}

		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		for (Role role : user.getRoles()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				grantedAuthorities);
	}

}
