package com.diloso.bookhair.app.negocio.dao;

import java.util.Date;
import java.util.List;

import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.RepeatClientDTO;

public interface RepeatClientDAO {

	RepeatClientDTO create(RepeatClientDTO event) throws Exception;

	RepeatClientDTO remove(long id) throws Exception;

	RepeatClientDTO update(RepeatClientDTO event) throws Exception;

	RepeatClientDTO getById(long id);

	List<RepeatClientDTO> getRepeatClientByClientAgo(CalendarDTO calendar, Long clientId, Date selectedDate, int numDays);
	//List<RepeatClientDTO> getRepeatClientByDay(CalendarDTO calendar, String selectedDate);

}