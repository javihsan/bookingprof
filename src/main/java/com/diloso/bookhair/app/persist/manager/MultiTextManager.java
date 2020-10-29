package com.diloso.bookhair.app.persist.manager;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.MultiTextDAO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.entities.MultiText;
import com.diloso.bookhair.app.persist.transformer.MultiTextTransformer;

@Component
@Scope(value = "singleton")
public class MultiTextManager extends Manager implements MultiTextDAO {

	public static final String KEY_MULTI_SYSTEM = "System_";
	
	//@Autowired
	protected MultiTextTransformer multiTextTransformer;
	
	public MultiTextManager() {
		
	}
	 
	public MultiTextDTO create(MultiTextDTO multiText) throws Exception {
		EntityManager em = getEntityManager();
		MultiText entityMultiText = multiTextTransformer.transformDTOToEntity(multiText);
		try {
			em.getTransaction().begin();
			em.persist(entityMultiText);
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
		return multiTextTransformer.transformEntityToDTO(entityMultiText);
	}

	public MultiTextDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		MultiText oldEntityMultiText = new MultiText();
		try {
			em.getTransaction().begin();
			MultiText entityMultiText = (MultiText) em.find(MultiText.class, id);
			PropertyUtils.copyProperties(oldEntityMultiText, entityMultiText);
			em.remove(em.merge(entityMultiText));
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
		return multiTextTransformer.transformEntityToDTO(oldEntityMultiText);
	}
	
	public MultiTextDTO update(MultiTextDTO multiText) throws Exception {
		EntityManager em = getEntityManager();
		MultiText entityMultiText = multiTextTransformer.transformDTOToEntity(multiText);
		MultiText oldEntityMultiText = null;
		try {
			em.getTransaction().begin();
			oldEntityMultiText = (MultiText) em.find(MultiText.class, entityMultiText.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityMultiText, oldEntityMultiText);
			entityMultiText = em.merge(entityMultiText);
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
		return multiTextTransformer.transformEntityToDTO(entityMultiText);
	}

	public MultiTextDTO getById(long id) {
		MultiText entityMultiText = null;
		EntityManager em = getEntityManager();
		try {
			entityMultiText= (MultiText) em.find(MultiText.class, id);
		} finally {
			em.close();
		}
		return multiTextTransformer.transformEntityToDTO(entityMultiText);
	}
	
	public MultiTextDTO getByLanCodeAndKey (String lanCode, String key) {
		EntityManager em = getEntityManager();
		List<MultiText> resultQuery = null;
		MultiTextDTO multiText = null;
		try {
			Query query = em.createNamedQuery("getByLanCodeAndKey");
			query.setParameter("mulLanCode", lanCode);
			query.setParameter("mulKey", key);
			resultQuery = (List<MultiText>) query.getResultList();
			if (resultQuery.size()==1){
				multiText = multiTextTransformer.transformEntityToDTO(resultQuery.get(0));
			}
		} finally {
			em.close();
		}
		return multiText;
	}
	
	public List<MultiTextDTO> getMultiTextSystemByLanCode (String lanCode) {
		EntityManager em = getEntityManager();
		List<MultiTextDTO> result = new ArrayList<MultiTextDTO>();
		List<MultiText> resultQuery = null;
		MultiTextDTO multiText = null;
		try {
			Query query = em.createNamedQuery("getMultiLangCode");
			query.setParameter("mulLanCode", lanCode);
			resultQuery = (List<MultiText>) query.getResultList();
			for (MultiText entityMultiText: resultQuery){
				if (entityMultiText.getMulKey().startsWith(KEY_MULTI_SYSTEM)){
					multiText = multiTextTransformer.transformEntityToDTO(entityMultiText);
					result.add(multiText);
				}
			}
		} finally {
			em.close();
		}
		return result;
	}
	
	public List<MultiTextDTO> getMultiTextByLanCode (String lanCode, Long localId) {
		EntityManager em = getEntityManager();
		List<MultiTextDTO> result = new ArrayList<MultiTextDTO>();
		List<MultiText> resultQuery = null;
		MultiTextDTO multiText = null;
		try {
			Query query = em.createNamedQuery("getMultiLangCode");
			query.setParameter("mulLanCode", lanCode);
			resultQuery = (List<MultiText>) query.getResultList();
			for (MultiText entityMultiText: resultQuery){
				if (entityMultiText.getMulKey().indexOf("_"+localId+"_")>=0){
					multiText = multiTextTransformer.transformEntityToDTO(entityMultiText);
					result.add(multiText);
				}
			}
		} finally {
			em.close();
		}
		return result;
	}
	
	public List<MultiTextDTO> getMultiTextByKey (String key) {
		EntityManager em = getEntityManager();
		List<MultiTextDTO> result = new ArrayList<MultiTextDTO>();
		List<MultiText> resultQuery = null;
		MultiTextDTO multiText = null;
		try {
			Query query = em.createNamedQuery("getMultiKey");
			query.setParameter("mulKey", key);
			resultQuery = (List<MultiText>) query.getResultList();
			for (MultiText entityMultiText: resultQuery){
				multiText = multiTextTransformer.transformEntityToDTO(entityMultiText);
				result.add(multiText);
			}
		} finally {
			em.close();
		}
		return result;
	}
	
	
	public List<MultiTextDTO> getMultiText(){
		EntityManager em = getEntityManager();
		List<MultiTextDTO> result = new ArrayList<MultiTextDTO>();
		List<MultiText> resultQuery = null;
		MultiTextDTO multiText = null;
		try {
			Query query = em.createNamedQuery("getMultiText");
			resultQuery = (List<MultiText>) query.getResultList();
			for (MultiText entityMultiText: resultQuery){
				multiText = multiTextTransformer.transformEntityToDTO(entityMultiText);
				result.add(multiText);
			}
		} finally {
			em.close();
		}
		return result;
	}

	public void setMultiTextTransformer(MultiTextTransformer multiTextTransformer) {
		this.multiTextTransformer = multiTextTransformer;
	}
	
	
}