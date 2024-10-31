package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.EventDTO;
import com.diloso.bookhair.app.negocio.dto.LocalDTO;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;

public interface IEventManagerGoogle {

	List<Event> getEvent(LocalDTO localDTO, DateTime startTime, DateTime endTime) throws Exception;
	
	String insertEvent(LocalDTO localDTO, EventDTO eventDTO) throws Exception;

	void removeEvent(LocalDTO localDTO, EventDTO eventDTO) throws Exception;
}