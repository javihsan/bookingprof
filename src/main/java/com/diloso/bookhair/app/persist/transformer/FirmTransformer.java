package com.diloso.bookhair.app.persist.transformer;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.config.impl.ConfigFirm;
import com.diloso.bookhair.app.negocio.dao.ProfessionalDAO;
import com.diloso.bookhair.app.negocio.dao.WhereDAO;
import com.diloso.bookhair.app.negocio.dto.FirmDTO;
import com.diloso.bookhair.app.negocio.dto.ProfessionalDTO;
import com.diloso.bookhair.app.negocio.dto.WhereDTO;
import com.diloso.bookhair.app.negocio.utils.ApplicationContextProvider;
import com.diloso.bookhair.app.persist.entities.Firm;
import com.diloso.bookhair.app.persist.manager.ProfessionalManager;
import com.diloso.bookhair.app.persist.manager.WhereManager;

@Component
@Scope(value = "singleton")
public class FirmTransformer {
	
	@Autowired
	protected ProfessionalDAO professionalDAO;
	
	@Autowired
	protected WhereDAO whereDAO;
	
	
	public FirmTransformer() {
		if (professionalDAO==null){
			professionalDAO = new ProfessionalManager();
			whereDAO = new WhereManager();
		}
	}


	public Firm transformDTOToEntity(FirmDTO firm) {

		Firm entityFirm = new Firm();

		try {
			PropertyUtils.copyProperties(entityFirm, firm);
		} catch (Exception e) {
		}

		if (firm.getFirRespon()!=null){
			entityFirm.setFirResponId(firm.getFirRespon().getId());
		}
		
		if (firm.getFirWhere()!=null){
			entityFirm.setFirWhereId(firm.getFirWhere().getId());
		}
		
		if (firm.getFirConfig()!=null){
			entityFirm.setFirConfigNum(firm.getFirConfig().getNumConfig());
		}
		
		return entityFirm;
	}

	
	public FirmDTO transformEntityToDTO(Firm entityFirm) {

		FirmDTO firm = new FirmDTO();

		try {
			PropertyUtils.copyProperties(firm, entityFirm);
		} catch (Exception e) {
		}
	
		// Propiedades de Respon
		if (entityFirm.getFirResponId() != null) {
			ProfessionalDTO respon = professionalDAO
					.getById(entityFirm.getFirResponId());
			firm.setFirRespon(respon);
		}
		
		// Propiedades de Where
		if (entityFirm.getFirWhereId() != null) {
			WhereDTO where = whereDAO
					.getById(entityFirm.getFirWhereId());
			firm.setFirWhere(where);
		}
		
		String numConfig = ConfigFirm.IDENT_DEFAULT;
		// Propiedades de Config
		if (entityFirm.getFirConfigNum() != null && !entityFirm.getFirConfigNum().equals("")) {
			numConfig = entityFirm.getFirConfigNum();
		} 
		ConfigFirm configFirm = (ConfigFirm) ApplicationContextProvider.getApplicationContext().getBean(ConfigFirm.PRE_IDENT_FIRM+numConfig);
		configFirm.setNumConfig(numConfig);
		firm.setFirConfig(configFirm);
		
		return firm;
	}

}
