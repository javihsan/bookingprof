package com.diloso.bookhair.app.persist.mapper;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.WhereDTO;
import com.diloso.bookhair.app.persist.entities.Where;


@Component
@Scope(value = "singleton")
public class WhereMapper {
	
	public WhereMapper() {

	}
	
	public Where map(WhereDTO where){
		
		Where entityWhere = new Where();
		
		try {
			PropertyUtils.copyProperties(entityWhere, where);
		} catch (Exception e) {
		} 
		
		return entityWhere;
	}
	
	public WhereDTO map(Where entityWhere){
		
		WhereDTO where = new WhereDTO();
		
		try {
			PropertyUtils.copyProperties(where, entityWhere);
		} catch (Exception e) {
		} 
			
		return where;
	}
	

	
}
