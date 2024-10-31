package com.diloso.bookhair.app.persist.mapper;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.DiaryDTO;
import com.diloso.bookhair.app.negocio.dto.SemanalDiaryDTO;
import com.diloso.bookhair.app.negocio.manager.IDiaryManager;
import com.diloso.bookhair.app.persist.entities.SemanalDiary;


@Component
@Scope(value = "singleton")
public class SemanalDiaryMapper {
	
	public SemanalDiaryMapper() {

	}
	
	@Autowired
	protected IDiaryManager diaryManager;
	
	
	public SemanalDiary map(SemanalDiaryDTO semanalDiary){
		
		SemanalDiary entitySemanalDiary = new SemanalDiary();
		
		try {
			PropertyUtils.copyProperties(entitySemanalDiary, semanalDiary);
		} catch (Exception e) {
		} 
		
		if (semanalDiary.getSemMonDiary()!=null){
			entitySemanalDiary.setSemMonDiaryId(semanalDiary.getSemMonDiary().getId());
		}
		if (semanalDiary.getSemTueDiary()!=null){
			entitySemanalDiary.setSemTueDiaryId(semanalDiary.getSemTueDiary().getId());
		}
		if (semanalDiary.getSemWedDiary()!=null){
			entitySemanalDiary.setSemWedDiaryId(semanalDiary.getSemWedDiary().getId());
		}
		if (semanalDiary.getSemThuDiary()!=null){
			entitySemanalDiary.setSemThuDiaryId(semanalDiary.getSemThuDiary().getId());
		}
		if (semanalDiary.getSemFriDiary()!=null){
			entitySemanalDiary.setSemFriDiaryId(semanalDiary.getSemFriDiary().getId());
		}
		if (semanalDiary.getSemSatDiary()!=null){
			entitySemanalDiary.setSemSatDiaryId(semanalDiary.getSemSatDiary().getId());
		}
		if (semanalDiary.getSemSunDiary()!=null){
			entitySemanalDiary.setSemSunDiaryId(semanalDiary.getSemSunDiary().getId());
		}
		return entitySemanalDiary;
	}
	
	public SemanalDiaryDTO map(SemanalDiary entitySemanalDiary) {

		SemanalDiaryDTO semanalDiary = new SemanalDiaryDTO();

		try {
			PropertyUtils.copyProperties(semanalDiary, entitySemanalDiary);
		
			// Propiedades de Diary
			if (entitySemanalDiary.getSemMonDiaryId() != null) {
				DiaryDTO diary = diaryManager
						.getById(entitySemanalDiary.getSemMonDiaryId());
				semanalDiary.setSemMonDiary(diary);
			}
			if (entitySemanalDiary.getSemTueDiaryId() != null) {
				DiaryDTO diary = diaryManager
						.getById(entitySemanalDiary.getSemTueDiaryId());
				semanalDiary.setSemTueDiary(diary);
			}
			if (entitySemanalDiary.getSemWedDiaryId() != null) {
				DiaryDTO diary = diaryManager
						.getById(entitySemanalDiary.getSemWedDiaryId());
				semanalDiary.setSemWedDiary(diary);
			}
			if (entitySemanalDiary.getSemThuDiaryId() != null) {
				DiaryDTO diary = diaryManager
						.getById(entitySemanalDiary.getSemThuDiaryId());
				semanalDiary.setSemThuDiary(diary);
			}
			if (entitySemanalDiary.getSemFriDiaryId() != null) {
				DiaryDTO diary = diaryManager
						.getById(entitySemanalDiary.getSemFriDiaryId());
				semanalDiary.setSemFriDiary(diary);
			}
			if (entitySemanalDiary.getSemSatDiaryId() != null) {
				DiaryDTO diary = diaryManager
						.getById(entitySemanalDiary.getSemSatDiaryId());
				semanalDiary.setSemSatDiary(diary);
			}
			if (entitySemanalDiary.getSemSunDiaryId() != null) {
				DiaryDTO diary = diaryManager
						.getById(entitySemanalDiary.getSemSunDiaryId());
				semanalDiary.setSemSunDiary(diary);
			}
		} catch (Exception e) {
		}
		return semanalDiary;
	}
	
//	public void setDiaryDAO(IDiaryManager iDiaryManager) {
//		this.diaryManager = iDiaryManager;
//	}
//	
}
