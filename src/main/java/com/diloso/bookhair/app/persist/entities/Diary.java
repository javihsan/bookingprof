package com.diloso.bookhair.app.persist.entities;

import java.util.List;

import com.diloso.bookhair.app.datastore.data.StorableWithModificationTimestamp;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * The persistent class for the Diary entity
 * 
 */
@Entity
@Cache
public class Diary extends StorableWithModificationTimestamp<Long> { 
		
	@Id
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
