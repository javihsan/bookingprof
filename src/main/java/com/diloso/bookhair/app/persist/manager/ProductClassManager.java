package com.diloso.bookhair.app.persist.manager;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.MultiTextDAO;
import com.diloso.bookhair.app.negocio.dao.ProductClassDAO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.dto.ProductClassDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.entities.ProductClass;
import com.diloso.bookhair.app.persist.transformer.ProductClassTransformer;

@Component
@Scope(value = "singleton")
public class ProductClassManager extends Manager implements ProductClassDAO {

	public static final String KEY_MULTI_TASKCLASS_NAME = "productClass_name_";

	//@Autowired
	protected MultiTextDAO multiTextDAO;

	//@Autowired
	protected ProductClassTransformer productClassTransformer;
	
	public ProductClassManager() {

	}

	public ProductClassDTO create(ProductClassDTO productClass) throws Exception {
		EntityManager em = getEntityManager();
		ProductClass entityProductClass = productClassTransformer.transformDTOToEntity(
				productClass);
		try {
			em.getTransaction().begin();
			em.persist(entityProductClass);
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
		return productClassTransformer.transformEntityToDTO(entityProductClass);
	}

	public ProductClassDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		ProductClass oldEntityProductClass = new ProductClass();
		try {
			em.getTransaction().begin();
			ProductClass entityProductClass = (ProductClass) em.find(ProductClass.class, id);
			PropertyUtils.copyProperties(oldEntityProductClass, entityProductClass);
			em.remove(em.merge(entityProductClass));
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
		return productClassTransformer
				.transformEntityToDTO(oldEntityProductClass);
	}

	public ProductClassDTO update(ProductClassDTO productClass) throws Exception {
		EntityManager em = getEntityManager();
		ProductClass entityProductClass = productClassTransformer.transformDTOToEntity(
				productClass);
		ProductClass oldEntityProductClass = null;
		try {
			em.getTransaction().begin();
			oldEntityProductClass = (ProductClass) em.find(ProductClass.class, entityProductClass.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityProductClass,
					oldEntityProductClass);
			entityProductClass = em.merge(entityProductClass);
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
		return productClassTransformer.transformEntityToDTO(entityProductClass);
	}

	public ProductClassDTO getById(long id) {
		ProductClass entityProductClass = null;
		EntityManager em = getEntityManager();
		try {
			entityProductClass = (ProductClass) em.find(ProductClass.class, id);
		} finally {
			em.close();
		}
		return productClassTransformer.transformEntityToDTO(entityProductClass);
	}

	public List<ProductClassDTO> getProductClassByLang(String lang) {
		EntityManager em = getEntityManager();
		List<ProductClassDTO> result = new ArrayList<ProductClassDTO>();
		List<ProductClass> resultQuery = null;
		ProductClassDTO productClass = null;
		MultiTextDTO multiTextKey = null;
		try {
			Query query = em.createNamedQuery("getProductClass");
			resultQuery = (List<ProductClass>) query.getResultList();
			String name = "";
			for (ProductClass entityProductClass : resultQuery) {
				productClass = productClassTransformer.transformEntityToDTO(
						entityProductClass);
				multiTextKey = multiTextDAO.getByLanCodeAndKey(lang,
						productClass.getPclNameMulti());
				name = multiTextKey.getMulText();
				if (name != null) {
					productClass.setPclName(name);
					result.add(productClass);
				}
			}
		} finally {
			em.close();
		}
		return result;
	}

	public List<ProductClassDTO> getProductClass() {
		EntityManager em = getEntityManager();
		List<ProductClassDTO> result = new ArrayList<ProductClassDTO>();
		List<ProductClass> resultQuery = null;
		ProductClassDTO productClass = null;
		try {
			Query query = em.createNamedQuery("getProductClass");
			resultQuery = (List<ProductClass>) query.getResultList();
			for (ProductClass entityProductClass : resultQuery) {
				productClass = productClassTransformer.transformEntityToDTO(
						entityProductClass);
				result.add(productClass);
			}
		} finally {
			em.close();
		}
		return result;
	}

	public void setMultiTextDAO(MultiTextDAO multiTextDAO) {
		this.multiTextDAO = multiTextDAO;
	}

	public void setProductClassTransformer(ProductClassTransformer productClassTransformer) {
		this.productClassTransformer = productClassTransformer;
	}

	
}