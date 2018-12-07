package com.diloso.bookhair.app.persist.transformer;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.LangDAO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.persist.entities.MultiText;


@Component
@Scope(value = "singleton")
public class MultiTextTransformer {
	
	public MultiTextTransformer() {

	}
	
	@Autowired
	protected LangDAO langDAO;
	
	public MultiText transformDTOToEntity(MultiTextDTO multiText){
		
		MultiText entityMultiText = new MultiText();
		
		try {
			PropertyUtils.copyProperties(entityMultiText, multiText);
		} catch (Exception e) {
		} 
		return entityMultiText;
	}
	
	public MultiTextDTO transformEntityToDTO(MultiText entityMultiText) {

		MultiTextDTO multiText = new MultiTextDTO();

		try {
			PropertyUtils.copyProperties(multiText, entityMultiText);
			
			// Set mulLanCode, not in entity Rate 
			multiText.setMulLanName(langDAO.getByCode(multiText.getMulLanCode()).getLanName());
			
		} catch (Exception e) {
		}
		return multiText;
	}
	
	
}
