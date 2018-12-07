package com.diloso.bookhair.app.persist.transformer;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.ClientDAO;
import com.diloso.bookhair.app.negocio.dao.LocalTaskDAO;
import com.diloso.bookhair.app.negocio.dto.ClientDTO;
import com.diloso.bookhair.app.negocio.dto.EventDTO;
import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;
import com.diloso.bookhair.app.persist.entities.Event;
import com.google.appengine.api.datastore.Entity;

@Component
@Scope(value = "singleton")
public class EventTransformer {
	
	public EventTransformer() {

	}

	@Autowired
	protected ClientDAO clientDAO;
	
	@Autowired
	protected LocalTaskDAO localTaskDAO;
	
	public Event transformDTOToEntity(EventDTO event) {

		Event entityEvent = new Event();

		try {
			PropertyUtils.copyProperties(entityEvent, event);
		} catch (Exception e) {
		}
		
		if (event.getEveClient()!=null){
			entityEvent.setEveClientId(event.getEveClient().getId());
		}
	
		if (event.getEveLocalTask()!=null){
			entityEvent.setEveLocalTaskId(event.getEveLocalTask().getId());
		}
		return entityEvent;
	}

	
	
	public EventDTO transformEntityToDTO(Event entityEvent) {
		
		EventDTO event = new EventDTO();

		try {
			PropertyUtils.copyProperties(event, entityEvent);
			
			// Propiedades de Client
			if (entityEvent.getEveClientId() != null) {
				ClientDTO client = clientDAO.getById(entityEvent
						.getEveClientId());
				event.setEveClient(client);
			}
			
			// Propiedades de LocalTask
			Long eveLocalTaskId = entityEvent.getEveLocalTaskId();
			if (eveLocalTaskId != null) {
				LocalTaskDTO localtask = localTaskDAO.getById(eveLocalTaskId);
				event.setEveLocalTask(localtask);
			}
			
		} catch (Exception e) {
		}
		return event;
	}
	
	public EventDTO transformEntityToDTO(Entity entityEvent) {

		EventDTO event = new EventDTO();

		try {
			event.setId(entityEvent.getKey().getId());
			PropertyDescriptor[] pd = PropertyUtils.getPropertyDescriptors(EventDTO.class);
			Object value;
			for (PropertyDescriptor descriptor : pd) {
				Method writeMethod = PropertyUtils.getWriteMethod(descriptor);
				if (writeMethod!=null){
					value = entityEvent.getProperty(descriptor.getName());
					if (value instanceof Long){
						if (writeMethod.getParameterTypes()[0].getName().equals("java.lang.Integer")){
							value = new Integer(value.toString());
						}
					} 
					if (value!=null) writeMethod.invoke(event,value);
				}
			}
		} catch (Exception e) {
		}
		
		// Propiedades de Client
		Long eveClientId = (Long)entityEvent.getProperty("eveClientId");
		if (eveClientId != null) {
			ClientDTO client = clientDAO.getById(eveClientId);
			event.setEveClient(client);
		}
		
		// Propiedades de LocalTask
		Long eveLocalTaskId =  (Long)entityEvent.getProperty("eveLocalTaskId");
		if (eveLocalTaskId != null) {
			LocalTaskDTO localtask = localTaskDAO.getById(eveLocalTaskId);
			event.setEveLocalTask(localtask);
		}	
		
		return event;
		
	}
	
	
}
