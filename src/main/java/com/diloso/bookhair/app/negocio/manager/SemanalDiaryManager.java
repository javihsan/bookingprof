package com.diloso.bookhair.app.negocio.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.SemanalDiaryDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.SemanalDiaryDAO;
import com.diloso.bookhair.app.persist.entities.SemanalDiary;
import com.diloso.bookhair.app.persist.mapper.SemanalDiaryMapper;

@Component
@Scope(value = "singleton")
public class SemanalDiaryManager implements ISemanalDiaryManager {

	@Autowired
	private SemanalDiaryDAO semanalDiaryDAO;
		
	@Autowired
	protected SemanalDiaryMapper mapper;
	
	public SemanalDiaryManager() {

	}

	@Override
	public SemanalDiaryDTO create(SemanalDiaryDTO semanalDiaryDTO) throws Exception {
		SemanalDiary semanalDiary = mapper.map(semanalDiaryDTO);
		semanalDiary = semanalDiaryDAO.create(semanalDiary);
		return mapper.map(semanalDiary);
	}

	@Override
	public SemanalDiaryDTO remove(long id) throws Exception {
		SemanalDiary semanalDiary = semanalDiaryDAO.get(id);
		semanalDiaryDAO.delete(id);
		if (semanalDiary == null) {
			return null;
		}
		return mapper.map(semanalDiary);
	}

	@Override
	public SemanalDiaryDTO update(SemanalDiaryDTO semanalDiaryDTO) throws Exception {
		SemanalDiary semanalDiary = mapper.map(semanalDiaryDTO);
		SemanalDiary oldSemanalDiary = semanalDiaryDAO.get(semanalDiaryDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(semanalDiary, oldSemanalDiary);
		} catch (Exception e) {
		}
		semanalDiary = semanalDiaryDAO.update(semanalDiary);
		if (semanalDiary == null) {
			return null;
		}
		return mapper.map(semanalDiary);
	}

	@Override
	public SemanalDiaryDTO getById(long id) {
		SemanalDiary semanalDiary = semanalDiaryDAO.get(id);
		if (semanalDiary == null) {
			return null;
		}
		return mapper.map(semanalDiary);		
	}
	
}