package com.diloso.bookhair.app.persist.transformer;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.DiaryDTO;
import com.diloso.bookhair.app.persist.entities.Diary;
import com.google.appengine.api.datastore.Entity;

@Component
@Scope(value = "singleton")
public class DiaryTransformer {
	
	public DiaryTransformer() {

	}	
	
	public Diary transformDTOToEntity(DiaryDTO diary){
		
		Diary entityDiary = new Diary();
		
		try {
			PropertyUtils.copyProperties(entityDiary, diary);
		} catch (Exception e) {
		} 
		return entityDiary;
	}
	
	public DiaryDTO transformEntityToDTO(Diary entityDiary) {

		DiaryDTO diary = new DiaryDTO();

		try {
			PropertyUtils.copyProperties(diary, entityDiary);
			
		} catch (Exception e) {
		}
		return diary;
	}
	
	public DiaryDTO transformEntityToDTO(Entity entityDiary) {

		DiaryDTO diary = new DiaryDTO();

		try {
			diary.setId(entityDiary.getKey().getId());
			PropertyDescriptor[] pd = PropertyUtils.getPropertyDescriptors(DiaryDTO.class);
			Object value;
			for (PropertyDescriptor descriptor : pd) {
				Method writeMethod = PropertyUtils.getWriteMethod(descriptor);
				if (writeMethod!=null){
					value = entityDiary.getProperty(descriptor.getName());
					if (value instanceof Long){
						if (writeMethod.getParameterTypes()[0].getName().equals("java.lang.Integer")){
							value = new Integer(value.toString());
						}
					} 
					if (value!=null) writeMethod.invoke(diary,value);
				}
			}
		} catch (Exception e) {
		}
		return diary;
	}
	
	
}
