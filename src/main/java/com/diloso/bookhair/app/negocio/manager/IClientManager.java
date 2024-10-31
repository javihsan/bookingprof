package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.ClientDTO;

public interface IClientManager {

	ClientDTO create(ClientDTO clientDTO) throws Exception;

	ClientDTO remove(long id) throws Exception;

	ClientDTO update(ClientDTO clientDTO) throws Exception;

	ClientDTO getById(long id);
	
	ClientDTO getByEmail(long resFirId, String email);
	
	List<ClientDTO> getClient(long resFirId);

}