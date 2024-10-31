package com.diloso.bookhair.app.persist.entities;

import com.diloso.bookhair.app.datastore.data.StorableWithModificationTimestamp;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * The persistent class for the Product entity
 * 
 */
@Entity
@Cache
public class Product extends StorableWithModificationTimestamp<Long> { 

	@Id
	protected Long id;
	
	@Index
	protected Integer enabled;
	
	protected Long proLocalId;
	
	protected Long proClassId;
		
	protected String proNameMulti;
	
	protected Float proRate;
	
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
	
	public Long getProClassId() {
		return proClassId;
	}

	public void setProClassId(Long proClassId) {
		this.proClassId = proClassId;
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

		
}
