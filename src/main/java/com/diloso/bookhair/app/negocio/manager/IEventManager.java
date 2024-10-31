package com.diloso.bookhair.app.negocio.manager;

import java.util.Date;
import java.util.List;

import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.EventDTO;

public interface IEventManager {

	EventDTO create(EventDTO eventDTO) throws Exception;

	EventDTO remove(long id) throws Exception;

	EventDTO update(EventDTO eventDTO) throws Exception;

	EventDTO getById(long id);
	
	List<EventDTO> getEventAdmin(CalendarDTO calendarDTO);
	
	List<EventDTO> getEventByDay(CalendarDTO calendarDTO, String selectedDate);
	
	List<EventDTO> getEventByWeek(CalendarDTO calendarDTO, String selectedDate);
	
	List<EventDTO> getEventByClientAgo(CalendarDTO calendarDTO, Long clientId, Date selectedDate, int numDays);
	
	List<EventDTO> getEventByICS(String ICS);
	
	Integer getEventNumber(CalendarDTO calendarDTO, String startDate, String endDate, Boolean consumed);
	
	Integer getEventNumberBooking(CalendarDTO calendaDTOr, String startDate, String endDate, Integer booking);
	
	Integer getEventNumberTask(CalendarDTO calendarDTO, String startDate, String endDate, Long localTaskId, Boolean consumed);
	
 }