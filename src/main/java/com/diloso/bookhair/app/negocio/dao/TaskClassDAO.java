package com.diloso.bookhair.app.negocio.dao;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.TaskClassDTO;

public interface TaskClassDAO {

	TaskClassDTO create(TaskClassDTO taskClass) throws Exception;

	TaskClassDTO remove(long id) throws Exception;

	TaskClassDTO update(TaskClassDTO taskClass) throws Exception;

	TaskClassDTO getById(long id);
	
	TaskClassDTO getByName(String multiKey);
	
	List<TaskClassDTO> getTaskClassByLang(String lang);
	
	List<TaskClassDTO> getTaskClass();
	
}