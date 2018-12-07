package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;
import java.util.List;

public class DiaryDTO implements Serializable {
	
	protected static final long serialVersionUID = 1L;

	protected Long id;
	
	protected Integer enabled;
	
	// Intervalos de horario de apertura para el dia
	protected List<String> diaTimes;
	
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

	public List<String> getDiaTimes() {
		return diaTimes;
	}

	public void setDiaTimes(List<String> diaTimes) {
		this.diaTimes = diaTimes;
	}

	
}
