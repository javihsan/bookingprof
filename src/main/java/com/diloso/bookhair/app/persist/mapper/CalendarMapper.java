package com.diloso.bookhair.app.persist.mapper;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.ProfessionalDTO;
import com.diloso.bookhair.app.negocio.dto.SemanalDiaryDTO;
import com.diloso.bookhair.app.negocio.manager.IProfessionalManager;
import com.diloso.bookhair.app.negocio.manager.ISemanalDiaryManager;
import com.diloso.bookhair.app.persist.entities.Calendar;


@Component
@Scope(value = "singleton")
public class CalendarMapper {
	
	public CalendarMapper() {

	}
	
	@Autowired
	protected IProfessionalManager professionalManager;
	
	@Autowired
	protected ISemanalDiaryManager semanalDiaryManager;

	public Calendar map(CalendarDTO calendar){
		
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
	
	public CalendarDTO map(Calendar entityCalendar) {

		CalendarDTO calendar = new CalendarDTO();

		try {
			PropertyUtils.copyProperties(calendar, entityCalendar);
			
			// Propiedades de Professional
			if (entityCalendar.getCalProfId() != null) {
				ProfessionalDTO profesional = professionalManager
						.getById(entityCalendar.getCalProfId());
					calendar.setCalProf(profesional);
			}
			
			// Propiedades de SemanalDiary
			if (entityCalendar.getCalSemanalDiaryId() != null) {
				SemanalDiaryDTO semanalDiary = semanalDiaryManager
						.getById(entityCalendar.getCalSemanalDiaryId());
				calendar.setCalSemanalDiary(semanalDiary);
			}
			
		} catch (Exception e) {
		}
		return calendar;
	}

//	public void setProfessionalDAO(IProfessionalManager iProfessionalManager) {
//		this.professionalManager = iProfessionalManager;
//	}
//
//	public void setSemanalDiaryDAO(ISemanalDiaryManager iSemanalDiaryManager) {
//		this.semanalDiaryManager = iSemanalDiaryManager;
//	}
			
	
}
