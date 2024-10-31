package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.RepeatDTO;

public interface IRepeatManager {

	RepeatDTO create(RepeatDTO eventDTO) throws Exception;

	RepeatDTO remove(long id) throws Exception;

	RepeatDTO update(RepeatDTO eventDTO) throws Exception;

	RepeatDTO getById(long id);

	List<RepeatDTO> getRepeatByDay(CalendarDTO calendarDTO, String selectedDate);
	
	List<RepeatDTO> getRepeatByWeek(CalendarDTO calendarDTO, String selectedDate);
	
	//List<RepeatDTO> getRepeatByClientAgo(CalendarDTO calendarDTO, Long clientId, Date selectedDate, int numDays);
	
	//List<RepeatDTO> getRepeatByICS(String ICS);
	
	/*List<RepeatDTO> getRepeatAdmin(CalendarDTO calendarDTO);
	
	Integer getRepeatNumber(CalendarDTO calendaDTOr, String startDate, String endDate, Boolean consumed);
	
	Integer getRepeatNumberBooking(CalendarDTO calendarDTO, String startDate, String endDate, Integer booking);
	
	Integer getRepeatNumberTask(CalendarDTO calendarDTO, String startDate, String endDate, Long localTaskId, Boolean consumed);
	*/
 }