package com.diloso.bookhair.app.negocio.dao;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.FirmDTO;

public interface FirmDAO {

	FirmDTO create(FirmDTO firm) throws Exception;

	FirmDTO remove(long id) throws Exception;

	FirmDTO update(FirmDTO firm) throws Exception;

	FirmDTO getById(long id);

	FirmDTO getFirmDomain(String domain);
	
	FirmDTO getFirmDomainAdmin(String domain);

	String getDomainServer(String server);
	
	List<FirmDTO> getFirm();
	
	List<FirmDTO> getFirmAdmin();
	
	List<String> findUsers(String domain);
	
	Long findId(String domain);
	
	boolean isRestrictedNivelUser(String domain);
		
 }