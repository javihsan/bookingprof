package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.LangDTO;

public interface ILangManager {

	LangDTO create(LangDTO langDTO) throws Exception;

	LangDTO remove(long id) throws Exception;

	LangDTO update(LangDTO langDTO) throws Exception;

	LangDTO getById(long id);
	
	LangDTO getByName(String name);
	
	LangDTO getByCode(String lanCode);

	List<LangDTO> getLang();
	
}