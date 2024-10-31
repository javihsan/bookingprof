package com.diloso.bookhair.app.negocio.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.WhereDTO;
import com.diloso.bookhair.app.persist.dao.WhereDAO;
import com.diloso.bookhair.app.persist.mapper.WhereMapper;

@Component
@Scope(value = "singleton")
public class WhereManager implements IWhereManager {

	@Autowired
	private WhereDAO whereDAO;
	
	@Autowired
	protected WhereMapper whereMapper;
	
	public WhereManager() {
		if (whereMapper==null){
			whereMapper = new WhereMapper();
		}
	}

	@Override
	public WhereDTO create(WhereDTO where) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WhereDTO remove(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WhereDTO update(WhereDTO where) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WhereDTO getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	public WhereDTO create(WhereDTO where) throws Exception {
		EntityManager em = getEntityManager();
		Where entityWhere = whereMapper
				.map(where);
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
		return whereMapper.map(entityWhere);
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
		return whereMapper.map(
				oldEntityWhere);
	}

	public WhereDTO update(WhereDTO where) throws Exception {
		EntityManager em = getEntityManager();
		Where entityWhere = whereMapper
				.map(where);
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
		return whereMapper.map(entityWhere);
	}

	public WhereDTO getById(long id) {
		Where entityWhere = null;
		EntityManager em = getEntityManager();
		try {
			entityWhere = (Where) em.find(Where.class, id);
		} finally {
			em.close();
		}
		return whereMapper.map(entityWhere);
	}

	public void setWhereTransformer(WhereMapper whereMapper) {
		this.whereMapper = whereMapper;
	}
	*/
	
}