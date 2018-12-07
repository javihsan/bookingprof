package com.diloso.bookhair.app.negocio.dao;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.CalendarDTO;

public interface CalendarDAO {

	CalendarDTO create(CalendarDTO calendar) throws Exception;

	CalendarDTO remove(long id) throws Exception;

	CalendarDTO update(CalendarDTO calendar) throws Exception;

	CalendarDTO getById(long id);

	List<CalendarDTO> getCalendar(long calLocalId);
	
	List<CalendarDTO> getCalendarAdmin(long calLocalId);

	Integer getNumCalendarAdmin(long calLocalId);
}