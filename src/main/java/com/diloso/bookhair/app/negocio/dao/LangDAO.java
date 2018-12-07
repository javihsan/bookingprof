package com.diloso.bookhair.app.negocio.dao;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.LangDTO;

public interface LangDAO {

	LangDTO create(LangDTO lang) throws Exception;

	LangDTO remove(long id) throws Exception;

	LangDTO update(LangDTO lang) throws Exception;

	LangDTO getById(long id);
	
	LangDTO getByName(String name);
	
	LangDTO getByCode(String lanCode);

	List<LangDTO> getLang();
	
}