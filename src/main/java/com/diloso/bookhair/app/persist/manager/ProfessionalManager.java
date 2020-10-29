package com.diloso.bookhair.app.persist.manager;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.ProfessionalDAO;
import com.diloso.bookhair.app.negocio.dto.ProfessionalDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.entities.Professional;
import com.diloso.bookhair.app.persist.transformer.ProfessionalTransformer;

@Component
@Scope(value = "singleton")
public class ProfessionalManager extends Manager implements ProfessionalDAO {

	//@Autowired
	protected ProfessionalTransformer professionalTransformer;
	
	public ProfessionalManager() {
		if (professionalTransformer==null){
			professionalTransformer = new ProfessionalTransformer();
		}
	}

	public ProfessionalDTO create(ProfessionalDTO professional)
			throws Exception {
		EntityManager em = getEntityManager();
		Professional entityProfessional = professionalTransformer
				.transformDTOToEntity(professional);
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
		return professionalTransformer.transformEntityToDTO(
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
		return professionalTransformer.transformEntityToDTO(
				oldEntityProfessional);
	}

	public ProfessionalDTO update(ProfessionalDTO professional)
			throws Exception {
		EntityManager em = getEntityManager();
		Professional entityProfessional = professionalTransformer
				.transformDTOToEntity(professional);
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
		return professionalTransformer.transformEntityToDTO(
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
		return professionalTransformer.transformEntityToDTO(
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
				professional = professionalTransformer
						.transformEntityToDTO(resultQuery.get(0));
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
				professional = professionalTransformer
						.transformEntityToDTO(entityProfessional);
				result.add(professional);
			}
		} finally {
			em.close();
		}
		return result;
	}

	public void setProfessionalTransformer(ProfessionalTransformer professionalTransformer) {
		this.professionalTransformer = professionalTransformer;
	}

	
	
}