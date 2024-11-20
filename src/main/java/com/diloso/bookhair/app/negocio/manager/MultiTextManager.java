package com.diloso.bookhair.app.negocio.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.MultiTextDAO;
import com.diloso.bookhair.app.persist.entities.MultiText;
import com.diloso.bookhair.app.persist.mapper.MultiTextMapper;

@Component
@Scope(value = "singleton")
public class MultiTextManager implements IMultiTextManager {

	public static final String KEY_MULTI_SYSTEM = "System_";
	
	public static final String ENABLED = "enabled";
	public static final String ORDER_KEY_ASC = "__key__";
	public static final String MUL_LAN_CODE = "mulLanCode";
	public static final String MUL_KEY = "mulKey";
	
	@Autowired
	private MultiTextDAO multiTextDAO;
	
	@Autowired
	protected MultiTextMapper mapper;
	
	public MultiTextManager() {
		
	}

	@Override
	public MultiTextDTO create(MultiTextDTO multiTextDTO) throws Exception {
		MultiText multiText = mapper.map(multiTextDTO);
		multiText = multiTextDAO.create(multiText);
		return mapper.map(multiText);
	}

	@Override
	public MultiTextDTO remove(long id) throws Exception {
		MultiText multiText = multiTextDAO.get(id);
		multiTextDAO.delete(id);
		if (multiText == null) {
			return null;
		}
		return mapper.map(multiText);
	}

	@Override
	public MultiTextDTO update(MultiTextDTO multiTextDTO) throws Exception {
		MultiText multiText = mapper.map(multiTextDTO);
		MultiText oldMultiText = multiTextDAO.get(multiTextDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(multiText, oldMultiText);
		} catch (Exception e) {
		}
		multiText = multiTextDAO.update(multiText);
		if (multiText == null) {
			return null;
		}
		return mapper.map(multiText);
	}

	@Override
	public MultiTextDTO getById(long id) {
		MultiText multiText = multiTextDAO.get(id);
		if (multiText == null) {
			return null;
		}
		return mapper.map(multiText);		
	}

	@Override
	public MultiTextDTO getByLanCodeAndKey(String lanCode, String key) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(MUL_LAN_CODE, lanCode);
		filters.put(MUL_KEY, key);
		filters.put(ENABLED, 1);
		List<MultiText> resultQuery = multiTextDAO.listFilter(filters);
		if (resultQuery.size() == 1) {
			return mapper.map(resultQuery.get(0));
		}
		return null;
	}

	@Override
	public List<MultiTextDTO> getMultiTextSystemByLanCode(String lanCode) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(MUL_LAN_CODE, lanCode);
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<MultiText> resultQuery = multiTextDAO.listOrderFilter(filters,orders);
		List<MultiTextDTO> result = new ArrayList<MultiTextDTO>();
		resultQuery.stream().forEach(entity -> {
			if (entity.getMulKey().startsWith(KEY_MULTI_SYSTEM)){
				result.add(mapper.map(entity));
			}
		});
		return result;
	}

	@Override
	public List<MultiTextDTO> getMultiTextByLanCode(String lanCode, Long localId) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(MUL_LAN_CODE, lanCode);
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<MultiText> resultQuery = multiTextDAO.listOrderFilter(filters,orders);
		List<MultiTextDTO> result = new ArrayList<MultiTextDTO>();
		resultQuery.stream().forEach(entity -> {
			if (entity.getMulKey().indexOf("_"+localId+"_")>=0){
				result.add(mapper.map(entity));
			}
		});
		return result;
	}

	@Override
	public List<MultiTextDTO> getMultiTextByKey(String key) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(MUL_KEY, key);
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<MultiText> resultQuery = multiTextDAO.listOrderFilter(filters,orders);
		List<MultiTextDTO> result = new ArrayList<MultiTextDTO>();
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});
		return result;
	}

	@Override
	public List<MultiTextDTO> getMultiText() {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<MultiText> resultQuery = multiTextDAO.listOrderFilter(filters,orders);
		List<MultiTextDTO> result = new ArrayList<MultiTextDTO>();
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});
		return result;
	}
	
}