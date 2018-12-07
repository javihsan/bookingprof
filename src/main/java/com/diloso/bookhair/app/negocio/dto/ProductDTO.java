package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;


public class ProductDTO implements Serializable {

	protected static final long serialVersionUID = 1L;
	
	protected Long id;
	
	protected Integer enabled;
	
	// Local
	protected Long proLocalId;
	
	// Categoria del producto
	protected ProductClassDTO proClass;
	
	// Nombre del producto en multiIdioma
	protected String proNameMulti;
	
	// Tarifa aplicada
	protected Float proRate;
	
	// Nombre del producto, solo para mostrar, no se guarda
	protected String proName;

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

	public Long getProLocalId() {
		return proLocalId;
	}

	public void setProLocalId(Long proLocalId) {
		this.proLocalId = proLocalId;
	}

	public ProductClassDTO getProClass() {
		return proClass;
	}

	public void setProClass(ProductClassDTO proClass) {
		this.proClass = proClass;
	}

	public String getProNameMulti() {
		return proNameMulti;
	}

	public void setProNameMulti(String proNameMulti) {
		this.proNameMulti = proNameMulti;
	}

	public Float getProRate() {
		return proRate;
	}

	public void setProRate(Float proRate) {
		this.proRate = proRate;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	
	
}
