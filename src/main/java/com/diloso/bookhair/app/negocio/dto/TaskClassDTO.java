package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;


public class TaskClassDTO implements Serializable {

	
	protected static final long serialVersionUID = 1L;

	protected Long id;
	
	protected Integer enabled;
	
	// Nombre de la categoria de servicio en multiIdioma
	protected String tclNameMulti;

	// Nombre de la categoria de servicio, solo para mostrar, no se guarda
	protected String tclName;

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

	public String getTclNameMulti() {
		return tclNameMulti;
	}

	public void setTclNameMulti(String tclNameMulti) {
		this.tclNameMulti = tclNameMulti;
	}
	
	public String getTclName() {
		return tclName;
	}

	public void setTclName(String tclName) {
		this.tclName = tclName;
	}

				
}
