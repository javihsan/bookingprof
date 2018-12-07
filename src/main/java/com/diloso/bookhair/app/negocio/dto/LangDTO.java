package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;


public class LangDTO implements Serializable {
	
	protected static final long serialVersionUID = 1L;

	protected Long id;
	
	protected Integer enabled;
	
	// Nombre del idioma
	protected String lanName;
	
	// Code del idioma para luego Locale(String language, String country, String variant)
	protected String lanCode;
	
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
	
	public String getLanName() {
		return lanName;
	}

	public void setLanName(String lanName) {
		this.lanName = lanName;
	}

	public String getLanCode() {
		return lanCode;
	}

	public void setLanCode(String lanCode) {
		this.lanCode = lanCode;
	}
	
	
}
