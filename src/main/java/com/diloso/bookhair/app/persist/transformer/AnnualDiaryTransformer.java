package com.diloso.bookhair.app.persist.transformer;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.DiaryDAO;
import com.diloso.bookhair.app.negocio.dto.AnnualDiaryDTO;
import com.diloso.bookhair.app.negocio.dto.DiaryDTO;
import com.diloso.bookhair.app.persist.entities.AnnualDiary;
import com.google.appengine.api.datastore.Entity;


@Component
@Scope(value = "singleton")
public class AnnualDiaryTransformer {
	
	public AnnualDiaryTransformer() {

	}
	
	
	//@Autowired
	protected DiaryDAO diaryDAO;

	
	public AnnualDiary transformDTOToEntity(AnnualDiaryDTO annualDiary){
		
		AnnualDiary entityAnnualDiary = new AnnualDiary();
		
		try {
			PropertyUtils.copyProperties(entityAnnualDiary, annualDiary);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		if (annualDiary.getAnuDayDiary()!=null){
			entityAnnualDiary.setAnuDayDiaryId(annualDiary.getAnuDayDiary().getId());
		}
		
		return entityAnnualDiary;
	}
	
	public AnnualDiaryDTO transformEntityToDTO(AnnualDiary entityAnnualDiary) {

		AnnualDiaryDTO annualDiary = new AnnualDiaryDTO();

		try {
			PropertyUtils.copyProperties(annualDiary, entityAnnualDiary);
		
			// Propiedades de Diary
			if (entityAnnualDiary.getAnuDayDiaryId() != null) {
				DiaryDTO diary = diaryDAO
						.getById(entityAnnualDiary.getAnuDayDiaryId());
				annualDiary.setAnuDayDiary(diary);
			}
			
		} catch (Exception e) {
		}
		return annualDiary;
	}
	
	public AnnualDiaryDTO transformEntityToDTO(Entity entityAnnualDiary) {

		AnnualDiaryDTO annualDiary = new AnnualDiaryDTO();

		try {
			annualDiary.setId(entityAnnualDiary.getKey().getId());
			PropertyDescriptor[] pd = PropertyUtils.getPropertyDescriptors(AnnualDiaryDTO.class);
			Object value;
			for (PropertyDescriptor descriptor : pd) {
				Method writeMethod = PropertyUtils.getWriteMethod(descriptor);
				if (writeMethod!=null){
					value = entityAnnualDiary.getProperty(descriptor.getName());
					if (value instanceof Long){
						if (writeMethod.getParameterTypes()[0].getName().equals("java.lang.Integer")){
							value = new Integer(value.toString());
						}
					} 
					if (value!=null) writeMethod.invoke(annualDiary,value);
				}
			}
		} catch (Exception e) {
		}
		
		// Propiedades de Diary
		Long anuDayDiaryId = (Long)entityAnnualDiary.getProperty("anuDayDiaryId");
		if (anuDayDiaryId != null) {
			DiaryDTO diary = diaryDAO.getById(anuDayDiaryId);
			annualDiary.setAnuDayDiary(diary);
		}

		return annualDiary;
	}

	public void setDiaryDAO(DiaryDAO diaryDAO) {
		this.diaryDAO = diaryDAO;
	}
	
	
	
}
