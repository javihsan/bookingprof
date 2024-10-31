package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.ProfessionalDTO;
import com.diloso.bookhair.app.persist.dao.ProfessionalDAO;
import com.diloso.bookhair.app.persist.mapper.ProfessionalMapper;

@Component
@Scope(value = "singleton")
public class ProfessionalManager implements IProfessionalManager {

	@Autowired
	private ProfessionalDAO professionalDAO;
	
	@Autowired
	protected ProfessionalMapper professionalMapper;
	
	public ProfessionalManager() {
		if (professionalMapper==null){
			professionalMapper = new ProfessionalMapper();
		}
	}

	@Override
	public ProfessionalDTO create(ProfessionalDTO professional) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProfessionalDTO remove(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProfessionalDTO update(ProfessionalDTO professional) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProfessionalDTO getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProfessionalDTO getByEmail(long resFirId, String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProfessionalDTO> getProfessional(long resFirId) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	/*
	public ProfessionalDTO create(ProfessionalDTO professional)
			throws Exception {
		EntityManager em = getEntityManager();
		Professional entityProfessional = professionalMapper
				.map(professional);
		try {
			em.getTransaction().begin();
			em.persist(entityProfessional);
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
		return professionalMapper.map(
				entityProfessional);
	}

	public ProfessionalDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		Professional oldEntityProfessional = new Professional();
		try {
			em.getTransaction().begin();
			Professional entityProfessional = (Professional) em.find(
					Professional.class, id);
			PropertyUtils.copyProperties(oldEntityProfessional,
					entityProfessional);
			em.remove(em.merge(entityProfessional));
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
		return professionalMapper.map(
				oldEntityProfessional);
	}

	public ProfessionalDTO update(ProfessionalDTO professional)
			throws Exception {
		EntityManager em = getEntityManager();
		Professional entityProfessional = professionalMapper
				.map(professional);
		Professional oldEntityProfessional = null;
		try {
			em.getTransaction().begin();
			oldEntityProfessional = (Professional) em.find(Professional.class,
					entityProfessional.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityProfessional,
					oldEntityProfessional);
			entityProfessional = em.merge(entityProfessional);
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
		return professionalMapper.map(
				entityProfessional);
	}

	public ProfessionalDTO getById(long id) {
		Professional entityProfessional = null;
		EntityManager em = getEntityManager();
		try {
			entityProfessional = (Professional) em.find(Professional.class, id);
		} finally {
			em.close();
		}
		return professionalMapper.map(
				entityProfessional);
	}

	public ProfessionalDTO getByEmail(long resFirId, String email) {
		EntityManager em = getEntityManager();
		List<Professional> resultQuery = null;
		ProfessionalDTO professional = null;
		try {
			Query query = em.createNamedQuery("getProfessionalEmail");
			query.setParameter("resFirId", resFirId);
			query.setParameter("whoEmail", email);
			resultQuery = (List<Professional>) query.getResultList();
			if (resultQuery.size() == 1) {
				professional = professionalMapper
						.map(resultQuery.get(0));
			}
		} finally {
			em.close();
		}
		return professional;
	}

	public List<ProfessionalDTO> getProfessional(long resFirId) {
		EntityManager em = getEntityManager();
		List<ProfessionalDTO> result = new ArrayList<ProfessionalDTO>();
		List<Professional> resultQuery = null;
		ProfessionalDTO professional = null;
		try {
			Query query = em.createNamedQuery("getProfessional");
			query.setParameter("resFirId", resFirId);
			resultQuery = (List<Professional>) query.getResultList();
			for (Professional entityProfessional : resultQuery) {
				professional = professionalMapper
						.map(entityProfessional);
				result.add(professional);
			}
		} finally {
			em.close();
		}
		return result;
	}

	public void setProfessionalTransformer(ProfessionalMapper professionalMapper) {
		this.professionalMapper = professionalMapper;
	}
*/
	
	
}