package com.diloso.bookhair.app.negocio.dao;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.ClientDTO;

public interface ClientDAO {

	ClientDTO create(ClientDTO client) throws Exception;

	ClientDTO remove(long id) throws Exception;

	ClientDTO update(ClientDTO client) throws Exception;

	ClientDTO getById(long id);
	
	ClientDTO getByEmail(long resFirId, String email);
	
	List<ClientDTO> getClient(long resFirId);

}