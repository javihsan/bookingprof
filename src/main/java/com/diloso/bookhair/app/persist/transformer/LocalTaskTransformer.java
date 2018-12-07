package com.diloso.bookhair.app.persist.transformer;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;
import com.diloso.bookhair.app.persist.entities.LocalTask;
import com.google.appengine.api.datastore.Entity;


@Component
@Scope(value = "singleton")
public class LocalTaskTransformer {
	
	public LocalTaskTransformer() {

	}	
	
	public LocalTask transformDTOToEntity(LocalTaskDTO task){
		
		LocalTask entityLocalTask = new LocalTask();
		
		try {
			PropertyUtils.copyProperties(entityLocalTask, task);
		} catch (Exception e) {
		} 
				
		return entityLocalTask;
	}
	
	public LocalTaskDTO transformEntityToDTO(LocalTask entityLocalTask) {

		LocalTaskDTO task = new LocalTaskDTO();

		try {
			PropertyUtils.copyProperties(task, entityLocalTask);
			
		} catch (Exception e) {
		}
		return task;
	}
	
	public LocalTaskDTO transformEntityToDTO(Entity entityLocalTask) {

		LocalTaskDTO task = new LocalTaskDTO();

		try {
			task.setId(entityLocalTask.getKey().getId());
			PropertyDescriptor[] pd = PropertyUtils.getPropertyDescriptors(LocalTaskDTO.class);
			Object value;
			for (PropertyDescriptor descriptor : pd) {
				Method writeMethod = PropertyUtils.getWriteMethod(descriptor);
				if (writeMethod!=null){
					value = entityLocalTask.getProperty(descriptor.getName());
					if (value instanceof Long){
						if (writeMethod.getParameterTypes()[0].getName().equals("java.lang.Integer")){
							value = new Integer(value.toString());
						}
					} else if (value instanceof Double){
						if (writeMethod.getParameterTypes()[0].getName().equals("java.lang.Float")){
							value = new Float(value.toString());
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
		return task;
	}
	
	
	
}
