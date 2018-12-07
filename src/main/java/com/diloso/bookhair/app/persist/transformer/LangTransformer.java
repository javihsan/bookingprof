package com.diloso.bookhair.app.persist.transformer;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.LangDTO;
import com.diloso.bookhair.app.persist.entities.Lang;


@Component
@Scope(value = "singleton")
public class LangTransformer {
	
	public LangTransformer() {

	}
	
	public Lang transformDTOToEntity(LangDTO lang){
		
		Lang entityLang = new Lang();
		
		try {
			PropertyUtils.copyProperties(entityLang, lang);
		} catch (Exception e) {
		} 
		return entityLang;
	}
	
	public LangDTO transformEntityToDTO(Lang entityLang) {

		LangDTO lang = new LangDTO();

		try {
			PropertyUtils.copyProperties(lang, entityLang);
			
		} catch (Exception e) {
		}
		return lang;
	}
	
	
}
