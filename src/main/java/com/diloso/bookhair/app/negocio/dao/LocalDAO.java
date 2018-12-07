package com.diloso.bookhair.app.negocio.dao;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.LocalDTO;

public interface LocalDAO {

	LocalDTO create(LocalDTO local) throws Exception;

	LocalDTO remove(long id) throws Exception;

	LocalDTO update(LocalDTO local) throws Exception;

	LocalDTO getById(long id);

	List<Long> getLocal(long resFirId);
	
	List<Long> getLocalClient(long resFirId);
	
	List<LocalDTO> getLocalList(long resFirId);
	
	List<LocalDTO> getLocalListClient(long resFirId);
	
	List<LocalDTO> getLocalAdmin(long resFirId);

 }