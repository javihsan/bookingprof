package com.diloso.bookhair.app.negocio.dao;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.EventDTO;
import com.diloso.bookhair.app.negocio.dto.LocalDTO;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;

public interface EventDAOGoogle {

	List<Event> getEvent(LocalDTO local, DateTime startTime, DateTime endTime) throws Exception;
	
	String insertEvent(LocalDTO local, EventDTO event) throws Exception;

	void removeEvent(LocalDTO local, EventDTO event) throws Exception;
}