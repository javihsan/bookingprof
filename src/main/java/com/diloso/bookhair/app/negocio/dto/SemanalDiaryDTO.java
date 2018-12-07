package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SemanalDiaryDTO implements Serializable {

	
	protected static final long serialVersionUID = 1L;

	protected Long id;
	
	protected Integer enabled;
	
	// Horarios de apertura para el Mon
	protected DiaryDTO semMonDiary;
	
	// Horarios de apertura para el Tue
	protected DiaryDTO semTueDiary;

	// Horarios de apertura para el Wed
	protected DiaryDTO semWedDiary;

	// Horarios de apertura para el Thu
	protected DiaryDTO semThuDiary;

	// Horarios de apertura para el Fri
	protected DiaryDTO semFriDiary;

	// Horarios de apertura para el Sat
	protected DiaryDTO semSatDiary;

	// Horarios de apertura para el Sun
	protected DiaryDTO semSunDiary;

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

	public DiaryDTO getSemMonDiary() {
		return semMonDiary;
	}

	public void setSemMonDiary(DiaryDTO semMonDiary) {
		this.semMonDiary = semMonDiary;
	}

	public DiaryDTO getSemTueDiary() {
		return semTueDiary;
	}

	public void setSemTueDiary(DiaryDTO semTueDiary) {
		this.semTueDiary = semTueDiary;
	}

	public DiaryDTO getSemWedDiary() {
		return semWedDiary;
	}

	public void setSemWedDiary(DiaryDTO semWedDiary) {
		this.semWedDiary = semWedDiary;
	}

	public DiaryDTO getSemThuDiary() {
		return semThuDiary;
	}

	public void setSemThuDiary(DiaryDTO semThuDiary) {
		this.semThuDiary = semThuDiary;
	}

	public DiaryDTO getSemFriDiary() {
		return semFriDiary;
	}

	public void setSemFriDiary(DiaryDTO semFriDiary) {
		this.semFriDiary = semFriDiary;
	}

	public DiaryDTO getSemSatDiary() {
		return semSatDiary;
	}

	public void setSemSatDiary(DiaryDTO semSatDiary) {
		this.semSatDiary = semSatDiary;
	}

	public DiaryDTO getSemSunDiary() {
		return semSunDiary;
	}

	public void setSemSunDiary(DiaryDTO semSunDiary) {
		this.semSunDiary = semSunDiary;
	}
	
	public DiaryDTO getDiary(int day){
		switch(day) { 
			case 0: return getSemMonDiary();
			case 1: return getSemTueDiary();
			case 2: return getSemWedDiary();
			case 3: return getSemThuDiary();
			case 4: return getSemFriDiary();
			case 5: return getSemSatDiary();
			case 6: return getSemSunDiary();
			default: return null;
		}
	}
	
	public List<Integer> getClosedDiary(){
		List<Integer> result = new ArrayList<Integer>();
		if (getSemMonDiary()==null || getSemMonDiary().getDiaTimes()==null) result.add(0);
		if (getSemTueDiary()==null || getSemTueDiary().getDiaTimes()==null) result.add(1);
		if (getSemWedDiary()==null || getSemWedDiary().getDiaTimes()==null) result.add(2);
		if (getSemThuDiary()==null || getSemThuDiary().getDiaTimes()==null) result.add(3);
		if (getSemFriDiary()==null || getSemFriDiary().getDiaTimes()==null) result.add(4);
		if (getSemSatDiary()==null || getSemSatDiary().getDiaTimes()==null) result.add(5);
		if (getSemSunDiary()==null || getSemSunDiary().getDiaTimes()==null) result.add(6);

		return result;
	}
	
}
