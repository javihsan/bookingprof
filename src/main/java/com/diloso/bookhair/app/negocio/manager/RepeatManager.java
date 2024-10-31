package com.diloso.bookhair.app.negocio.manager;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.RepeatDTO;
import com.diloso.bookhair.app.persist.dao.RepeatDAO;
import com.diloso.bookhair.app.persist.mapper.RepeatMapper;

@Component
@Scope(value = "singleton")
public class RepeatManager implements IRepeatManager {

	protected static final Logger log = Logger.getLogger(RepeatManager.class.getName());

	public static final String KEY_MULTI_REPEAT_NAME = "repeat_name_";
	public static final String FIELD_MULTI_REPEAT_NAME = "repNameMulti";
	public static final String FIELD_MULTI_REPEAT_ENTITY_NAME = KEY_MULTI_REPEAT_NAME
			+ "Id";

	@Autowired
	private RepeatDAO repeatDAO;
	
	@Autowired
	protected RepeatMapper repeatMapper;

	public RepeatManager() {

	}

	@Override
	public RepeatDTO create(RepeatDTO event) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RepeatDTO remove(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RepeatDTO update(RepeatDTO event) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RepeatDTO getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RepeatDTO> getRepeatByDay(CalendarDTO calendar, String selectedDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RepeatDTO> getRepeatByWeek(CalendarDTO calendar, String selectedDate) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/*
	public RepeatDTO create(RepeatDTO repeat) throws Exception {
		EntityManager em = getEntityManager();
		Repeat entityRepeat = repeatMapper.map(repeat);
		try {
			em.getTransaction().begin();
			em.persist(entityRepeat);
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
		return repeatMapper.map(entityRepeat);
	}

	public RepeatDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		Repeat oldEntityRepeat = new Repeat();
		try {
			em.getTransaction().begin();
			Repeat entityRepeat = (Repeat) em.find(Repeat.class, id);
			PropertyUtils.copyProperties(oldEntityRepeat, entityRepeat);
			em.remove(em.merge(entityRepeat));
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
			// log.warn("Eliminado repeat: " +
			// oldEntityRepeat.getEveCalendarId()
			// + " " + oldEntityRepeat.getEveBookingTime() + " "
			// + oldEntityRepeat.getEveClientId());
		}
		return repeatMapper.map(oldEntityRepeat);
	}

	public RepeatDTO update(RepeatDTO repeat) throws Exception {
		EntityManager em = getEntityManager();
		Repeat entityRepeat = repeatMapper.map(repeat);
		Repeat oldEntityRepeat = null;
		try {
			em.getTransaction().begin();
			oldEntityRepeat = (Repeat) em.find(Repeat.class,
					entityRepeat.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityRepeat,
					oldEntityRepeat);
			entityRepeat = em.merge(entityRepeat);
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
		return repeatMapper.map(entityRepeat);
	}

	public RepeatDTO getById(long id) {
		Repeat entityRepeat = null;
		EntityManager em = getEntityManager();
		try {
			entityRepeat = (Repeat) em.find(Repeat.class, id);
		} finally {
			em.close();
		}
		return repeatMapper.map(entityRepeat);
	}

	public List<RepeatDTO> getRepeatByDay(CalendarDTO calendar,
			String selectedDate) {

		List<Entity> resultQuery = null;
		List<RepeatDTO> result = new ArrayList<RepeatDTO>();
		RepeatDTO repeat = null;
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

			Filter calendarFilter = new FilterPredicate("eveCalendarId",
					FilterOperator.EQUAL, calendar.getId());
			Filter enabledFilter = new FilterPredicate("enabled",
					FilterOperator.EQUAL, 1);
			Filter untilFilter = new FilterPredicate("eveEndTime",
					FilterOperator.GREATER_THAN_OR_EQUAL, startTime);

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter, enabledFilter, untilFilter);

			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					"Repeat").setFilter(compositeFilter);

			query.addSort("eveEndTime", SortDirection.ASCENDING);

			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity : resultQuery) {
				if (endTime.after((Date) entity.getProperty("eveStartTime"))) {
					repeat = repeatMapper.map(entity);
					result.add(repeat);
				}
			}

		} catch (Exception ex) {
		}

		return result;

	}

	public List<RepeatDTO> getRepeatByWeek(CalendarDTO calendar,
			String selectedDate) {

		List<Entity> resultQuery = null;
		RepeatDTO repeat = null;
		List<RepeatDTO> result = new ArrayList<RepeatDTO>();
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

			Filter calendarFilter = new FilterPredicate("eveCalendarId",
					FilterOperator.EQUAL, calendar.getId());
			Filter enabledFilter = new FilterPredicate("enabled",
					FilterOperator.EQUAL, 1);

			Filter untilFilter = new FilterPredicate("eveEndTime",
					FilterOperator.GREATER_THAN_OR_EQUAL, startTime);

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter, enabledFilter, untilFilter);

			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					"Repeat").setFilter(compositeFilter);

			query.addSort("eveEndTime", SortDirection.ASCENDING);
			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity : resultQuery) {
				if (endTime.after((Date) entity.getProperty("eveStartTime"))) {
					repeat = repeatMapper.map(entity);
					result.add(repeat);
				}
			}

		} catch (Exception ex) {
		}

		return result;

	}
	

	public void setRepeatTransformer(RepeatMapper repeatMapper) {
		this.repeatMapper = repeatMapper;
	}
	*/

	/*
	 * public List<RepeatDTO> getRepeatAdmin(CalendarDTO calendar) {
	 * 
	 * List<Entity> resultQuery = null; List<RepeatDTO> result = new
	 * ArrayList<RepeatDTO>(); RepeatDTO repeat = null; try {
	 * 
	 * Filter calendarFilter = new FilterPredicate("eveCalendarId",
	 * FilterOperator.EQUAL, calendar.getId());
	 * 
	 * Filter compositeFilter = CompositeFilterOperator.and( calendarFilter);
	 * 
	 * com.google.appengine.api.datastore.Query query = new
	 * com.google.appengine.api.datastore.Query(
	 * "Repeat").setFilter(compositeFilter);
	 * 
	 * query.addSort("eveStartTime", SortDirection.ASCENDING);
	 * 
	 * DatastoreService dataStore = DatastoreServiceFactory
	 * .getDatastoreService(); PreparedQuery pq = dataStore.prepare(query);
	 * resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000)); for
	 * (Entity entity : resultQuery) { repeat =
	 * repeatTransformer.map( entity); result.add(repeat); }
	 * 
	 * } catch (Exception ex) { }
	 * 
	 * return result;
	 * 
	 * }
	 */

	
	
	/*
	 * public List<RepeatDTO> getRepeatByClientAgo(CalendarDTO calendar, Long
	 * clientId, Date selectedDate, int numDays) {
	 * 
	 * List<Entity> resultQuery = null; RepeatDTO repeat = null; List<RepeatDTO>
	 * result = new ArrayList<RepeatDTO>(); try {
	 * 
	 * Filter clientFilter = new FilterPredicate("repClientId",
	 * FilterOperator.EQUAL, clientId); Filter calendarFilter = new
	 * FilterPredicate("eveCalendarId", FilterOperator.EQUAL, calendar.getId());
	 * Filter enabledFilter = new FilterPredicate("enabled",
	 * FilterOperator.EQUAL, 1);
	 * 
	 * Filter compositeFilter = CompositeFilterOperator.and(clientFilter,
	 * calendarFilter, enabledFilter);
	 * 
	 * if (selectedDate != null) { Calendar calendarGreg = new
	 * GregorianCalendar(); calendarGreg.setTime(selectedDate);
	 * calendarGreg.set(Calendar.MILLISECOND, 0);
	 * calendarGreg.add(Calendar.DAY_OF_MONTH, -numDays); Date startTime =
	 * calendarGreg.getTime();
	 * 
	 * Filter fromFilter = new FilterPredicate("repBookingTime",
	 * FilterOperator.GREATER_THAN_OR_EQUAL, startTime);
	 * 
	 * compositeFilter = CompositeFilterOperator.and(compositeFilter,
	 * fromFilter); }
	 * 
	 * com.google.appengine.api.datastore.Query query = new
	 * com.google.appengine.api.datastore.Query(
	 * "Repeat").setFilter(compositeFilter);
	 * 
	 * query.addSort("repBookingTime", SortDirection.ASCENDING);
	 * DatastoreService dataStore = DatastoreServiceFactory
	 * .getDatastoreService(); PreparedQuery pq = dataStore.prepare(query);
	 * resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000)); for
	 * (Entity entity : resultQuery) { repeat
	 * =repeatTransformer.map( entity); result.add(repeat); }
	 * 
	 * } catch (Exception ex) { }
	 * 
	 * return result;
	 * 
	 * }
	 * 
	 * public List<RepeatDTO> getRepeatByICS(String ICS){
	 * 
	 * List<Entity> resultQuery = null; RepeatDTO repeat = null; List<RepeatDTO>
	 * result = new ArrayList<RepeatDTO>(); try {
	 * 
	 * Filter ICSFilter = new FilterPredicate("repICS", FilterOperator.EQUAL,
	 * ICS); Filter enabledFilter = new FilterPredicate("enabled",
	 * FilterOperator.EQUAL, 1);
	 * 
	 * Filter compositeFilter = CompositeFilterOperator.and( ICSFilter,
	 * enabledFilter);
	 * 
	 * com.google.appengine.api.datastore.Query query = new
	 * com.google.appengine.api.datastore.Query(
	 * "Repeat").setFilter(compositeFilter);
	 * 
	 * DatastoreService dataStore = DatastoreServiceFactory
	 * .getDatastoreService(); PreparedQuery pq = dataStore.prepare(query);
	 * resultQuery = pq.asList(FetchOptions.Builder.withLimit(100)); for (Entity
	 * entity : resultQuery) { repeat =
	 * repeatTransformer.map(entity); result.add(repeat); }
	 * 
	 * } catch (Exception ex) { }
	 * 
	 * return result;
	 * 
	 * }
	 * 
	 * public Integer getRepeatNumber(CalendarDTO calendar, String startDate,
	 * String endDate, Boolean consumed) {
	 * 
	 * Integer result = 0; List<Entity> resultQuery = null; String ICS = null;
	 * List<String> listICS = new ArrayList<String>(); try {
	 * 
	 * String[] dates = startDate.split(CalendarController.CHAR_SEP_DATE);
	 * String year = dates[0]; String month = dates[1]; String day = dates[2];
	 * 
	 * Calendar calendarGreg = new GregorianCalendar();
	 * calendarGreg.set(Calendar.YEAR, new Integer(year));
	 * calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
	 * calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
	 * calendarGreg.set(Calendar.HOUR_OF_DAY, 0);
	 * calendarGreg.set(Calendar.MINUTE, 0); calendarGreg.set(Calendar.SECOND,
	 * 0); calendarGreg.set(Calendar.MILLISECOND, 0); Date startTime =
	 * calendarGreg.getTime();
	 * 
	 * dates = endDate.split(CalendarController.CHAR_SEP_DATE); year = dates[0];
	 * month = dates[1]; day = dates[2];
	 * 
	 * calendarGreg.set(Calendar.YEAR, new Integer(year));
	 * calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
	 * calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
	 * calendarGreg.add(Calendar.DAY_OF_MONTH, 1);
	 * 
	 * Date endTime = calendarGreg.getTime();
	 * 
	 * Filter calendarFilter = new FilterPredicate("eveCalendarId",
	 * FilterOperator.EQUAL, calendar.getId()); Filter enabledFilter = new
	 * FilterPredicate("enabled", FilterOperator.EQUAL, 1); Filter fromFilter =
	 * new FilterPredicate("eveStartTime", FilterOperator.GREATER_THAN_OR_EQUAL,
	 * startTime); Filter untilFilter = new FilterPredicate("eveStartTime",
	 * FilterOperator.LESS_THAN_OR_EQUAL, endTime);
	 * 
	 * Filter compositeFilter = CompositeFilterOperator.and( calendarFilter,
	 * enabledFilter, fromFilter, untilFilter);
	 * 
	 * if (consumed != null) { int intConsumed = consumed.booleanValue() ? 1 :
	 * 0; Filter consumedFilter = new FilterPredicate("repConsumed",
	 * FilterOperator.EQUAL, intConsumed); compositeFilter =
	 * CompositeFilterOperator.and(compositeFilter, consumedFilter);
	 * 
	 * }
	 * 
	 * com.google.appengine.api.datastore.Query query = new
	 * com.google.appengine.api.datastore.Query(
	 * "Repeat").setFilter(compositeFilter);
	 * 
	 * DatastoreService dataStore = DatastoreServiceFactory
	 * .getDatastoreService(); PreparedQuery pq = dataStore.prepare(query);
	 * resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000)); for
	 * (Entity entity : resultQuery) { ICS =
	 * (String)entity.getProperty("repICS"); if (!listICS.contains(ICS)){
	 * listICS.add(ICS); result ++; } } } catch (Exception ex) { } return
	 * result; }
	 * 
	 * 
	 * public Integer getRepeatNumberBooking(CalendarDTO calendar, String
	 * startDate, String endDate, Integer booking) {
	 * 
	 * Integer result = 0; List<Entity> resultQuery = null; String ICS = null;
	 * List<String> listICS = new ArrayList<String>();
	 * 
	 * try {
	 * 
	 * String[] dates = startDate.split(CalendarController.CHAR_SEP_DATE);
	 * String year = dates[0]; String month = dates[1]; String day = dates[2];
	 * 
	 * Calendar calendarGreg = new GregorianCalendar();
	 * calendarGreg.set(Calendar.YEAR, new Integer(year));
	 * calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
	 * calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
	 * calendarGreg.set(Calendar.HOUR_OF_DAY, 0);
	 * calendarGreg.set(Calendar.MINUTE, 0); calendarGreg.set(Calendar.SECOND,
	 * 0); calendarGreg.set(Calendar.MILLISECOND, 0); Date startTime =
	 * calendarGreg.getTime();
	 * 
	 * dates = endDate.split(CalendarController.CHAR_SEP_DATE); year = dates[0];
	 * month = dates[1]; day = dates[2];
	 * 
	 * calendarGreg.set(Calendar.YEAR, new Integer(year));
	 * calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
	 * calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
	 * calendarGreg.add(Calendar.DAY_OF_MONTH, 1);
	 * 
	 * Date endTime = calendarGreg.getTime();
	 * 
	 * Filter calendarFilter = new FilterPredicate("eveCalendarId",
	 * FilterOperator.EQUAL, calendar.getId()); Filter enabledFilter = new
	 * FilterPredicate("enabled", FilterOperator.EQUAL, 1); Filter fromFilter =
	 * new FilterPredicate("eveStartTime", FilterOperator.GREATER_THAN_OR_EQUAL,
	 * startTime); Filter untilFilter = new FilterPredicate("eveStartTime",
	 * FilterOperator.LESS_THAN_OR_EQUAL, endTime);
	 * 
	 * Filter compositeFilter = CompositeFilterOperator.and( calendarFilter,
	 * enabledFilter, fromFilter, untilFilter);
	 * 
	 * if (booking != null) { Filter consumedFilter = new
	 * FilterPredicate("repBooking", FilterOperator.EQUAL, booking);
	 * compositeFilter = CompositeFilterOperator.and(compositeFilter,
	 * consumedFilter); }
	 * 
	 * com.google.appengine.api.datastore.Query query = new
	 * com.google.appengine.api.datastore.Query(
	 * "Repeat").setFilter(compositeFilter);
	 * 
	 * DatastoreService dataStore = DatastoreServiceFactory
	 * .getDatastoreService(); PreparedQuery pq = dataStore.prepare(query);
	 * resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000)); for
	 * (Entity entity : resultQuery) { ICS =
	 * (String)entity.getProperty("repICS"); if (!listICS.contains(ICS)){
	 * listICS.add(ICS); result ++; } }
	 * 
	 * } catch (Exception ex) { } return result; }
	 * 
	 * public Integer getRepeatNumberTask(CalendarDTO calendar, String
	 * startDate, String endDate, Long localTaskId, Boolean consumed) {
	 * 
	 * Integer result = null;
	 * 
	 * try {
	 * 
	 * String[] dates = startDate.split(CalendarController.CHAR_SEP_DATE);
	 * String year = dates[0]; String month = dates[1]; String day = dates[2];
	 * 
	 * Calendar calendarGreg = new GregorianCalendar();
	 * calendarGreg.set(Calendar.YEAR, new Integer(year));
	 * calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
	 * calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
	 * calendarGreg.set(Calendar.HOUR_OF_DAY, 0);
	 * calendarGreg.set(Calendar.MINUTE, 0); calendarGreg.set(Calendar.SECOND,
	 * 0); calendarGreg.set(Calendar.MILLISECOND, 0); Date startTime =
	 * calendarGreg.getTime();
	 * 
	 * dates = endDate.split(CalendarController.CHAR_SEP_DATE); year = dates[0];
	 * month = dates[1]; day = dates[2];
	 * 
	 * calendarGreg.set(Calendar.YEAR, new Integer(year));
	 * calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
	 * calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
	 * calendarGreg.add(Calendar.DAY_OF_MONTH, 1);
	 * 
	 * Date endTime = calendarGreg.getTime();
	 * 
	 * Filter calendarFilter = new FilterPredicate("eveCalendarId",
	 * FilterOperator.EQUAL, calendar.getId()); Filter enabledFilter = new
	 * FilterPredicate("enabled", FilterOperator.EQUAL, 1); Filter fromFilter =
	 * new FilterPredicate("eveStartTime", FilterOperator.GREATER_THAN_OR_EQUAL,
	 * startTime); Filter untilFilter = new FilterPredicate("eveStartTime",
	 * FilterOperator.LESS_THAN_OR_EQUAL, endTime);
	 * 
	 * Filter compositeFilter = CompositeFilterOperator.and( calendarFilter,
	 * enabledFilter, fromFilter, untilFilter);
	 * 
	 * if (localTaskId != null) { Filter taskFilter = new
	 * FilterPredicate("repLocalTaskId", FilterOperator.EQUAL, localTaskId);
	 * compositeFilter = CompositeFilterOperator.and(compositeFilter,
	 * taskFilter); } if (consumed != null) { int intConsumed =
	 * consumed.booleanValue() ? 1 : 0; Filter consumedFilter = new
	 * FilterPredicate("repConsumed", FilterOperator.EQUAL, intConsumed);
	 * compositeFilter = CompositeFilterOperator.and(compositeFilter,
	 * consumedFilter); } com.google.appengine.api.datastore.Query query = new
	 * com.google.appengine.api.datastore.Query(
	 * "Repeat").setFilter(compositeFilter);
	 * 
	 * DatastoreService dataStore = DatastoreServiceFactory
	 * .getDatastoreService(); PreparedQuery pq = dataStore.prepare(query);
	 * result = pq.countEntities(FetchOptions.Builder.withDefaults());
	 * 
	 * } catch (Exception ex) { } return result; }
	 */
	
}