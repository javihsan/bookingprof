package com.diloso.bookhair.app.negocio.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.SincroDTO;
import com.diloso.bookhair.app.persist.dao.SincroDAO;
import com.diloso.bookhair.app.persist.mapper.SincroMapper;

@Component
@Scope(value = "singleton")
public class SincroManager implements ISincroManager {

	@Autowired
	private SincroDAO sincroDAO;
	
	@Autowired
	protected SincroMapper sincroMapper;
	
	public SincroManager() {

	}

	@Override
	public SincroDTO create(SincroDTO sincro) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SincroDTO remove(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SincroDTO update(SincroDTO sincro) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SincroDTO getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/*
	public SincroDTO create(SincroDTO sincro) throws Exception {
		EntityManager em = getEntityManager();
		Sincro entitySincro = sincroMapper
				.map(sincro);
		try {
			em.getTransaction().begin();
			em.persist(entitySincro);
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
		return sincroMapper.map(entitySincro);
	}

	public SincroDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		Sincro oldEntitySincro = new Sincro();
		try {
			em.getTransaction().begin();
			Sincro entitySincro = (Sincro) em.find(Sincro.class, id);
			PropertyUtils.copyProperties(oldEntitySincro, entitySincro);
			em.remove(em.merge(entitySincro));
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
		return sincroMapper.map(
				oldEntitySincro);
	}

	public SincroDTO update(SincroDTO sincro) throws Exception {
		EntityManager em = getEntityManager();
		Sincro entitySincro = sincroMapper
				.map(sincro);
		Sincro oldEntitySincro = null;
		try {
			em.getTransaction().begin();
			oldEntitySincro = (Sincro) em.find(Sincro.class, entitySincro.getId());
			new NullAwareBeanUtilsBean().copyProperties(entitySincro,
					oldEntitySincro);
			entitySincro = em.merge(entitySincro);
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
		return sincroMapper.map(entitySincro);
	}

	public SincroDTO getById(long id) {
		Entity entitySincro = null;
		try {
			Key k = KeyFactory.createKey(Sincro.class.getSimpleName(), id);
			entitySincro = DatastoreServiceFactory.getDatastoreService().get(k);
		} catch (Exception ex) {
		}
		return sincroMapper.map(entitySincro);
	}

	public void setSincroTransformer(SincroMapper sincroMapper) {
		this.sincroMapper = sincroMapper;
	}	
	*/

}