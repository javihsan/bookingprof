package com.diloso.bookhair.app.persist.mapper;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.ProductClassDTO;
import com.diloso.bookhair.app.persist.entities.ProductClass;


@Component
@Scope(value = "singleton")
public class ProductClassMapper {
	
	public ProductClassMapper() {

	}
	
	public ProductClass map(ProductClassDTO productClass){
		
		ProductClass entityProductClass = new ProductClass();
		
		try {
			PropertyUtils.copyProperties(entityProductClass, productClass);
		} catch (Exception e) {
		}
		return entityProductClass;
	}
	
	public ProductClassDTO map(ProductClass entityProductClass) {

		ProductClassDTO productClass = new ProductClassDTO();

		try {
			PropertyUtils.copyProperties(productClass, entityProductClass);
			
		} catch (Exception e) {
		}
		return productClass;
	}
	

	
}
