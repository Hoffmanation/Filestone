package com.filestone.service.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.filestone.entity.FileInfo;
import com.filestone.entity.Role;
import com.filestone.entity.UserDetail;
import com.filestone.entity.Users;
import com.filestone.pojo.LoginError;
import com.filestone.pojo.Message;
import com.filestone.pojo.RepositoryInfo;
import com.filestone.service.SecurityService;
import com.filestone.service.UserService;
import com.filestone.service.impl.AccessTokenServiceImpl;
import com.filestone.util.AppUtil;
import com.filestone.util.Constants;
import com.filestone.util.FileUploadUtil;
import com.filestone.util.UserValidator;

/**
 * A service manager class that will handle the Business-Logic for
 * {@link UserDetail} ,{@link Users} and {@link Role} Entities. This class
 * serves us as the layer that connect between the REST-API's Layer and the DAO
 * layer for the Users and he's relationship
 * 
 * @author Hoffman
 *
 */
@Service
public class UserServiceManager {

	/*
	 * Spring Dependency Injection
	 */
	@Autowired
	private UserService userService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private UserValidator userValidator;
	@Autowired
	private AccessTokenServiceImpl accessTokenService;
	@Autowired
	private AppUtil appUtil;

	public Response registration(UserDetail userDetail, BindingResult bindingResult, HttpSession session) {
		userValidator.validate(userDetail, bindingResult);
		if (bindingResult.hasErrors()) {
			LoginError errors = userValidator.getError(bindingResult);
			return Response.status(401).entity(errors).build();
		}

		Users user = new Users(userDetail.getUsername(), appUtil.encryptPassword(userDetail.getPassword()),
				userDetail.getPasswordConfirm());
		userService.createUser(user);
		FileUploadUtil.createUserResourcesFolder(userDetail);
		login(userDetail, session);
		return Response.status(200).entity(Status.OK.getReasonPhrase()).build();
	}

	public Response login(UserDetail userDetail, HttpSession session) {
		if (securityService.autologin(userDetail.getUsername(), userDetail.getPassword())) {
			Users user = userService.loginUser(userDetail);
			session.setAttribute(Constants.USER_LOGIN, user);
			return Response.status(200).entity(Status.OK.getReasonPhrase()).build();
		}
		return Response.status(401).entity("*Username or password are incorrect.").build();
	}

	public Response updatePassword(String newPassword, String token, String email, HttpServletRequest req,
			HttpSession session) {
		Users user = userService.findByUsername(email);
		if (accessTokenService.isEligible(user, token)) {
			user.setPassword(newPassword);
			userService.createUser(user);
			return Response.status(200).entity(new Message("Your password has been rest successfully")).build();
		}
		return Response.status(200).entity(new Message("*Request not authorized")).build();
	}

	public Response logout(HttpSession session) {
		Users user = (Users) session.getAttribute(Constants.USER_LOGIN);
		userService.logout(user);
		session.invalidate();
		if (session != null) {
			session = null;
		}
		return Response.status(200).entity(Status.OK.getReasonPhrase()).build();

	}
}
