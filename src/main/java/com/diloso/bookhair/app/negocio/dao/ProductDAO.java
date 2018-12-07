package com.diloso.bookhair.app.negocio.dao;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.ProductDTO;

public interface ProductDAO {

	ProductDTO create(ProductDTO product) throws Exception;

	ProductDTO remove(long id) throws Exception;

	ProductDTO update(ProductDTO product) throws Exception;

	ProductDTO getById(long id);
	
	List<ProductDTO> getProductByLang(long localeId, String lang);
	
	List<ProductDTO> getProductAdminByLang(long localeId, String lang);
	
	List<ProductDTO> getProduct(long localeId);
	
}