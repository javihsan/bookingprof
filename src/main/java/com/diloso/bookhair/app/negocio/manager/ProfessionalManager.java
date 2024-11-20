package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.ProfessionalDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.ProfessionalDAO;
import com.diloso.bookhair.app.persist.entities.Professional;
import com.diloso.bookhair.app.persist.mapper.ProfessionalMapper;

@Component
@Scope(value = "singleton")
public class ProfessionalManager implements IProfessionalManager {

	@Autowired
	private ProfessionalDAO professionalDAO;
	
	@Autowired
	protected ProfessionalMapper mapper;
	
	public ProfessionalManager() {
		if (mapper==null){
			mapper = new ProfessionalMapper();
		}
	}

	@Override
	public ProfessionalDTO create(ProfessionalDTO professionalDTO) throws Exception {
		Professional professional = mapper.map(professionalDTO);
		professional = professionalDAO.create(professional);
		return mapper.map(professional);
	}

	@Override
	public ProfessionalDTO remove(long id) throws Exception {
		Professional professional = professionalDAO.get(id);
		professionalDAO.delete(id);
		if (professional == null) {
			return null;
		}
		return mapper.map(professional);
	}

	@Override
	public ProfessionalDTO update(ProfessionalDTO professionalDTO) throws Exception {
		Professional professional = mapper.map(professionalDTO);
		Professional oldProfessional = professionalDAO.get(professionalDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(professional, oldProfessional);
		} catch (Exception e) {
		}
		professional = professionalDAO.update(professional);
		if (professional == null) {
			return null;
		}
		return mapper.map(professional);
	}

	@Override
	public ProfessionalDTO getById(long id) {
		Professional professional = professionalDAO.get(id);
		if (professional == null) {
			return null;
		}
		return mapper.map(professional);		
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

*/
	
	
}