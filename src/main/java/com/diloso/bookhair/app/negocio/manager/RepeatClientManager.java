package com.diloso.bookhair.app.negocio.manager;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.RepeatClientDTO;
import com.diloso.bookhair.app.persist.dao.RepeatClientDAO;
import com.diloso.bookhair.app.persist.mapper.RepeatClientMapper;

@Component
@Scope(value = "singleton")
public class RepeatClientManager implements IRepeatClientManager {

	@Autowired
	private RepeatClientDAO repeatClientDAO;
	
	@Autowired
	protected RepeatClientMapper repeatClientMapper;
	
	public RepeatClientManager() {

	}

	@Override
	public RepeatClientDTO create(RepeatClientDTO event) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RepeatClientDTO remove(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RepeatClientDTO update(RepeatClientDTO event) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RepeatClientDTO getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RepeatClientDTO> getRepeatClientByClientAgo(CalendarDTO calendar, Long clientId, Date selectedDate,
			int numDays) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/*
	public RepeatClientDTO create(RepeatClientDTO repeatClient) throws Exception {
		EntityManager em = getEntityManager();
		RepeatClient entityRepeatClient = repeatClientMapper
				.map(repeatClient);
		try {
			em.getTransaction().begin();
			em.persist(entityRepeatClient);
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
		return repeatClientMapper.map(
				entityRepeatClient);
	}

	public RepeatClientDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		RepeatClient oldEntityRepeatClient = new RepeatClient();
		try {
			em.getTransaction().begin();
			RepeatClient entityRepeatClient = (RepeatClient) em.find(RepeatClient.class, id);
			PropertyUtils.copyProperties(oldEntityRepeatClient, entityRepeatClient);
			em.remove(em.merge(entityRepeatClient));
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
		return repeatClientMapper.map(
				oldEntityRepeatClient);
	}

	public RepeatClientDTO update(RepeatClientDTO repeatClient) throws Exception {
		EntityManager em = getEntityManager();
		RepeatClient entityRepeatClient = repeatClientMapper
				.map(repeatClient);
		RepeatClient oldEntityRepeatClient = null;
		try {
			em.getTransaction().begin();
			oldEntityRepeatClient = (RepeatClient) em.find(RepeatClient.class,
					entityRepeatClient.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityRepeatClient,
					oldEntityRepeatClient);
			entityRepeatClient = em.merge(entityRepeatClient);
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
		return repeatClientMapper.map(
				entityRepeatClient);
	}

	public RepeatClientDTO getById(long id) {
		RepeatClient entityRepeatClient = null;
		EntityManager em = getEntityManager();
		try {
			entityRepeatClient = (RepeatClient) em.find(RepeatClient.class, id);
		} finally {
			em.close();
		}
		return repeatClientMapper.map(
				entityRepeatClient);
	}


	@Override
	public List<RepeatClientDTO> getRepeatClientByClientAgo(
			CalendarDTO calendar, Long clientId, Date selectedDate, int numDays) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setRepeatClientTransformer(RepeatClientMapper repeatClientMapper) {
		this.repeatClientMapper = repeatClientMapper;
	}
	*/
	
	/*
	public List<RepeatClientDTO> getRepeatClient(long calLocalId) {
		EntityManager em = getEntityManager();
		List<RepeatClientDTO> result = new ArrayList<RepeatClientDTO>();
		List<RepeatClient> resultQuery = null;
		RepeatClientDTO repeatClient = null;
		try {
			Query query = em.createNamedQuery("getRepeatClient");
			query.setParameter("calLocalId", calLocalId);
			resultQuery = (List<RepeatClient>) query.getResultList();
			for (RepeatClient entityRepeatClient : resultQuery) {
				repeatClient = repeatClientTransformer
						.map(entityRepeatClient);
				result.add(repeatClient);
			}
		} finally {
			em.close();
		}
		return result;
	}
*/
}