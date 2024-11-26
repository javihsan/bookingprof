package com.diloso.bookhair.app.negocio.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.dto.ProductDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.ProductDAO;
import com.diloso.bookhair.app.persist.entities.Product;
import com.diloso.bookhair.app.persist.mapper.ProductMapper;

@Component
@Scope(value = "singleton")
public class ProductManager implements IProductManager {

	public static final String ENABLED = "enabled";
	public static final String ORDER_KEY_ASC = "__key__";
	public static final String PRO_LOCAL_ID = "proLocalId";
	
	public static final String KEY_MULTI_RATE_NAME = "product_name_";
	
	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	protected IMultiTextManager multiTextManager;
	
	@Autowired
	protected ProductMapper mapper;
	
	public ProductManager() {

	}

	@Override
	public ProductDTO create(ProductDTO productDTO) throws Exception {
		Product product = mapper.map(productDTO);
		product = productDAO.create(product);
		return mapper.map(product);
	}

	@Override
	public ProductDTO remove(long id) throws Exception {
		Product product = productDAO.get(id);
		productDAO.delete(id);
		if (product == null) {
			return null;
		}
		return mapper.map(product);
	}

	@Override
	public ProductDTO update(ProductDTO productDTO) throws Exception {
		Product product = mapper.map(productDTO);
		Product oldProduct = productDAO.get(productDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(product, oldProduct);
		} catch (Exception e) {
		}
		product = productDAO.update(product);
		if (product == null) {
			return null;
		}
		return mapper.map(product);
	}

	@Override
	public ProductDTO getById(long id) {
		Product product = productDAO.get(id);
		if (product == null) {
			return null;
		}
		return mapper.map(product);		
	}

	@Override
	public List<ProductDTO> getProductByLang(long localeId, String lang) {
		List<ProductDTO> result = new ArrayList<ProductDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(PRO_LOCAL_ID, localeId);
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<Product> resultQuery = productDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			ProductDTO product = mapper.map(entity);
			MultiTextDTO multiTextKey = multiTextManager.getByLanCodeAndKey(lang,
					product.getProNameMulti());
			String name = multiTextKey.getMulText();
			if (name != null) {
				product.setProName(name);
				result.add(product);
			}
		});
		return result;
	}

	@Override
	public List<ProductDTO> getProductAdminByLang(long localeId, String lang) {
		List<ProductDTO> result = new ArrayList<ProductDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(PRO_LOCAL_ID, localeId);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<Product> resultQuery = productDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			ProductDTO product = mapper.map(entity);
			MultiTextDTO multiTextKey = multiTextManager.getByLanCodeAndKey(lang,
					product.getProNameMulti());
			String name = multiTextKey.getMulText();
			if (name != null) {
				product.setProName(name);
				result.add(product);
			}
		});
		return result;
	}

	@Override
	public List<ProductDTO> getProduct(long localeId) {
		List<ProductDTO> result = new ArrayList<ProductDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(PRO_LOCAL_ID, localeId);
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<Product> resultQuery = productDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});
		return result;
	}	
	
}