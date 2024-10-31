package com.diloso.bookhair.app.persist.mapper;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.TaskClassDTO;
import com.diloso.bookhair.app.persist.entities.TaskClass;


@Component
@Scope(value = "singleton")
public class TaskClassMapper {
	
	public TaskClassMapper() {

	}
	
	public TaskClass map(TaskClassDTO taskClass){
		
		TaskClass entityTaskClass = new TaskClass();
		
		try {
			PropertyUtils.copyProperties(entityTaskClass, taskClass);
		} catch (Exception e) {
		}
		return entityTaskClass;
	}
	
	public TaskClassDTO map(TaskClass entityTaskClass) {

		TaskClassDTO taskClass = new TaskClassDTO();

		try {
			PropertyUtils.copyProperties(taskClass, entityTaskClass);
			
		} catch (Exception e) {
		}
		return taskClass;
	}
	

	
}
