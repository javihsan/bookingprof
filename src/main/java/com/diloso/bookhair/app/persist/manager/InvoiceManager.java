package com.diloso.bookhair.app.persist.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.controllers.CalendarController;
import com.diloso.bookhair.app.negocio.dao.InvoiceDAO;
import com.diloso.bookhair.app.negocio.dao.MultiTextDAO;
import com.diloso.bookhair.app.negocio.dao.TaskDAO;
import com.diloso.bookhair.app.negocio.dto.InvoiceDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.entities.Invoice;
import com.diloso.bookhair.app.persist.transformer.InvoiceTransformer;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

@Component
@Scope(value = "singleton")
public class InvoiceManager extends Manager implements InvoiceDAO {

	public static final String KEY_MULTI_LOCAL_TASK_NAME = "local_task_name_";
	public static final String FIELD_MULTI_LOCAL_TASK_NAME = "invNameMulti";
	public static final String FIELD_MULTI_LOCAL_TASK_ENTITY_NAME = FIELD_MULTI_LOCAL_TASK_NAME + "Id";

	//@Autowired
	protected MultiTextDAO multiTextDAO;
	
	//@Autowired
	protected TaskDAO taskDAO;
	
	//@Autowired
	protected InvoiceTransformer invoiceTransformer;
	
	public InvoiceManager() {

	}

	public InvoiceDTO create(InvoiceDTO invoice) throws Exception {
		EntityManager em = getEntityManager();
		Invoice entityInvoice = invoiceTransformer
				.transformDTOToEntity(invoice);
		try {
			em.getTransaction().begin();
			em.persist(entityInvoice);
			em.getTransaction().commit();
		} catch (Exception ex) {
			try {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
			} catch (Exception e) {
				throw e;
			}
			throw ex;
		} finally {
			em.close();
		}
		return invoiceTransformer.transformEntityToDTO(
				entityInvoice);
	}

	public InvoiceDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		Invoice oldEntityInvoice = new Invoice();
		try {
			em.getTransaction().begin();
			Invoice entityInvoice = (Invoice) em
					.find(Invoice.class, id);
			PropertyUtils.copyProperties(oldEntityInvoice, entityInvoice);
			em.remove(em.merge(entityInvoice));
			em.getTransaction().commit();
		} catch (Exception ex) {
			try {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
			} catch (Exception e) {
				throw e;
			}
			throw ex;
		} finally {
			em.close();
		}
		return invoiceTransformer.transformEntityToDTO(
				oldEntityInvoice);
	}

	public InvoiceDTO update(InvoiceDTO invoice) throws Exception {
		EntityManager em = getEntityManager();
		Invoice entityInvoice = invoiceTransformer
				.transformDTOToEntity(invoice);
		Invoice oldEntityInvoice = null;
		try {
			em.getTransaction().begin();
			oldEntityInvoice = (Invoice) em.find(Invoice.class,
					entityInvoice.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityInvoice,
					oldEntityInvoice);
			entityInvoice = em.merge(entityInvoice);
			em.getTransaction().commit();
		} catch (Exception ex) {
			try {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
			} catch (Exception e) {
				throw e;
			}
			throw ex;
		} finally {
			em.close();
		}
		return invoiceTransformer.transformEntityToDTO(
				entityInvoice);
	}

	public InvoiceDTO getById(long id) {
		Entity entityInvoice = null;
		try {
			Key k = KeyFactory.createKey(Invoice.class.getSimpleName(), id);
			entityInvoice = DatastoreServiceFactory.getDatastoreService()
					.get(k);
		} catch (Exception ex) {
		}
		return invoiceTransformer.transformEntityToDTO(
				entityInvoice);
	}

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
				invoice = invoiceTransformer.transformEntityToDTO(entity);
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
				invoice = invoiceTransformer.transformEntityToDTO(entity);
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
				invoice = invoiceTransformer.transformEntityToDTO(entity);
				result.add(invoice);
			}

		} catch (Exception ex) {
		}

		return result;

	}

	public void setMultiTextDAO(MultiTextDAO multiTextDAO) {
		this.multiTextDAO = multiTextDAO;
	}

	public void setTaskDAO(TaskDAO taskDAO) {
		this.taskDAO = taskDAO;
	}

	public void setInvoiceTransformer(InvoiceTransformer invoiceTransformer) {
		this.invoiceTransformer = invoiceTransformer;
	}

	
	
}