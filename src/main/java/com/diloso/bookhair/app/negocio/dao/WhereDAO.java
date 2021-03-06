package com.diloso.bookhair.app.negocio.dao;

import com.diloso.bookhair.app.negocio.dto.WhereDTO;

public interface WhereDAO {

	WhereDTO create(WhereDTO where) throws Exception;

	WhereDTO remove(long id) throws Exception;

	WhereDTO update(WhereDTO where) throws Exception;

	WhereDTO getById(long id);
	
}