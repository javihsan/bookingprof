package com.diloso.bookhair.app.persist.mapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;
import com.diloso.bookhair.app.negocio.dto.ProfessionalDTO;
import com.diloso.bookhair.app.negocio.dto.RepeatDTO;
import com.diloso.bookhair.app.negocio.dto.SemanalDiaryDTO;
import com.diloso.bookhair.app.negocio.manager.ILocalTaskManager;
import com.diloso.bookhair.app.negocio.manager.IProfessionalManager;
import com.diloso.bookhair.app.negocio.manager.ISemanalDiaryManager;
import com.diloso.bookhair.app.persist.entities.Repeat;
import com.google.appengine.api.datastore.Entity;

@Component
@Scope(value = "singleton")
public class RepeatMapper {

	public RepeatMapper() {

	}

	@Autowired
	protected IProfessionalManager professionalManager;

	@Autowired
	protected ISemanalDiaryManager semanalDiaryManager;

	@Autowired
	protected ILocalTaskManager localTaskManager;
	
	public Repeat map(RepeatDTO repeat) {

		Repeat entityRepeat = new Repeat();

		try {
			PropertyUtils.copyProperties(entityRepeat, repeat);
		} catch (Exception e) {
		}

		if (repeat.getRepProf() != null) {
			entityRepeat.setRepProfId(repeat.getRepProf().getId());
		}
		if (repeat.getRepSemanalDiary() != null) {
			entityRepeat.setRepSemanalDiaryId(repeat.getRepSemanalDiary()
					.getId());
		}
		if (repeat.getEveLocalTask() != null) {
			entityRepeat.setEveLocalTaskId(repeat.getEveLocalTask().getId());
		}

		return entityRepeat;
	}

	public RepeatDTO map(Repeat entityRepeat) {

		RepeatDTO repeat = new RepeatDTO();

		try {
			PropertyUtils.copyProperties(repeat, entityRepeat);

			// Propiedades de Professional
			if (entityRepeat.getRepProfId() != null) {
				ProfessionalDTO profesional = professionalManager
						.getById(entityRepeat.getRepProfId());
				repeat.setRepProf(profesional);
			}

			// Propiedades de SemanalDiary
			if (entityRepeat.getRepSemanalDiaryId() != null) {
				SemanalDiaryDTO semanalDiary = semanalDiaryManager
						.getById(entityRepeat.getRepSemanalDiaryId());
				repeat.setRepSemanalDiary(semanalDiary);
			}

			// Propiedades de LocalTask
			Long eveLocalTaskId = entityRepeat.getEveLocalTaskId();
			if (eveLocalTaskId != null) {
				LocalTaskDTO localtask = localTaskManager.getById(eveLocalTaskId);
				repeat.setEveLocalTask(localtask);
			}

		} catch (Exception e) {
		}
		return repeat;
	}

	public RepeatDTO map(Entity entityRepeat) {

		RepeatDTO repeat = new RepeatDTO();

		try {
			repeat.setId(entityRepeat.getKey().getId());
			PropertyDescriptor[] pd = PropertyUtils
					.getPropertyDescriptors(RepeatDTO.class);
			Object value;
			for (PropertyDescriptor descriptor : pd) {
				Method writeMethod = PropertyUtils.getWriteMethod(descriptor);
				if (writeMethod != null) {
					value = entityRepeat.getProperty(descriptor.getName());
					if (value instanceof Long) {
						if (writeMethod.getParameterTypes()[0].getName()
								.equals("java.lang.Integer")) {
							value = new Integer(value.toString());
						}
					}
					if (value != null)
						writeMethod.invoke(repeat, value);
				}
			}
		} catch (Exception e) {
		}

		// Propiedades de Professional
		Long repProfId = (Long) entityRepeat.getProperty("repProfId");
		if (repProfId != null) {
			ProfessionalDTO profesional = professionalManager.getById(repProfId);
			repeat.setRepProf(profesional);
		}

		// Propiedades de SemanalDiary
		Long repSemanalDiaryId = (Long) entityRepeat
				.getProperty("repSemanalDiaryId");
		if (repSemanalDiaryId != null) {
			SemanalDiaryDTO semanalDiary = semanalDiaryManager
					.getById(repSemanalDiaryId);
			repeat.setRepSemanalDiary(semanalDiary);
		}

		// Propiedades de LocalTask
		Long eveLocalTaskId = (Long) entityRepeat.getProperty("eveLocalTaskId");
		if (eveLocalTaskId != null) {
			LocalTaskDTO localtask = localTaskManager.getById(eveLocalTaskId);
			repeat.setEveLocalTask(localtask);
		}
		return repeat;

	}
//
//	public void setProfessionalDAO(IProfessionalManager iProfessionalManager) {
//		this.professionalManager = iProfessionalManager;
//	}
//
//	public void setSemanalDiaryDAO(ISemanalDiaryManager iSemanalDiaryManager) {
//		this.semanalDiaryManager = iSemanalDiaryManager;
//	}
//
//	public void setLocalTaskDAO(ILocalTaskManager iLocalTaskManager) {
//		this.localTaskManager = iLocalTaskManager;
//	}

	
	
}
