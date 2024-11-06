package com.diloso.bookhair.app.negocio.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.DiaryDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.DiaryDAO;
import com.diloso.bookhair.app.persist.entities.Diary;
import com.diloso.bookhair.app.persist.mapper.DiaryMapper;

@Component
@Scope(value = "singleton")
public class DiaryManager implements IDiaryManager {

	@Autowired
	private DiaryDAO diaryDAO;
	
	@Autowired
	protected DiaryMapper mapper;
	
	public DiaryManager() {

	}

	@Override
	public DiaryDTO create(DiaryDTO diaryDTO) throws Exception {
		Diary diary = mapper.map(diaryDTO);
		diary = diaryDAO.create(diary);
		return mapper.map(diary);
	}

	@Override
	public DiaryDTO remove(long id) throws Exception {
		Diary diary = diaryDAO.get(id);
		diaryDAO.delete(id);
		if (diary == null) {
			return null;
		}
		return mapper.map(diary);
	}

	@Override
	public DiaryDTO update(DiaryDTO diaryDTO) throws Exception {
		Diary diary = mapper.map(diaryDTO);
		Diary oldDiary = diaryDAO.get(diaryDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(diary, oldDiary);
		} catch (Exception e) {
		}
		diary = diaryDAO.update(diary);
		if (diary == null) {
			return null;
		}
		return mapper.map(diary);
	}

	@Override
	public DiaryDTO getById(long id) {
		Diary diary = diaryDAO.get(id);
		if (diary == null) {
			return null;
		}
		return mapper.map(diary);		
	}
	
	
/*
	public DiaryDTO create(DiaryDTO diary) throws Exception {
		EntityManager em = getEntityManager();
		Diary entityDiary = diaryMapper
				.map(diary);
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
		return diaryMapper.map(entityDiary);
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
		return diaryMapper.map(
				oldEntityDiary);
	}

	public DiaryDTO update(DiaryDTO diary) throws Exception {
		EntityManager em = getEntityManager();
		Diary entityDiary = diaryMapper
				.map(diary);
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
		return diaryMapper.map(entityDiary);
	}

	public DiaryDTO getById(long id) {
		Entity entityDiary = null;
		try {
			Key k = KeyFactory.createKey(Diary.class.getSimpleName(), id);
			entityDiary = DatastoreServiceFactory.getDatastoreService().get(k);
		} catch (Exception ex) {
		}
		return diaryMapper.map(entityDiary);
	}

	public void setDiaryTransformer(DiaryMapper diaryMapper) {
		this.diaryMapper = diaryMapper;
	}
*/
	
	
}