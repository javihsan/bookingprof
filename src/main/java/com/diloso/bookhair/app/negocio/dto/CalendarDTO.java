package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;
import java.util.List;

public class CalendarDTO extends ResourceDTO implements Serializable {
	protected static final long serialVersionUID = 1L;

	protected String calName;
	
	protected String calDesc;	
	
	protected Long calLocalId;
	// Profesional al que pertenece el puesto
	protected ProfessionalDTO calProf;
	
	// Opciones del puesto

	/* Propiedades aplicables a los dias de la semana 
	  (Horarios de apertura semanal en horas) 
	  del puesto */
	protected SemanalDiaryDTO calSemanalDiary;
	// Lista de tareas disponibles en el puesto
	protected List<Long> calLocalTasksId;

	// Lista como label de tareas disponibles en el puesto, solo para mostrar, no se guarda
	protected String calLabelLocalTasks;
	
	public CalendarDTO() {
    }

	public String getCalName() {
		return calName;
	}

	public void setCalName(String calName) {
		this.calName = calName;
	}

	public String getCalDesc() {
		return calDesc;
	}

	public void setCalDesc(String calDesc) {
		this.calDesc = calDesc;
	}
	
	public Long getCalLocalId() {
		return calLocalId;
	}

	public void setCalLocalId(Long calLocalId) {
		this.calLocalId = calLocalId;
	}
	
	public SemanalDiaryDTO getCalSemanalDiary() {
		return calSemanalDiary;
	}

	public void setCalSemanalDiary(SemanalDiaryDTO calSemanalDiary) {
		this.calSemanalDiary = calSemanalDiary;
	}

	public List<Long> getCalLocalTasksId() {
		return calLocalTasksId;
	}

	public void setCalLocalTasksId(List<Long> calLocalTasksId) {
		this.calLocalTasksId = calLocalTasksId;
	}

	public String getCalLabelLocalTasks() {
		return calLabelLocalTasks;
	}

	public void setCalLabelLocalTasks(String calLabelLocalTasks) {
		this.calLabelLocalTasks = calLabelLocalTasks;
	}

	public ProfessionalDTO getCalProf() {
		return calProf;
	}

	public void setCalProf(ProfessionalDTO calProf) {
		this.calProf = calProf;
	}
			  
}