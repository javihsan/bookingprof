package com.diloso.bookhair.app.persist.mapper;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.manager.ILangManager;
import com.diloso.bookhair.app.persist.entities.MultiText;


@Component
@Scope(value = "singleton")
public class MultiTextMapper {
	
	public MultiTextMapper() {

	}
	
	@Autowired
	protected ILangManager langManager;
	
	public MultiText map(MultiTextDTO multiText){
		
		MultiText entityMultiText = new MultiText();
		
		try {
			PropertyUtils.copyProperties(entityMultiText, multiText);
		} catch (Exception e) {
		} 
		return entityMultiText;
	}
	
	public MultiTextDTO map(MultiText entityMultiText) {

		MultiTextDTO multiText = new MultiTextDTO();

		try {
			PropertyUtils.copyProperties(multiText, entityMultiText);
			
			// Set mulLanCode, not in entity Rate 
			multiText.setMulLanName(langManager.getByCode(multiText.getMulLanCode()).getLanName());
			
		} catch (Exception e) {
		}
		return multiText;
	}
//
//	public void setLangDAO(ILangManager iLangManager) {
//		this.langManager = iLangManager;
//	}
//	
//	
	
}
