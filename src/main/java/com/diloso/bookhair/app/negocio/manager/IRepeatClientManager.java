package com.diloso.bookhair.app.negocio.manager;

import java.util.Date;
import java.util.List;

import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.RepeatClientDTO;

public interface IRepeatClientManager {

	RepeatClientDTO create(RepeatClientDTO eventDTO) throws Exception;

	RepeatClientDTO remove(long id) throws Exception;

	RepeatClientDTO update(RepeatClientDTO eventDTO) throws Exception;

	RepeatClientDTO getById(long id);

	List<RepeatClientDTO> getRepeatClientByClientAgo(CalendarDTO calendarDTO, Long clientId, Date selectedDate, int numDays);
	//List<RepeatClientDTO> getRepeatClientByDay(CalendarDTO calendarDTO, String selectedDate);

}