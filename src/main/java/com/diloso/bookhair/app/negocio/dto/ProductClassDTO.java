package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;


public class ProductClassDTO implements Serializable {

	
	protected static final long serialVersionUID = 1L;

	protected Long id;
	
	protected Integer enabled;
	
	// Nombre de la categoria de producto en multiIdioma
	protected String pclNameMulti;

	// Nombre de la categoria de producto, solo para mostrar, no se guarda
	protected String pclName;

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

	public String getPclNameMulti() {
		return pclNameMulti;
	}

	public void setPclNameMulti(String pclNameMulti) {
		this.pclNameMulti = pclNameMulti;
	}

	public String getPclName() {
		return pclName;
	}

	public void setPclName(String pclName) {
		this.pclName = pclName;
	}

				
}
