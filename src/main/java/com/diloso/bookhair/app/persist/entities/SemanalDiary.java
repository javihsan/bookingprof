package com.diloso.bookhair.app.persist.entities;

import com.diloso.bookhair.app.datastore.data.StorableWithModificationTimestamp;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * The persistent class for the SemanalDiary entity
 * 
 */
@Entity
@Cache
public class SemanalDiary extends StorableWithModificationTimestamp<Long> { 
	
	@Id
	protected Long id;
	
	@Index
	protected Integer enabled;

	protected Long semMonDiaryId;

	protected Long semTueDiaryId;

	protected Long semWedDiaryId;

	protected Long semThuDiaryId;

	protected Long semFriDiaryId;

	protected Long semSatDiaryId;

	protected Long semSunDiaryId;

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

	public Long getSemMonDiaryId() {
		return semMonDiaryId;
	}

	public void setSemMonDiaryId(Long semMonDiaryId) {
		this.semMonDiaryId = semMonDiaryId;
	}

	public Long getSemTueDiaryId() {
		return semTueDiaryId;
	}

	public void setSemTueDiaryId(Long semTueDiaryId) {
		this.semTueDiaryId = semTueDiaryId;
	}

	public Long getSemWedDiaryId() {
		return semWedDiaryId;
	}

	public void setSemWedDiaryId(Long semWedDiaryId) {
		this.semWedDiaryId = semWedDiaryId;
	}

	public Long getSemThuDiaryId() {
		return semThuDiaryId;
	}

	public void setSemThuDiaryId(Long semThuDiaryId) {
		this.semThuDiaryId = semThuDiaryId;
	}

	public Long getSemFriDiaryId() {
		return semFriDiaryId;
	}

	public void setSemFriDiaryId(Long semFriDiaryId) {
		this.semFriDiaryId = semFriDiaryId;
	}

	public Long getSemSatDiaryId() {
		return semSatDiaryId;
	}

	public void setSemSatDiaryId(Long semSatDiaryId) {
		this.semSatDiaryId = semSatDiaryId;
	}

	public Long getSemSunDiaryId() {
		return semSunDiaryId;
	}

	public void setSemSunDiaryId(Long semSunDiaryId) {
		this.semSunDiaryId = semSunDiaryId;
	}

		
}
