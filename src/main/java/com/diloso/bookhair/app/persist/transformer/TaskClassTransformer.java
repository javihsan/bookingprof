package com.diloso.bookhair.app.persist.transformer;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.TaskClassDTO;
import com.diloso.bookhair.app.persist.entities.TaskClass;


@Component
@Scope(value = "singleton")
public class TaskClassTransformer {
	
	public TaskClassTransformer() {

	}
	
	public TaskClass transformDTOToEntity(TaskClassDTO taskClass){
		
		TaskClass entityTaskClass = new TaskClass();
		
		try {
			PropertyUtils.copyProperties(entityTaskClass, taskClass);
		} catch (Exception e) {
		}
		return entityTaskClass;
	}
	
	public TaskClassDTO transformEntityToDTO(TaskClass entityTaskClass) {

		TaskClassDTO taskClass = new TaskClassDTO();

		try {
			PropertyUtils.copyProperties(taskClass, entityTaskClass);
			
		} catch (Exception e) {
		}
		return taskClass;
	}
	

	
}
