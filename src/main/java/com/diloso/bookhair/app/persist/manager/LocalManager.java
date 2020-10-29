package com.diloso.bookhair.app.persist.manager;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.LocalDAO;
import com.diloso.bookhair.app.negocio.dto.LocalDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.entities.Local;
import com.diloso.bookhair.app.persist.transformer.LocalTransformer;

@Component
@Scope(value = "singleton")
public class LocalManager extends Manager implements LocalDAO {

	//@Autowired
	protected LocalTransformer localTransformer;
	
	public LocalManager() {

	}

	public LocalDTO create(LocalDTO local) throws Exception {
		EntityManager em = getEntityManager();
		Local entityLocal = localTransformer
				.transformDTOToEntity(local);
		try {
			em.getTransaction().begin();
			em.persist(entityLocal);
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
		return localTransformer.transformEntityToDTO(entityLocal);
	}

	public LocalDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		Local oldEntityLocal = new Local();
		try {
			em.getTransaction().begin();
			Local entityLocal = (Local) em.find(Local.class, id);
			PropertyUtils.copyProperties(oldEntityLocal, entityLocal);
			em.remove(em.merge(entityLocal));
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
		return localTransformer.transformEntityToDTO(
				oldEntityLocal);
	}

	public LocalDTO update(LocalDTO local) throws Exception {
		EntityManager em = getEntityManager();
		Local entityLocal = localTransformer
				.transformDTOToEntity(local);
		Local oldEntityLocal = null;
		try {
			em.getTransaction().begin();
			oldEntityLocal = (Local) em.find(Local.class, entityLocal.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityLocal,
					oldEntityLocal);
			entityLocal = em.merge(entityLocal);
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
		return localTransformer.transformEntityToDTO(entityLocal);
	}

	public LocalDTO getById(long id) {
		Local entityLocal = null;
		EntityManager em = getEntityManager();
		try {
			entityLocal = (Local) em.find(Local.class, id);
		} finally {
			em.close();
		}
		return localTransformer.transformEntityToDTO(entityLocal);
	}


	public List<Long> getLocal(long resFirId) {
		EntityManager em = getEntityManager();
		List<Long> result = new ArrayList<Long>();
		List<Local> resultQuery = null;
		try {
			Query query = em.createNamedQuery("getLocal");
			query.setParameter("resFirId", resFirId);
			resultQuery = (List<Local>) query.getResultList();
			for (Local entityLocal : resultQuery) {
				result.add(entityLocal.getId());
			}
		} finally {
			em.close();
		}
		return result;
	}

	public List<Long> getLocalClient(long resFirId) {
		EntityManager em = getEntityManager();
		List<Long> result = new ArrayList<Long>();
		List<Local> resultQuery = null;
		try {
			Query query = em.createNamedQuery("getLocalClient");
			query.setParameter("resFirId", resFirId);
			resultQuery = (List<Local>) query.getResultList();
			for (Local entityLocal : resultQuery) {
				result.add(entityLocal.getId());
			}
		} finally {
			em.close();
		}
		return result;
	}
	
	
	public List<LocalDTO> getLocalList(long resFirId) {
		EntityManager em = getEntityManager();
		List<LocalDTO> result = new ArrayList<LocalDTO>();
		List<Local> resultQuery = null;
		LocalDTO local = null;
		try {
			Query query = em.createNamedQuery("getLocal");
			query.setParameter("resFirId", resFirId);
			resultQuery = (List<Local>) query.getResultList();
			for (Local entityLocal : resultQuery) {
				local = localTransformer.transformEntityToDTO(entityLocal);
				result.add(local);
			}
		} finally {
			em.close();
		}
		return result;
	}

	public List<LocalDTO> getLocalListClient(long resFirId) {
		EntityManager em = getEntityManager();
		List<LocalDTO> result = new ArrayList<LocalDTO>();
		List<Local> resultQuery = null;
		LocalDTO local = null;
		try {
			Query query = em.createNamedQuery("getLocalClient");
			query.setParameter("resFirId", resFirId);
			resultQuery = (List<Local>) query.getResultList();
			for (Local entityLocal : resultQuery) {
				local = localTransformer.transformEntityToDTO(entityLocal);
				result.add(local);
			}
		} finally {
			em.close();
		}
		return result;
	}
	
	public List<LocalDTO> getLocalAdmin(long resFirId) {
		EntityManager em = getEntityManager();
		List<LocalDTO> result = new ArrayList<LocalDTO>();
		List<Local> resultQuery = null;
		LocalDTO local = null;
		try {
			Query query = em.createNamedQuery("getLocalAdmin");
			query.setParameter("resFirId", resFirId);
			resultQuery = (List<Local>) query.getResultList();
			for (Local entityLocal : resultQuery) {
				local = localTransformer.transformEntityToDTO(entityLocal);
				result.add(local);
			}
		} finally {
			em.close();
		}
		return result;
	}

	public void setLocalTransformer(LocalTransformer localTransformer) {
		this.localTransformer = localTransformer;
	}
	
	
	
}