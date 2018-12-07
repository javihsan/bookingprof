package com.diloso.bookhair.app.persist.transformer;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.LocalTaskDAO;
import com.diloso.bookhair.app.negocio.dao.ProfessionalDAO;
import com.diloso.bookhair.app.negocio.dao.SemanalDiaryDAO;
import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;
import com.diloso.bookhair.app.negocio.dto.ProfessionalDTO;
import com.diloso.bookhair.app.negocio.dto.RepeatDTO;
import com.diloso.bookhair.app.negocio.dto.SemanalDiaryDTO;
import com.diloso.bookhair.app.persist.entities.Repeat;
import com.google.appengine.api.datastore.Entity;

@Component
@Scope(value = "singleton")
public class RepeatTransformer {

	public RepeatTransformer() {

	}

	@Autowired
	protected ProfessionalDAO professionalDAO;

	@Autowired
	protected SemanalDiaryDAO semanalDiaryDAO;

	@Autowired
	protected LocalTaskDAO localTaskDAO;
	
	public Repeat transformDTOToEntity(RepeatDTO repeat) {

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

	public RepeatDTO transformEntityToDTO(Repeat entityRepeat) {

		RepeatDTO repeat = new RepeatDTO();

		try {
			PropertyUtils.copyProperties(repeat, entityRepeat);

			// Propiedades de Professional
			if (entityRepeat.getRepProfId() != null) {
				ProfessionalDTO profesional = professionalDAO
						.getById(entityRepeat.getRepProfId());
				repeat.setRepProf(profesional);
			}

			// Propiedades de SemanalDiary
			if (entityRepeat.getRepSemanalDiaryId() != null) {
				SemanalDiaryDTO semanalDiary = semanalDiaryDAO
						.getById(entityRepeat.getRepSemanalDiaryId());
				repeat.setRepSemanalDiary(semanalDiary);
			}

			// Propiedades de LocalTask
			Long eveLocalTaskId = entityRepeat.getEveLocalTaskId();
			if (eveLocalTaskId != null) {
				LocalTaskDTO localtask = localTaskDAO.getById(eveLocalTaskId);
				repeat.setEveLocalTask(localtask);
			}

		} catch (Exception e) {
		}
		return repeat;
	}

	public RepeatDTO transformEntityToDTO(Entity entityRepeat) {

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
			ProfessionalDTO profesional = professionalDAO.getById(repProfId);
			repeat.setRepProf(profesional);
		}

		// Propiedades de SemanalDiary
		Long repSemanalDiaryId = (Long) entityRepeat
				.getProperty("repSemanalDiaryId");
		if (repSemanalDiaryId != null) {
			SemanalDiaryDTO semanalDiary = semanalDiaryDAO
					.getById(repSemanalDiaryId);
			repeat.setRepSemanalDiary(semanalDiary);
		}

		// Propiedades de LocalTask
		Long eveLocalTaskId = (Long) entityRepeat.getProperty("eveLocalTaskId");
		if (eveLocalTaskId != null) {
			LocalTaskDTO localtask = localTaskDAO.getById(eveLocalTaskId);
			repeat.setEveLocalTask(localtask);
		}
		return repeat;

	}

}
