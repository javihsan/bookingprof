package com.diloso.bookhair.app.persist.transformer;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.WhereDAO;
import com.diloso.bookhair.app.negocio.dto.ProfessionalDTO;
import com.diloso.bookhair.app.negocio.dto.WhereDTO;
import com.diloso.bookhair.app.persist.entities.Professional;


@Component
@Scope(value = "singleton")
public class ProfessionalTransformer {
	
	@Autowired
	protected WhereDAO whereDAO;
	
	public ProfessionalTransformer() {

	}
	
	public Professional transformDTOToEntity(ProfessionalDTO professional){
		
		Professional entityProfessional = new Professional();
		
		try {
			PropertyUtils.copyProperties(entityProfessional, professional);
		} catch (Exception e) {
		} 

		if (professional.getWhoWhere()!=null){
			entityProfessional.setWhoWhereId(professional.getWhoWhere().getId());
		}
		
		return entityProfessional;
	}
	
	public ProfessionalDTO transformEntityToDTO(Professional entityProfessional){
		
		ProfessionalDTO professional = new ProfessionalDTO();
		
		try {
			PropertyUtils.copyProperties(professional, entityProfessional);
		} catch (Exception e) {
		} 
		
		// Propiedades de Where
		if (entityProfessional.getWhoWhereId() != null) {
			WhereDTO where = whereDAO
					.getById(entityProfessional.getWhoWhereId());
			professional.setWhoWhere(where);
		}
		
		return professional;
	}

	
}
