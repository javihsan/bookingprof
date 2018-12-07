package com.diloso.bookhair.app.negocio.dao;

import com.diloso.bookhair.app.negocio.dto.SincroDTO;

public interface SincroDAO {

	SincroDTO create(SincroDTO sincro) throws Exception;

	SincroDTO remove(long id) throws Exception;

	SincroDTO update(SincroDTO sincro) throws Exception;

	SincroDTO getById(long id);
	
}