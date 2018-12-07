package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;


public class TaskDTO implements Serializable {

	protected static final long serialVersionUID = 1L;
	
	protected Long id;
	
	protected Integer enabled;
	
	// Categoria del servicio
	protected TaskClassDTO tasClass;
	
	// Nombre del servicio en multiIdioma
	protected String tasNameMulti;
	
	// Nombre del servicio, solo para mostrar, no se guarda
	protected String tasName;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}
	
	public TaskClassDTO getTasClass() {
		return tasClass;
	}

	public void setTasClass(TaskClassDTO tasClass) {
		this.tasClass = tasClass;
	}

	public String getTasNameMulti() {
		return tasNameMulti;
	}

	public void setTasNameMulti(String tasNameMulti) {
		this.tasNameMulti = tasNameMulti;
	}

	public String getTasName() {
		return tasName;
	}

	public void setTasName(String tasName) {
		this.tasName = tasName;
	}
	
	
}
