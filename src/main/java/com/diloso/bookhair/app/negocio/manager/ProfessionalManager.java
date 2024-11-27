package com.diloso.bookhair.app.negocio.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public static final String ENABLED = "enabled";
	public static final String RES_FIR_ID = "resFirId";
	public static final String WHO_EMAIL = "whoEmail";
	public static final String ORDER_KEY_DESC = "-__key__";
	
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
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(RES_FIR_ID, resFirId);
		filters.put(WHO_EMAIL, email);
		filters.put(ENABLED, 1);
		List<Professional> resultQuery = professionalDAO.listFilter(filters);
		if (resultQuery.size() == 1) {
			return mapper.map(resultQuery.get(0));
		}
		return null;
	}

	@Override
	public List<ProfessionalDTO> getProfessional(long resFirId) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(RES_FIR_ID, resFirId);
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_DESC);
		List<Professional> resultQuery = professionalDAO.listOrderFilter(filters, orders);
		List<ProfessionalDTO> result = new ArrayList<ProfessionalDTO>();
		for (Professional entity : resultQuery) {
			result.add(mapper.map(entity));
		}
		return result;
	}

}