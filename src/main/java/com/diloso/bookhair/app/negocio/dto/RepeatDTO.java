package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;



public class RepeatDTO extends EventDTO implements Serializable {
	protected static final long serialVersionUID = 1L;

	// Nombre del servicio del local en multiIdioma
	protected String repNameMulti;
	
	// Matriz de la repetición
	protected String repMatrix;
	
	// Tipo de la repetición (0: curso; 1: incripción)
	protected Integer repType;
	
	// Colección a la que pertenece la repetición
	//protected CollectionEvent repCollection;
	// Profesional que trabaja la repetición
	protected ProfessionalDTO repProf;
	
	// Máximo num de clientes de la repetición
	protected Integer repMaxClients;
	
	/* Propiedades aplicables a los dias de la semana 
	  (Horarios de apertura semanal en horas) 
	  de la repetición */
	protected SemanalDiaryDTO repSemanalDiary;

	// Nombre de la repetición, solo para mostrar, no se guarda
	protected String repName;

	
    public RepeatDTO() {
    }

    
	public String getRepNameMulti() {
		return repNameMulti;
	}

	public void setRepNameMulti(String repNameMulti) {
		this.repNameMulti = repNameMulti;
	}
	
	public String getRepMatrix() {
		return repMatrix;
	}

	public void setRepMatrix(String repMatrix) {
		this.repMatrix = repMatrix;
	}

	public Integer getRepType() {
		return repType;
	}

	public void setRepType(Integer repType) {
		this.repType = repType;
	}
	
	public ProfessionalDTO getRepProf() {
		return repProf;
	}

	public void setRepProf(ProfessionalDTO repProf) {
		this.repProf = repProf;
	}

	public Integer getRepMaxClients() {
		return repMaxClients;
	}

	public void setRepMaxClients(Integer repMaxClients) {
		this.repMaxClients = repMaxClients;
	}

	public SemanalDiaryDTO getRepSemanalDiary() {
		return repSemanalDiary;
	}

	public void setRepSemanalDiary(SemanalDiaryDTO repSemanalDiary) {
		this.repSemanalDiary = repSemanalDiary;
	}
	

	public String getRepName() {
		return repName;
	}

	public void setRepName(String repName) {
		this.repName = repName;
	}
  	
}