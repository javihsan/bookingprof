package com.diloso.bookhair.app.negocio.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.controllers.CalendarController;
import com.diloso.bookhair.app.negocio.dto.AnnualDiaryDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.AnnualDiaryDAO;
import com.diloso.bookhair.app.persist.entities.AnnualDiary;
import com.diloso.bookhair.app.persist.mapper.AnnualDiaryMapper;




@Component
@Scope(value = "singleton")
public class AnnualDiaryManager implements IAnnualDiaryManager {

	public static final String ANN_DATE = "anuDate";
	public static final String ANN_LOCAL_ID = "anuLocalId";
	public static final String ANN_CALENDAR_ID = "anuCalendarId";
	public static final String ANN_REPEAT_ID = "anuRepeatId";
	public static final String ENABLED = "enabled";
	public static final String ORDER_ANN_DATE_DESC = "-anuDate";
	public static final String ORDER_KEY_DESC = "-__key__";
	
	public static final String ORDER_ANN_DATE_MAYQ = "anuDate >=";
	public static final String ORDER_ANN_DATE_MENQ = "anuDate <="; 
	
	@Autowired
	private AnnualDiaryDAO annualDiaryDAO;
	
	@Autowired
	protected AnnualDiaryMapper mapper;
	
	public AnnualDiaryManager() {
	
	}
	
	public AnnualDiaryDTO create(AnnualDiaryDTO annualDiary) {
		AnnualDiary entityAnnualDiary = mapper.map(annualDiary);
		entityAnnualDiary = annualDiaryDAO.create(entityAnnualDiary);
		return mapper.map(entityAnnualDiary);
	}

	@Override
	public AnnualDiaryDTO remove(long id) throws Exception {
		AnnualDiary annualDiary = annualDiaryDAO.get(id);
		annualDiaryDAO.delete(id);
		if (annualDiary == null) {
			return null;
		}
		return mapper.map(annualDiary);
	}


	@Override
	public AnnualDiaryDTO update(AnnualDiaryDTO annualDiaryDTO) throws Exception {
		AnnualDiary annualDiary = mapper.map(annualDiaryDTO);
		AnnualDiary oldAnnualDiary = annualDiaryDAO.get(annualDiaryDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(annualDiary, oldAnnualDiary);
		} catch (Exception e) {
		}
		annualDiary = annualDiaryDAO.update(annualDiary);
		if (annualDiary == null) {
			return null;
		}
		return mapper.map(annualDiary);
	}


	@Override
	public AnnualDiaryDTO getById(long id) {
		AnnualDiary annualDiary = annualDiaryDAO.get(id);
		if (annualDiary == null) {
			return null;
		}
		return mapper.map(annualDiary);		
	}

