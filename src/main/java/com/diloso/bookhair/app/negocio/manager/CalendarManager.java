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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getNumCalendarAdmin(long calLocalId) {

		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(CAL_LOCAL_ID, calLocalId);
		return calendarDAO.listFilter(filters).size();
	}
/*
	public CalendarDTO create(CalendarDTO calendar) throws Exception {
		EntityManager em = getEntityManager();
		Calendar entityCalendar = calendarMapper
				.map(calendar);
		try {
			em.getTransaction().begin();
			em.persist(entityCalendar);
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
		return calendarMapper.map(
				entityCalendar);
	}

	public CalendarDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		Calendar oldEntityCalendar = new Calendar();
		try {
			em.getTransaction().begin();
			Calendar entityCalendar = (Calendar) em.find(Calendar.class, id);
			PropertyUtils.copyProperties(oldEntityCalendar, entityCalendar);
			em.remove(em.merge(entityCalendar));
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
		return calendarMapper.map(
				oldEntityCalendar);
	}

	public CalendarDTO update(CalendarDTO calendar) throws Exception {
		EntityManager em = getEntityManager();
		Calendar entityCalendar = calendarMapper
				.map(calendar);
		Calendar oldEntityCalendar = null;
		try {
			em.getTransaction().begin();
			oldEntityCalendar = (Calendar) em.find(Calendar.class,
					entityCalendar.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityCalendar,
					oldEntityCalendar);
			entityCalendar = em.merge(entityCalendar);
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
		return calendarMapper.map(
				entityCalendar);
	}

	public CalendarDTO getById(long id) {
		Calendar entityCalendar = null;
		EntityManager em = getEntityManager();
		try {
			entityCalendar = (Calendar) em.find(Calendar.class, id);
		} finally {
			em.close();
		}
		return calendarMapper.map(
				entityCalendar);
	}

	public List<CalendarDTO> getCalendar(long calLocalId) {
		EntityManager em = getEntityManager();
		List<CalendarDTO> result = new ArrayList<CalendarDTO>();
		List<Calendar> resultQuery = null;
		CalendarDTO calendar = null;
		try {
			Query query = em.createNamedQuery("getCalendar");
			query.setParameter("calLocalId", calLocalId);
			resultQuery = (List<Calendar>) query.getResultList();
			for (Calendar entityCalendar : resultQuery) {
				calendar = calendarMapper
						.map(entityCalendar);
				result.add(calendar);
			}
		} finally {
			em.close();
		}
		return result;
	}

	public List<CalendarDTO> getCalendarAdmin(long calLocalId) {
		EntityManager em = getEntityManager();
		List<CalendarDTO> result = new ArrayList<CalendarDTO>();
		List<Calendar> resultQuery = null;
		CalendarDTO calendar = null;
		try {
			Query query = em.createNamedQuery("getCalendarAdmin");
			query.setParameter("calLocalId", calLocalId);
			resultQuery = (List<Calendar>) query.getResultList();
			for (Calendar entityCalendar : resultQuery) {
				calendar = calendarMapper
						.map(entityCalendar);
				result.add(calendar);
			}
		} finally {
			em.close();
		}
		return result;
	}

	public Integer getNumCalendarAdmin(long calLocalId) {
		EntityManager em = getEntityManager();
		List<Calendar> resultQuery = null;
		try {
			Query query = em.createNamedQuery("getCalendarAdmin");
			query.setParameter("calLocalId", calLocalId);
			resultQuery = (List<Calendar>) query.getResultList();
		} finally {
			em.close();
		}
		return resultQuery.size();
	}

	public void setCalendarTransformer(CalendarMapper calendarMapper) {
		this.calendarMapper = calendarMapper;
	}
	*/
	
}