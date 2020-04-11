package com.filestone.conf;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import com.filestone.util.Constants;
import com.filestone.entity.Users;

/**
 * One of the Application HTTP Filter.
 *1.Provides the HTTP request validation and/or creation of the access token - XSRF-TOKEN and X-CSRF-TOKEN.
 *2.Provides the HTTP request validation of an already logged in user
 * @author Hoffman
 *
 */
public class FileStoneFilter extends OncePerRequestFilter {


	/**
	 * Main Filter method 
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String requestUrl = null;
		HttpSession session = request.getSession(false);

		
		//validates that a session exist and if a user object present in it.
		//If it doesn't - redirect to login page.
		//If it does - and the user is not trying to 
		//1.invoke any rest-endpooint ('/filestone') and 
		//2.get a static resource (css,html,js files) redirect to the index page 
		//else continue request invocation
		if (session != null) {
			Users userDetail = (Users) session.getAttribute(Constants.USER_LOGIN);
			if (userDetail != null) {
				requestUrl = request.getRequestURL().toString();
				if (!requestUrl.contains("/resources") && !requestUrl.endsWith("index.html")&& !requestUrl.contains("/filestone")) {
					String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath() + "/index.html";
					response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
					response.setHeader("Location", url);
					return ;
				}
			}

		}
	
		
		//Validate that the Csrf-Token and the XSRF-TOKEN cookie are present in 
		//the request and that they are the same value  
		CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
		if (csrf != null) {
			Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
			String token = csrf.getToken();
			if (cookie == null || token != null && !token.equals(cookie.getValue())) {
				cookie = new Cookie("XSRF-TOKEN", token);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
		filterChain.doFilter(request, response);
	}
}