package com.diloso.bookhair.app.negocio.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.controllers.CalendarController;
import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.EventDTO;
import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.EventDAO;
import com.diloso.bookhair.app.persist.entities.Event;
import com.diloso.bookhair.app.persist.mapper.EventMapper;

@Component
@Scope(value = "singleton")
public class EventManager implements IEventManager {


	public static final String EVE_CALENDAR_ID = "eveCalendarId";
	public static final String EVE_CLIENT_ID = "eveClientId";
	public static final String EVE_ICS = "eveICS";
	public static final String EVE_BOOKING = "eveBooking";
	public static final String EVE_CONSUMED = "eveConsumed";
	public static final String EVE_LOCAL_ID = "eveLocalTaskId";
	public static final String ENABLED = "enabled";
	public static final String ORDER_EVE_START_TIME_ASC = "eveStartTime";
	public static final String EVE_START_TIME_MAYQ = "eveStartTime >=";
	public static final String EVE_START_TIME_MENQ = "eveStartTime <="; 
	public static final String ORDER_EVE_BOOKING_TIME_ASC = "eveBookingTime";
	public static final String EVE_BOOKING_TIME_MAYQ = "eveBookingTime >=";
	
	protected static final Logger log = Logger.getLogger(EventManager.class.getName());

	@Autowired
	private EventDAO eventDAO;
	
	@Autowired
	protected EventMapper mapper;
	
	public EventManager() {

	}

	@Override
	public EventDTO create(EventDTO eventDTO) throws Exception {
		Event event = mapper.map(eventDTO);
		event = eventDAO.create(event);
		return mapper.map(event);
	}

	@Override
	public EventDTO remove(long id) throws Exception {
		Event event = eventDAO.get(id);
		eventDAO.delete(id);
		if (event == null) {
			return null;
		}
		return mapper.map(event);
	}

	@Override
	public EventDTO update(EventDTO eventDTO) throws Exception {
		Event event = mapper.map(eventDTO);
		Event oldEvent = eventDAO.get(eventDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(event, oldEvent);
		} catch (Exception e) {
		}
		event = eventDAO.update(event);
		if (event == null) {
			return null;
		}
		return mapper.map(event);
	}

	@Override
	public EventDTO getById(long id) {
		Event event = eventDAO.get(id);
		if (event == null) {
			return null;
		}
		return mapper.map(event);		
	}

