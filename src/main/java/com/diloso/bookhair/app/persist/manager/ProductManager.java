package com.diloso.bookhair.app.persist.manager;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.MultiTextDAO;
import com.diloso.bookhair.app.negocio.dao.ProductDAO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.dto.ProductDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.entities.Product;
import com.diloso.bookhair.app.persist.transformer.ProductTransformer;

@Component
@Scope(value = "singleton")
public class ProductManager extends Manager implements ProductDAO {

	public static final String KEY_MULTI_RATE_NAME = "product_name_";
	
	@Autowired
	protected MultiTextDAO multiTextDAO;
	
	@Autowired
	protected ProductTransformer productTransformer;
	
	public ProductManager() {

	}

	public ProductDTO create(ProductDTO product) throws Exception {
		EntityManager em = getEntityManager();
		Product entityProduct = productTransformer.transformDTOToEntity(
				product);
		try {
			em.getTransaction().begin();
			em.persist(entityProduct);
			em.getTransaction().commit();
		} catch (Exception ex) {
			try {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
			} catch (Exception e) {
				throw e;
			}
			throw ex;
		} finally {
			em.close();
		}
		return productTransformer.transformEntityToDTO(entityProduct);
	}

	public ProductDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		Product oldEntityProduct = new Product();
		try {
			em.getTransaction().begin();
			Product entityProduct = (Product) em.find(Product.class, id);
			PropertyUtils.copyProperties(oldEntityProduct, entityProduct);
			em.remove(em.merge(entityProduct));
			em.getTransaction().commit();
		} catch (Exception ex) {
			try {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
			} catch (Exception e) {
				throw e;
			}
			throw ex;
		} finally {
			em.close();
		}
		return productTransformer
				.transformEntityToDTO(oldEntityProduct);
	}

	public ProductDTO update(ProductDTO product) throws Exception {
		EntityManager em = getEntityManager();
		Product entityProduct = productTransformer.transformDTOToEntity(
				product);
		Product oldEntityProduct = null;
		try {
			em.getTransaction().begin();
			oldEntityProduct = (Product) em.find(Product.class, entityProduct.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityProduct,
					oldEntityProduct);
			entityProduct = em.merge(entityProduct);
			em.getTransaction().commit();
		} catch (Exception ex) {
			try {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
			} catch (Exception e) {
				throw e;
			}
			throw ex;
		} finally {
			em.close();
		}
		return productTransformer.transformEntityToDTO(entityProduct);
	}

	public ProductDTO getById(long id) {
		Product entityProduct = null;
		EntityManager em = getEntityManager();
		try {
			entityProduct = (Product) em.find(Product.class, id);
		} finally {
			em.close();
		}
		return productTransformer.transformEntityToDTO(entityProduct);
	}

	public List<ProductDTO> getProductByLang(long localeId, String lang) {
		EntityManager em = getEntityManager();
		List<ProductDTO> result = new ArrayList<ProductDTO>();
		List<Product> resultQuery = null;
		ProductDTO product = null;
		MultiTextDTO multiTextKey = null;
		try {
			Query query = em.createNamedQuery("getProduct");
			query.setParameter("proLocalId", localeId);
			resultQuery = (List<Product>) query.getResultList();
			String name = "";
			for (Product entityProduct : resultQuery) {
				product = productTransformer.transformEntityToDTO(
						entityProduct);
				multiTextKey = multiTextDAO.getByLanCodeAndKey(lang,
						product.getProNameMulti());
				name = multiTextKey.getMulText();
				if (name != null) {
					product.setProName(name);
					result.add(product);
				}
			}
		} finally {
			em.close();
		}
		return result;
	}

	public List<ProductDTO> getProductAdminByLang(long localeId, String lang) {
		EntityManager em = getEntityManager();
		List<ProductDTO> result = new ArrayList<ProductDTO>();
		List<Product> resultQuery = null;
		ProductDTO product = null;
		MultiTextDTO multiTextKey = null;
		try {
			Query query = em.createNamedQuery("getProductAdmin");
			query.setParameter("proLocalId", localeId);
			resultQuery = (List<Product>) query.getResultList();
			String name = "";
			for (Product entityProduct : resultQuery) {
				product = productTransformer.transformEntityToDTO(
						entityProduct);
				multiTextKey = multiTextDAO.getByLanCodeAndKey(lang,
						product.getProNameMulti());
				name = multiTextKey.getMulText();
				if (name != null) {
					product.setProName(name);
					result.add(product);
				}
			}
		} finally {
			em.close();
		}
		return result;
	}
	
	public List<ProductDTO> getProduct(long localeId) {
		EntityManager em = getEntityManager();
		List<ProductDTO> result = new ArrayList<ProductDTO>();
		List<Product> resultQuery = null;
		ProductDTO product = null;
		try {
			Query query = em.createNamedQuery("getProduct");
			query.setParameter("proLocalId", localeId);
			resultQuery = (List<Product>) query.getResultList();
			for (Product entityProduct : resultQuery) {
				product = productTransformer.transformEntityToDTO(
						entityProduct);
				result.add(product);
			}
		} finally {
			em.close();
		}
		return result;
	}

}