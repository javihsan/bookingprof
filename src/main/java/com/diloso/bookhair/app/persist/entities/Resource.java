package com.diloso.bookhair.app.persist.entities;

import com.diloso.bookhair.app.datastore.data.StorableWithModificationTimestamp;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
/**
 * The persistent class for the Resource entity
 * 
 */
@Entity
@Cache
public class Resource extends StorableWithModificationTimestamp<Long> { 
	
	@Id
	protected Long id;
	
	@Index
	protected Integer enabled;

	@Index
	protected Long resFirId;
	
	public Resource() {
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