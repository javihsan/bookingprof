package com.diloso.bookhair.app.negocio.dao;

import java.util.Date;
import java.util.List;

import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.EventDTO;

public interface EventDAO {

	EventDTO create(EventDTO event) throws Exception;

	EventDTO remove(long id) throws Exception;

	EventDTO update(EventDTO event) throws Exception;

	EventDTO getById(long id);
	
	List<EventDTO> getEventAdmin(CalendarDTO calendar);
	
	List<EventDTO> getEventByDay(CalendarDTO calendar, String selectedDate);
	
	List<EventDTO> getEventByWeek(CalendarDTO calendar, String selectedDate);
	
	List<EventDTO> getEventByClientAgo(CalendarDTO calendar, Long clientId, Date selectedDate, int numDays);
	
	List<EventDTO> getEventByICS(String ICS);
	
	Integer getEventNumber(CalendarDTO calendar, String startDate, String endDate, Boolean consumed);
	
	Integer getEventNumberBooking(CalendarDTO calendar, String startDate, String endDate, Integer booking);
	
	Integer getEventNumberTask(CalendarDTO calendar, String startDate, String endDate, Long localTaskId, Boolean consumed);
	
 }