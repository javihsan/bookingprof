package com.diloso.bookhair.app.persist.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * The persistent class for the Event entity
 * 
 */
@Entity
public class Event implements Serializable {
	protected static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;

	protected Integer enabled;

	protected String eveDesc;
	
	protected Long eveCalendarId;
	
	protected Long eveClientId;
	
	protected Integer eveBooking;
	
	protected Date eveBookingTime;
	
	protected Date eveStartTime;

	protected Date eveEndTime;
	
	protected Integer eveConsumed;

	protected Integer eveNotified;
	
	protected Long eveLocalTaskId;
	
	protected String eveICS;
	
	protected String eveIDGCalendar;
	
	/**** Campos extra ficha  **********/
	protected Date eveCelebrationTime;
	 	
    public Event() {
    	
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
	
	public String getEveDesc() {
		return eveDesc;
	}

	public void setEveDesc(String eveDesc) {
		this.eveDesc = eveDesc;
	}
	
	public Long getEveCalendarId() {
		return eveCalendarId;
	}

	public void setEveCalendarId(Long eveCalendarId) {
		this.eveCalendarId = eveCalendarId;
	}

	public Long getEveClientId() {
		return eveClientId;
	}

	public void setEveClientId(Long eveClientId) {
		this.eveClientId = eveClientId;
	}
	
	public Integer getEveBooking() {
		return eveBooking;
	}

	public void setEveBooking(Integer eveBooking) {
		this.eveBooking = eveBooking;
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

	public Integer getEveNotified() {
		return eveNotified;
	}

	public void setEveNotified(Integer eveNotified) {
		this.eveNotified = eveNotified;
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

	public Date getEveCelebrationTime() {
		return eveCelebrationTime;
	}

	public void setEveCelebrationTime(Date eveCelebrationTime) {
		this.eveCelebrationTime = eveCelebrationTime;
	}
	
}