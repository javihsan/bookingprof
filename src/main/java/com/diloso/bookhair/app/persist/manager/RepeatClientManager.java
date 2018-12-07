package com.diloso.bookhair.app.persist.manager;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.RepeatClientDAO;
import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.RepeatClientDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.entities.RepeatClient;
import com.diloso.bookhair.app.persist.transformer.RepeatClientTransformer;

@Component
@Scope(value = "singleton")
public class RepeatClientManager extends Manager implements RepeatClientDAO {

	
	@Autowired
	protected RepeatClientTransformer repeatClientTransformer;
	
	public RepeatClientManager() {

	}

	public RepeatClientDTO create(RepeatClientDTO repeatClient) throws Exception {
		EntityManager em = getEntityManager();
		RepeatClient entityRepeatClient = repeatClientTransformer
				.transformDTOToEntity(repeatClient);
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
		return repeatClientTransformer.transformEntityToDTO(
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
		return repeatClientTransformer.transformEntityToDTO(
				oldEntityRepeatClient);
	}

	public RepeatClientDTO update(RepeatClientDTO repeatClient) throws Exception {
		EntityManager em = getEntityManager();
		RepeatClient entityRepeatClient = repeatClientTransformer
				.transformDTOToEntity(repeatClient);
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
		return repeatClientTransformer.transformEntityToDTO(
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
		return repeatClientTransformer.transformEntityToDTO(
				entityRepeatClient);
	}
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
						.transformEntityToDTO(entityRepeatClient);
				result.add(repeatClient);
			}
		} finally {
			em.close();
		}
		return result;
	}
*/

	@Override
	public List<RepeatClientDTO> getRepeatClientByClientAgo(
			CalendarDTO calendar, Long clientId, Date selectedDate, int numDays) {
		// TODO Auto-generated method stub
		return null;
	}
	

}