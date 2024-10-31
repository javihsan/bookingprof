package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.ProductDTO;

public interface IProductManager {

	ProductDTO create(ProductDTO productDTO) throws Exception;

	ProductDTO remove(long id) throws Exception;

	ProductDTO update(ProductDTO productDTO) throws Exception;

	ProductDTO getById(long id);
	
	List<ProductDTO> getProductByLang(long localeId, String lang);
	
	List<ProductDTO> getProductAdminByLang(long localeId, String lang);
	
	List<ProductDTO> getProduct(long localeId);
	
}