package com.diloso.weblogin.aut;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.google.appengine.api.users.UserServiceFactory;


public class AccountsAuthenticationEntryPoint implements
		AuthenticationEntryPoint {
	
	protected String redirectUrl;
	
	protected final static String ACCEPT_HEADER = "Accept";
	protected final static String ACCEPT_APP_JSON = "application/json";
	
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		
		if (request.getHeader(ACCEPT_HEADER).equals(ACCEPT_APP_JSON)){
			response.sendError(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value());
		} else {
			String redirectURL = UserServiceFactory.getUserService().createLoginURL(request.getRequestURI());
			response.sendRedirect(redirectURL);
		}
		
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	
	
	
}
