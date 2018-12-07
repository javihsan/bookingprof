package com.diloso.bookhair.app.persist.manager;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.WhereDAO;
import com.diloso.bookhair.app.negocio.dto.WhereDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.entities.Where;
import com.diloso.bookhair.app.persist.transformer.WhereTransformer;

@Component
@Scope(value = "singleton")
public class WhereManager extends Manager implements WhereDAO {

	@Autowired
	protected WhereTransformer whereTransformer;
	
	public WhereManager() {
		if (whereTransformer==null){
			whereTransformer = new WhereTransformer();
		}
	}

	public WhereDTO create(WhereDTO where) throws Exception {
		EntityManager em = getEntityManager();
		Where entityWhere = whereTransformer
				.transformDTOToEntity(where);
		try {
			em.getTransaction().begin();
			em.persist(entityWhere);
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
		return whereTransformer.transformEntityToDTO(entityWhere);
	}

	public WhereDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		Where oldEntityWhere = new Where();
		try {
			em.getTransaction().begin();
			Where entityWhere = (Where) em.find(Where.class, id);
			PropertyUtils.copyProperties(oldEntityWhere, entityWhere);
			em.remove(em.merge(entityWhere));
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
		return whereTransformer.transformEntityToDTO(
				oldEntityWhere);
	}

	public WhereDTO update(WhereDTO where) throws Exception {
		EntityManager em = getEntityManager();
		Where entityWhere = whereTransformer
				.transformDTOToEntity(where);
		Where oldEntityWhere = null;
		try {
			em.getTransaction().begin();
			oldEntityWhere = (Where) em.find(Where.class, entityWhere.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityWhere,
					oldEntityWhere);
			entityWhere = em.merge(entityWhere);
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
		return whereTransformer.transformEntityToDTO(entityWhere);
	}

	public WhereDTO getById(long id) {
		Where entityWhere = null;
		EntityManager em = getEntityManager();
		try {
			entityWhere = (Where) em.find(Where.class, id);
		} finally {
			em.close();
		}
		return whereTransformer.transformEntityToDTO(entityWhere);
	}

	public WhereDTO getByEmail(long resFirId, String email) {
		EntityManager em = getEntityManager();
		List<Where> resultQuery = null;
		WhereDTO where = null;
		try {
			Query query = em.createNamedQuery("getWhereEmail");
			query.setParameter("resFirId", resFirId);
			query.setParameter("whoEmail", email);
			resultQuery = (List<Where>) query.getResultList();
			if (resultQuery.size() == 1) {
				where = whereTransformer.transformEntityToDTO(
						resultQuery.get(0));
			}
		} finally {
			em.close();
		}
		return where;
	}

	public List<WhereDTO> getWhere(long resFirId) {
		EntityManager em = getEntityManager();
		List<WhereDTO> result = new ArrayList<WhereDTO>();
		List<Where> resultQuery = null;
		WhereDTO where = null;
		try {

			javax.persistence.Query query = em.createNamedQuery("getWhere");
			query.setParameter("resFirId", resFirId);
			resultQuery = (List<Where>) query.getResultList();
			for (Where entityWhere : resultQuery) {
				where = whereTransformer.transformEntityToDTO(
						entityWhere);
				result.add(where);
			}

		} finally {
			em.close();
		}
		return result;
	}

}