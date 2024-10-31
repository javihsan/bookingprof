package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.LocalDTO;

public interface ILocalManager {

	LocalDTO create(LocalDTO localDTO) throws Exception;

	LocalDTO remove(long id) throws Exception;

	LocalDTO update(LocalDTO locaDTOl) throws Exception;

	LocalDTO getById(long id);

	List<Long> getLocal(long resFirId);
	
	List<Long> getLocalClient(long resFirId);
	
	List<LocalDTO> getLocalList(long resFirId);
	
	List<LocalDTO> getLocalListClient(long resFirId);
	
	List<LocalDTO> getLocalAdmin(long resFirId);

 }