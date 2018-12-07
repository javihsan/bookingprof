package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;
import java.util.Date;

public class AnnualDiaryDTO implements Serializable {
	
	protected static final long serialVersionUID = 1L;

	protected Long id;
	
	protected Integer enabled;
	
	protected Date anuDate;
	
	protected Long anuLocalId;
	
	protected Long anuCalendarId;
	// La fecha está cerrada
	protected Integer anuClosed;
	// La fecha tiene un horario de apertura distinto al default
	protected DiaryDTO anuDayDiary;
	
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

	public void setAnuDate(Date anuDate) {
		this.anuDate = anuDate;
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

	public Integer getAnuClosed() {
		return anuClosed;
	}

	public void setAnuClosed(Integer anuClosed) {
		this.anuClosed = anuClosed;
	}

	public DiaryDTO getAnuDayDiary() {
		return anuDayDiary;
	}

	public void setAnuDayDiary(DiaryDTO anuDayDiary) {
		this.anuDayDiary = anuDayDiary;
	}

	
	
}
