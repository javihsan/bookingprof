package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.ProductClassDTO;

public interface IProductClassManager {

	ProductClassDTO create(ProductClassDTO productClassDTO) throws Exception;

	ProductClassDTO remove(long id) throws Exception;

	ProductClassDTO update(ProductClassDTO productClassDTO) throws Exception;

	ProductClassDTO getById(long id);
	
	List<ProductClassDTO> getProductClassByLang(String lang);
	
	List<ProductClassDTO> getProductClass();
	
}