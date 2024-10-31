package com.diloso.bookhair.app.negocio.manager;

import com.diloso.bookhair.app.negocio.dto.SincroDTO;

public interface ISincroManager {

	SincroDTO create(SincroDTO sincroDTO) throws Exception;

	SincroDTO remove(long id) throws Exception;

	SincroDTO update(SincroDTO sincroDTO) throws Exception;

	SincroDTO getById(long id);
	
}