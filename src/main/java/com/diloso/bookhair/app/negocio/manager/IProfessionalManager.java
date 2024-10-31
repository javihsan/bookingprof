package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.ProfessionalDTO;

public interface IProfessionalManager {

	ProfessionalDTO create(ProfessionalDTO professionalDTO) throws Exception;

	ProfessionalDTO remove(long id) throws Exception;

	ProfessionalDTO update(ProfessionalDTO professionalDTO) throws Exception;

	ProfessionalDTO getById(long id);
	
	ProfessionalDTO getByEmail(long resFirId, String email);

	List<ProfessionalDTO> getProfessional(long resFirId);
	
}