package com.diloso.bookhair.app.persist.transformer;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.ProfessionalDAO;
import com.diloso.bookhair.app.negocio.dao.SemanalDiaryDAO;
import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.ProfessionalDTO;
import com.diloso.bookhair.app.negocio.dto.SemanalDiaryDTO;
import com.diloso.bookhair.app.persist.entities.Calendar;


@Component
@Scope(value = "singleton")
public class CalendarTransformer {
	
	public CalendarTransformer() {

	}
	
	//@Autowired
	protected ProfessionalDAO professionalDAO;
	
	//@Autowired
	protected SemanalDiaryDAO semanalDiaryDAO;

	public Calendar transformDTOToEntity(CalendarDTO calendar){
		
		Calendar entityCalendar = new Calendar();
		
		try {
			PropertyUtils.copyProperties(entityCalendar, calendar);
		} catch (Exception e) {
		} 
		
		if (calendar.getCalProf()!=null){
			entityCalendar.setCalProfId(calendar.getCalProf().getId());
		}
		if (calendar.getCalSemanalDiary()!=null){
			entityCalendar.setCalSemanalDiaryId(calendar.getCalSemanalDiary().getId());
		}
		
		return entityCalendar;
	}
	
	public CalendarDTO transformEntityToDTO(Calendar entityCalendar) {

		CalendarDTO calendar = new CalendarDTO();

		try {
			PropertyUtils.copyProperties(calendar, entityCalendar);
			
			// Propiedades de Professional
			if (entityCalendar.getCalProfId() != null) {
				ProfessionalDTO profesional = professionalDAO
						.getById(entityCalendar.getCalProfId());
					calendar.setCalProf(profesional);
			}
			
			// Propiedades de SemanalDiary
			if (entityCalendar.getCalSemanalDiaryId() != null) {
				SemanalDiaryDTO semanalDiary = semanalDiaryDAO
						.getById(entityCalendar.getCalSemanalDiaryId());
				calendar.setCalSemanalDiary(semanalDiary);
			}
			
		} catch (Exception e) {
		}
		return calendar;
	}

	public void setProfessionalDAO(ProfessionalDAO professionalDAO) {
		this.professionalDAO = professionalDAO;
	}

	public void setSemanalDiaryDAO(SemanalDiaryDAO semanalDiaryDAO) {
		this.semanalDiaryDAO = semanalDiaryDAO;
	}
	
		
	
}
