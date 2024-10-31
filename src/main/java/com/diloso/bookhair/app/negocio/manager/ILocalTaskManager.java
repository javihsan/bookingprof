package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;

public interface ILocalTaskManager {

	LocalTaskDTO create(LocalTaskDTO taskDTO) throws Exception;

	LocalTaskDTO remove(long id) throws Exception;

	LocalTaskDTO update(LocalTaskDTO taskDTO) throws Exception;

	LocalTaskDTO getById(long id);
	
	LocalTaskDTO getByName(String multiKey);
	
	List<LocalTaskDTO> getLocalTaskSimple(long lotLocalId, String lang);
	
	List<LocalTaskDTO> getLocalTaskSimpleInv(long lotLocalId, String lang);
	
	List<LocalTaskDTO> getLocalTaskCombi(long lotLocalId, String lang, String charAND);
	
	List<LocalTaskDTO> getLocalTaskAndCombi(long lotLocalId, String lang, String charAND);
	
	List<LocalTaskDTO> getLocalTaskAndCombiVisible(long lotLocalId, String lang, String charAND);

	List<LocalTaskDTO> getLocalTask(long lotLocalId, String lang, String charAND);
	
	List<LocalTaskDTO> getLocalTaskSimpleAdmin(long lotLocalId, String lang);
	
	public List<LocalTaskDTO> getLocalTaskAdmin(long lotLocalId, String lang);

	
}