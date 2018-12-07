package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;


public class MultiTextDTO implements Serializable {

	protected static final long serialVersionUID = 1L;

	protected Long id;
	
	protected Integer enabled;
	
	// key del texto
	protected String mulKey;
	
	// Code del idioma del texto
	protected String mulLanCode;
	
	// Texto
	protected String mulText;
	
	// Name del idioma del texto, solo para mostrar, no se guarda
	protected String mulLanName;

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
	
	public String getMulKey() {
		return mulKey;
	}

	public void setMulKey(String mulKey) {
		this.mulKey = mulKey;
	}

	public String getMulLanCode() {
		return mulLanCode;
	}

	public void setMulLanCode(String mulLanCode) {
		this.mulLanCode = mulLanCode;
	}

	public String getMulText() {
		return mulText;
	}

	public void setMulText(String mulText) {
		this.mulText = mulText;
	}

	public String getMulLanName() {
		return mulLanName;
	}

	public void setMulLanName(String mulLanName) {
		this.mulLanName = mulLanName;
	}
	
		
}
