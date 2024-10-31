package com.diloso.bookhair.app.persist.entities;

import com.diloso.bookhair.app.datastore.data.StorableWithModificationTimestamp;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * The persistent class for the Task entity
 * 
 */
@Entity
@Cache
public class Task extends StorableWithModificationTimestamp<Long> { 

	@Id
	protected Long id;
	
	@Index
	protected Integer enabled;
	
	protected Long tasClassId;
	
	protected String tasNameMulti;
	
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
	
	public Long getTasClassId() {
		return tasClassId;
	}

	public void setTasClassId(Long tasClassId) {
		this.tasClassId = tasClassId;
	}

	public String getTasNameMulti() {
		return tasNameMulti;
	}

	public void setTasNameMulti(String tasNameMulti) {
		this.tasNameMulti = tasNameMulti;
	}
}
