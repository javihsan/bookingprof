package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.TaskDTO;

public interface ITaskManager {

	TaskDTO create(TaskDTO taskDTO) throws Exception;

	TaskDTO remove(long id) throws Exception;

	TaskDTO update(TaskDTO taskDTO) throws Exception;

	TaskDTO getById(long id);
	
	TaskDTO getByName(String multiKey);
	
	List<TaskDTO> getTaskByLang(String lang, List<Long> classTasksFirm);
	
	List<TaskDTO> getTask();
}