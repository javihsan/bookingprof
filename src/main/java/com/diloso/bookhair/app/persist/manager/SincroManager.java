package com.diloso.bookhair.app.persist.manager;

import javax.persistence.EntityManager;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.SincroDAO;
import com.diloso.bookhair.app.negocio.dto.SincroDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.entities.Sincro;
import com.diloso.bookhair.app.persist.transformer.SincroTransformer;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@Component
@Scope(value = "singleton")
public class SincroManager extends Manager implements SincroDAO {

	@Autowired
	protected SincroTransformer sincroTransformer;
	
	public SincroManager() {

	}

	public SincroDTO create(SincroDTO sincro) throws Exception {
		EntityManager em = getEntityManager();
		Sincro entitySincro = sincroTransformer
				.transformDTOToEntity(sincro);
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
		return sincroTransformer.transformEntityToDTO(entitySincro);
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
		return sincroTransformer.transformEntityToDTO(
				oldEntitySincro);
	}

	public SincroDTO update(SincroDTO sincro) throws Exception {
		EntityManager em = getEntityManager();
		Sincro entitySincro = sincroTransformer
				.transformDTOToEntity(sincro);
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
		return sincroTransformer.transformEntityToDTO(entitySincro);
	}

	public SincroDTO getById(long id) {
		Entity entitySincro = null;
		try {
			Key k = KeyFactory.createKey(Sincro.class.getSimpleName(), id);
			entitySincro = DatastoreServiceFactory.getDatastoreService().get(k);
		} catch (Exception ex) {
		}
		return sincroTransformer.transformEntityToDTO(entitySincro);
	}

}