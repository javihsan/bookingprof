package com.diloso.bookhair.app.persist.manager;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.CalendarDAO;
import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.entities.Calendar;
import com.diloso.bookhair.app.persist.transformer.CalendarTransformer;

@Component
@Scope(value = "singleton")
public class CalendarManager extends Manager implements CalendarDAO {

	
	@Autowired
	protected CalendarTransformer calendarTransformer;
	
	public CalendarManager() {

	}

	public CalendarDTO create(CalendarDTO calendar) throws Exception {
		EntityManager em = getEntityManager();
		Calendar entityCalendar = calendarTransformer
				.transformDTOToEntity(calendar);
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
		return calendarTransformer.transformEntityToDTO(
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
		return calendarTransformer.transformEntityToDTO(
				oldEntityCalendar);
	}

	public CalendarDTO update(CalendarDTO calendar) throws Exception {
		EntityManager em = getEntityManager();
		Calendar entityCalendar = calendarTransformer
				.transformDTOToEntity(calendar);
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
		return calendarTransformer.transformEntityToDTO(
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
		return calendarTransformer.transformEntityToDTO(
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
				calendar = calendarTransformer
						.transformEntityToDTO(entityCalendar);
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
				calendar = calendarTransformer
						.transformEntityToDTO(entityCalendar);
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
}