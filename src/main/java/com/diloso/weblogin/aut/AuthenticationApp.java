package com.diloso.weblogin.aut;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public interface AuthenticationApp {

	Long findFirm(HttpServletRequest arg0, HttpServletResponse arg1);
	
	boolean isRestrictedNivelUser(HttpServletRequest request, HttpServletResponse response);
}
