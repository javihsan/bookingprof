package com.diloso.bookhair.app.persist.manager;

import javax.persistence.EntityManager;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.DiaryDAO;
import com.diloso.bookhair.app.negocio.dto.DiaryDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.entities.Diary;
import com.diloso.bookhair.app.persist.transformer.DiaryTransformer;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@Component
@Scope(value = "singleton")
public class DiaryManager extends Manager implements DiaryDAO {

	//@Autowired
	protected DiaryTransformer diaryTransformer;
	
	public DiaryManager() {

	}

	public DiaryDTO create(DiaryDTO diary) throws Exception {
		EntityManager em = getEntityManager();
		Diary entityDiary = diaryTransformer
				.transformDTOToEntity(diary);
		try {
			em.getTransaction().begin();
			em.persist(entityDiary);
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
		return diaryTransformer.transformEntityToDTO(entityDiary);
	}

	public DiaryDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		Diary oldEntityDiary = new Diary();
		try {
			em.getTransaction().begin();
			Diary entityDiary = (Diary) em.find(Diary.class, id);
			PropertyUtils.copyProperties(oldEntityDiary, entityDiary);
			em.remove(em.merge(entityDiary));
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
		return diaryTransformer.transformEntityToDTO(
				oldEntityDiary);
	}

	public DiaryDTO update(DiaryDTO diary) throws Exception {
		EntityManager em = getEntityManager();
		Diary entityDiary = diaryTransformer
				.transformDTOToEntity(diary);
		Diary oldEntityDiary = null;
		try {
			em.getTransaction().begin();
			oldEntityDiary = (Diary) em.find(Diary.class, entityDiary.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityDiary,
					oldEntityDiary);
			entityDiary = em.merge(entityDiary);
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
		return diaryTransformer.transformEntityToDTO(entityDiary);
	}

	public DiaryDTO getById(long id) {
		Entity entityDiary = null;
		try {
			Key k = KeyFactory.createKey(Diary.class.getSimpleName(), id);
			entityDiary = DatastoreServiceFactory.getDatastoreService().get(k);
		} catch (Exception ex) {
		}
		return diaryTransformer.transformEntityToDTO(entityDiary);
	}

	public void setDiaryTransformer(DiaryTransformer diaryTransformer) {
		this.diaryTransformer = diaryTransformer;
	}

	
	
}