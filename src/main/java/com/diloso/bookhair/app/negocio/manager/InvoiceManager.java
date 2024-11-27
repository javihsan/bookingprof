package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.InvoiceDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.InvoiceDAO;
import com.diloso.bookhair.app.persist.entities.Invoice;
import com.diloso.bookhair.app.persist.mapper.InvoiceMapper;

@Component
@Scope(value = "singleton")
public class InvoiceManager implements IInvoiceManager {

	public static final String KEY_MULTI_LOCAL_TASK_NAME = "local_task_name_";
	public static final String FIELD_MULTI_LOCAL_TASK_NAME = "invNameMulti";
	public static final String FIELD_MULTI_LOCAL_TASK_ENTITY_NAME = FIELD_MULTI_LOCAL_TASK_NAME + "Id";

	@Autowired
	private InvoiceDAO invoiceDAO;
	
	@Autowired
	protected IMultiTextManager multiTextManager;
	
	@Autowired
	protected ITaskManager taskManager;
	
	@Autowired
	protected InvoiceMapper mapper;
	
	public InvoiceManager() {

	}

	@Override
	public InvoiceDTO create(InvoiceDTO invoiceDTO) throws Exception {
		Invoice invoice = mapper.map(invoiceDTO);
		invoice = invoiceDAO.create(invoice);
		return mapper.map(invoice);
	}

	@Override
	public InvoiceDTO remove(long id) throws Exception {
		Invoice invoice = invoiceDAO.get(id);
		invoiceDAO.delete(id);
		if (invoice == null) {
			return null;
		}
		return mapper.map(invoice);
	}

	@Override
	public InvoiceDTO update(InvoiceDTO invoiceDTO) throws Exception {
		Invoice invoice = mapper.map(invoiceDTO);
		Invoice oldInvoice = invoiceDAO.get(invoiceDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(invoice, oldInvoice);
		} catch (Exception e) {
		}
		invoice = invoiceDAO.update(invoice);
		if (invoice == null) {
			return null;
		}
		return mapper.map(invoice);
	}

	@Override
	public InvoiceDTO getById(long id) {
		Invoice invoice = invoiceDAO.get(id);
		if (invoice == null) {
			return null;
		}
		return mapper.map(invoice);		
	}

	@Override
	public List<InvoiceDTO> getInvoiceByWeek(long invLocalId, String selectedDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceDTO> getInvoiceByDay(long invLocalId, String selectedDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceDTO> getInvoiceByClientAgo(long invLocalId, long clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	/*
	

	public List<InvoiceDTO> getInvoiceByWeek(long invLocalId, String selectedDate) {

		List<Entity> resultQuery = null;
		InvoiceDTO invoice = null;
		List<InvoiceDTO> result = new ArrayList<InvoiceDTO>();
		try {

			String[] dates = selectedDate
					.split(CalendarController.CHAR_SEP_DATE);
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

			Filter calendarFilter = new FilterPredicate("invLocalId",
					FilterOperator.EQUAL, invLocalId);
			Filter enabledFilter = new FilterPredicate("enabled",
					FilterOperator.EQUAL, 1);
			Filter fromFilter = new FilterPredicate("invTime",
					FilterOperator.GREATER_THAN_OR_EQUAL, startTime);
			Filter untilFilter = new FilterPredicate("invTime",
					FilterOperator.LESS_THAN_OR_EQUAL, endTime);

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter, enabledFilter, fromFilter, untilFilter);

			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					"Invoice").setFilter(compositeFilter);

			query.addSort("invTime", SortDirection.ASCENDING);
			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity : resultQuery) {
				invoice = invoiceMapper.map(entity);
				result.add(invoice);
			}

		} catch (Exception ex) {
		}

		return result;

	}
	
	public List<InvoiceDTO> getInvoiceByDay(long invLocalId, String selectedDate) {

		List<Entity> resultQuery = null;
		InvoiceDTO invoice = null;
		List<InvoiceDTO> result = new ArrayList<InvoiceDTO>();
		try {

			String[] dates = selectedDate
					.split(CalendarController.CHAR_SEP_DATE);
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

			Filter calendarFilter = new FilterPredicate("invLocalId",
					FilterOperator.EQUAL, invLocalId);
			Filter enabledFilter = new FilterPredicate("enabled",
					FilterOperator.EQUAL, 1);
			Filter fromFilter = new FilterPredicate("invTime",
					FilterOperator.GREATER_THAN_OR_EQUAL, startTime);
			Filter untilFilter = new FilterPredicate("invTime",
					FilterOperator.LESS_THAN_OR_EQUAL, endTime);

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter, enabledFilter, fromFilter, untilFilter);

			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					"Invoice").setFilter(compositeFilter);

			query.addSort("invTime", SortDirection.ASCENDING);

			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity : resultQuery) {
				invoice = invoiceMapper.map(entity);
				result.add(invoice);
			}

		} catch (Exception ex) {
		}

		return result;

	}
	
	public List<InvoiceDTO> getInvoiceByClientAgo(long invLocalId, long clientId) {

		List<Entity> resultQuery = null;
		InvoiceDTO invoice = null;
		List<InvoiceDTO> result = new ArrayList<InvoiceDTO>();
		try {

			Filter clientFilter = new FilterPredicate("invClientId",
					FilterOperator.EQUAL, clientId);
			Filter calendarFilter = new FilterPredicate("invLocalId",
					FilterOperator.EQUAL, invLocalId);
			Filter enabledFilter = new FilterPredicate("enabled",
					FilterOperator.EQUAL, 1);

			Filter compositeFilter = CompositeFilterOperator.and(clientFilter,
					calendarFilter, enabledFilter);

			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					"Invoice").setFilter(compositeFilter);

			DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(50));
			for (Entity entity : resultQuery) {
				invoice = invoiceMapper.map(entity);
				result.add(invoice);
			}

		} catch (Exception ex) {
		}

		return result;

	}


	*/
	
	
}