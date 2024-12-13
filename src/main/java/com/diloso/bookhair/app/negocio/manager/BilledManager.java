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
import com.diloso.bookhair.app.negocio.dto.BilledDTO;
import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.dto.ProductDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.BilledDAO;
import com.diloso.bookhair.app.persist.entities.Billed;
import com.diloso.bookhair.app.persist.mapper.BilledMapper;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@Component
@Scope(value = "singleton")
public class BilledManager implements IBilledManager {

	public static final String BIL_INVOICE_ID = "bilInvoiceId";
	public static final String BIL_CALENDAR_ID = "bilCalendarId";
	public static final String BIL_RATE = "bilRate";
	public static final String BIL_TIME = "bilTime";
	public static final String BIL_LOCAL_TASK_ID = "bilLocalTaskId";
	public static final String BIL_PRODUCT_ID = "bilProductId";
	public static final String ENABLED = "enabled";
	public static final String ORDER_BIL_DATE_ASC = "bilTime";
	public static final String ORDER_BIL_DATE_MAYQ = "bilTime >=";
	public static final String ORDER_BIL_DATE_MENQ = "bilTime <=";
	
	public static final String BIL_ENTITY = "Billed";

	@Autowired
	private BilledDAO billedDAO;

	@Autowired
	protected IMultiTextManager multiTextManager;

	@Autowired
	protected ITaskManager taskManager;

	@Autowired
	protected ILocalTaskManager localTaskManager;

	@Autowired
	protected IProductManager productManager;

	@Autowired
	protected BilledMapper mapper;

	public BilledManager() {

	}

	@Override
	public BilledDTO create(BilledDTO billedDTO) throws Exception {
		Billed billed = mapper.map(billedDTO);
		billed = billedDAO.create(billed);
		return mapper.map(billed);
	}

	@Override
	public BilledDTO remove(long id) throws Exception {
		Billed billed = billedDAO.get(id);
		billedDAO.delete(id);
		if (billed == null) {
			return null;
		}
		return mapper.map(billed);
	}

	@Override
	public BilledDTO update(BilledDTO billedDTO) throws Exception {
		Billed billed = mapper.map(billedDTO);
		Billed oldBilled = billedDAO.get(billedDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(billed, oldBilled);
		} catch (Exception e) {
		}
		billed = billedDAO.update(billed);
		if (billed == null) {
			return null;
		}
		return mapper.map(billed);
	}

	@Override
	public BilledDTO getById(long id) {
		Billed billed = billedDAO.get(id);
		if (billed == null) {
			return null;
		}
		return mapper.map(billed);
	}

	@Override
	public List<BilledDTO> getBilledByInvoice(long bilInvoiceId, String lang) {

		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(BIL_INVOICE_ID, bilInvoiceId);
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_BIL_DATE_ASC);
		List<Billed> resultQuery = billedDAO.listOrderFilter(filters, orders);
		List<BilledDTO> result = new ArrayList<BilledDTO>();
		resultQuery.stream().forEach(entity -> {
			BilledDTO billed = new BilledDTO();
			// Propiedades de LocalTask
			Long bilLocalTaskId = (Long) entity.getBilLocalTaskId();
			if (bilLocalTaskId != null) {
				LocalTaskDTO localbilled = localTaskManager.getById(bilLocalTaskId);
				MultiTextDTO multiTextKey = multiTextManager.getByLanCodeAndKey(lang, localbilled.getLotNameMulti());
				String name = multiTextKey.getMulText();
				if (name != null) {
					localbilled.setLotName(name);
					billed.setBilLocalTask(localbilled);
					result.add(billed);
				}
			} else {

				// Propiedades de Product
				Long bilProductId = (Long) entity.getBilProductId();
				if (bilProductId != null) {
					ProductDTO product = productManager.getById(bilProductId);
					MultiTextDTO multiTextKey = multiTextManager.getByLanCodeAndKey(lang, product.getProNameMulti());
					String name = multiTextKey.getMulText();
					if (name != null) {
						product.setProName(name);
						billed.setBilProduct(product);
						result.add(billed);
					}
				}
			}
		});

