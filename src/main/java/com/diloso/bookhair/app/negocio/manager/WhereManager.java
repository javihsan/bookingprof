package com.diloso.bookhair.app.negocio.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.WhereDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.WhereDAO;
import com.diloso.bookhair.app.persist.entities.Where;
import com.diloso.bookhair.app.persist.mapper.WhereMapper;

@Component
@Scope(value = "singleton")
public class WhereManager implements IWhereManager {

	@Autowired
	private WhereDAO whereDAO;
	
	@Autowired
	protected WhereMapper mapper;
	
	public WhereManager() {
		if (mapper==null){
			mapper = new WhereMapper();
		}
	}

	@Override
	public WhereDTO create(WhereDTO whereDTO) throws Exception {
		Where where = mapper.map(whereDTO);
		where = whereDAO.create(where);
		return mapper.map(where);
	}

	@Override
	public WhereDTO remove(long id) throws Exception {
		Where where = whereDAO.get(id);
		whereDAO.delete(id);
		if (where == null) {
			return null;
		}
		return mapper.map(where);
	}

	@Override
	public WhereDTO update(WhereDTO whereDTO) throws Exception {
		Where where = mapper.map(whereDTO);
		Where oldWhere = whereDAO.get(whereDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(where, oldWhere);
		} catch (Exception e) {
		}
		where = whereDAO.update(where);
		if (where == null) {
			return null;
		}
		return mapper.map(where);
	}

	@Override
	public WhereDTO getById(long id) {
		Where where = whereDAO.get(id);
		if (where == null) {
			return null;
		}
		return mapper.map(where);		
	}

}