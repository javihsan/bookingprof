package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;
import java.util.Date;


public class AppointmentDTO implements Serializable {
	protected static final long serialVersionUID = 1L;

	protected String apoName;
	
	protected Date apoStartTime;
	
	protected Long apoCalendarId;
	
	protected String apoCalendarName;
	
	public AppointmentDTO() {
    }

	public String getApoName() {
		return apoName;
	}

	public void setApoName(String apoName) {
		this.apoName = apoName;
	}

	public Date getApoStartTime() {
		return apoStartTime;
	}

	public void setApoStartTime(Date apoStartTime) {
		this.apoStartTime = apoStartTime;
	}

	public Long getApoCalendarId() {
		return apoCalendarId;
	}

	public void setApoCalendarId(Long apoCalendarId) {
		this.apoCalendarId = apoCalendarId;
	}

	public String getApoCalendarName() {
		return apoCalendarName;
	}

	public void setApoCalendarName(String apoCalendarName) {
		this.apoCalendarName = apoCalendarName;
	}
	
	
	
}