	@Override
	public List<EventDTO> getEventAdmin(CalendarDTO calendar) {
		List<EventDTO> result = new ArrayList<EventDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(EVE_CALENDAR_ID, calendar.getId());
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_EVE_START_TIME_ASC);
		List<Event> resultQuery = eventDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});
		return result;
	}

	@Override
	public List<EventDTO> getEventByDay(CalendarDTO calendar, String selectedDate) {

		List<EventDTO> result = new ArrayList<EventDTO>();
		
		String[] dates = selectedDate.split(CalendarController.CHAR_SEP_DATE);
		String year = dates[0];
		String month = dates[1];
		String day = dates[2];

		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.YEAR, new Integer(year));
		calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
		calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
		calendarGreg.set(Calendar.HOUR_OF_DAY, 0);
		calendarGreg.set(Calendar.MINUTE, 0);
		calendarGreg.set(Calendar.SECOND, 0);
		calendarGreg.set(Calendar.MILLISECOND, 0);
		Date startTime = calendarGreg.getTime();

		calendarGreg.add(Calendar.HOUR, 24);
		Date endTime = calendarGreg.getTime();

		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(EVE_CALENDAR_ID, calendar.getId());
		filters.put(ENABLED, 1);
		filters.put(EVE_START_TIME_MAYQ, startTime);
		filters.put(EVE_START_TIME_MENQ, endTime);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_EVE_START_TIME_ASC);
		List<Event> resultQuery = eventDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			EventDTO event = mapper.map(entity);
			if (event.getEveEndTime() == null) {
				setEveEndTime(event);
			}
			result.add(event);
		});
		return result;
	}

	@Override
	public List<EventDTO> getEventByWeek(CalendarDTO calendar, String selectedDate) {
		String[] dates = selectedDate.split(CalendarController.CHAR_SEP_DATE);
		String year = dates[0];
		String month = dates[1];
		String day = dates[2];

		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.YEAR, new Integer(year));
		calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
		calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
		calendarGreg.set(Calendar.HOUR_OF_DAY, 0);
		calendarGreg.set(Calendar.MINUTE, 0);
		calendarGreg.set(Calendar.SECOND, 0);
		calendarGreg.set(Calendar.MILLISECOND, 0);
		Date startTime = calendarGreg.getTime();

		calendarGreg.add(Calendar.DAY_OF_MONTH, 7);
		Date endTime = calendarGreg.getTime();

		List<EventDTO> result = new ArrayList<EventDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(EVE_CALENDAR_ID, calendar.getId());
		filters.put(ENABLED, 1);
		filters.put(EVE_START_TIME_MAYQ, startTime);
		filters.put(EVE_START_TIME_MENQ, endTime);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_EVE_START_TIME_ASC);
		List<Event> resultQuery = eventDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});
		return result;
	}

	@Override
	public List<EventDTO> getEventByClientAgo(CalendarDTO calendar, Long clientId, Date selectedDate, int numDays) {

		List<EventDTO> result = new ArrayList<EventDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(EVE_CALENDAR_ID, calendar.getId());
		filters.put(EVE_CLIENT_ID, clientId);
		filters.put(ENABLED, 1);

		if (selectedDate != null) {
			Calendar calendarGreg = new GregorianCalendar();
			calendarGreg.setTime(selectedDate);
			calendarGreg.set(Calendar.MILLISECOND, 0);
			calendarGreg.add(Calendar.DAY_OF_MONTH, -numDays);
			Date startTime = calendarGreg.getTime();

			filters.put(EVE_BOOKING_TIME_MAYQ, startTime);
		}

		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_EVE_BOOKING_TIME_ASC);
		List<Event> resultQuery = eventDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});
		return result;
	}

	@Override
	public List<EventDTO> getEventByICS(String ICS) {
		List<EventDTO> result = new ArrayList<EventDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(EVE_ICS, ICS);
		List<Event> resultQuery = eventDAO.listFilter(filters);
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});
		return result;
	}

	@Override
	public Integer getEventNumber(CalendarDTO calendar, String startDate, String endDate, Boolean consumed) {
		String[] dates = startDate.split(CalendarController.CHAR_SEP_DATE);
		String year = dates[0];
		String month = dates[1];
		String day = dates[2];

		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.YEAR, new Integer(year));
		calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
		calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
		calendarGreg.set(Calendar.HOUR_OF_DAY, 0);
		calendarGreg.set(Calendar.MINUTE, 0);
		calendarGreg.set(Calendar.SECOND, 0);
		calendarGreg.set(Calendar.MILLISECOND, 0);
		Date startTime = calendarGreg.getTime();

		dates = endDate.split(CalendarController.CHAR_SEP_DATE);
		year = dates[0];
		month = dates[1];
		day = dates[2];

		calendarGreg.set(Calendar.YEAR, new Integer(year));
		calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
		calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
		calendarGreg.add(Calendar.DAY_OF_MONTH, 1);

		Date endTime = calendarGreg.getTime();

		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(EVE_CALENDAR_ID, calendar.getId());
		filters.put(ENABLED, 1);
		filters.put(EVE_START_TIME_MAYQ, startTime);
		filters.put(EVE_START_TIME_MENQ, endTime);

		if (consumed != null) {
			int intConsumed = consumed.booleanValue() ? 1 : 0;
			filters.put(EVE_CONSUMED, intConsumed);
		}
		List<Event> resultQuery = eventDAO.listFilter(filters);
		List<String> listICS = new ArrayList<String>();
		resultQuery.stream().forEach(entity -> {
			String ICS = (String) entity.getEveICS();
			if (!listICS.contains(ICS)) {
				listICS.add(ICS);
			}
		});
		return listICS.size();
	}

	@Override
	public Integer getEventNumberBooking(CalendarDTO calendar, String startDate, String endDate, Integer booking) {

		String[] dates = startDate.split(CalendarController.CHAR_SEP_DATE);
		String year = dates[0];
		String month = dates[1];
		String day = dates[2];

		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.YEAR, new Integer(year));
		calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
		calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
		calendarGreg.set(Calendar.HOUR_OF_DAY, 0);
		calendarGreg.set(Calendar.MINUTE, 0);
		calendarGreg.set(Calendar.SECOND, 0);
		calendarGreg.set(Calendar.MILLISECOND, 0);
		Date startTime = calendarGreg.getTime();

		dates = endDate.split(CalendarController.CHAR_SEP_DATE);
		year = dates[0];
		month = dates[1];
		day = dates[2];

		calendarGreg.set(Calendar.YEAR, new Integer(year));
		calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
		calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
		calendarGreg.add(Calendar.DAY_OF_MONTH, 1);

		Date endTime = calendarGreg.getTime();

		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(EVE_CALENDAR_ID, calendar.getId());
		filters.put(ENABLED, 1);
		filters.put(EVE_START_TIME_MAYQ, startTime);
		filters.put(EVE_START_TIME_MENQ, endTime);
		if (booking != null) {
			filters.put(EVE_BOOKING, booking);
		}
		List<Event> resultQuery = eventDAO.listFilter(filters);
		List<String> listICS = new ArrayList<String>();
		resultQuery.stream().forEach(entity -> {
			String ICS = (String) entity.getEveICS();
			if (!listICS.contains(ICS)) {
				listICS.add(ICS);
			}
		});
		return listICS.size();
	}

	@Override
	public Integer getEventNumberTask(CalendarDTO calendar, String startDate, String endDate, Long localTaskId,
			Boolean consumed) {
		
		String[] dates = startDate.split(CalendarController.CHAR_SEP_DATE);
		String year = dates[0];
		String month = dates[1];
		String day = dates[2];

		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.YEAR, new Integer(year));
		calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
		calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
		calendarGreg.set(Calendar.HOUR_OF_DAY, 0);
		calendarGreg.set(Calendar.MINUTE, 0);
		calendarGreg.set(Calendar.SECOND, 0);
		calendarGreg.set(Calendar.MILLISECOND, 0);
		Date startTime = calendarGreg.getTime();

		dates = endDate.split(CalendarController.CHAR_SEP_DATE);
		year = dates[0];
		month = dates[1];
		day = dates[2];

		calendarGreg.set(Calendar.YEAR, new Integer(year));
		calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
		calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
		calendarGreg.add(Calendar.DAY_OF_MONTH, 1);

		Date endTime = calendarGreg.getTime();

		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(EVE_CALENDAR_ID, calendar.getId());
		filters.put(ENABLED, 1);
		filters.put(EVE_START_TIME_MAYQ, startTime);
		filters.put(EVE_START_TIME_MENQ, endTime);
		if (consumed != null) {
			int intConsumed = consumed.booleanValue() ? 1 : 0;
			filters.put(EVE_CONSUMED, intConsumed);
		}
		if (localTaskId != null) {
			filters.put(EVE_LOCAL_ID, localTaskId);
		}

		return eventDAO.listFilter(filters).size();
		
	}
	
	public void setEveEndTime(EventDTO event){
		LocalTaskDTO localTask = event.getEveLocalTask();
		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.setTime(event.getEveStartTime());
		calendarGreg.add(Calendar.MINUTE,
				localTask.getLotTaskDuration());
		event.setEveEndTime(calendarGreg.getTime());
	}

}