package com.diloso.bookhair.app.negocio.manager;

import com.diloso.bookhair.app.negocio.dto.WhereDTO;

public interface IWhereManager {

	WhereDTO create(WhereDTO whereDTO) throws Exception;

	WhereDTO remove(long id) throws Exception;

	WhereDTO update(WhereDTO whereDTO) throws Exception;

	WhereDTO getById(long id);
	
}