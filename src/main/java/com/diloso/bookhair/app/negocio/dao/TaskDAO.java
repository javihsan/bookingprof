package com.diloso.bookhair.app.negocio.dao;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.TaskDTO;

public interface TaskDAO {

	TaskDTO create(TaskDTO task) throws Exception;

	TaskDTO remove(long id) throws Exception;

	TaskDTO update(TaskDTO task) throws Exception;

	TaskDTO getById(long id);
	
	TaskDTO getByName(String multiKey);
	
	List<TaskDTO> getTaskByLang(String lang, List<Long> classTasksFirm);
	
	List<TaskDTO> getTask();
}