package com.diloso.bookhair.app.persist.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.LangDTO;
import com.diloso.bookhair.app.negocio.dto.LocalDTO;
import com.diloso.bookhair.app.negocio.dto.ProfessionalDTO;
import com.diloso.bookhair.app.negocio.dto.SemanalDiaryDTO;
import com.diloso.bookhair.app.negocio.dto.SincroDTO;
import com.diloso.bookhair.app.negocio.dto.WhereDTO;
import com.diloso.bookhair.app.negocio.manager.ILangManager;
import com.diloso.bookhair.app.negocio.manager.IProfessionalManager;
import com.diloso.bookhair.app.negocio.manager.ISemanalDiaryManager;
import com.diloso.bookhair.app.negocio.manager.ISincroManager;
import com.diloso.bookhair.app.negocio.manager.IWhereManager;
import com.diloso.bookhair.app.persist.entities.Local;

@Component
@Scope(value = "singleton")
public class LocalMapper {
	
	public LocalMapper() {

	}

	@Autowired
	protected IWhereManager whereManager;
	
	@Autowired
	protected ISemanalDiaryManager semanalDiaryManager;
	
	@Autowired
	protected ILangManager langManager;
	
	@Autowired
	protected IProfessionalManager professionalManager;	

	@Autowired
	protected ISincroManager sincroManager;	
	
	public Local map(LocalDTO local) {

		Local entityLocal = new Local();

		try {
			PropertyUtils.copyProperties(entityLocal, local);
		} catch (Exception e) {
		}

		if (local.getLocSemanalDiary()!=null){
			entityLocal.setLocSemanalDiaryId(local.getLocSemanalDiary().getId());
		}
		
		if (local.getLocWhere()!=null){
			entityLocal.setLocWhereId(local.getLocWhere().getId());
		}
		
		if (local.getLocLangs()!=null){
			List<Long> listLangs = new ArrayList<Long>();
			for (LangDTO lang : local.getLocLangs()) {
				listLangs.add(lang.getId());
			}
			entityLocal.setLocLangsId(listLangs);
		}
		
		if (local.getLocRespon()!=null){
			entityLocal.setLocResponId(local.getLocRespon().getId());
		}
		
		if (local.getLocSinGCalendar()!=null){
			entityLocal.setLocSinGCalendarId(local.getLocSinGCalendar().getId());
		}
		
		if (local.getLocSinMChimp()!=null){
			entityLocal.setLocSinMChimpId(local.getLocSinMChimp().getId());
		}
		
		return entityLocal;
	}

	public LocalDTO map(Local entityLocal) {

		LocalDTO local = new LocalDTO();

		try {
			PropertyUtils.copyProperties(local, entityLocal);
		} catch (Exception e) {
		}
		if (entityLocal!=null){
			// Propiedades de SemanalDiary
			if (entityLocal.getLocSemanalDiaryId() != null) {
				SemanalDiaryDTO semanalDiary = semanalDiaryManager
						.getById(entityLocal.getLocSemanalDiaryId());
				local.setLocSemanalDiary(semanalDiary);
			}
			
			// Propiedades de Where
			if (entityLocal.getLocWhereId() != null) {
				WhereDTO where = whereManager
						.getById(entityLocal.getLocWhereId());
				local.setLocWhere(where);
			}
			
			// Propiedades de Lang
			if (entityLocal.getLocLangsId() != null) {
				List<LangDTO> listLangs = new ArrayList<LangDTO>();
				for (Long id: entityLocal.getLocLangsId()) {
					LangDTO lang = langManager.getById(id);
					listLangs.add(lang);
				}
				local.setLocLangs(listLangs);
			}
			
			// Propiedades de Respon
			if (entityLocal.getLocResponId() != null) {
				ProfessionalDTO respon = professionalManager
						.getById(entityLocal.getLocResponId());
				local.setLocRespon(respon);
			}
			
			// Propiedades de SinGCalendar
			if (entityLocal.getLocSinGCalendarId() != null) {
				SincroDTO sincro = sincroManager
						.getById(entityLocal.getLocSinGCalendarId());
				local.setLocSinGCalendar(sincro);
			}
			
			// Propiedades de SinMChimp
			if (entityLocal.getLocSinMChimpId() != null) {
				SincroDTO sincro = sincroManager
						.getById(entityLocal.getLocSinMChimpId());
				local.setLocSinMChimp(sincro);
			}

		}
		return local;
	}
//
//	public void setWhereDAO(IWhereManager iWhereManager) {
//		this.whereManager = iWhereManager;
//	}
//
//	public void setSemanalDiaryDAO(ISemanalDiaryManager iSemanalDiaryManager) {
//		this.semanalDiaryManager = iSemanalDiaryManager;
//	}
//
//	public void setLangDAO(ILangManager iLangManager) {
//		this.langManager = iLangManager;
//	}
//
//	public void setProfessionalDAO(IProfessionalManager iProfessionalManager) {
//		this.professionalManager = iProfessionalManager;
//	}
//
//	public void setSincroDAO(ISincroManager iSincroManager) {
//		this.sincroManager = iSincroManager;
//	}

	
	
}
