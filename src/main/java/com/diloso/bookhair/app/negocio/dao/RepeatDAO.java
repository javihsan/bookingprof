package com.diloso.bookhair.app.negocio.dao;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.RepeatDTO;

public interface RepeatDAO {

	RepeatDTO create(RepeatDTO event) throws Exception;

	RepeatDTO remove(long id) throws Exception;

	RepeatDTO update(RepeatDTO event) throws Exception;

	RepeatDTO getById(long id);

	List<RepeatDTO> getRepeatByDay(CalendarDTO calendar, String selectedDate);
	
	List<RepeatDTO> getRepeatByWeek(CalendarDTO calendar, String selectedDate);
	
	//List<RepeatDTO> getRepeatByClientAgo(CalendarDTO calendar, Long clientId, Date selectedDate, int numDays);
	
	//List<RepeatDTO> getRepeatByICS(String ICS);
	
	/*List<RepeatDTO> getRepeatAdmin(CalendarDTO calendar);
	
	Integer getRepeatNumber(CalendarDTO calendar, String startDate, String endDate, Boolean consumed);
	
	Integer getRepeatNumberBooking(CalendarDTO calendar, String startDate, String endDate, Integer booking);
	
	Integer getRepeatNumberTask(CalendarDTO calendar, String startDate, String endDate, Long localTaskId, Boolean consumed);
	*/
 }