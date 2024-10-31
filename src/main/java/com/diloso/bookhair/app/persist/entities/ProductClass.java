package com.diloso.bookhair.app.persist.entities;

import com.diloso.bookhair.app.datastore.data.StorableWithModificationTimestamp;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * The persistent class for the ProductClass entity
 * 
 */
@Entity
@Cache
public class ProductClass extends StorableWithModificationTimestamp<Long> { 

	@Id
	protected Long id;
	
	@Index
	protected Integer enabled;
	
	protected String pclNameMulti;
	
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
	
	public String getPclNameMulti() {
		return pclNameMulti;
	}

	public void setPclNameMulti(String pclNameMulti) {
		this.pclNameMulti = pclNameMulti;
	}
	
	
}