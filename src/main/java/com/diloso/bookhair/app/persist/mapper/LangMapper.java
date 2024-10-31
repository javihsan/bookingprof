package com.diloso.bookhair.app.persist.mapper;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.LangDTO;
import com.diloso.bookhair.app.persist.entities.Lang;


@Component
@Scope(value = "singleton")
public class LangMapper {
	
	public LangMapper() {

	}
	
	public Lang map(LangDTO lang){
		
		Lang entityLang = new Lang();
		
		try {
			PropertyUtils.copyProperties(entityLang, lang);
		} catch (Exception e) {
		} 
		return entityLang;
	}
	
	public LangDTO map(Lang entityLang) {

		LangDTO lang = new LangDTO();

		try {
			PropertyUtils.copyProperties(lang, entityLang);
			
		} catch (Exception e) {
		}
		return lang;
	}
	
	
}
