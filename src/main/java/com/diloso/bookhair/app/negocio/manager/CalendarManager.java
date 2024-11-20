package com.diloso.bookhair.app.negocio.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.CalendarDAO;
import com.diloso.bookhair.app.persist.entities.Calendar;
import com.diloso.bookhair.app.persist.mapper.CalendarMapper;

@Component
@Scope(value = "singleton")
public class CalendarManager implements ICalendarManager {

	public static final String ENABLED = "enabled";
	public static final String CAL_LOCAL_ID = "calLocalId";
	public static final String ORDER_CAL_NAME_ASC = "calName";
	
	@Autowired
	private CalendarDAO calendarDAO;
	
	@Autowired
	protected CalendarMapper mapper;
	
	public CalendarManager() {

	}

	@Override
	public CalendarDTO create(CalendarDTO calendarDTO) throws Exception {
		Calendar calendar = mapper.map(calendarDTO);
		calendar = calendarDAO.create(calendar);
		return mapper.map(calendar);
	}

	@Override
	public CalendarDTO remove(long id) throws Exception {
		Calendar calendar = calendarDAO.get(id);
		calendarDAO.delete(id);
		if (calendar == null) {
			return null;
		}
		return mapper.map(calendar);
	}

	@Override
	public CalendarDTO update(CalendarDTO calendarDTO) throws Exception {
		Calendar calendar = mapper.map(calendarDTO);
		Calendar oldCalendar = calendarDAO.get(calendarDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(calendar, oldCalendar);
		} catch (Exception e) {
		}
		calendar = calendarDAO.update(calendar);
		if (calendar == null) {
			return null;
		}
		return mapper.map(calendar);
	}

	@Override
	public CalendarDTO getById(long id) {
		Calendar calendar = calendarDAO.get(id);
		if (calendar == null) {
			return null;
		}
		return mapper.map(calendar);		
	}

	@Override
	public List<CalendarDTO> getCalendar(long calLocalId) {
		List<CalendarDTO> result = new ArrayList<CalendarDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(CAL_LOCAL_ID, calLocalId);
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_CAL_NAME_ASC);
		List<Calendar> resultQuery = calendarDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});

		return result;
	}

	@Override
	public List<CalendarDTO> getCalendarAdmin(long calLocalId) {
		List<CalendarDTO> result = new ArrayList<CalendarDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(CAL_LOCAL_ID, calLocalId);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_CAL_NAME_ASC);
		List<Calendar> resultQuery = calendarDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});

		return result;
	}

	@Override
	public Integer getNumCalendarAdmin(long calLocalId) {

		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(CAL_LOCAL_ID, calLocalId);
		return calendarDAO.listFilter(filters).size();
	}
	
}