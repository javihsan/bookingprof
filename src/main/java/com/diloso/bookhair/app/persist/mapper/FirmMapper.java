package com.diloso.bookhair.app.persist.mapper;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.config.impl.ConfigFirm;
import com.diloso.bookhair.app.negocio.dto.FirmDTO;
import com.diloso.bookhair.app.negocio.dto.ProfessionalDTO;
import com.diloso.bookhair.app.negocio.dto.WhereDTO;
import com.diloso.bookhair.app.negocio.manager.IProfessionalManager;
import com.diloso.bookhair.app.negocio.manager.IWhereManager;
import com.diloso.bookhair.app.negocio.manager.ProfessionalManager;
import com.diloso.bookhair.app.negocio.manager.WhereManager;
import com.diloso.bookhair.app.negocio.utils.ApplicationContextProvider;
import com.diloso.bookhair.app.persist.entities.Firm;

@Component
@Scope(value = "singleton")
public class FirmMapper {
	
	@Autowired
	protected IProfessionalManager professionalManager;
	
	@Autowired
	protected IWhereManager whereManager;
	
	
	public FirmMapper() {
		if (professionalManager==null){
			professionalManager = new ProfessionalManager();
			whereManager = new WhereManager();
		}
	}


	public Firm map(FirmDTO firm) {

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

	
	public FirmDTO map(Firm entityFirm) {

		FirmDTO firm = new FirmDTO();

		try {
			PropertyUtils.copyProperties(firm, entityFirm);
		} catch (Exception e) {
		}
	
		// Propiedades de Respon
		if (entityFirm.getFirResponId() != null) {
			ProfessionalDTO respon = professionalManager
					.getById(entityFirm.getFirResponId());
			firm.setFirRespon(respon);
		}
		
		// Propiedades de Where
		if (entityFirm.getFirWhereId() != null) {
			WhereDTO where = whereManager
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

//
//	public void setProfessionalDAO(IProfessionalManager iProfessionalManager) {
//		this.professionalManager = iProfessionalManager;
//	}
//
//
//	public void setWhereDAO(IWhereManager iWhereManager) {
//		this.whereManager = iWhereManager;
//	}
	
	

}
