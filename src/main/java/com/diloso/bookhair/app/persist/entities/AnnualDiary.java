package com.diloso.bookhair.app.persist.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


/**
 * The persistent class for the AnnualDiary entity
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="getAnnualDiaryLocal", query = "SELECT t FROM AnnualDiary t WHERE t.anuLocalId = :anuLocalId and t.anuDate = :anuDate and t.enabled =1 order by t.id desc"),
	@NamedQuery(name="getAnnualDiaryCalendar", query = "SELECT t FROM AnnualDiary t WHERE t.anuCalendarId = :anuCalendarId and t.anuDate = :anuDate and t.enabled =1 order by t.id desc")
})
public class AnnualDiary implements Serializable {
	
	protected static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;
	
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
