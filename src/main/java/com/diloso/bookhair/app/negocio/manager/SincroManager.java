package com.diloso.bookhair.app.negocio.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.SincroDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.SincroDAO;
import com.diloso.bookhair.app.persist.entities.Sincro;
import com.diloso.bookhair.app.persist.mapper.SincroMapper;

@Component
@Scope(value = "singleton")
public class SincroManager implements ISincroManager {

	@Autowired
	private SincroDAO sincroDAO;
	
	@Autowired
	protected SincroMapper mapper;
	
	public SincroManager() {

	}

	@Override
	public SincroDTO create(SincroDTO sincroDTO) throws Exception {
		Sincro sincro = mapper.map(sincroDTO);
		sincro = sincroDAO.create(sincro);
		return mapper.map(sincro);
	}

	@Override
	public SincroDTO remove(long id) throws Exception {
		Sincro sincro = sincroDAO.get(id);
		sincroDAO.delete(id);
		if (sincro == null) {
			return null;
		}
		return mapper.map(sincro);
	}

	@Override
	public SincroDTO update(SincroDTO sincroDTO) throws Exception {
		Sincro sincro = mapper.map(sincroDTO);
		Sincro oldSincro = sincroDAO.get(sincroDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(sincro, oldSincro);
		} catch (Exception e) {
		}
		sincro = sincroDAO.update(sincro);
		if (sincro == null) {
			return null;
		}
		return mapper.map(sincro);
	}

	@Override
	public SincroDTO getById(long id) {
		Sincro sincro = sincroDAO.get(id);
		if (sincro == null) {
			return null;
		}
		return mapper.map(sincro);		
	}

}