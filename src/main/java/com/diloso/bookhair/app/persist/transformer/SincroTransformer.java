package com.diloso.bookhair.app.persist.transformer;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.SincroDTO;
import com.diloso.bookhair.app.persist.entities.Sincro;
import com.google.appengine.api.datastore.Entity;

@Component
@Scope(value = "singleton")
public class SincroTransformer {
	
	public SincroTransformer() {

	}	
	
	public Sincro transformDTOToEntity(SincroDTO sincro){
		
		Sincro entitySincro = new Sincro();
		
		try {
			PropertyUtils.copyProperties(entitySincro, sincro);
		} catch (Exception e) {
		} 
		return entitySincro;
	}
	
	public SincroDTO transformEntityToDTO(Sincro entitySincro) {

		SincroDTO sincro = new SincroDTO();

		try {
			PropertyUtils.copyProperties(sincro, entitySincro);
			
		} catch (Exception e) {
		}
		return sincro;
	}
	
	public SincroDTO transformEntityToDTO(Entity entitySincro) {

		SincroDTO sincro = new SincroDTO();

		try {
			sincro.setId(entitySincro.getKey().getId());
			PropertyDescriptor[] pd = PropertyUtils.getPropertyDescriptors(SincroDTO.class);
			Object value;
			for (PropertyDescriptor descriptor : pd) {
				Method writeMethod = PropertyUtils.getWriteMethod(descriptor);
				if (writeMethod!=null){
					value = entitySincro.getProperty(descriptor.getName());
					if (value instanceof Long){
						if (writeMethod.getParameterTypes()[0].getName().equals("java.lang.Integer")){
							value = new Integer(value.toString());
						}
					} 
					if (value!=null) writeMethod.invoke(sincro,value);
				}
			}
		} catch (Exception e) {
		}
		return sincro;
	}
	
	
}
