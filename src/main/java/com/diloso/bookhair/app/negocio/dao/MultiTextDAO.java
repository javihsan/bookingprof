package com.diloso.bookhair.app.negocio.dao;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;

public interface MultiTextDAO {

	MultiTextDTO create(MultiTextDTO multiText) throws Exception;

	MultiTextDTO remove(long id) throws Exception;

	MultiTextDTO update(MultiTextDTO multiText) throws Exception;

	MultiTextDTO getById(long id);
	
	MultiTextDTO getByLanCodeAndKey(String lanCode, String key);
	
	List<MultiTextDTO> getMultiTextSystemByLanCode(String lanCode);
	
	List<MultiTextDTO> getMultiTextByLanCode(String lanCode, Long localId);
	
	List<MultiTextDTO> getMultiTextByKey (String key);
	
	List<MultiTextDTO> getMultiText();
	
}