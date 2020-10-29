package com.diloso.bookhair.app.persist.transformer;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.TaskClassDAO;
import com.diloso.bookhair.app.negocio.dto.TaskClassDTO;
import com.diloso.bookhair.app.negocio.dto.TaskDTO;
import com.diloso.bookhair.app.persist.entities.Task;
import com.google.appengine.api.datastore.Entity;


@Component
@Scope(value = "singleton")
public class TaskTransformer {
	
	public TaskTransformer() {

	}	
	
	//@Autowired
	protected TaskClassDAO taskClassDAO;
	

	public Task transformDTOToEntity(TaskDTO task){
		
		Task entityTask = new Task();
		
		try {
			PropertyUtils.copyProperties(entityTask, task);
		} catch (Exception e) {
		} 
		
		if (task.getTasClass()!=null){
			entityTask.setTasClassId(task.getTasClass().getId());
		}
		
		return entityTask;
	}
	
	public TaskDTO transformEntityToDTO(Task entityTask) {

		TaskDTO task = new TaskDTO();

		try {
			PropertyUtils.copyProperties(task, entityTask);
			
			// Propiedades de TaskClass
			if (entityTask.getTasClassId() != null) {
				TaskClassDTO taskClass = taskClassDAO
						.getById(entityTask.getTasClassId());
				task.setTasClass(taskClass);
			}
			
		} catch (Exception e) {
		}
		return task;
	}
	
	public TaskDTO transformEntityToDTO(Entity entityTask) {

		TaskDTO task = new TaskDTO();

		try {
			task.setId(entityTask.getKey().getId());
			PropertyDescriptor[] pd = PropertyUtils.getPropertyDescriptors(TaskDTO.class);
			Object value;
			for (PropertyDescriptor descriptor : pd) {
				Method writeMethod = PropertyUtils.getWriteMethod(descriptor);
				if (writeMethod!=null){
					value = entityTask.getProperty(descriptor.getName());
					if (value instanceof Long){
						if (writeMethod.getParameterTypes()[0].getName().equals("java.lang.Integer")){
							value = new Integer(value.toString());
						}
					} else if (value instanceof List<?>){
						if (((List)value).get(0) instanceof Long){
							if (writeMethod.toGenericString().indexOf("(java.util.List<java.lang.Integer>)")!=-1){
								List<Integer> aux = new ArrayList<Integer>();
								for (Long longValue : ((List<Long>)value)) {
									aux.add(new Integer(longValue.toString()));
								}
								value = aux;
							}
						}
					}
					if (value!=null) writeMethod.invoke(task,value);
				}
			}
			
		} catch (Exception e) {
		}
		
		// Propiedades de TaskClass
		Long tasClassId =  (Long)entityTask.getProperty("tasClassId");
		if (tasClassId != null) {
			TaskClassDTO taskClass = taskClassDAO.getById(tasClassId);
			task.setTasClass(taskClass);
		}

		return task;
	}
	
	public void setTaskClassDAO(TaskClassDAO taskClassDAO) {
		this.taskClassDAO = taskClassDAO;
	}

	
}
