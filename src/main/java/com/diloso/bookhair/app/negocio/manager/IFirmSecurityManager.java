package com.diloso.bookhair.app.negocio.manager;

public interface IFirmSecurityManager {

	String getDomainServer(String server);
		
	Long findId(String domain);
	
	boolean isRestrictedNivelUser(String domain);
		
 }