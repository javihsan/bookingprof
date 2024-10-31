package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;

public interface IMultiTextManager {

	MultiTextDTO create(MultiTextDTO multiTextDTO) throws Exception;

	MultiTextDTO remove(long id) throws Exception;

	MultiTextDTO update(MultiTextDTO multiTextDTO) throws Exception;

	MultiTextDTO getById(long id);
	
	MultiTextDTO getByLanCodeAndKey(String lanCode, String key);
	
	List<MultiTextDTO> getMultiTextSystemByLanCode(String lanCode);
	
	List<MultiTextDTO> getMultiTextByLanCode(String lanCode, Long localId);
	
	List<MultiTextDTO> getMultiTextByKey (String key);
	
	List<MultiTextDTO> getMultiText();
	
}