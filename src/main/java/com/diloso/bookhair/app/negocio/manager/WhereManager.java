package com.diloso.bookhair.app.negocio.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.WhereDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.WhereDAO;
import com.diloso.bookhair.app.persist.entities.Where;
import com.diloso.bookhair.app.persist.mapper.WhereMapper;

@Component
@Scope(value = "singleton")
public class WhereManager implements IWhereManager {

	@Autowired
	private WhereDAO whereDAO;
	
	@Autowired
	protected WhereMapper mapper;
	
	public WhereManager() {
		if (mapper==null){
			mapper = new WhereMapper();
		}
	}

	@Override
	public WhereDTO create(WhereDTO whereDTO) throws Exception {
		Where where = mapper.map(whereDTO);
		where = whereDAO.create(where);
		return mapper.map(where);
	}

	@Override
	public WhereDTO remove(long id) throws Exception {
		Where where = whereDAO.get(id);
		whereDAO.delete(id);
		if (where == null) {
			return null;
		}
		return mapper.map(where);
	}

	@Override
	public WhereDTO update(WhereDTO whereDTO) throws Exception {
		Where where = mapper.map(whereDTO);
		Where oldWhere = whereDAO.get(whereDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(where, oldWhere);
		} catch (Exception e) {
		}
		where = whereDAO.update(where);
		if (where == null) {
			return null;
		}
		return mapper.map(where);
	}

	@Override
	public WhereDTO getById(long id) {
		Where where = whereDAO.get(id);
		if (where == null) {
			return null;
		}
		return mapper.map(where);		
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