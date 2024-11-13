package com.diloso.bookhair.app.negocio.dto.input;

import java.io.Serializable;
import java.util.List;



public class EventNewDTO implements Serializable {
	
	protected static final long serialVersionUID = 1L;
	
	protected Long localId;
	
	protected List<String> selectedCalendars;
	
	protected Long cliId;
	
	protected String cliEmail;
	
	protected String cliName;
	
	protected String cliTelf;
	
	protected String eveDescAlega;
	
	protected Long eveStartTime;
	
	protected List<Long> selectedTasks;
	
	protected List<Long> selectedTasksCount;
	
	protected Long celebrationDate;

	public Long getLocalId() {
		return localId;
	}

	public void setLocalId(Long localId) {
		this.localId = localId;
	}

	public List<String> getSelectedCalendars() {
		return selectedCalendars;
	}

	public void setSelectedCalendars(List<String> selectedCalendars) {
		this.selectedCalendars = selectedCalendars;
	}
	
	public Long getCliId() {
		return cliId;
	}

	public void setCliId(Long cliId) {
		this.cliId = cliId;
	}

	public String getCliEmail() {
		return cliEmail;
	}

	public void setCliEmail(String cliEmail) {
		this.cliEmail = cliEmail;
	}

	public String getCliName() {
		return cliName;
	}

	public void setCliName(String cliName) {
		this.cliName = cliName;
	}

	public String getCliTelf() {
		return cliTelf;
	}

	public void setCliTelf(String cliTelf) {
		this.cliTelf = cliTelf;
	}

	public String getEveDescAlega() {
		return eveDescAlega;
	}

	public void setEveDescAlega(String eveDescAlega) {
		this.eveDescAlega = eveDescAlega;
	}

	public Long getEveStartTime() {
		return eveStartTime;
	}

	public void setEveStartTime(Long eveStartTime) {
		this.eveStartTime = eveStartTime;
	}

	public List<Long> getSelectedTasks() {
		return selectedTasks;
	}

	public void setSelectedTasks(List<Long> selectedTasks) {
		this.selectedTasks = selectedTasks;
	}

	public List<Long> getSelectedTasksCount() {
		return selectedTasksCount;
	}

	public void setSelectedTasksCount(List<Long> selectedTasksCount) {
		this.selectedTasksCount = selectedTasksCount;
	}

	public Long getCelebrationDate() {
		return celebrationDate;
	}

	public void setCelebrationDate(Long celebrationDate) {
		this.celebrationDate = celebrationDate;
	}	

}	
