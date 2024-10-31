package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.ProductClassDTO;
import com.diloso.bookhair.app.persist.dao.ProductClassDAO;
import com.diloso.bookhair.app.persist.mapper.ProductClassMapper;

@Component
@Scope(value = "singleton")
public class ProductClassManager implements IProductClassManager {

	public static final String KEY_MULTI_TASKCLASS_NAME = "productClass_name_";

	@Autowired
	private ProductClassDAO productClassDAO;
	
	@Autowired
	protected IMultiTextManager multiTextManager;

	@Autowired
	protected ProductClassMapper productClassMapper;
	
	public ProductClassManager() {

	}

	@Override
	public ProductClassDTO create(ProductClassDTO productClass) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductClassDTO remove(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductClassDTO update(ProductClassDTO productClass) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductClassDTO getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductClassDTO> getProductClassByLang(String lang) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductClassDTO> getProductClass() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	/*
	public ProductClassDTO create(ProductClassDTO productClass) throws Exception {
		EntityManager em = getEntityManager();
		ProductClass entityProductClass = productClassMapper.map(
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
		return productClassMapper.map(entityProductClass);
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
		return productClassMapper
				.map(oldEntityProductClass);
	}

	public ProductClassDTO update(ProductClassDTO productClass) throws Exception {
		EntityManager em = getEntityManager();
		ProductClass entityProductClass = productClassMapper.map(
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
		return productClassMapper.map(entityProductClass);
	}

	public ProductClassDTO getById(long id) {
		ProductClass entityProductClass = null;
		EntityManager em = getEntityManager();
		try {
			entityProductClass = (ProductClass) em.find(ProductClass.class, id);
		} finally {
			em.close();
		}
		return productClassMapper.map(entityProductClass);
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
				productClass = productClassMapper.map(
						entityProductClass);
				multiTextKey = multiTextManager.getByLanCodeAndKey(lang,
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
				productClass = productClassMapper.map(
						entityProductClass);
				result.add(productClass);
			}
		} finally {
			em.close();
		}
		return result;
	}

	public void setMultiTextDAO(IMultiTextManager multiTextManager) {
		this.multiTextManager = multiTextManager;
	}

	public void setProductClassTransformer(ProductClassMapper productClassMapper) {
		this.productClassMapper = productClassMapper;
	}
	*/
	
}