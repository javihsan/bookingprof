package com.diloso.bookhair.app.persist.entities;

import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;

/**
 * The persistent class for the RepeatClient entity
 * 
 */
@Entity
@Cache
public class RepeatClient extends Resource { 
	
	protected Long recRepeatId;

	protected Long recClientId;

	protected Date recBookingTime;
	
	protected Integer recBooking;
	
	public RepeatClient() {
    }

	public Long getRecRepeatId() {
		return recRepeatId;
	}

	public void setRecRepeatId(Long recRepeatId) {
		this.recRepeatId = recRepeatId;
	}

	public Long getRecClientId() {
		return recClientId;
	}

	public void setRecClientId(Long recClientId) {
		this.recClientId = recClientId;
	}

	public Date getRecBookingTime() {
		return recBookingTime;
	}

	public void setRecBookingTime(Date recBookingTime) {
		this.recBookingTime = recBookingTime;
	}

	public Integer getRecBooking() {
		return recBooking;
	}

	public void setRecBooking(Integer recBooking) {
		this.recBooking = recBooking;
	}
	
				  
}