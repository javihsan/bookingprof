package com.diloso.bookhair.app.negocio.manager;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.EventDTO;
import com.diloso.bookhair.app.persist.dao.EventDAO;
import com.diloso.bookhair.app.persist.mapper.EventMapper;

@Component
@Scope(value = "singleton")
public class EventManager implements IEventManager {

	protected static final Logger log = Logger.getLogger(EventManager.class.getName());

	@Autowired
	private EventDAO eventDAO;
	
	@Autowired
	protected EventMapper eventMapper;
	
	public EventManager() {

	}

	@Override
	public EventDTO create(EventDTO event) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventDTO remove(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventDTO update(EventDTO event) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventDTO getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EventDTO> getEventAdmin(CalendarDTO calendar) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EventDTO> getEventByDay(CalendarDTO calendar, String selectedDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EventDTO> getEventByWeek(CalendarDTO calendar, String selectedDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EventDTO> getEventByClientAgo(CalendarDTO calendar, Long clientId, Date selectedDate, int numDays) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EventDTO> getEventByICS(String ICS) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getEventNumber(CalendarDTO calendar, String startDate, String endDate, Boolean consumed) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getEventNumberBooking(CalendarDTO calendar, String startDate, String endDate, Integer booking) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getEventNumberTask(CalendarDTO calendar, String startDate, String endDate, Long localTaskId,
			Boolean consumed) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
/*
	public EventDTO create(EventDTO event) throws Exception {
		EntityManager em = getEntityManager();
		Event entityEvent =eventMapper
				.map(event);
		try {
			em.getTransaction().begin();
			em.persist(entityEvent);
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
		return eventMapper.map(entityEvent);
	}

	public EventDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		Event oldEntityEvent = new Event();
		try {
			em.getTransaction().begin();
			Event entityEvent = (Event) em.find(Event.class, id);
			PropertyUtils.copyProperties(oldEntityEvent, entityEvent);
			em.remove(em.merge(entityEvent));
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
			log.warning("Eliminado evento: " + oldEntityEvent.getEveCalendarId()
					+ " " + oldEntityEvent.getEveBookingTime() + " "
					+ oldEntityEvent.getEveClientId());
		}
		return eventMapper.map(
				oldEntityEvent);
	}

	public EventDTO update(EventDTO event) throws Exception {
		EntityManager em = getEntityManager();
		Event entityEvent =eventMapper
				.map(event);
		Event oldEntityEvent = null;
		try {
			em.getTransaction().begin();
			oldEntityEvent = (Event) em.find(Event.class, entityEvent.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityEvent,
					oldEntityEvent);
			entityEvent = em.merge(entityEvent);
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
		return eventMapper.map(entityEvent);
	}

	public EventDTO getById(long id) {
		Event entityEvent = null;
		EntityManager em = getEntityManager();
		try {
			entityEvent = (Event) em.find(Event.class, id);
		} finally {
			em.close();
		}
		return eventMapper.map(entityEvent);
	}
	
	public List<EventDTO> getEventAdmin(CalendarDTO calendar) {

		List<Entity> resultQuery = null;
		List<EventDTO> result = new ArrayList<EventDTO>();
		EventDTO event = null;
		try {

			Filter calendarFilter = new FilterPredicate("eveCalendarId",
					FilterOperator.EQUAL, calendar.getId());

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter);

			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					"Event").setFilter(compositeFilter);

			query.addSort("eveStartTime", SortDirection.ASCENDING);

			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity : resultQuery) {
				event = eventMapper.map(
						entity);
				result.add(event);
			}

		} catch (Exception ex) {
		}

		return result;

	}
	
	public List<EventDTO> getEventByDay(CalendarDTO calendar,
			String selectedDate) {

		List<Entity> resultQuery = null;
		List<EventDTO> result = new ArrayList<EventDTO>();
		EventDTO event = null;
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
			Filter fromFilter = new FilterPredicate("eveStartTime",
					FilterOperator.GREATER_THAN_OR_EQUAL, startTime);
			Filter untilFilter = new FilterPredicate("eveStartTime",
					FilterOperator.LESS_THAN_OR_EQUAL, endTime);

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter, enabledFilter, fromFilter, untilFilter);

			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					"Event").setFilter(compositeFilter);

			query.addSort("eveStartTime", SortDirection.ASCENDING);

			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity : resultQuery) {
				event = eventMapper.map(
						entity);
				if (event.getEveEndTime()==null){
					setEveEndTime(event);
				}	
				result.add(event);
			}

		} catch (Exception ex) {
		}

		return result;

	}
	
	public void setEveEndTime(EventDTO event){
		LocalTaskDTO localTask = event.getEveLocalTask();
		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.setTime(event.getEveStartTime());
		calendarGreg.add(Calendar.MINUTE,
				localTask.getLotTaskDuration());
		event.setEveEndTime(calendarGreg.getTime());
	}
	
	public List<EventDTO> getEventByWeek(CalendarDTO calendar,
			String selectedDate) {

		List<Entity> resultQuery = null;
		EventDTO event = null;
		List<EventDTO> result = new ArrayList<EventDTO>();
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
			Filter fromFilter = new FilterPredicate("eveStartTime",
					FilterOperator.GREATER_THAN_OR_EQUAL, startTime);
			Filter untilFilter = new FilterPredicate("eveStartTime",
					FilterOperator.LESS_THAN_OR_EQUAL, endTime);

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter, enabledFilter, fromFilter, untilFilter);

			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					"Event").setFilter(compositeFilter);

			query.addSort("eveStartTime", SortDirection.ASCENDING);
			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity : resultQuery) {
				event = eventMapper.map(entity);
				result.add(event);
			}

		} catch (Exception ex) {
		}

		return result;

	}

	public List<EventDTO> getEventByClientAgo(CalendarDTO calendar,
			Long clientId, Date selectedDate, int numDays) {

		List<Entity> resultQuery = null;
		EventDTO event = null;
		List<EventDTO> result = new ArrayList<EventDTO>();
		try {

			Filter clientFilter = new FilterPredicate("eveClientId",
					FilterOperator.EQUAL, clientId);
			Filter calendarFilter = new FilterPredicate("eveCalendarId",
					FilterOperator.EQUAL, calendar.getId());
			Filter enabledFilter = new FilterPredicate("enabled",
					FilterOperator.EQUAL, 1);

			Filter compositeFilter = CompositeFilterOperator.and(clientFilter,
					calendarFilter, enabledFilter);

			if (selectedDate != null) {
				Calendar calendarGreg = new GregorianCalendar();
				calendarGreg.setTime(selectedDate);
				calendarGreg.set(Calendar.MILLISECOND, 0);
				calendarGreg.add(Calendar.DAY_OF_MONTH, -numDays);
				Date startTime = calendarGreg.getTime();

				Filter fromFilter = new FilterPredicate("eveBookingTime",
						FilterOperator.GREATER_THAN_OR_EQUAL, startTime);

				compositeFilter = CompositeFilterOperator.and(compositeFilter,
						fromFilter);
			}

			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					"Event").setFilter(compositeFilter);

			query.addSort("eveBookingTime", SortDirection.ASCENDING);
			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity : resultQuery) {
				event =eventMapper.map(
						entity);
				result.add(event);
			}

		} catch (Exception ex) {
		}

		return result;

	}
	
	public List<EventDTO> getEventByICS(String ICS){
	
		List<Entity> resultQuery = null;
		EventDTO event = null;
		List<EventDTO> result = new ArrayList<EventDTO>();
		try {
		
			Filter ICSFilter = new FilterPredicate("eveICS",
					FilterOperator.EQUAL, ICS);
			Filter enabledFilter = new FilterPredicate("enabled",
					FilterOperator.EQUAL, 1);

			Filter compositeFilter = CompositeFilterOperator.and(
					ICSFilter, enabledFilter);

			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					"Event").setFilter(compositeFilter);

			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(100));
			for (Entity entity : resultQuery) {
				event = eventMapper.map(entity);
				result.add(event);
			}

		} catch (Exception ex) {
		}

		return result;

	}
	
	public Integer getEventNumber(CalendarDTO calendar, String startDate,
			String endDate, Boolean consumed) {

		Integer result = 0;
		List<Entity> resultQuery = null;
		String ICS = null;
		List<String> listICS = new ArrayList<String>();
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

			Filter calendarFilter = new FilterPredicate("eveCalendarId",
					FilterOperator.EQUAL, calendar.getId());
			Filter enabledFilter = new FilterPredicate("enabled",
					FilterOperator.EQUAL, 1);
			Filter fromFilter = new FilterPredicate("eveStartTime",
					FilterOperator.GREATER_THAN_OR_EQUAL, startTime);
			Filter untilFilter = new FilterPredicate("eveStartTime",
					FilterOperator.LESS_THAN_OR_EQUAL, endTime);

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter, enabledFilter, fromFilter, untilFilter);

			if (consumed != null) {
				int intConsumed = consumed.booleanValue() ? 1 : 0;
				Filter consumedFilter = new FilterPredicate("eveConsumed",
						FilterOperator.EQUAL, intConsumed);
				compositeFilter = CompositeFilterOperator.and(compositeFilter,
						consumedFilter);

			}

			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					"Event").setFilter(compositeFilter);
		
			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity : resultQuery) {
				ICS = (String)entity.getProperty("eveICS");
				if (!listICS.contains(ICS)){ 
					listICS.add(ICS);
					result ++;
				}	
			}
		} catch (Exception ex) {
		}
		return result;
	}

	
	public Integer getEventNumberBooking(CalendarDTO calendar, String startDate,
			String endDate, Integer booking) {

		Integer result = 0;
		List<Entity> resultQuery = null;
		String ICS = null;
		List<String> listICS = new ArrayList<String>();

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

			Filter calendarFilter = new FilterPredicate("eveCalendarId",
					FilterOperator.EQUAL, calendar.getId());
			Filter enabledFilter = new FilterPredicate("enabled",
					FilterOperator.EQUAL, 1);
			Filter fromFilter = new FilterPredicate("eveStartTime",
					FilterOperator.GREATER_THAN_OR_EQUAL, startTime);
			Filter untilFilter = new FilterPredicate("eveStartTime",
					FilterOperator.LESS_THAN_OR_EQUAL, endTime);

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter, enabledFilter, fromFilter, untilFilter);

			if (booking != null) {
				Filter consumedFilter = new FilterPredicate("eveBooking",
						FilterOperator.EQUAL, booking);
				compositeFilter = CompositeFilterOperator.and(compositeFilter,
						consumedFilter);
			}
						
			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					"Event").setFilter(compositeFilter);

			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(10000));
			for (Entity entity : resultQuery) {
				ICS = (String)entity.getProperty("eveICS");
				if (!listICS.contains(ICS)){ 
					listICS.add(ICS);
					result ++;
				}	
			}

		} catch (Exception ex) {
		}
		return result;
	}
	
	public Integer getEventNumberTask(CalendarDTO calendar, String startDate,
			String endDate, Long localTaskId, Boolean consumed) {

		Integer result = null;

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

			Filter calendarFilter = new FilterPredicate("eveCalendarId",
					FilterOperator.EQUAL, calendar.getId());
			Filter enabledFilter = new FilterPredicate("enabled",
					FilterOperator.EQUAL, 1);
			Filter fromFilter = new FilterPredicate("eveStartTime",
					FilterOperator.GREATER_THAN_OR_EQUAL, startTime);
			Filter untilFilter = new FilterPredicate("eveStartTime",
					FilterOperator.LESS_THAN_OR_EQUAL, endTime);

			Filter compositeFilter = CompositeFilterOperator.and(
					calendarFilter, enabledFilter, fromFilter,
					untilFilter);

			if (localTaskId != null) {
				Filter taskFilter = new FilterPredicate("eveLocalTaskId",
						FilterOperator.EQUAL, localTaskId);
				compositeFilter = CompositeFilterOperator.and(compositeFilter,
						taskFilter);
			}
			if (consumed != null) {
//				if (consumed){
//					Filter consumedFilter = new FilterPredicate("eveConsumed",
//							FilterOperator.GREATER_THAN, 0);
//				} else {
//					Filter consumedFilter = new FilterPredicate("eveConsumed",
//							FilterOperator.EQUAL, 0);
//				}
				int intConsumed = consumed.booleanValue() ? 1 : 0;
				Filter consumedFilter = new FilterPredicate("eveConsumed",
						FilterOperator.EQUAL, intConsumed);
				compositeFilter = CompositeFilterOperator.and(compositeFilter,
						consumedFilter);
			}
			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					"Event").setFilter(compositeFilter);

			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			result = pq.countEntities(FetchOptions.Builder.withDefaults());

		} catch (Exception ex) {
		}
		return result;
	}

	public void setEventTransformer(EventMapper eventMapper) {
		this.eventMapper = eventMapper;
	}	

	*/
}