package com.diloso.bookhair.app.persist.entities;

import java.util.Date;

import com.diloso.bookhair.app.datastore.data.StorableWithModificationTimestamp;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


/**
 * The persistent class for the AnnualDiary entity
 * 
 */
@Entity
@Cache
public class AnnualDiary extends StorableWithModificationTimestamp<Long> {  
		
	@Id
	protected Long id;
	
	@Index
	protected Integer enabled;
	
	protected Date anuDate;
	
	protected Long anuLocalId;
	
	protected Long anuCalendarId;
	
	protected Integer anuClosed;

	protected Long anuDayDiaryId;
	
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

	public Date getAnuDate() {
		return anuDate;
	}	
	
	public Long getAnuLocalId() {
		return anuLocalId;
	}

	public void setAnuLocalId(Long anuLocalId) {
		this.anuLocalId = anuLocalId;
	}
	
	public Long getAnuCalendarId() {
		return anuCalendarId;
	}

	public void setAnuCalendarId(Long anuCalendarId) {
		this.anuCalendarId = anuCalendarId;
	}



	public void setAnuDate(Date anuDate) {
		this.anuDate = anuDate;
	}

	public Integer getAnuClosed() {
		return anuClosed;
	}

	public void setAnuClosed(Integer anuClosed) {
		this.anuClosed = anuClosed;
	}

	public Long getAnuDayDiaryId() {
		return anuDayDiaryId;
	}

	public void setAnuDayDiaryId(Long anuDayDiaryId) {
		this.anuDayDiaryId = anuDayDiaryId;
	}

	
	
}
