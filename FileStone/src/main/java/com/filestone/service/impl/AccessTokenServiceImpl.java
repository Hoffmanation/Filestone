package com.filestone.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.filestone.entity.FileInfo;
import com.filestone.entity.Users;

/**
 * A Service class that manage Access-Token registry to be used be the 'Forgot my password' flow
 * When a user clicks on the 'Forgot my password' a unique token will be generated and stored inside the @param tokenRegister  Map
 * An email will be send with this exact  token and will be validate when the user will send the request back to the server
 * @author Hoffman
 *
 */
@Component
public class AccessTokenServiceImpl {

	//Access-Token registry
	Map<String, String> tokenRegister ;
	
	public AccessTokenServiceImpl() {
		tokenRegister = new ConcurrentHashMap<String, String>();
	}
	
	/**
	 * Method will validate if user have the same Access-Token that was send to hem in the 'Reset your password' mail
	 * @param user
	 * @param token
	 * @return {@link Boolean}
	 */
	public boolean isEligible(Users user, String token) {
		try {
			String tokenInMap = tokenRegister.get(user.getUsername());
			return token.equals(tokenInMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Method will add a new key pair of  <User-mail , Access-Token> to the registry
	 * @param sendConformationMailTo
	 * @param token
	 */
	public void  addAccessToken(String sendConformationMailTo, String token) {
		try {
			 tokenRegister.put(sendConformationMailTo, token) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	
}
