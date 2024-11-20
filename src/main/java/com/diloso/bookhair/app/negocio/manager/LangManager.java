package com.diloso.bookhair.app.negocio.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.LangDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.LangDAO;
import com.diloso.bookhair.app.persist.entities.Lang;
import com.diloso.bookhair.app.persist.mapper.LangMapper;

@Component
@Scope(value = "singleton")
public class LangManager implements ILangManager {

	public static final String LAN_NAME = "lanName";
	public static final String LAN_CODE = "lanCode";
	public static final String ENABLED = "enabled";
	public static final String ORDER_LAN_NAME_ASC = "lanName";

	@Autowired
	private LangDAO langDAO;

	@Autowired
	protected LangMapper mapper;

	public LangManager() {
		if (mapper == null) {
			mapper = new LangMapper();
		}
	}

	@Override
	public LangDTO create(LangDTO langDTO) throws Exception {
		Lang lang = mapper.map(langDTO);
		lang = langDAO.create(lang);
		return mapper.map(lang);
	}

	@Override
	public LangDTO remove(long id) throws Exception {
		Lang lang = langDAO.get(id);
		langDAO.delete(id);
		if (lang == null) {
			return null;
		}
		return mapper.map(lang);
	}

	@Override
	public LangDTO update(LangDTO langDTO) throws Exception {
		Lang lang = mapper.map(langDTO);
		Lang oldLang = langDAO.get(langDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(lang, oldLang);
		} catch (Exception e) {
		}
		lang = langDAO.update(lang);
		if (lang == null) {
			return null;
		}
		return mapper.map(lang);
	}

	@Override
	public LangDTO getById(long id) {
		Lang lang = langDAO.get(id);
		if (lang == null) {
			return null;
		}
		return mapper.map(lang);
	}

	@Override
	public LangDTO getByName(String name) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(LAN_NAME, name);
		filters.put(ENABLED, 1);
		List<Lang> resultQuery = langDAO.listFilter(filters);
		if (resultQuery.size() == 1) {
			return mapper.map(resultQuery.get(0));
		}
		return null;
	}

	@Override
	public LangDTO getByCode(String lanCode) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(LAN_CODE, lanCode);
		filters.put(ENABLED, 1);
		List<Lang> resultQuery = langDAO.listFilter(filters);
		if (resultQuery.size() == 1) {
			return mapper.map(resultQuery.get(0));
		}
		return null;
	}

	@Override
	public List<LangDTO> getLang() {
		List<LangDTO> result = new ArrayList<LangDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_LAN_NAME_ASC);
		List<Lang> resultQuery = langDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});
		return result;
	}

}