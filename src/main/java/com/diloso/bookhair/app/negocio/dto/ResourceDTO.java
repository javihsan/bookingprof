package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;

public class ResourceDTO implements Serializable {
	protected static final long serialVersionUID = 1L;

	protected Long id;
	
	protected Integer enabled;

	protected Long resFirId;
	
	public ResourceDTO() {
    }


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

	public Long getResFirId() {
		return resFirId;
	}

	public void setResFirId(Long resFirId) {
		this.resFirId = resFirId;
	}
	
   
}