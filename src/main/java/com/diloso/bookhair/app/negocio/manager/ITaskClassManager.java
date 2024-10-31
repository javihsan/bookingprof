package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.TaskClassDTO;

public interface ITaskClassManager {

	TaskClassDTO create(TaskClassDTO taskClassDTO) throws Exception;

	TaskClassDTO remove(long id) throws Exception;

	TaskClassDTO update(TaskClassDTO taskClassDTO) throws Exception;

	TaskClassDTO getById(long id);
	
	TaskClassDTO getByName(String multiKey);
	
	List<TaskClassDTO> getTaskClassByLang(String lang);
	
	List<TaskClassDTO> getTaskClass();
	
}