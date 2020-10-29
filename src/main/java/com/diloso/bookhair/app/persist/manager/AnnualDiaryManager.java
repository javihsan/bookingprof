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
import com.diloso.bookhair.app.negocio.dao.AnnualDiaryDAO;
import com.diloso.bookhair.app.negocio.dto.AnnualDiaryDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.entities.AnnualDiary;
import com.diloso.bookhair.app.persist.transformer.AnnualDiaryTransformer;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

@Component
@Scope(value = "singleton")
public class AnnualDiaryManager extends Manager implements AnnualDiaryDAO {

	//@Autowired
	protected AnnualDiaryTransformer annualDiaryTransformer;
	
	public AnnualDiaryManager() {
	
	}
	 
	public AnnualDiaryDTO create(AnnualDiaryDTO annualDiary) throws Exception {
		EntityManager em = getEntityManager();
		AnnualDiary entityAnnualDiary = annualDiaryTransformer.transformDTOToEntity(annualDiary);
		try {
			em.getTransaction().begin();
			em.persist(entityAnnualDiary);
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
		return annualDiaryTransformer.transformEntityToDTO(entityAnnualDiary);
	}

	public AnnualDiaryDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		AnnualDiary oldEntityAnnualDiary = new AnnualDiary();
		try {
			em.getTransaction().begin();
			AnnualDiary entityAnnualDiary = (AnnualDiary) em.find(AnnualDiary.class, id);
			PropertyUtils.copyProperties(oldEntityAnnualDiary, entityAnnualDiary);
			em.remove(em.merge(entityAnnualDiary));
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
		return annualDiaryTransformer.transformEntityToDTO(oldEntityAnnualDiary);
	}
	
	public AnnualDiaryDTO update(AnnualDiaryDTO annualDiary) throws Exception {
		EntityManager em = getEntityManager();
		AnnualDiary entityAnnualDiary = annualDiaryTransformer.transformDTOToEntity(annualDiary);
		AnnualDiary oldEntityAnnualDiary = null;
		try {
			em.getTransaction().begin();
			oldEntityAnnualDiary = (AnnualDiary) em.find(AnnualDiary.class, entityAnnualDiary.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityAnnualDiary, oldEntityAnnualDiary);
			entityAnnualDiary = em.merge(entityAnnualDiary);
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
		return annualDiaryTransformer.transformEntityToDTO(entityAnnualDiary);
	}

	public AnnualDiaryDTO getById(long id) {
		AnnualDiary entityAnnualDiary = null;
		EntityManager em = getEntityManager();
		try {
			entityAnnualDiary = (AnnualDiary) em.find(AnnualDiary.class, id);
		} finally {
			em.close();
		}
		return annualDiaryTransformer.transformEntityToDTO(entityAnnualDiary);
	}

	public AnnualDiaryDTO getAnnualDiaryByDay(long localId, String selectedDate) {
		EntityManager em = getEntityManager();
		List<AnnualDiary> resultQuery = null;
		AnnualDiaryDTO annualDiary = null;
		try {
			
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
			
			javax.persistence.Query query = em.createNamedQuery("getAnnualDiaryLocal");
			query.setParameter("anuLocalId", localId);
			query.setParameter("anuDate", startTime);
			resultQuery = (List<AnnualDiary>) query.getResultList();
			if (resultQuery.size()==1){
				annualDiary = annualDiaryTransformer.transformEntityToDTO(resultQuery.get(0));
			}
		} finally {
			em.close();
		}
			
		return annualDiary;
	}
	
	public List<AnnualDiaryDTO> getAnnualDiary(long localId) {
		List<AnnualDiaryDTO> result = new ArrayList<AnnualDiaryDTO>();
		List<Entity> resultQuery = null;
		AnnualDiaryDTO diary = null;
		try {
			
			
			Filter calendarFilter = new FilterPredicate("anuLocalId",
					FilterOperator.EQUAL, localId);
			Filter enabledFilter = new FilterPredicate("enabled",
					FilterOperator.EQUAL, 1);

			Filter compositeFilter = CompositeFilterOperator.and(calendarFilter, enabledFilter);

			Query query = new Query("AnnualDiary").setFilter(compositeFilter);
			
			query.addSort("anuDate", SortDirection.DESCENDING);
			
			DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity: resultQuery){
				diary = annualDiaryTransformer.transformEntityToDTO(entity);
				result.add(diary);
			}
			
		} catch (Exception ex){
		}
		return result;
	}

	
	public List<AnnualDiaryDTO> getAnnualDiaryByMonth(long localId, String selectedDate) {
		List<AnnualDiaryDTO> result = new ArrayList<AnnualDiaryDTO>();
		List<Entity> resultQuery = null;
		AnnualDiaryDTO diary = null;
		try {
			
			String[] dates = selectedDate.split(CalendarController.CHAR_SEP_DATE);
			String year = dates[0];
			String month = dates[1];
			// Hay que tener en cuenta los dias de antes y los de despues del mes a mostrar
			Calendar calendarGreg = new GregorianCalendar();
			calendarGreg.set(Calendar.YEAR, new Integer(year));
			calendarGreg.set(Calendar.MONTH, new Integer(month) - 1 - 1);
			calendarGreg.set(Calendar.DAY_OF_MONTH, 1);
			calendarGreg.set(Calendar.HOUR_OF_DAY,0);
			calendarGreg.set(Calendar.MINUTE,0);
			calendarGreg.set(Calendar.SECOND,0);
			calendarGreg.set(Calendar.MILLISECOND,0);
			Date startTime = calendarGreg.getTime();
			
			calendarGreg.add(Calendar.DAY_OF_YEAR, 93);
			Date endTime = calendarGreg.getTime();
			
			Filter calendarFilter = new FilterPredicate("anuLocalId",
					FilterOperator.EQUAL, localId);
			Filter enabledFilter = new FilterPredicate("enabled",
					FilterOperator.EQUAL, 1);
			Filter fromFilter = new FilterPredicate("anuDate",
					FilterOperator.GREATER_THAN_OR_EQUAL, startTime);
			Filter untilFilter = new FilterPredicate("anuDate",
					FilterOperator.LESS_THAN_OR_EQUAL, endTime);

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter, enabledFilter, fromFilter, untilFilter);

			Query query = new Query("AnnualDiary").setFilter(compositeFilter);
			
			query.addSort("anuDate", SortDirection.DESCENDING);
			
			DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity: resultQuery){
				diary = annualDiaryTransformer.transformEntityToDTO(entity);
				result.add(diary);
			}
			
		} catch (Exception ex){
		}
		return result;
	}
	
	public List<AnnualDiaryDTO> getAnnualDiaryByDate(long localId, String selectedDate, int numDays) {
		List<AnnualDiaryDTO> result = new ArrayList<AnnualDiaryDTO>();
		List<Entity> resultQuery = null;
		AnnualDiaryDTO diary = null;
		try {
			
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
			
			calendarGreg.add(Calendar.DAY_OF_YEAR, numDays);
			Date endTime = calendarGreg.getTime();
			
			Filter calendarFilter = new FilterPredicate("anuLocalId",
					FilterOperator.EQUAL, localId);
			Filter enabledFilter = new FilterPredicate("enabled",
					FilterOperator.EQUAL, 1);
			Filter fromFilter = new FilterPredicate("anuDate",
					FilterOperator.GREATER_THAN_OR_EQUAL, startTime);
			Filter untilFilter = new FilterPredicate("anuDate",
					FilterOperator.LESS_THAN_OR_EQUAL, endTime);

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter, enabledFilter, fromFilter, untilFilter);

			Query query = new Query("AnnualDiary").setFilter(compositeFilter);
			
			query.addSort("anuDate", SortDirection.DESCENDING);
			
			DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity: resultQuery){
				diary = annualDiaryTransformer.transformEntityToDTO(entity);
				result.add(diary);
			}
			
		} catch (Exception ex){
		}
		return result;
	}
	
	public AnnualDiaryDTO getAnnualDiaryCalendarByDay(long calId, String selectedDate) {
		EntityManager em = getEntityManager();
		List<AnnualDiary> resultQuery = null;
		AnnualDiaryDTO annualDiary = null;
		try {
			
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
			
			javax.persistence.Query query = em.createNamedQuery("getAnnualDiaryCalendar");
			query.setParameter("anuCalendarId", calId);
			query.setParameter("anuDate", startTime);
			resultQuery = (List<AnnualDiary>) query.getResultList();
			if (resultQuery.size()==1){
				annualDiary = annualDiaryTransformer.transformEntityToDTO(resultQuery.get(0));
			}
		} finally {
			em.close();
		}
			
		return annualDiary;
	}
	
	public List<AnnualDiaryDTO> getAnnualDiaryCalendarByMonth(long calId, String selectedDate) {
		List<AnnualDiaryDTO> result = new ArrayList<AnnualDiaryDTO>();
		List<Entity> resultQuery = null;
		AnnualDiaryDTO diary = null;
		try {
			
			String[] dates = selectedDate.split(CalendarController.CHAR_SEP_DATE);
			String year = dates[0];
			String month = dates[1];
			// Hay que tener en cuenta los dias de antes y los de despues del mes a mostrar
			Calendar calendarGreg = new GregorianCalendar();
			calendarGreg.set(Calendar.YEAR, new Integer(year));
			calendarGreg.set(Calendar.MONTH, new Integer(month) - 1 - 1);
			calendarGreg.set(Calendar.DAY_OF_MONTH, 1);
			calendarGreg.set(Calendar.HOUR_OF_DAY,0);
			calendarGreg.set(Calendar.MINUTE,0);
			calendarGreg.set(Calendar.SECOND,0);
			calendarGreg.set(Calendar.MILLISECOND,0);
			Date startTime = calendarGreg.getTime();
			
			calendarGreg.add(Calendar.DAY_OF_YEAR, 93);
			Date endTime = calendarGreg.getTime();
			
			Filter calendarFilter = new FilterPredicate("anuCalendarId",
					FilterOperator.EQUAL, calId);
			Filter enabledFilter = new FilterPredicate("enabled",
					FilterOperator.EQUAL, 1);
			Filter fromFilter = new FilterPredicate("anuDate",
					FilterOperator.GREATER_THAN_OR_EQUAL, startTime);
			Filter untilFilter = new FilterPredicate("anuDate",
					FilterOperator.LESS_THAN_OR_EQUAL, endTime);

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter, enabledFilter, fromFilter, untilFilter);

			Query query = new Query("AnnualDiary").setFilter(compositeFilter);
			
			query.addSort("anuDate", SortDirection.DESCENDING);
			
			DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity: resultQuery){
				diary = annualDiaryTransformer.transformEntityToDTO(entity);
				result.add(diary);
			}
			
		} catch (Exception ex){
		}

		return result;
	}
	
	public List<AnnualDiaryDTO> getAnnualDiaryCalendarByDate(long calId, String selectedDate, int numDays) {
		List<AnnualDiaryDTO> result = new ArrayList<AnnualDiaryDTO>();
		List<Entity> resultQuery = null;
		AnnualDiaryDTO diary = null;
		try {
			
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
			
			calendarGreg.add(Calendar.DAY_OF_YEAR, numDays);
			Date endTime = calendarGreg.getTime();
			
			Filter calendarFilter = new FilterPredicate("anuCalendarId",
					FilterOperator.EQUAL, calId);
			Filter enabledFilter = new FilterPredicate("enabled",
					FilterOperator.EQUAL, 1);
			Filter fromFilter = new FilterPredicate("anuDate",
					FilterOperator.GREATER_THAN_OR_EQUAL, startTime);
			Filter untilFilter = new FilterPredicate("anuDate",
					FilterOperator.LESS_THAN_OR_EQUAL, endTime);

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter, enabledFilter, fromFilter, untilFilter);

			Query query = new Query("AnnualDiary").setFilter(compositeFilter);
			
			query.addSort("anuDate", SortDirection.DESCENDING);
			
			DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity: resultQuery){
				diary = annualDiaryTransformer.transformEntityToDTO(entity);
				result.add(diary);
			}
			
		} catch (Exception ex){
		}

		return result;
	}
	
	public AnnualDiaryDTO getAnnualDiaryRepatByDay(long repeatId, String selectedDate) {
		EntityManager em = getEntityManager();
		List<AnnualDiary> resultQuery = null;
		AnnualDiaryDTO annualDiary = null;
		try {
			
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
			
			javax.persistence.Query query = em.createNamedQuery("getAnnualDiaryRepeat");
			query.setParameter("anuRepeatId", repeatId);
			query.setParameter("anuDate", startTime);
			resultQuery = (List<AnnualDiary>) query.getResultList();
			if (resultQuery.size()==1){
				annualDiary = annualDiaryTransformer.transformEntityToDTO(resultQuery.get(0));
			}
		} finally {
			em.close();
		}
			
		return annualDiary;
	}

	public void setAnnualDiaryTransformer(AnnualDiaryTransformer annualDiaryTransformer) {
		this.annualDiaryTransformer = annualDiaryTransformer;
	}
	
	

 }