package com.diloso.bookhair.app.negocio.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.dto.ProductClassDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.ProductClassDAO;
import com.diloso.bookhair.app.persist.entities.ProductClass;
import com.diloso.bookhair.app.persist.mapper.ProductClassMapper;

@Component
@Scope(value = "singleton")
public class ProductClassManager implements IProductClassManager {

	public static final String ENABLED = "enabled";
	public static final String ORDER_KEY_ASC = "__key__";
	
	public static final String KEY_MULTI_TASKCLASS_NAME = "productClass_name_";

	@Autowired
	private ProductClassDAO productClassDAO;
	
	@Autowired
	protected IMultiTextManager multiTextManager;

	@Autowired
	protected ProductClassMapper mapper;
	
	public ProductClassManager() {

	}

	@Override
	public ProductClassDTO create(ProductClassDTO productClassDTO) throws Exception {
		ProductClass productClass = mapper.map(productClassDTO);
		productClass = productClassDAO.create(productClass);
		return mapper.map(productClass);
	}

	@Override
	public ProductClassDTO remove(long id) throws Exception {
		ProductClass productClass = productClassDAO.get(id);
		productClassDAO.delete(id);
		if (productClass == null) {
			return null;
		}
		return mapper.map(productClass);
	}

	@Override
	public ProductClassDTO update(ProductClassDTO productClassDTO) throws Exception {
		ProductClass productClass = mapper.map(productClassDTO);
		ProductClass oldProductClass = productClassDAO.get(productClassDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(productClass, oldProductClass);
		} catch (Exception e) {
		}
		productClass = productClassDAO.update(productClass);
		if (productClass == null) {
			return null;
		}
		return mapper.map(productClass);
	}

	@Override
	public ProductClassDTO getById(long id) {
		ProductClass productClass = productClassDAO.get(id);
		if (productClass == null) {
			return null;
		}
		return mapper.map(productClass);		
	}

	@Override
	public List<ProductClassDTO> getProductClassByLang(String lang) {
		List<ProductClassDTO> result = new ArrayList<ProductClassDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<ProductClass> resultQuery = productClassDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			ProductClassDTO productClass = mapper.map(entity);
			MultiTextDTO multiTextKey = multiTextManager.getByLanCodeAndKey(lang,
					productClass.getPclNameMulti());
			String name = multiTextKey.getMulText();
			if (name != null) {
				productClass.setPclName(name);
				result.add(productClass);
			}
		});
		return result;
	}

	@Override
	public List<ProductClassDTO> getProductClass() {
		List<ProductClassDTO> result = new ArrayList<ProductClassDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<ProductClass> resultQuery = productClassDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});
		return result;
	}
	
}