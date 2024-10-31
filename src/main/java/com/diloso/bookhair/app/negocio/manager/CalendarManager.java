package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.persist.dao.CalendarDAO;
import com.diloso.bookhair.app.persist.mapper.CalendarMapper;

@Component
@Scope(value = "singleton")
public class CalendarManager implements ICalendarManager {

	@Autowired
	private CalendarDAO calendarDAO;
	
	@Autowired
	protected CalendarMapper calendarMapper;
	
	public CalendarManager() {

	}

	@Override
	public CalendarDTO create(CalendarDTO calendar) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CalendarDTO remove(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CalendarDTO update(CalendarDTO calendar) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CalendarDTO getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CalendarDTO> getCalendar(long calLocalId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CalendarDTO> getCalendarAdmin(long calLocalId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getNumCalendarAdmin(long calLocalId) {
		// TODO Auto-generated method stub
		return null;
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