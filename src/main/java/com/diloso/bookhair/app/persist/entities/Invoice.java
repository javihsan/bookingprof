package com.diloso.bookhair.app.persist.entities;

import java.util.Date;

import com.diloso.bookhair.app.datastore.data.StorableWithModificationTimestamp;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


/**
 * The persistent class for the Invoice entity
 * 
 */
@Entity
@Cache
public class Invoice extends StorableWithModificationTimestamp<Long> { 
	
	@Id
	protected Long id;
	
	@Index
	protected Integer enabled;

	protected Long invLocalId;
	
	protected String invDesc;

	protected Long invClientId;

	protected Date invIssueTime;

	protected Date invTime;

	protected Float invRate;
	
    public Invoice() {
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
	
	public Long getInvLocalId() {
		return invLocalId;
	}

	public void setInvLocalId(Long invLocalId) {
		this.invLocalId = invLocalId;
	}
	
	public String getInvDesc() {
		return invDesc;
	}

	public void setInvDesc(String invDesc) {
		this.invDesc = invDesc;
	}

	public Long getInvClientId() {
		return invClientId;
	}

	public void setInvClientId(Long invClientId) {
		this.invClientId = invClientId;
	}

	public Date getInvIssueTime() {
		return invIssueTime;
	}

	public void setInvIssueTime(Date invIssueTime) {
		this.invIssueTime = invIssueTime;
	}

	public Date getInvTime() {
		return invTime;
	}

	public void setInvTime(Date invTime) {
		this.invTime = invTime;
	}

	public Float getInvRate() {
		return invRate;
	}

	public void setInvRate(Float invRate) {
		this.invRate = invRate;
	}
    
    			
}