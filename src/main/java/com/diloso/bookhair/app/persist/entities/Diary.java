package com.diloso.bookhair.app.persist.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * The persistent class for the Diary entity
 * 
 */
@Entity 
public class Diary implements Serializable {
	protected static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;
	
	protected Integer enabled;
	
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
