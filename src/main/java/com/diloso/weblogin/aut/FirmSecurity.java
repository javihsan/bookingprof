package com.diloso.weblogin.aut;

import java.util.logging.Logger;

import com.diloso.bookhair.app.controllers.InitController;
import com.diloso.bookhair.app.negocio.manager.IFirmSecurityManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FirmSecurity implements AuthenticationApp {
	
	protected static final Logger log = Logger.getLogger(FirmSecurity.class.getName());
	
	protected IFirmSecurityManager firmSecurityManager;
	
	public void setFirmSecurityManager(IFirmSecurityManager firmSecurityManager) {
		this.firmSecurityManager = firmSecurityManager;
	}
	
	public Long findFirm(HttpServletRequest arg0, HttpServletResponse arg1){

		String serverName = arg0.getServerName();
		String domain = "";
		if (InitController.isAppUrl(serverName)){
			String path = arg0.getRequestURI().toLowerCase();
			String[] a = path.split("/");
			domain = InitController.DEMO_APP;
			if  (a.length>0){
				domain = a[1];
			}
		} else {
			domain = firmSecurityManager.getDomainServer(serverName);
		}

		return firmSecurityManager.findId(domain);
		
	}
	
	public boolean isRestrictedNivelUser(HttpServletRequest arg0, HttpServletResponse arg1){
			
		String serverName = arg0.getServerName();
		String domain = "";
		if (InitController.isAppUrl(serverName)){
			String path = arg0.getRequestURI().toLowerCase();
			String[] a = path.split("/");
			domain = InitController.DEMO_APP;
			if  (a.length>0){
				domain = a[1];
			}
		} else {
			domain = firmSecurityManager.getDomainServer(serverName);
		}
		
		return firmSecurityManager.isRestrictedNivelUser(domain);
	}
	
}
