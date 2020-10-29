package com.diloso.bookhair.app.persist.manager;

import javax.persistence.EntityManager;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.SemanalDiaryDAO;
import com.diloso.bookhair.app.negocio.dto.SemanalDiaryDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.entities.SemanalDiary;
import com.diloso.bookhair.app.persist.transformer.SemanalDiaryTransformer;

@Component
@Scope(value = "singleton")
public class SemanalDiaryManager extends Manager implements SemanalDiaryDAO {

	//@Autowired
	protected SemanalDiaryTransformer semanalDiaryTransformer;
	
	public SemanalDiaryManager() {

	}

	public SemanalDiaryDTO create(SemanalDiaryDTO semanalDiary)
			throws Exception {
		EntityManager em = getEntityManager();
		SemanalDiary entitySemanalDiary = semanalDiaryTransformer
				.transformDTOToEntity(semanalDiary);
		try {
			em.getTransaction().begin();
			em.persist(entitySemanalDiary);
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
		return semanalDiaryTransformer.transformEntityToDTO(
				entitySemanalDiary);
	}

	public SemanalDiaryDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		SemanalDiary oldEntitySemanalDiary = new SemanalDiary();
		try {
			em.getTransaction().begin();
			SemanalDiary entitySemanalDiary = (SemanalDiary) em.find(
					SemanalDiary.class, id);
			PropertyUtils.copyProperties(oldEntitySemanalDiary,
					entitySemanalDiary);
			em.remove(em.merge(entitySemanalDiary));
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
		return semanalDiaryTransformer.transformEntityToDTO(
				oldEntitySemanalDiary);
	}

	public SemanalDiaryDTO update(SemanalDiaryDTO semanalDiary)
			throws Exception {
		EntityManager em = getEntityManager();
		SemanalDiary entitySemanalDiary = semanalDiaryTransformer
				.transformDTOToEntity(semanalDiary);
		SemanalDiary oldEntitySemanalDiary = null;
		try {
			em.getTransaction().begin();
			oldEntitySemanalDiary = (SemanalDiary) em.find(SemanalDiary.class,
					entitySemanalDiary.getId());
			new NullAwareBeanUtilsBean().copyProperties(entitySemanalDiary,
					oldEntitySemanalDiary);
			entitySemanalDiary = em.merge(entitySemanalDiary);
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
		return semanalDiaryTransformer.transformEntityToDTO(
				entitySemanalDiary);
	}

	public SemanalDiaryDTO getById(long id) {
		SemanalDiary entitySemanalDiary = null;
		EntityManager em = getEntityManager();
		try {
			entitySemanalDiary = (SemanalDiary) em.find(SemanalDiary.class, id);
		} finally {
			em.close();
		}
		return semanalDiaryTransformer.transformEntityToDTO(
				entitySemanalDiary);
	}

	public void setSemanalDiaryTransformer(SemanalDiaryTransformer semanalDiaryTransformer) {
		this.semanalDiaryTransformer = semanalDiaryTransformer;
	}
	
	

}