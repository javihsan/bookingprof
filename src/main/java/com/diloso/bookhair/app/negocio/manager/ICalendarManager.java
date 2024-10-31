package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.CalendarDTO;

public interface ICalendarManager {

	CalendarDTO create(CalendarDTO calendarDTO) throws Exception;

	CalendarDTO remove(long id) throws Exception;

	CalendarDTO update(CalendarDTO calendarDTO) throws Exception;

	CalendarDTO getById(long id);

	List<CalendarDTO> getCalendar(long calLocalId);
	
	List<CalendarDTO> getCalendarAdmin(long calLocalId);

	Integer getNumCalendarAdmin(long calLocalId);
}