package com.diloso.bookhair.app.negocio.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.SemanalDiaryDTO;
import com.diloso.bookhair.app.persist.dao.SemanalDiaryDAO;
import com.diloso.bookhair.app.persist.mapper.SemanalDiaryMapper;

@Component
@Scope(value = "singleton")
public class SemanalDiaryManager implements ISemanalDiaryManager {

	@Autowired
	private SemanalDiaryDAO semanalDiaryDAO;
		
	@Autowired
	protected SemanalDiaryMapper semanalDiaryMapper;
	
	public SemanalDiaryManager() {

	}

	@Override
	public SemanalDiaryDTO create(SemanalDiaryDTO semanalDiary) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SemanalDiaryDTO remove(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SemanalDiaryDTO update(SemanalDiaryDTO semanalDiary) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SemanalDiaryDTO getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	/*
	public SemanalDiaryDTO create(SemanalDiaryDTO semanalDiary)
			throws Exception {
		EntityManager em = getEntityManager();
		SemanalDiary entitySemanalDiary = semanalDiaryMapper
				.map(semanalDiary);
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
		return semanalDiaryMapper.map(
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
		return semanalDiaryMapper.map(
				oldEntitySemanalDiary);
	}

	public SemanalDiaryDTO update(SemanalDiaryDTO semanalDiary)
			throws Exception {
		EntityManager em = getEntityManager();
		SemanalDiary entitySemanalDiary = semanalDiaryMapper
				.map(semanalDiary);
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
		return semanalDiaryMapper.map(
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
		return semanalDiaryMapper.map(
				entitySemanalDiary);
	}

	public void setSemanalDiaryTransformer(SemanalDiaryMapper semanalDiaryMapper) {
		this.semanalDiaryMapper = semanalDiaryMapper;
	}
	*/
	

}