package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.BilledDTO;
import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.BilledDAO;
import com.diloso.bookhair.app.persist.entities.Billed;
import com.diloso.bookhair.app.persist.mapper.BilledMapper;

@Component
@Scope(value = "singleton")
public class BilledManager implements IBilledManager {

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
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Float getBilledSales(CalendarDTO calendar, String startDate, String endDate) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Float getBilledSalesTask(CalendarDTO calendar, String startDate, String endDate, Long localTaskId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Float getBilledSalesProduct(CalendarDTO calendar, String startDate, String endDate, Long productId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
/*


	public List<BilledDTO> getBilledByInvoice(long bilInvoiceId, String lang) {
		List<Entity> resultQuery = null;
		BilledDTO billed = null;
		List<BilledDTO> result = new ArrayList<BilledDTO>();
		try {
			Filter calendarFilter = new FilterPredicate("bilInvoiceId",
					FilterOperator.EQUAL, bilInvoiceId);
			Filter enabledFilter = new FilterPredicate("enabled",
					FilterOperator.EQUAL, 1);
		

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter, enabledFilter);

			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					"Billed").setFilter(compositeFilter);

			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			String name = "";
			MultiTextDTO multiTextKey = null;
			for (Entity entity : resultQuery) {
				billed =  new BilledDTO();
				// Propiedades de LocalTask
				Long bilLocalTaskId =  (Long)entity.getProperty("bilLocalTaskId");
				if (bilLocalTaskId != null) {
					LocalTaskDTO localbilled = iLocalTaskManager.getById(bilLocalTaskId);
						multiTextKey = iMultiTextManager.getByLanCodeAndKey(lang,
								localbilled.getLotNameMulti());
					name = multiTextKey.getMulText();
					if (name != null) {
						localbilled.setLotName(name);
						billed.setBilLocalTask(localbilled);
						result.add(billed);
					}
				} else {	
				
					// Propiedades de Product
					Long bilProductId = (Long)entity.getProperty("bilProductId");
					if (bilProductId != null) {
						ProductDTO product = iProductManager.getById(bilProductId);
						multiTextKey = iMultiTextManager.getByLanCodeAndKey(lang,
								product.getProNameMulti());
						name = multiTextKey.getMulText();
						if (name != null) {
							product.setProName(name);
							billed.setBilProduct(product);
							result.add(billed);
						}
					}
				}
			}

		} catch (Exception ex) {
		}

		return result;
	}
	
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

			Filter calendarFilter = new FilterPredicate("bilCalendarId",
					FilterOperator.EQUAL, calendar.getId());
			Filter enabledFilter = new FilterPredicate("enabled",
					FilterOperator.EQUAL, 1);
			Filter fromFilter = new FilterPredicate("bilTime",
					FilterOperator.GREATER_THAN_OR_EQUAL, startTime);
			Filter untilFilter = new FilterPredicate("bilTime",
					FilterOperator.LESS_THAN_OR_EQUAL, endTime);

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter, enabledFilter, fromFilter,	untilFilter);

			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					"Billed").setFilter(compositeFilter);

			query.addProjection(new PropertyProjection("bilRate", Float.class));

			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity : resultQuery) {
				result += ((Double) entity.getProperty("bilRate")).floatValue();
			}

		} catch (Exception ex) {
		}
		return result;
	}

		
	public Float getBilledSalesTask(CalendarDTO calendar, String startDate,
			String endDate, Long localTaskId) {

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

			Filter calendarFilter = new FilterPredicate("bilCalendarId",
					FilterOperator.EQUAL, calendar.getId());
			Filter enabledFilter = new FilterPredicate("enabled",
					FilterOperator.EQUAL, 1);
			Filter fromFilter = new FilterPredicate("bilTime",
					FilterOperator.GREATER_THAN_OR_EQUAL, startTime);
			Filter untilFilter = new FilterPredicate("bilTime",
					FilterOperator.LESS_THAN_OR_EQUAL, endTime);

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter, enabledFilter, fromFilter, untilFilter);

			if (localTaskId != null) {
				Filter taskFilter = new FilterPredicate("bilLocalTaskId",
						FilterOperator.EQUAL, localTaskId);
				compositeFilter = CompositeFilterOperator.and(compositeFilter,
						taskFilter);
			} else {
				Filter productFilter = new FilterPredicate("bilProductId",
						FilterOperator.EQUAL, null);
				compositeFilter = CompositeFilterOperator.and(compositeFilter, productFilter);
			}
			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					"Billed").setFilter(compositeFilter);

			query.addProjection(new PropertyProjection("bilRate", Float.class));

			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity : resultQuery) {
				result += ((Double) entity.getProperty("bilRate")).floatValue();
			}

		} catch (Exception ex) {
		}
		return result;
	}
	
	public Float getBilledSalesProduct(CalendarDTO calendar, String startDate,
			String endDate, Long productId) {

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

			Filter calendarFilter = new FilterPredicate("bilCalendarId",
					FilterOperator.EQUAL, calendar.getId());
			Filter enabledFilter = new FilterPredicate("enabled",
					FilterOperator.EQUAL, 1);
			Filter fromFilter = new FilterPredicate("bilTime",
					FilterOperator.GREATER_THAN_OR_EQUAL, startTime);
			Filter untilFilter = new FilterPredicate("bilTime",
					FilterOperator.LESS_THAN_OR_EQUAL, endTime);

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter, enabledFilter, fromFilter, untilFilter);

			if (productId != null) {
				Filter productFilter = new FilterPredicate("bilProductId",
						FilterOperator.EQUAL, productId);
				compositeFilter = CompositeFilterOperator.and(compositeFilter, productFilter);
			} else {
				Filter taskFilter = new FilterPredicate("bilLocalTaskId",
						FilterOperator.EQUAL, null);
				compositeFilter = CompositeFilterOperator.and(compositeFilter,
						taskFilter);
			}
			
			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					"Billed").setFilter(compositeFilter);

			query.addProjection(new PropertyProjection("bilRate", Float.class));

			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity : resultQuery) {
				result += ((Double) entity.getProperty("bilRate")).floatValue();
			}

		} catch (Exception ex) {
		}
		return result;
	}


*/
	

}