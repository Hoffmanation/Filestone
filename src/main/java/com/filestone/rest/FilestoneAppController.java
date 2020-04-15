package com.filestone.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.filestone.entity.UserDetail;
import com.filestone.pojo.ResetPasswordRequest;
import com.filestone.service.manager.UserServiceManager;

/**
 * A Collection of {@link RestController} class that will accept HTTP request to interact with the JS client
 * A Collection of REST-API's for validating and authenticating user's login, register, update password and logout 
 * @author Hoffman
 *
 */
@RestController
public class FilestoneAppController {
	
	/*
	 * Spring Dependency Injection
	 */
	@Autowired
	private UserServiceManager userServiceManager;

	@RequestMapping(path = "filestone/registration", method = RequestMethod.POST)
	public Response registration(@RequestBody(required = true) UserDetail userDetail, BindingResult bindingResult,HttpSession session) {
		return userServiceManager.registration(userDetail, bindingResult, session);
	}

	@RequestMapping(path = "filestone/login", method = RequestMethod.POST)
	public Response login(@RequestBody UserDetail userDetail, HttpSession session) {
		return userServiceManager.login(userDetail, session);
	} 
	
	@RequestMapping(path = "filestone/updatePassword", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public Response updatePassword(@RequestBody ResetPasswordRequest ResetPasswordRequest ,@Context HttpServletRequest req, HttpSession session) {
		return userServiceManager.updatePassword(ResetPasswordRequest, req, session);
	}
	
	
	@RequestMapping(path = "filestone/logout", method = RequestMethod.GET)
	public Response logout(HttpSession session) {
		return userServiceManager.logout( session);
	}
	
}