	@Override
	public AnnualDiaryDTO getAnnualDiaryByDay(long localId, String selectedDate) {
		String[] dates = selectedDate.split(CalendarController.CHAR_SEP_DATE);
		String year = dates[0];
		String month = dates[1];
		String day = dates[2];

		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.YEAR, new Integer(year));
		calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
		calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
		calendarGreg.set(Calendar.HOUR_OF_DAY,0);
		calendarGreg.set(Calendar.MINUTE,0);
		calendarGreg.set(Calendar.SECOND,0);
		calendarGreg.set(Calendar.MILLISECOND,0);
		Date startTime = calendarGreg.getTime();
		
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ANN_LOCAL_ID, localId);
		filters.put(ENABLED, 1);
		filters.put(ANN_DATE, startTime);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_DESC);
		List<AnnualDiary> resultQuery = annualDiaryDAO.listOrderFilter(filters, orders);
		if (resultQuery.size()==1){
			return mapper.map(resultQuery.get(0));
		}
		return null;
	}


	@Override
	public List<AnnualDiaryDTO> getAnnualDiaryByMonth(long localId, String selectedDate) {
		List<AnnualDiaryDTO> result = new ArrayList<AnnualDiaryDTO>();

		String[] dates = selectedDate.split(CalendarController.CHAR_SEP_DATE);
		String year = dates[0];
		String month = dates[1];
		// Hay que tener en cuenta los dias de antes y los de despues del mes a mostrar
		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.YEAR, new Integer(year));
		calendarGreg.set(Calendar.MONTH, new Integer(month) - 1 - 1);
		calendarGreg.set(Calendar.DAY_OF_MONTH, 1);
		calendarGreg.set(Calendar.HOUR_OF_DAY, 0);
		calendarGreg.set(Calendar.MINUTE, 0);
		calendarGreg.set(Calendar.SECOND, 0);
		calendarGreg.set(Calendar.MILLISECOND, 0);
		Date startTime = calendarGreg.getTime();

		calendarGreg.add(Calendar.DAY_OF_YEAR, 93);
		Date endTime = calendarGreg.getTime();

		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ANN_LOCAL_ID, localId);
		filters.put(ENABLED, 1);
		filters.put(ORDER_ANN_DATE_MAYQ, startTime);
		filters.put(ORDER_ANN_DATE_MENQ, endTime);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_ANN_DATE_DESC);
		List<AnnualDiary> resultQuery = annualDiaryDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});

		return result;
	}


	
	@Override
	public AnnualDiaryDTO getAnnualDiaryCalendarByDay(long calId, String selectedDate) {

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

		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ANN_CALENDAR_ID, calId);
		filters.put(ENABLED, 1);
		filters.put(ANN_DATE, startTime);
		List<AnnualDiary> resultQuery = annualDiaryDAO.listFilter(filters);
		if (resultQuery.size() == 1) {
			return mapper.map(resultQuery.get(0));
		}
		return null;
	}


	@Override
	public AnnualDiaryDTO getAnnualDiaryRepatByDay(long repeatId, String selectedDate) {

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

		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ANN_REPEAT_ID, repeatId);
		filters.put(ANN_DATE, startTime);
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_ANN_DATE_DESC);
		List<AnnualDiary> resultQuery = annualDiaryDAO.listFilter(filters);
		if (resultQuery.size() == 1) {
			return mapper.map(resultQuery.get(0));
		}
		return null;
	}

	@Override
	public List<AnnualDiaryDTO> getAnnualDiary(long localId) {

		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ANN_LOCAL_ID, localId);
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_ANN_DATE_DESC);
		List<AnnualDiary> resultQuery = annualDiaryDAO.listOrderFilter(filters, orders);
		List<AnnualDiaryDTO> result = new ArrayList<AnnualDiaryDTO>();
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});

		return result;
	}

	@Override
	public List<AnnualDiaryDTO> getAnnualDiaryByDate(long localId, String selectedDate, int numDays) {

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

		calendarGreg.add(Calendar.DAY_OF_YEAR, numDays);
		Date endTime = calendarGreg.getTime();

		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ANN_LOCAL_ID, localId);
		filters.put(ENABLED, 1);
		filters.put(ORDER_ANN_DATE_MAYQ, startTime);
		filters.put(ORDER_ANN_DATE_MENQ, endTime);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_ANN_DATE_DESC);
		List<AnnualDiary> resultQuery = annualDiaryDAO.listOrderFilter(filters, orders);
		List<AnnualDiaryDTO> result = new ArrayList<AnnualDiaryDTO>();
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});

		return result;
	}

	@Override
	public List<AnnualDiaryDTO> getAnnualDiaryCalendarByMonth(long calId, String selectedDate) {

		String[] dates = selectedDate.split(CalendarController.CHAR_SEP_DATE);
		String year = dates[0];
		String month = dates[1];
		// Hay que tener en cuenta los dias de antes y los de despues del mes a mostrar
		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.YEAR, new Integer(year));
		calendarGreg.set(Calendar.MONTH, new Integer(month) - 1 - 1);
		calendarGreg.set(Calendar.DAY_OF_MONTH, 1);
		calendarGreg.set(Calendar.HOUR_OF_DAY, 0);
		calendarGreg.set(Calendar.MINUTE, 0);
		calendarGreg.set(Calendar.SECOND, 0);
		calendarGreg.set(Calendar.MILLISECOND, 0);
		Date startTime = calendarGreg.getTime();

		calendarGreg.add(Calendar.DAY_OF_YEAR, 93);
		Date endTime = calendarGreg.getTime();

		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ANN_CALENDAR_ID, calId);
		filters.put(ENABLED, 1);
		filters.put(ORDER_ANN_DATE_MAYQ, startTime);
		filters.put(ORDER_ANN_DATE_MENQ, endTime);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_ANN_DATE_DESC);
		List<AnnualDiary> resultQuery = annualDiaryDAO.listOrderFilter(filters, orders);
		List<AnnualDiaryDTO> result = new ArrayList<AnnualDiaryDTO>();
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});

		return result;
	}

	@Override
	public List<AnnualDiaryDTO> getAnnualDiaryCalendarByDate(long calId, String selectedDate, int numDays) {

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

		calendarGreg.add(Calendar.DAY_OF_YEAR, numDays);
		Date endTime = calendarGreg.getTime();

		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ANN_CALENDAR_ID, calId);
		filters.put(ENABLED, 1);
		filters.put(ORDER_ANN_DATE_MAYQ, startTime);
		filters.put(ORDER_ANN_DATE_MENQ, endTime);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_ANN_DATE_DESC);
		List<AnnualDiary> resultQuery = annualDiaryDAO.listOrderFilter(filters, orders);
		List<AnnualDiaryDTO> result = new ArrayList<AnnualDiaryDTO>();
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});

		return result;
	}

 }