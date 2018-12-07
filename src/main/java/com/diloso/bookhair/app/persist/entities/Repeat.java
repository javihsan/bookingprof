package com.diloso.bookhair.app.persist.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * The persistent class for the Repeat entity
 * 
 */
@Entity
public class Repeat implements Serializable {
	protected static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;

	protected Integer enabled;
	
	protected Long eveCalendarId;
	
	protected Date eveBookingTime;
	
	protected Date eveStartTime;

	protected Date eveEndTime;
	
	protected Integer eveConsumed;

	protected Long eveLocalTaskId;
	
	protected String eveICS;
	
	protected String eveIDGCalendar;
	
	protected String repNameMulti;
	
	protected String repMatrix;
	
	protected Integer repType;
	
	protected Long repProfId;
	
	protected Integer repMaxClients;
	
	protected Long repSemanalDiaryId;
	
    public Repeat() {
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
    	
  	public Long getEveCalendarId() {
  		return eveCalendarId;
  	}

  	public void setEveCalendarId(Long eveCalendarId) {
  		this.eveCalendarId = eveCalendarId;
  	}

  	public Date getEveBookingTime() {
  		return eveBookingTime;
  	}

  	public void setEveBookingTime(Date eveBookingTime) {
  		this.eveBookingTime = eveBookingTime;
  	}
  	
  	public Date getEveStartTime() {
  		return eveStartTime;
  	}
  	
  	public void setEveStartTime(Date eveStartTime) {
  		this.eveStartTime = eveStartTime;
  	}
  	
  	public Date getEveEndTime() {
  		return eveEndTime;
  	}

  	public void setEveEndTime(Date eveEndTime) {
  		this.eveEndTime = eveEndTime;
  	}
  	
  	public Integer getEveConsumed() {
  		return eveConsumed;
  	}

  	public void setEveConsumed(Integer eveConsumed) {
  		this.eveConsumed = eveConsumed;
  	}

   	public Long getEveLocalTaskId() {
  		return eveLocalTaskId;
  	}

  	public void setEveLocalTaskId(Long eveLocalTaskId) {
  		this.eveLocalTaskId = eveLocalTaskId;
  	}

  	public String getEveICS() {
  		return eveICS;
  	}

  	public void setEveICS(String eveICS) {
  		this.eveICS = eveICS;
  	}
  	
  	public String getEveIDGCalendar() {
  		return eveIDGCalendar;
  	}

  	public void setEveIDGCalendar(String eveIDGCalendar) {
  		this.eveIDGCalendar = eveIDGCalendar;
  	}
    
	public String getRepNameMulti() {
		return repNameMulti;
	}

	public void setRepNameMulti(String repNameMulti) {
		this.repNameMulti = repNameMulti;
	}

	public String getRepMatrix() {
		return repMatrix;
	}

	public void setRepMatrix(String repMatrix) {
		this.repMatrix = repMatrix;
	}

	public Integer getRepType() {
		return repType;
	}

	public void setRepType(Integer repType) {
		this.repType = repType;
	}

	public Long getRepProfId() {
		return repProfId;
	}

	public void setRepProfId(Long repProfId) {
		this.repProfId = repProfId;
	}

	public Integer getRepMaxClients() {
		return repMaxClients;
	}

	public void setRepMaxClients(Integer repMaxClients) {
		this.repMaxClients = repMaxClients;
	}

	public Long getRepSemanalDiaryId() {
		return repSemanalDiaryId;
	}

	public void setRepSemanalDiaryId(Long repSemanalDiaryId) {
		this.repSemanalDiaryId = repSemanalDiaryId;
	}

	     	
}