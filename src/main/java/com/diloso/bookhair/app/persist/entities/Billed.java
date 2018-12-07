package com.diloso.bookhair.app.persist.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * The persistent class for the Billed entity
 * 
 */
@Entity
public class Billed implements Serializable {
	
	protected static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;
	
	protected Integer enabled;
	
	protected Long bilCalendarId;

	protected Long bilClientId;

	protected Long bilLocalTaskId;
	
	protected Long bilProductId;

	protected Date bilTime;
	
	protected Float bilRate;

	protected Long bilInvoiceId;

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

	public Long getBilCalendarId() {
		return bilCalendarId;
	}

	public void setBilCalendarId(Long bilCalendarId) {
		this.bilCalendarId = bilCalendarId;
	}

	public Long getBilClientId() {
		return bilClientId;
	}

	public void setBilClientId(Long bilClientId) {
		this.bilClientId = bilClientId;
	}

	public Long getBilLocalTaskId() {
		return bilLocalTaskId;
	}

	public void setBilLocalTaskId(Long bilLocalTaskId) {
		this.bilLocalTaskId = bilLocalTaskId;
	}

	public Long getBilProductId() {
		return bilProductId;
	}

	public void setBilProductId(Long bilProductId) {
		this.bilProductId = bilProductId;
	}

	public Date getBilTime() {
		return bilTime;
	}

	public void setBilTime(Date bilTime) {
		this.bilTime = bilTime;
	}

	public Float getBilRate() {
		return bilRate;
	}

	public void setBilRate(Float bilRate) {
		this.bilRate = bilRate;
	}

	public Long getBilInvoiceId() {
		return bilInvoiceId;
	}

	public void setBilInvoiceId(Long bilInvoiceId) {
		this.bilInvoiceId = bilInvoiceId;
	}
	
       
}