package com.diloso.bookhair.app.persist.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


/**
 * The persistent class for the Calendar entity
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="getCalendar", query = "SELECT t FROM Calendar t WHERE t.calLocalId = :calLocalId and t.enabled =1 order by calName asc"),
	@NamedQuery(name="getCalendarAdmin", query = "SELECT t FROM Calendar t WHERE t.calLocalId = :calLocalId order by calName asc")
})
public class Calendar extends Resource implements Serializable {
	protected static final long serialVersionUID = 1L;

	protected String calName;
	
	protected String calDesc;
	
	protected Long calLocalId;
	
	protected Long calProfId;
	
	protected Long calSemanalDiaryId;
	
	protected List<Long> calLocalTasksId;

    public Calendar() {
    	
    }

  	public String getCalName() {
		return calName;
	}

	public void setCalName(String calName) {
		this.calName = calName;
	}

	public String getCalDesc() {
		return calDesc;
	}

	public void setCalDesc(String calDesc) {
		this.calDesc = calDesc;
	}
	
	public Long getCalLocalId() {
		return calLocalId;
	}

	public void setCalLocalId(Long calLocalId) {
		this.calLocalId = calLocalId;
	}
	
	public Long getCalProfId() {
		return calProfId;
	}

	public void setCalProfId(Long calProfId) {
		this.calProfId = calProfId;
	}

	public Long getCalSemanalDiaryId() {
		return calSemanalDiaryId;
	}

	public void setCalSemanalDiaryId(Long calSemanalDiaryId) {
		this.calSemanalDiaryId = calSemanalDiaryId;
	}
	
	public List<Long> getCalLocalTasksId() {
		return calLocalTasksId;
	}

	public void setCalLocalTasksId(List<Long> calLocalTasksId) {
		this.calLocalTasksId = calLocalTasksId;
	}


}