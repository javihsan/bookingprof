package com.diloso.weblogin.aut;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface AuthenticationApp {

	Long findFirm(HttpServletRequest arg0, HttpServletResponse arg1);
	
	boolean isRestrictedNivelUser(HttpServletRequest request, HttpServletResponse response);
}
