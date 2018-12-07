package com.diloso.bookhair.app.persist.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.controllers.CalendarController;
import com.diloso.bookhair.app.negocio.dao.BilledDAO;
import com.diloso.bookhair.app.negocio.dao.LocalTaskDAO;
import com.diloso.bookhair.app.negocio.dao.MultiTextDAO;
import com.diloso.bookhair.app.negocio.dao.ProductDAO;
import com.diloso.bookhair.app.negocio.dao.TaskDAO;
import com.diloso.bookhair.app.negocio.dto.BilledDTO;
import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.dto.ProductDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.entities.Billed;
import com.diloso.bookhair.app.persist.transformer.BilledTransformer;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@Component
@Scope(value = "singleton")
public class BilledManager extends Manager implements BilledDAO {


	@Autowired
	protected MultiTextDAO multiTextDAO;
	
	@Autowired
	protected TaskDAO taskDAO;
	
	@Autowired
	protected LocalTaskDAO localTaskDAO;
	
	@Autowired
	protected ProductDAO productDAO;
	
	@Autowired
	protected BilledTransformer billedTransformer;

	
	public BilledManager() {

	}

	public BilledDTO create(BilledDTO billed) throws Exception {
		EntityManager em = getEntityManager();
		Billed entityBilled = billedTransformer
				.transformDTOToEntity(billed);
		try {
			em.getTransaction().begin();
			em.persist(entityBilled);
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
		return billedTransformer.transformEntityToDTO(
				entityBilled);
	}

	public BilledDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		Billed oldEntityBilled = new Billed();
		try {
			em.getTransaction().begin();
			Billed entityBilled = (Billed) em
					.find(Billed.class, id);
			PropertyUtils.copyProperties(oldEntityBilled, entityBilled);
			em.remove(em.merge(entityBilled));
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
		return billedTransformer.transformEntityToDTO(
				oldEntityBilled);
	}

	public BilledDTO update(BilledDTO billed) throws Exception {
		EntityManager em = getEntityManager();
		Billed entityBilled = billedTransformer
				.transformDTOToEntity(billed);
		Billed oldEntityBilled = null;
		try {
			em.getTransaction().begin();
			oldEntityBilled = (Billed) em.find(Billed.class,
					entityBilled.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityBilled,
					oldEntityBilled);
			entityBilled = em.merge(entityBilled);
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
		return billedTransformer.transformEntityToDTO(
				entityBilled);
	}

	public BilledDTO getById(long id) {
		Entity entityBilled = null;
		try {
			Key k = KeyFactory.createKey(Billed.class.getSimpleName(), id);
			entityBilled = DatastoreServiceFactory.getDatastoreService()
					.get(k);
		} catch (Exception ex) {
		}
		return billedTransformer.transformEntityToDTO(
				entityBilled);
	}

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
					LocalTaskDTO localbilled = localTaskDAO.getById(bilLocalTaskId);
						multiTextKey = multiTextDAO.getByLanCodeAndKey(lang,
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
						ProductDTO product = productDAO.getById(bilProductId);
						multiTextKey = multiTextDAO.getByLanCodeAndKey(lang,
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

}