		return result;
	}

	@Override
	public Float getBilledSales(CalendarDTO calendar, String startDate, String endDate) {
		
		Float result = new Float(0);
		List<Entity> resultQuery = null;

		try {

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

			Filter calendarFilter = new FilterPredicate(BIL_CALENDAR_ID,
					FilterOperator.EQUAL, calendar.getId());
			Filter enabledFilter = new FilterPredicate(ENABLED,
					FilterOperator.EQUAL, 1);
			Filter fromFilter = new FilterPredicate(BIL_TIME,
					FilterOperator.GREATER_THAN_OR_EQUAL, startTime);
			Filter untilFilter = new FilterPredicate(BIL_TIME,
					FilterOperator.LESS_THAN_OR_EQUAL, endTime);

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter, enabledFilter, fromFilter,	untilFilter);

			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					BIL_ENTITY).setFilter(compositeFilter);

			query.addProjection(new PropertyProjection(BIL_RATE, Float.class));

			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity : resultQuery) {
				result += ((Double) entity.getProperty(BIL_RATE)).floatValue();
			}

		} catch (Exception ex) {
		}
		return result;
	}

	@Override
	public Float getBilledSalesTask(CalendarDTO calendar, String startDate, String endDate, Long localTaskId) {
		Float result = new Float(0);
		List<Entity> resultQuery = null;

		try {

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

			Filter calendarFilter = new FilterPredicate(BIL_CALENDAR_ID,
					FilterOperator.EQUAL, calendar.getId());
			Filter enabledFilter = new FilterPredicate(ENABLED,
					FilterOperator.EQUAL, 1);
			Filter fromFilter = new FilterPredicate(BIL_TIME,
					FilterOperator.GREATER_THAN_OR_EQUAL, startTime);
			Filter untilFilter = new FilterPredicate(BIL_TIME,
					FilterOperator.LESS_THAN_OR_EQUAL, endTime);

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter, enabledFilter, fromFilter, untilFilter);

			if (localTaskId != null) {
				Filter taskFilter = new FilterPredicate(BIL_LOCAL_TASK_ID,
						FilterOperator.EQUAL, localTaskId);
				compositeFilter = CompositeFilterOperator.and(compositeFilter,
						taskFilter);
			} else {
				Filter productFilter = new FilterPredicate(BIL_PRODUCT_ID,
						FilterOperator.EQUAL, null);
				compositeFilter = CompositeFilterOperator.and(compositeFilter, productFilter);
			}
			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					BIL_ENTITY).setFilter(compositeFilter);

			query.addProjection(new PropertyProjection(BIL_RATE, Float.class));

			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity : resultQuery) {
				result += ((Double) entity.getProperty(BIL_RATE)).floatValue();
			}

		} catch (Exception ex) {
		}
		return result;
	}

	@Override
	public Float getBilledSalesProduct(CalendarDTO calendar, String startDate, String endDate, Long productId) {
		Float result = new Float(0);
		List<Entity> resultQuery = null;

		try {

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

			Filter calendarFilter = new FilterPredicate(BIL_CALENDAR_ID,
					FilterOperator.EQUAL, calendar.getId());
			Filter enabledFilter = new FilterPredicate(ENABLED,
					FilterOperator.EQUAL, 1);
			Filter fromFilter = new FilterPredicate(BIL_TIME,
					FilterOperator.GREATER_THAN_OR_EQUAL, startTime);
			Filter untilFilter = new FilterPredicate(BIL_TIME,
					FilterOperator.LESS_THAN_OR_EQUAL, endTime);

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter, enabledFilter, fromFilter, untilFilter);

			if (productId != null) {
				Filter productFilter = new FilterPredicate(BIL_PRODUCT_ID,
						FilterOperator.EQUAL, productId);
				compositeFilter = CompositeFilterOperator.and(compositeFilter, productFilter);
			} else {
				Filter taskFilter = new FilterPredicate(BIL_LOCAL_TASK_ID,
						FilterOperator.EQUAL, null);
				compositeFilter = CompositeFilterOperator.and(compositeFilter,
						taskFilter);
			}
			
			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					BIL_ENTITY).setFilter(compositeFilter);

			query.addProjection(new PropertyProjection(BIL_RATE, Float.class));

			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity : resultQuery) {
				result += ((Double) entity.getProperty(BIL_RATE)).floatValue();
			}

		} catch (Exception ex) {
		}
		return result;
	}

	
}