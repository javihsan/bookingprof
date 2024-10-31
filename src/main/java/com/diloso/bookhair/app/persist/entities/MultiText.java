package com.diloso.bookhair.app.persist.entities;

import com.diloso.bookhair.app.datastore.data.StorableWithModificationTimestamp;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * The persistent class for the MultiText entity
 * 
 */
@Entity
@Cache
public class MultiText extends StorableWithModificationTimestamp<Long> { 

	@Id
	protected Long id;
	
	@Index
	protected Integer enabled;
	
	protected String mulKey;
	
	protected String mulLanCode;
	
	protected String mulText;
	
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

	
}
